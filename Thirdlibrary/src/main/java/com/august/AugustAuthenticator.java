package com.august;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Response;

import static com.august.AugustApi.HEADER_AUGUST_ACCESS_TOKEN;


public class AugustAuthenticator {

    public static final String AUGUST_SP_NAME = "august";
    private AugustApi api;
    private String loginMethod;
    private String username;
    private String password;
    private Authentication authentication;
    private SharePreUtil sharePreUtil;


    private void saveAuthenticaitionToSp(Authentication auth){
        if (auth!=null && sharePreUtil!=null){
          sharePreUtil.setValue("install_id",auth.getInstallID());
          sharePreUtil.setValue("access_token",auth.getAccessToken());
          sharePreUtil.setValue("access_token_expires",auth.getAccessTokenExpires());
          sharePreUtil.setValue("state",auth.getState());
        }
    }

    private Authentication getAuthenticaitionfromSp(){
        String status = sharePreUtil.getValue("state", AuthenticationState.REQUIRES_AUTHENTICATION);
        String installID = sharePreUtil.getValue("install_id", UUID.randomUUID().toString());
        String token = sharePreUtil.getValue("access_token","");
        String expires = sharePreUtil.getValue("access_token_expires","");
        return  new Authentication(status,installID,token,expires);
    }


    public AugustAuthenticator(Activity activity, AugustApi api, String loginMethod, String username, String password) {
        this.api = api;
        this.loginMethod = loginMethod;
        this.username = username;
        this.password = password;
        this.sharePreUtil =  new SharePreUtil(activity,AUGUST_SP_NAME);
        this.authentication = getAuthenticaitionfromSp();

    }

//{"installId":"79fd0eb6-381d-4adf-95a0-47721289d1d9","applicationId":"","userId":"2d5e7b80-0d29-4252-85af-308b19e800fd","vInstallId":false,
// "vPassword":true,"vEmail":false,"vPhone":false,"hasInstallId":true,"hasPassword":true,"hasEmail":true,"hasPhone":true,"isLockedOut":false,
// "captcha":"","email":[],"phone":[],"expiresAt":"2019-03-20T07:45:15.340Z","temporaryAccountCreationPasswordLink":"","iat":1542699915,
// "exp":null,"LastName":"Xun","FirstName":"Run"}


    public Authentication authenticate(){
        if (this.authentication.getState().equals(AuthenticationState.AUTHENTICATED)){
            return  authentication;
        }
        String indentifier = loginMethod + ":" +username;
        String installID = authentication.getInstallID();
        Response response = api.getSession(installID,indentifier,password);
        String accessToken="";
        if (response!=null) {
            accessToken= response.header(HEADER_AUGUST_ACCESS_TOKEN);
        }
        String body = null;
        try {
            if (response.body()!=null)
            body = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String accessTokenExpirs ="";
        boolean vPassword =false;
        boolean vInstallID =false;
        String state ;
        try {
            JSONObject data = new JSONObject(body);
            accessTokenExpirs = data.getString("expiresAt");
            vPassword = data.getBoolean("vPassword");
            vInstallID = data.getBoolean("vInstallId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!vPassword){
            state = AuthenticationState.BAD_PASSWORD;
        }else if(!vInstallID){
            state = AuthenticationState.REQUIRES_VALIDATION;
        }else {
            state = AuthenticationState.AUTHENTICATED;
        }
        authentication = new Authentication(state,installID,accessToken,accessTokenExpirs);

        if (state.equals(AuthenticationState.AUTHENTICATED)){
            saveAuthenticaitionToSp(authentication);
        }
        return authentication;
    }

    public boolean sendVerificationCode(){
        return api.sendVerificationCode(authentication.getAccessToken(),loginMethod,username);
    }

    public String validateVerificationCode(String verificationCode){
        if (verificationCode == null ||verificationCode.equals("") || verificationCode.length() < 4){
            return ValidationResult.INVALID_VERIFICATION_CODE;
        }

        boolean succeed = api.validateVerificationCode(authentication.getAccessToken(),loginMethod,username,verificationCode);
        if (succeed){
            return ValidationResult.VALIDATED;
        }else {
            return ValidationResult.INVALID_VERIFICATION_CODE;
        }

    }


    public class Authentication{
        private String state;
        private String installID;
        private String accessToken;
        private String accessTokenExpires;

        public Authentication(String state, String installID, String accessToken, String accessTokenExpires) {
            this.state = state;
            this.installID = installID;
            this.accessToken = accessToken;
            this.accessTokenExpires = accessTokenExpires;
        }

        public String getState() {
            return state;
        }

        public String getInstallID() {
            return installID;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getAccessTokenExpires() {
            return accessTokenExpires;
        }

        public void setState(String state) {
            this.state = state;
        }
    }


     public class  AuthenticationState{
        public static final String REQUIRES_AUTHENTICATION = "requires_authentication";
        public static final String REQUIRES_VALIDATION = "requires_validation";
        public static final String AUTHENTICATED = "authenticated";
        public static final String BAD_PASSWORD = "bad_password";
    }

      public class ValidationResult{
        public static final String VALIDATED = "validated";
        public static final String INVALID_VERIFICATION_CODE = "invalid_verification_code";
    }



}

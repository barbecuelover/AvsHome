package com.ecobee;

import com.ecobee.bean.CodeMessageBean;
import com.ecobee.bean.GetAccessTokenBean;
import com.ecobee.bean.GetTemperatureInfoBean;
import com.ecobee.bean.PinCodBean;
import com.ecobee.event.SentAccessTokenMessage;
import com.ecobee.event.SentPinCodeMessage;
import com.ecobee.event.SentTemperatureInfoMessage;
import com.ecobee.event.sentRefreshTokenMessage;
import com.ecobee.utils.EcoBeeHttpUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zuo.biao.library.util.Log;

import static android.content.ContentValues.TAG;

/**
 * 作者：RedKeyset on 2018/12/13 17:28
 * 邮箱：redkeyset@aliyun.com
 */
public class Oauth2Certification {

    public Oauth2Certification() {

    }

    /**
     * 获取PIN值
     */
    public void ShowPIN() {
        String formattedUrl = String.format(ConstantUrls.Authorization.ShowPIN,
                ecobeeConfig.response_type,
                ecobeeConfig.APIKey,
                ecobeeConfig.scope);

        Request request = new Request.Builder()
                .url(formattedUrl)
                .get()
                .build();

        EcoBeeHttpUtils.getInstance().getHttpObject().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String JsonString = response.body().string();
                    Gson gson = new Gson();
                    PinCodBean pinCodBean = gson.fromJson(JsonString, PinCodBean.class);
                    String pinCode = pinCodBean.getEcobeePin();
                    String code = pinCodBean.getCode();
                    EventBus.getDefault().post(new SentPinCodeMessage(pinCode, code));
                } catch (IOException e) {
                }
            }
        });
    }

    /**
     * 验证用户是否添加成功，添加成功后获得
     * 既能获取 access_token 也能 刷新 access_token
     * access_token
     * refresh_token
     * token_type
     * refreshToken = oauthCode
     *
     * @param oauthCode
     */
    public void AddUserSucess(String oauthCode) {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", ecobeeConfig.response_type)
                .add("code", oauthCode)
                .add("client_id", ecobeeConfig.APIKey)
                .build();

        final Request request = new Request.Builder()
                .url(ConstantUrls.Authorization.getAccessToken)
                .post(formBody)
                .build();

        EcoBeeHttpUtils.getInstance().getHttpObject().newCall(request).enqueue(new Callback() {

            private String refreshToken;
            private String accessToken;

            @Override
            public void onFailure(Call call, IOException e) {
                Log.v(TAG, "mHttpClient--onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String JsonString = response.body().string();
                    Gson gsonCode = new Gson();
                    CodeMessageBean codeMessageBean = gsonCode.fromJson(JsonString, CodeMessageBean.class);
                    Log.v(TAG, "mHttpClient--Successful:" + JsonString);
                        Gson gson = new Gson();
                        GetAccessTokenBean getAccessTokenBean = gson.fromJson(JsonString, GetAccessTokenBean.class);
                        accessToken = getAccessTokenBean.getAccess_token();
                        refreshToken = getAccessTokenBean.getRefresh_token();
                    EventBus.getDefault().post(new SentAccessTokenMessage(accessToken, refreshToken, 200));
                } catch (IOException e) {
                }
            }
        });
    }

    /**
     * 获取主恒温器设备的一些信息
     * Sensor与之不同
     * code = 14表示 Token过期
     *
     * @param accessToken
     */
    public void getThermostatDevices(String accessToken) {
        final Request request = new Request.Builder()
                .url(ConstantUrls.Authorization.getCurrentTemperature)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("charset", "UTF-8")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        EcoBeeHttpUtils.getInstance().getHttpObject().newCall(request).enqueue(new Callback() {

            private GetTemperatureInfoBean temperatureInfoBean;

            @Override
            public void onFailure(Call call, IOException e) {
                Log.v(TAG, "getThermostatDevices--onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String JsonString = response.body().string();
                    Log.v(TAG, "getThermostatDevices--onResponse:" + JsonString);
                    Gson gsonCode = new Gson();
                    CodeMessageBean codeMessageBean = gsonCode.fromJson(JsonString, CodeMessageBean.class);
                    int code = codeMessageBean.getStatus().getCode();
                    if (code == 0) {
                        Gson gson = new Gson();
                        temperatureInfoBean = gson.fromJson(JsonString, GetTemperatureInfoBean.class);
                        Log.v(TAG, "getThermostatDevices--Successful:" + JsonString);
                    }

                    EventBus.getDefault().post(new SentTemperatureInfoMessage(temperatureInfoBean, 0));

                } catch (IOException e) {

                }
            }
        });
    }

    public static void RefreshAccessToken(String refreshToken) {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", ecobeeConfig.refresh_token)
                .add("code", refreshToken)
                .add("client_id", ecobeeConfig.APIKey)
                .build();

        final Request request = new Request.Builder()
                .url(ConstantUrls.Authorization.getAccessToken)
                .post(formBody)
                .build();

        EcoBeeHttpUtils.getInstance().getHttpObject().newCall(request).enqueue(new Callback() {

            private String refreshToken;
            private String accessToken;

            @Override
            public void onFailure(Call call, IOException e) {
                Log.v(TAG, "mHttpClient--onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v(TAG, "response--code:" + response.code());
                try {
                    String JsonString = response.body().string();
                    Gson gson = new Gson();
                    GetAccessTokenBean getAccessTokenBean = gson.fromJson(JsonString, GetAccessTokenBean.class);
                    accessToken = getAccessTokenBean.getAccess_token();
                    refreshToken = getAccessTokenBean.getRefresh_token();
                    EventBus.getDefault().post(new sentRefreshTokenMessage(refreshToken, accessToken, 200));

                } catch (IOException e) {
                }
            }
        });
    }
}

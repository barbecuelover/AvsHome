/*
 * Copyright 2017-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.zw.avshome.alexa.impl.AuthProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amazon.aace.alexa.AuthProvider;
import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.ScopeFactory;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.identity.auth.device.api.workflow.RequestContext;

import com.zw.avshome.utils.Constant;

//import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;



public class LoginWithAmazonBrowser extends Observable {

    private static final String sTag = "LoginWithAmazonBrowser";

    private static final String sAlexaAllScope = "alexa:all";
    private static final String sProfileScope = "profile";

    // To fetch User Profile data, set the sUserProfileEnabled to true
    // You will need additional parameters in your Security Profile for the profile scope request to succeed,
    // please see the README CBL section for more.
    private static final boolean sUserProfileEnabled = true;

    // Refresh access token 2 minutes before it expires
    private static final int sRefreshAccessTokenTime = 120000;
    // Access token expires after one hour
    private static final int sAccessTokenExpirationTime = 3600000;

    private final SharedPreferences mPreferences;
    private final AuthProviderHandler mAuthProvider;
    private final RequestContext mRequestContext;
    private String mProductID;
    private String mProductDSN;
    private boolean mHasApiKey = false;
    private Timer mTimer = new Timer();
    private TimerTask mRefreshTimerTask;
    private Context mContext;

    public LoginWithAmazonBrowser(Context context,
                                  SharedPreferences preferences,
                                  AuthProviderHandler authProvider) {
        mContext = context;
        mPreferences = preferences;
        mAuthProvider = authProvider;
        mHasApiKey = false;

        mProductID = mPreferences.getString(Constant.SpKeys.preference_product_id, "");
        mProductDSN = mPreferences.getString(Constant.SpKeys.preference_product_dsn, "");

        // Check for API key
        try {
            if (Arrays.asList(context.getResources().getAssets().list(""))
                    .contains("api_key.txt")) {
                mHasApiKey = true;
            } else {
                Log.d(sTag, "api_key.txt does not exist in assets folder");
            }
        } catch (IOException e) {
            Log.d(sTag, "Cannot find api_key.txt in assets folder");
        }

        mRequestContext = RequestContext.create(context);

        setupLWA();
    }

    private void setupLWA() {
        try {
            mRequestContext.registerListener(new AuthorizeListener() {

                @Override
                public void onSuccess(AuthorizeResult result) {
                    String accessToken = result.getAccessToken();
                    Log.d(sTag, "---" + accessToken);
                    if (accessToken != null && !accessToken.equals("")) {
                        mAuthProvider.setAuthToken(accessToken);
                        mAuthProvider.onAuthStateChanged(AuthProvider.AuthState.REFRESHED,
                                AuthProvider.AuthError.NO_ERROR);
                        setChanged();
                        notifyObservers("logged in");
//                        HubApplication.flagAlexaLogin = true;
//                        EventBus.getDefault().post(new SettingListRefreshMessage(false));
                        // show user profile info
                        if ( sUserProfileEnabled ) {
                            logUserProfile(result.getUser());
                        }

                        startRefreshTimer();

                    } else {
                        Log.d(sTag, "Authorization failed. Access token was not set.");
                    }

                }

                /* Inform the AuthProvider of auth failure  */
                @Override
                public void onError(AuthError ae) {
                    android.util.Log.d(sTag, "---false" + ae.getMessage());
                    mAuthProvider.onAuthStateChanged(AuthProvider.AuthState.UNINITIALIZED,
                            AuthProvider.AuthError.AUTHORIZATION_FAILED);
                }

                /* Authorization was cancelled before it could be completed. */
                @Override
                public void onCancel(AuthCancellation cancellation) {
                    android.util.Log.d(sTag, "---onCancel");
                }
            });

        } catch (Exception e) {
            Log.d(sTag, e.toString());
        }
    }

    public void login() {
        final JSONObject scopeData = new JSONObject();
        final JSONObject productInstanceAttributes = new JSONObject();
        if (mHasApiKey) {
            try {
                productInstanceAttributes.put("deviceSerialNumber", mProductDSN);
                scopeData.put("productInstanceAttributes", productInstanceAttributes);
                scopeData.put("productID", mProductID);

                // Save logged in method and access token
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(Constant.SpKeys.preference_login_method, LoginWithAmazon.LWA_LOGIN_METHOD_KEY);
                editor.apply();

                if ( sUserProfileEnabled ) {
                    AuthorizationManager.authorize( new AuthorizeRequest
                            .Builder( mRequestContext )
                            .addScopes( ScopeFactory.scopeNamed( sAlexaAllScope, scopeData ), ScopeFactory.scopeNamed( sProfileScope ) )
                            .forGrantType( AuthorizeRequest.GrantType.ACCESS_TOKEN )
                            .shouldReturnUserData( true )
                            .build()
                    );
                } else {
                    AuthorizationManager.authorize(new AuthorizeRequest
                            .Builder( mRequestContext )
                            .addScope( ScopeFactory.scopeNamed( sAlexaAllScope, scopeData ) )
                            .forGrantType( AuthorizeRequest.GrantType.ACCESS_TOKEN )
                            .shouldReturnUserData( false )
                            .build()
                    );
                }
            } catch ( Exception e ) { Log.e( sTag, e.getMessage() ); }

        } else Log.w( sTag, "Cannot authenticate. assets/api_key.txt does not exist" );
    }

    void logout() {
        AuthorizationManager.signOut(mContext.getApplicationContext(),
                new Listener<Void, AuthError>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (mRefreshTimerTask != null) mRefreshTimerTask.cancel();

                        // Save logged in method and access token
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString(Constant.SpKeys.preference_login_method, "");
                        editor.apply();

                        mAuthProvider.clearAuthToken();
                        mAuthProvider.onAuthStateChanged(AuthProvider.AuthState.UNINITIALIZED,
                                AuthProvider.AuthError.NO_ERROR);
                        setChanged();
                        notifyObservers("logged out");
//                        HubApplication.flagAlexaLogin = false;
//                        EventBus.getDefault().post(new SettingListRefreshMessage(false));
                    }

                    @Override
                    public void onError(AuthError ae) {
                        Log.d(sTag, "Unable to log out. Error: " + ae.getMessage());
                    }
                });
    }

    void onInitialize() {
        if (mHasApiKey) {
            try {
                if ( sUserProfileEnabled ) {
                    AuthorizationManager.getToken(mContext, new Scope[]{ScopeFactory.scopeNamed(sAlexaAllScope),
                            ScopeFactory.scopeNamed(sProfileScope)}, new TokenListener());
                } else {
                    AuthorizationManager.getToken(mContext, new Scope[]{ScopeFactory.scopeNamed(sAlexaAllScope)},
                            new TokenListener());
                }
            } catch ( Exception e ) { Log.e( sTag, e.getMessage() ); }
        }
    }

    void onResume() {
        try {
            if (mRequestContext != null) mRequestContext.onResume();
        }catch (Exception e){
            Log.e(sTag,e.toString());
        }
    }

    private class TokenListener implements Listener<AuthorizeResult, AuthError> {

        /* will authorize if client already has access token from previous session */
        @Override
        public void onSuccess(AuthorizeResult result) {
            Log.d(sTag, "TokenListener---onSuccess");
            String accessToken = result.getAccessToken();
            if (accessToken != null) {
                mAuthProvider.setAuthToken(accessToken);
                mAuthProvider.onAuthStateChanged(AuthProvider.AuthState.REFRESHED,
                        AuthProvider.AuthError.NO_ERROR);
                setChanged();
                notifyObservers("logged in");
                startRefreshTimer();
//                HubApplication.flagAlexaLogin = true;
            }
        }

        @Override
        public void onError(AuthError ae) {
            mAuthProvider.onAuthStateChanged(AuthProvider.AuthState.UNINITIALIZED,
                    AuthProvider.AuthError.AUTHORIZATION_FAILED);
            Log.d(sTag, "Authorization failed. Error: " + ae.getMessage());
//            HubApplication.flagAlexaLogin = false;
        }
    }

    private void startRefreshTimer() {
        mTimer.schedule(mRefreshTimerTask = new TimerTask() {
            public void run() {
                if ( mHasApiKey ) {
                    if ( sUserProfileEnabled ) {
                        AuthorizationManager.getToken(mContext, new Scope[]{ScopeFactory.scopeNamed(sAlexaAllScope),
                                ScopeFactory.scopeNamed(sProfileScope)}, new TokenListener());
                    } else {
                        AuthorizationManager.getToken(mContext, new Scope[]{ScopeFactory.scopeNamed(sAlexaAllScope)},
                                new TokenListener());
                    }
                }
            }
        }, sAccessTokenExpirationTime - sRefreshAccessTokenTime);
    }

    private void logUserProfile(User user) {
        if (user != null) {
            Log.d(sTag, String.format("User Profile: Name: %s, Email: %s, User ID: %s",
                    user.getUserName(), user.getUserEmail(), user.getUserId()));
            /*Store userProfile information*/
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(Constant.SpKeys.user_profile_name, user.getUserName());
            editor.putString(Constant.SpKeys.user_profile_id, user.getUserId());
            editor.putString(Constant.SpKeys.user_profile_email, user.getUserEmail());
            editor.apply();
        } else {
            Log.d(sTag, "Fetched user is null.");
        }
    }

}

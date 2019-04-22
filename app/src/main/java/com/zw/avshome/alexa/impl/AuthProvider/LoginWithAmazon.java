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

import com.amazon.aace.network.NetworkInfoProvider;
import com.zw.avshome.alexa.impl.NetworkInfoProvider.NetworkInfoProviderHandler;
import com.zw.avshome.utils.Constant;


import java.util.Observable;
import java.util.Observer;



class LoginWithAmazon implements Observer {

    static final String CBL_LOGIN_METHOD_KEY = "CBL";
    static final String LWA_LOGIN_METHOD_KEY = "LWA";

    private static final String sTag = "LoginWithAmazon";

    private final LoginWithAmazonBrowser mLwaBrowser;
    private final LoginWithAmazonCBL mLwaCBL;
    private final SharedPreferences mPreferences;

    private Context mContext;
	private final NetworkInfoProviderHandler mNetworkInfoProviderHandler;
    private String loginMethod = LWA_LOGIN_METHOD_KEY;

    LoginWithAmazon(
            Context context,
            AuthProviderHandler authProvider,
			NetworkInfoProviderHandler networkInfoProviderHandler) {
        mContext = context;
        mNetworkInfoProviderHandler = networkInfoProviderHandler;
        mPreferences = context.getSharedPreferences(Constant.SpKeys.preference_file_key, Context.MODE_PRIVATE);
        mLwaBrowser = new LoginWithAmazonBrowser(context, mPreferences, authProvider);
        mLwaCBL = new LoginWithAmazonCBL(context, mPreferences, authProvider);

//        setupGUI();
    }

    synchronized void onInitialize() {
//        String loginMethod = mPreferences.getString( Constant.SpKeys.preference_login_method , "" );
//        if ( !loginMethod.isEmpty() ) { //We were already logged in previous session.
//            if ( isConnected() ) {
                if ( loginMethod.equals( CBL_LOGIN_METHOD_KEY ) ) mLwaCBL.onInitialize();
                else if ( loginMethod.equals( LWA_LOGIN_METHOD_KEY ) ) mLwaBrowser.onInitialize();
//            } else {
//                AlertDialog.Builder builder = new AlertDialog.Builder( mContext );
//                builder.setTitle( "No Internet Connection" );
//                builder.setIcon( android.R.drawable.ic_dialog_alert );
//                builder.setMessage( "Cannot refresh connection for the logged in user.\nPlease verify your network settings and restart the app to retry or click 'Logout' to log out." );
//                builder.setCancelable( false );
//                builder.setPositiveButton( "OK", null );
//                builder.setNegativeButton( "Logout", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick( DialogInterface dialogInterface, int i ) {
//                        logout();
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
//        }
    }

    void onResume() {
        //String loginMethod = mPreferences.getString(Constant.SpKeys.preference_login_method, "");
        if (loginMethod.equals(LWA_LOGIN_METHOD_KEY)) mLwaBrowser.onResume();
    }

    public void logout() {
        //String loginMethod = mPreferences.getString(Constant.SpKeys.preference_login_method, "");
        if (loginMethod.equals(CBL_LOGIN_METHOD_KEY)) mLwaCBL.logout();
        else if (loginMethod.equals(LWA_LOGIN_METHOD_KEY)) mLwaBrowser.logout();
        else {
            /* to do something*/
        }
        Log.d(sTag, "Logout Called, but no Login method saved in preferences");
    }

    //
    // For updating GUI
    //

    public void login() {
        if (loginMethod.equals(LWA_LOGIN_METHOD_KEY)) {
            mLwaBrowser.login();
        } else mLwaCBL.login();
    }

    @Override
    public void update(final Observable o, final Object arg) {
        if (o instanceof LoginWithAmazonBrowser || o instanceof LoginWithAmazonCBL) {
            final String message = arg.toString();
            Log.d(sTag, "LoginWithAmazonBrowser--" + message.toString());
        }
    }

    private boolean isConnected() {
        return ( mNetworkInfoProviderHandler.getNetworkStatus().equals( NetworkInfoProvider.NetworkStatus.CONNECTED ) );
    }
}
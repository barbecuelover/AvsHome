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

package com.zw.avshome.alexa.impl.NetworkInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.amazon.aace.network.NetworkInfoProvider;
import com.zw.avshome.alexa.impl.AuthProvider.AuthProviderHandler;




public class NetworkInfoProviderHandler extends NetworkInfoProvider {

    private static final String sTag = "NetworkInfoProvider";

    private final Context mContext;
    //    private final LoggerHandler mLogger;
    private final WifiManager mWifiManager;
    private final ConnectivityManager mConnectivityManager;
    private final NetworkChangeReceiver mReceiver;
    //    private final TextView mNetworkStatusText;
    private NetworkStatus mStatus;
    private AuthProviderHandler mAuthProvider;

    public NetworkInfoProviderHandler(Context context) {
        mContext = context;
        mStatus = NetworkStatus.UNKNOWN;
//        mNetworkStatusText = activity.findViewById( R.id.networkStatus );

        // Note: >=API 24 should use NetworkCallback to receive network change updates
        // instead of CONNECTIVITY_ACTION
        mReceiver = new NetworkChangeReceiver();
        context.registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mWifiManager = ( WifiManager ) context.getSystemService( Context.WIFI_SERVICE );
        mConnectivityManager = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );

        updateNetworkStatus();

    }

    public void setAuthProvider(AuthProviderHandler authProvider) {
        mAuthProvider = authProvider;
    }

    @Override
    public NetworkStatus getNetworkStatus() {
        return mStatus;
    }

    @Override
    public int getWifiSignalStrength() {
        return mWifiManager.getConnectionInfo().getRssi();
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive( final Context context, final Intent intent )
        {
            if ( mConnectivityManager != null ) {
                updateNetworkStatus();
                int rssi = mWifiManager.getConnectionInfo().getRssi();

                Log.i( sTag, String.format( "Network status changed. STATUS: %s, RSSI: %s",
                        mStatus, rssi ) );
                updateGUI( mStatus );
                networkStatusChanged( mStatus, rssi );

                // Notify AuthProvider to login if we aren't logged on yet
                if (mStatus == NetworkStatus.CONNECTED && mAuthProvider != null) {
                    Log.i( sTag, String.format( "Calling auth provider to reinitialize if needed" ));
                    mAuthProvider.onInitialize();
                }
            }
        }
    }

    public void unregister() { mContext.getApplicationContext().unregisterReceiver( mReceiver ); }

    private void updateGUI( final NetworkInfoProvider.NetworkStatus status ) {
//        mActivity.runOnUiThread( new Runnable() {
//            @Override
//            public void run() {
//                mNetworkStatusText.setText( status != null ? status.toString() : "" );
//            }
//        } );
    }
    private void updateNetworkStatus() {
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        if ( activeNetwork != null ) {
            NetworkInfo.State state = activeNetwork.getState();
            switch ( state ) {
                case CONNECTED:
                    mStatus = NetworkStatus.CONNECTED;
                    break;
                case CONNECTING:
                    mStatus = NetworkStatus.CONNECTING;
                    break;
                case DISCONNECTING:
                    mStatus = NetworkStatus.DISCONNECTING;
                    break;
                case DISCONNECTED:
                case SUSPENDED:
                    mStatus = NetworkStatus.DISCONNECTED;
                    break;
                case UNKNOWN:
                    mStatus = NetworkStatus.UNKNOWN;
                    break;
            }
        } else {
            mStatus = NetworkStatus.UNKNOWN;
        }
    }
}

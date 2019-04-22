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

package com.zw.avshome.alexa.impl.AlexaClient;

import android.content.Context;
import android.util.Log;

import com.amazon.aace.alexa.AlexaClient;
import com.zw.avshome.alexa.interfaces.AlexaClientConnectStateListener;
import com.zw.avshome.alexa.interfaces.AvsStateChangeListener;


public class AlexaClientHandler extends AlexaClient {

    private static final String sTag = "AlexaClient";
    private final Context mContext;
    private ConnectionStatus mConnectionStatus = ConnectionStatus.DISCONNECTED;

    public AlexaClientHandler(Context context) {
        mContext = context;
    }

    /* dialogStateChanged callback*/
    private AvsStateChangeListener avsStateChangeListener;

    public void setAvsStateChangeListener(AvsStateChangeListener listener) {
        avsStateChangeListener = listener;
    }

    /* connectionStatusChanged callack*/
    private AlexaClientConnectStateListener alexaClientConnectStateListener;

    public void setAlexaClientConnectStateChange(AlexaClientConnectStateListener listener) {
        alexaClientConnectStateListener = listener;
    }

    @Override
    public void dialogStateChanged(final DialogState state) {
        try {
            avsStateChangeListener.setAvsStateChangeListener(state);
        } catch (Exception e) {
            Log.d(sTag, e.toString());
        }
    }

    @Override
    public void authStateChanged(final AuthState state, final AuthError error) {


        if (error == AuthError.NO_ERROR) {
            /* */
        } else {
            /* */
        }

    }

    @Override
    public void connectionStatusChanged(final ConnectionStatus status,
                                        final ConnectionChangedReason reason) {
        mConnectionStatus = status;
        alexaClientConnectStateListener.setAlexaClientConnectStateListener(status);
    }

    public ConnectionStatus getConnectionStatus() {
        return mConnectionStatus;
    }

}

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

package com.zw.avshome.alexa.impl.Alerts;

import android.content.Context;
import android.util.Log;

import com.amazon.aace.alexa.Alerts;
import com.amazon.aace.alexa.MediaPlayer;
import com.amazon.aace.alexa.Speaker;
import com.zw.avshome.alexa.impl.MediaPlayer.MediaPlayerHandler;
import com.zw.avshome.alexa.interfaces.AlertListener;



public class AlertsHandler extends Alerts {

    private static final String sTag = "Alerts";

    private final Context mContext;

    private AlertListener mAlertListener;

    public void setAlertListener(AlertListener alertListener) {
        mAlertListener = alertListener;
    }

    public AlertsHandler(Context context,
                         MediaPlayer mediaPlayer,
                         Speaker speaker) {
        super(mediaPlayer, speaker);
        mContext = context;
    }

    public AlertsHandler(Context context,
                         MediaPlayerHandler mediaPlayer) {
        this(context, mediaPlayer, mediaPlayer.getSpeaker());
    }

    @Override
    public void alertCreated(String alertToken, String detailedInfo) {
        mAlertListener.setAlertCreateListener(alertToken, detailedInfo);
        Log.i(sTag, String.format("Alert Created. TOKEN: %s, Detailed Info payload: %s", alertToken, detailedInfo));
    }

    @Override
    public void alertDeleted(String alertToken) {
        mAlertListener.setAlertDeleteListener(alertToken);
        Log.i(sTag, String.format("Alert Deleted. TOKEN: %s", alertToken));
    }

    private void onLocalStop() {
        Log.i(sTag, "Stopping active alert");
        super.localStop();
    }

    private void onRemoveAllAlerts() {
        Log.i(sTag, "Removing all pending alerts from storage");
        super.removeAllAlerts();
    }

    private void setupGUI() {

//        mStateText = mActivity.findViewById( R.id.alertState );
//
//        mActivity.findViewById( R.id.stopAlertButton ).setOnClickListener(
//            new View.OnClickListener() {
//                @Override
//                public void onClick( View v ) { onLocalStop(); }
//            }
//        );
//
//        mActivity.findViewById( R.id.removeAlertsButton ).setOnClickListener(
//            new View.OnClickListener() {
//                @Override
//                public void onClick( View v ) { onRemoveAllAlerts(); }
//            }
//        );
    }
}

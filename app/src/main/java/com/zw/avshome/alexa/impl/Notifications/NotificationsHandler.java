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

package com.zw.avshome.alexa.impl.Notifications;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.amazon.aace.alexa.MediaPlayer;
import com.amazon.aace.alexa.Notifications;
import com.amazon.aace.alexa.Speaker;
import com.zw.avshome.alexa.impl.MediaPlayer.MediaPlayerHandler;


public class NotificationsHandler extends Notifications {

    private static final String sTag = "Notifications";

    private final Context mContext;
    //    private final LoggerHandler mLogger;
    private TextView mStateText;
    private AlexaNotification alexaNotification;

    public NotificationsHandler(Context context,
                                MediaPlayer mediaPlayer,
                                Speaker speaker) {
        super(mediaPlayer, speaker);
        mContext = context;
        alexaNotification = new AlexaNotification(mContext);
    }

    public NotificationsHandler(Context context,
//                                 LoggerHandler logger,
                                MediaPlayerHandler mediaPlayer) {
        this(context, mediaPlayer, mediaPlayer.getSpeaker());
    }

    @Override
    public void setIndicator(final IndicatorState state) {
        Log.i( sTag, "Notifications ----Indicator State: " + state.toString() );
        String AlexaNotificationState = state.toString();
        if (AlexaNotificationState.equals("ON")){
            alexaNotification.createNotification();
        }else if(AlexaNotificationState.equals("OFF")){
            alexaNotification.clearNotification();
        }else {

        }

    }

}

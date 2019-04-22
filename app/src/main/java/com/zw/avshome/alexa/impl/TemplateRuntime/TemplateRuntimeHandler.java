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

package com.zw.avshome.alexa.impl.TemplateRuntime;

import androidx.annotation.Nullable;
import android.util.Log;

import com.amazon.aace.alexa.TemplateRuntime;
import com.zw.avshome.alexa.impl.PlaybackController.PlaybackControllerHandler;
import com.zw.avshome.alexa.interfaces.ClearTemplateListener;
import com.zw.avshome.alexa.interfaces.PlayerInfoListener;
import com.zw.avshome.alexa.interfaces.TemplateListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TemplateRuntimeHandler extends TemplateRuntime {

    private final String sTag = "TemplateRuntime";
    /*TemplateListener*/
    private TemplateListener mTemplateListener;
    /*PlayerInfoListener*/
    private PlayerInfoListener mPlayerInfoListener;
    private final PlaybackControllerHandler mPlaybackController;

    private List<ClearTemplateListener> templateList = new ArrayList<>();

    public TemplateRuntimeHandler(@Nullable PlaybackControllerHandler playbackController) {
        mPlaybackController = playbackController;
    }

    public void setTemplateListener(TemplateListener templateListener) {
        mTemplateListener = templateListener;
    }

    public void setClearTemplateListener(ClearTemplateListener clearTemplateListener) {
        synchronized (templateList){
            Log.i(sTag,"add listener ="+clearTemplateListener);
            templateList.add(clearTemplateListener);
        }
    }

    public void setPlayerInfoListener(PlayerInfoListener playerInfoListener) {
        mPlayerInfoListener = playerInfoListener;
    }


    @Override
    public void renderTemplate(String payload) {
        try {
            // Log payload
            Log.d(sTag, "handle renderTemplate ");
            JSONObject template = new JSONObject(payload);
            String type = template.getString("type");
            mTemplateListener.alexaTemplateListener(type, template);
        } catch (JSONException e) {
            Log.d(sTag, e.toString());
        }
    }

    @Override
    public void renderPlayerInfo(String payload) {
        Log.d(sTag, "handle renderPlayerInfo ");
        clearTemplate();
        try {
            JSONObject playerInfo = new JSONObject(payload);
            if (mPlaybackController != null){
                mPlayerInfoListener.onRenderPlayerInfo(playerInfo);
            }
        } catch (JSONException e) {
            Log.d(sTag, e.toString());
        }

    }

    @Override
    public void clearTemplate() {
        try {
            synchronized (templateList){
                Iterator<ClearTemplateListener> iterator = templateList.listIterator();
                while (iterator.hasNext()){
                    ClearTemplateListener listener = iterator.next();
                    listener.onClearTemplate();
                    iterator.remove();
                    Log.d(sTag, "handle clearTemplate succeed ="+listener.toString());
                }
            }
        } catch (Exception e) {
            Log.d(sTag, e.toString());
        }
    }

    @Override
    public void clearPlayerInfo() {
        Log.d(sTag, "handle clearPlayerInfo()");
        if (mPlaybackController != null){
            mPlaybackController.setPlayerInfo("", "", "");
            mPlaybackController.hidePlayerInfoControls();
            mPlayerInfoListener.onClearPlayerInfo();
        }

    }
}

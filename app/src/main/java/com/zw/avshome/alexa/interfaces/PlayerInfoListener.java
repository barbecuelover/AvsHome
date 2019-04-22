package com.zw.avshome.alexa.interfaces;

import org.json.JSONObject;

public interface PlayerInfoListener {
    void onRenderPlayerInfo(JSONObject template);
    void onClearPlayerInfo();
}

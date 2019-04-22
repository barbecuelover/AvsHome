package com.zw.avshome.alexa.interfaces;

import org.json.JSONObject;

public interface TemplateListener {
    void alexaTemplateListener(String type, JSONObject template);
}

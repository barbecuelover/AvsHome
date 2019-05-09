package com.ecobee;

/**
 * 作者：RedKeyset on 2018/12/13 17:29
 * 邮箱：redkeyset@aliyun.com
 */
public class ConstantUrls {
    private static String SERVERURL = "https://api.ecobee.com";

    public static class Authorization {
        //GET https://api.ecobee.com/authorize?
        //        response_type=code&
        //        client_id=APP_KEY&
        //        redirect_uri=YOUR_SERVER_URI&
        //        scope=SCOPE&
        //        state=YOUR_STATE
        public static final String ShowPIN = SERVERURL + "/authorize?response_type=%s&client_id=%s&scope=%s";
        public static final String getAccessToken = SERVERURL + "/token";
        public static final String getCurrentTemperature = SERVERURL + "/1/thermostat?format=json&body={\"selection\":{\"selectionType\":\"registered\",\"includeSensors\":true}}";
    }
}

package com.zw.avshome.utils;

import android.os.Environment;

/**
 * 作者：RedKeyset on 2018/10/11 02:41
 * 邮箱：redkeyset@aliyun.com
 */
public class Constant {

    public static final String D5_FIRMWARE_FILE_FULL_PATH =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartHomeHub/d5firmware.bin";

    public class Intent {

        public static final String UPDATE_COMMON_APP = "updateCommonApp";
        public static final String ACTION_MIC_MUTE = "com.ecs.action.micmute";
        public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
        public static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
        public static final String EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";

    }

    public class MqttInfo {

        public static final String PRODUCT_ID = "123456";
        public static final String PRODUCT_SN = "sn123456";
        public static final String PRODUCT_NAME = "name123456";
        public static final String PRODUCT_MAC = "mac123456";
        public static final String FWVER = "1.0";
        public static final String SERVER = "tcp://172.28.32.100";
        public static final int PORT = 1883;

        public static final String SHARED_PRE_FILE_NAME = "hubConfig";
        public static final String SHARED_PRE_KEY_TOKEN = "hubToken";
        public static final String SHARED_PRE_KEY_USER_NAME = "userName";
        public static final String SHARED_PRE_KEY_USER_PWD = "userPwd";
        public static final String SERVER_URL = "tcp://172.28.32.100:1883";
        public static final String TOPIC_CONNECT = "Hub.HubConnect";
        public static final String TOPIC_REPORT = "Hub.HubReport";

        public static final String USER_NAME = "sn";
        public static final String USER_PWD = "sn";

    }

    public class Device {

        public static final String SWITCH = "switch";
        public static final String LIGHT = "light";
        public static final String MEDIA_PLAYER = "MediaPlayer";
        public static final String LOCK = "Lock";
        public static final String CAMERA = "Camera";
        public static final String AIR_CONDITIONER = "AirConditioner";
        public static final String SENSOR = "Sensor";
        public static final String FAN = "Fan";
        public static final String DEVICE_TRAKER = "DeviceTracker";
    }

    public class Company {
        public static final String PHILIPS = "Philips";
        public static final String PHILIPS_HUELIGHT = "HueLight";

    }

    public class DeviceSpecificType {
        public static final String SWITCH = "";
        public static final String SWITCH_BROADLINKMP1 = "com.ecs.smarthub.devices.switchs.BroadlinkMp1";
        public static final String MEDIA_PLAYER = "MediaPlayer";
        public static final String AUGUST_LOCK = "com.ecs.smarthub.devices.lock.AugustLock";
        public static final String CAMERA = "Camera";
        public static final String NEST_CAMERA = "com.ecs.smarthub.devices.camera.NestCamera";
        public static final String AIR_CONDITIONER = "AirConditioner";
        public static final String SENSOR = "Sensor";
        public static final String FAN = "Fan";
        public static final String DEVICE_TRAKER = "DeviceTracker";

        public static final String LIGHT_PHILIPSHUELIGHT = "com.ecs.smarthub.devices.light.PhilipsHueLight";
        public static final String SENSOR_ECOBEE_TEMPERATURE = "com.ecs.smarthub.devices.sensor.EcoBeeTemperature";
        public static final String SENSOR_ECOBEE_REMOTESENSORS = "com.ecs.smarthub.devices.sensor.EcoBeeRemoeSensor";
    }

    public class Json {
        /**
         * JsonString main body
         */
        public static final String PRODUCT_ID = "ProductID";
        public static final String TYPE = "Type";
        public static final String TOKEN = "Token";
        public static final String TIME_STAMP = "TimeStamp";
        public static final String DATA = "Data";

        /**
         * First time connect DataJsonString
         */
        public static final String PRODUCT_SN = "ProductSN";
        public static final String PRODUCT_NAME = "ProductName";
        public static final String PRODUCT_MAC = "ProductMac";
        public static final String FWVER = "FWVer";
        public static final String SERVER = "Server";
        public static final String PORT = "Port";


        /**
         * First connect Data jsonString from Server reply
         */
        public static final String USER_NAME = "Username";
        public static final String USER_PWD = "Password";

        /**
         * All Type
         */
        public static final String TYPE_CCONNECT = "CConnect";
        public static final String TYPE_CREPORT = "CReport";
        public static final String TYPE_SRESP_CCONNECT = "SRespCConnect";
        public static final String TYPE_SRESP_CREPORT = "SRespCReport";

        public static final String TYPE_SCOMMAND = "SCommand";
        public static final String TYPE_CRESP_SCOMMAND = "CRespSCommand";

        /**
         * Data JsonString  from Server SCommand
         */
        public static final String DEVICE_SN = "DeviceSN";
        public static final String DEVICE_TYPE = "DeviceType";

        public static final String COMMAND = "Command";
        public static final String COMMAND_CHANGE_STATUS = "ChangeStatus";
        public static final String COMMAND_REPORT_INFO = "ReportInfo";

        public static final String TARGET_STATUS = "TargetStatus";
        public static final String INDEX = "Index";
        public static final String CURRENT_STATUS = "CurrentStatus";

        /**
         * reply content
         */
        public static final String STATUS = "Status";
        public static final String OK = "OK";
        public static final String ERROR = "Error";
        public static final String ERROR_MSG = "ErrorMsg";


        /**
         * Status value
         */
        public static final int STATUS_OK = 2000;
        public static final int STATUS_ERROR_TOKEN = 2000;
        public static final int STATUS_ERROR_DEVICE_NULL = 3001;
        public static final int STATUS_ERROR_DEVICE_NOT_ONLINE = 3002;

        public static final int STATUS_ERROR_DEVICE_CHANGE_STATUS_FAILED = 3101;


    }

    public class NestConfig {

        /**
         * Replace this with your Nest Product Client ID in the OAuth section.
         */
        public static final String CLIENT_ID = "a28af8f5-fa0a-4322-beb9-94cb80e41a33";

        /**
         * Replace this with your Nest Product Client Secret in the OAuth section. Keep this secret safe.
         */
        public static final String CLIENT_SECRET = "jWvh47NHKbeMK9tE9x12pX2BB";

        /**
         * Replace this with your Nest Product Redirect URI above the OAuth section (not the Authorization URL).
         * Remember to set this Url in Product Configuration (click the edit button on that product view).
         *
         * @see <a href="https://developers.nest.com/documentation/cloud/how-to-auth#redirect_uri_experience">Redirect URI experience</a>
         */
        public static final String REDIRECT_URL = "http://localhost:8080/auth/nest/callback"; //ex: "http://localhost/"

        public static final int AUTH_TOKEN_REQUEST_CODE = 123;

    }


    /**
     * 存储 Alexa Auth相关的SharePreference config
     */
    public class AlexaAuthConfig {


        /* Alexa Config SharedPreference*/
        public static final String SP_FILE_NAME = "alexa_auth_config";

        public static final String SP_KEY_CLIENT_ID = "client_id";
        public static final String SP_KEY_CLIENT_SECRET = "client_secret";
        public static final String SP_KEY_PRODUCT_ID = "product_id";
        public static final String SP_KEY_PRODUCT_DSN = "product_dsn";

        public static final String SP_KEY_USER_EMAIL = "user_email";
        public static final String SP_KEY_USER_ID = "user_id";
        public static final String SP_KEY_USER_NAME = "user_name";

    }


    public class Setting {
        public static final String ALEXASETTING = "Alexa";
        public static final String THEMESETTING = "Theme";
        public static final String THIRDETTING = "Third";
        public static final String FIRMWARE = " D5 FW Update";
    }

    public class Notification {
        public static final String SETACTION = "SETACTION";
        public static final String DATA = "DATA";

        public static final String WIDGETPREV = "WIDGETPREV";
        public static final String WIDGETPLAY = "WIDGETPLAY";
        public static final String WIDGETPAUSE = "WIDGETPAUSE";
        public static final String WIDGETNEXT = "WIDGETNEXT";
        public static final String WIDGETUPIMAGE = "WIDGETUPIMAGE";

        public static final String GOMEDIAPLAYACTIVITY = "GOMEDIAPLAYACTIVITY";

        public static final String ALEXAPLAY = "ALEXAPLAY";
        public static final String ALEXAPAUSE = "ALEXAPAUSE";
        public static final String ALEXASTOP = "ALEXASTOP";
    }
}

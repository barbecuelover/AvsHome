package com.august;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.august.AugustApi.AugustLockStatus.UNKNOWN;

public class AugustApi {

    private static final String TAG ="AugustApi" ;

    private final String HEADER_ACCEPT_VERSION = "Accept-Version";
    public  static final String HEADER_AUGUST_ACCESS_TOKEN = "x-august-access-token";
    private final String HEADER_AUGUST_API_KEY = "x-august-api-key";
    private final String HEADER_KEASE_API_KEY = "x-kease-api-key";
    private final String HEADER_CONTENT_TYPE = "Content-Type";
    private final String HEADER_USER_AGENT = "User-Agent";

    private final String HEADER_VALUE_API_KEY = "79fd0eb6-381d-4adf-95a0-47721289d1d9";
    private final String HEADER_VALUE_CONTENT_TYPE = "application/json";
    private final String HEADER_VALUE_USER_AGENT = "August/Luna-3.2.2";
    private final String HEADER_VALUE_ACCEPT_VERSION = "0.0.1";

    private final String API_BASE_URL = "https://api-production.august.com";
    private final String API_GET_SESSION_URL = API_BASE_URL + "/session";

    private final   String API_GET_DOORBELLS_URL = API_BASE_URL + "/users/doorbells/mine";
    private final   String API_GET_HOUSES_URL = API_BASE_URL + "/users/houses/mine";
    private final   String API_GET_LOCKS_URL = API_BASE_URL + "/users/locks/mine";

    private String[] LOCKED_STATUS ={"locked", "kAugLockState_Locked"};
    private String[] UNLOCKED_STATUS ={"unlocked", "kAugLockState_Unlocked"};
    private String[] CLOSED_STATUS ={"closed", "kAugLockDoorState_Closed"};
    private String[] OPEN_STATUS ={"open", "kAugLockDoorState_Open"};

    private Map<String,String> API_VALIDATE_VERIFICATION_CODE_URLS = new HashMap<String,String>(){
        {
            put("phone", API_BASE_URL + "/validate/phone");
            put("email", API_BASE_URL + "/validate/email");
        }
    };

    private Map<String,String> API_SEND_VERIFICATION_CODE_URLS = new HashMap<String,String>(){
        {
            put("phone", API_BASE_URL + "/validation/phone");
            put("email", API_BASE_URL + "/validation/email");
        }
    };


    private  String API_GET_HOUSE_ACTIVITIES_URL(String house_id){
        return API_BASE_URL + "/houses/"+house_id+"/activities";
    }

    private  String API_GET_DOORBELL_URL(String doorbell_id){
        return API_BASE_URL + "/doorbells/"+doorbell_id;
    }

    private  String API_WAKEUP_DOORBELL_URL(String doorbell_id){
        return API_BASE_URL + "/doorbells/"+doorbell_id +"/wakeup";
    }

    private  String API_GET_HOUSE_URL(String house_id){
        return API_BASE_URL + "/houses/" +house_id;
    }

    private  String API_GET_LOCK_URL(String lock_id){
        return API_BASE_URL + "/locks/" +lock_id;
    }
    private  String API_GET_LOCK_STATUS_URL(String lock_id){
        return API_BASE_URL + "/locks/"+lock_id+"/status";
    }

    private  String API_GET_PINS_URL(String lock_id){
        return API_BASE_URL + "/locks/"+lock_id+"/pins";
    }

    private  String API_LOCK_URL(String lock_id){
        return API_BASE_URL + "/remoteoperate/"+lock_id+"/lock";
    }

    private  String API_UNLOCK_URL(String lock_id){
        return API_BASE_URL + "/remoteoperate/"+lock_id+"/unlock";
    }

    private OkHttpClient mHttpClient;

    /**
     */
    public AugustApi() {
        mHttpClient= new OkHttpClient();
    }

    private Map<String,String> getApiHeader(String accessToken){
        Map<String,String> header = new HashMap<>();
        header.put(HEADER_ACCEPT_VERSION,HEADER_VALUE_ACCEPT_VERSION);
        header.put(HEADER_AUGUST_API_KEY,HEADER_VALUE_API_KEY);
        header.put(HEADER_KEASE_API_KEY,HEADER_VALUE_API_KEY);
        header.put(HEADER_CONTENT_TYPE,HEADER_VALUE_CONTENT_TYPE);
        header.put(HEADER_USER_AGENT,HEADER_VALUE_USER_AGENT);
        if (accessToken!=null && !accessToken.equals("")){
            header.put(HEADER_AUGUST_ACCESS_TOKEN,accessToken);
        }
        return header;
    }

    private Response callApi(String method,String url,String accessToken,String jsonStrbody){
        Headers headers;
        Headers.Builder builder= new Headers.Builder();

        Map<String,String> head =getApiHeader(accessToken);

        Set<Map.Entry<String, String>> set = head == null ? null : head.entrySet();
        if (set != null) {
            for (Map.Entry<String, String> entry : set) {
                builder.add(entry.getKey().trim(),entry.getValue().trim());
            }
        }

        headers = builder.build();
        if (jsonStrbody==null){
            jsonStrbody="";
        }
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, jsonStrbody);
        Response response= null;
        if (method.equals("post")){
            /**
             * post
             */
            final Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(requestBody)
                    .build();
            Call call =mHttpClient.newCall(request);
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (method.equals("get")){
            /**
             * get
             */
            final Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .build();
            
            Call call = mHttpClient.newCall(request);
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            /**
             * put
             */
            final Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .put(requestBody)
                    .build();
            Call call = mHttpClient.newCall(request);
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }



    protected Response getSession(String installID,String indentifier,String password){
        String json ="{\"installId\": \""+installID+"\" ,\"identifier\":\""+indentifier+"\",\"password\":\""+password+"\"}";
        Response response = callApi( "post",API_GET_SESSION_URL,null,json);
        return response;
    }



    protected boolean validateVerificationCode(String access_token, String login_method, String username, String verification_code){
        String json ="{\""+login_method+"\":\""+username+"\",\"code\":"+"\""+verification_code+"\"}";
        Response response = callApi( "post",API_VALIDATE_VERIFICATION_CODE_URLS.get(login_method),access_token,json);
        String body= "";
        try {
            if (response!=null &&response.body()!=null)
            body= response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
         String resolution ="";
        try {
            JSONObject jsonObject = new JSONObject(body);
            resolution = jsonObject.getString("resolution");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return !"".equals(resolution);
    }

    protected boolean sendVerificationCode(String access_token,String login_method,String username){
        String json ="{\"value\":\""+username+"\"}";
        Response response = callApi("post",API_SEND_VERIFICATION_CODE_URLS.get(login_method),access_token,json);
        return (response != null);
    }


    public String getLocks(String access_token){
        String body="";
        Response response = callApi("get",API_GET_LOCKS_URL,access_token,"");
        if (response!=null){
            try {
                body= response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }

    public boolean lock(String access_token,String lock_id){
        Response response = callApi("put",API_LOCK_URL(lock_id),access_token,"");
        try {
            if (response!=null){
                String bodyStr =response.body().string() ;
                JSONObject object = new JSONObject(bodyStr);
                String state = object.getString("status");
                state = determineLockStatus(state);
                if (AugustLockStatus.LOCKED.equals(state)){
                    //"lockStatusChanged":false ??
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
//{"status":"kAugLockState_Unlocked","info":{"action":"unlock","startTime":"2018-11-22T07:44:32.222Z",
// "context":{"transactionID":"iSR4J-WqpV","startDate":"2018-11-22T07:44:32.217Z","retryCount":1,"connectStartDate":"2018-11-22T07:44:32.225Z","connectedDate":"2018-11-22T07:44:36.270Z","operationDoneDate":"2018-11-22T07:44:38.400Z"},
// "lockType":"lock_version_3","serialNumber":"L3FXJ04YX9","rssi":-65,"wlanRSSI":-27,"wlanSNR":39,"duration":6181,"lockID":"AA324635C3DD45AB942E514956F588ED","bridgeID":"5bf2a8c08044ee001e543d67","lockStatusChanged":false,"serial":"C4WX80058J"},
// "doorState":"kAugDoorState_Init","retryCount":1,"totalTime":6200,"resultsFromOperationCache":false}
    public boolean unlock(String access_token,String lock_id){

        Response response = callApi("put",API_UNLOCK_URL(lock_id),access_token,null);
        try {
            if (response!=null){
                String bodyStr =response.body().string() ;
                JSONObject object = new JSONObject(bodyStr);
                String state = object.getString("status");
                state = determineLockStatus(state);
                if (AugustLockStatus.UNLOCKED.equals(state)){
                    //"lockStatusChanged":false ??
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

//{"status":"unknown_error_during_connect","dateTime":"2018-12-05T12:09:43.203Z","isLockStatusChanged":true,"valid":true,"doorState":"unknown"}
   //getLockStatus: {"status":"unlocked","dateTime":"2018-11-22T07:44:32.203Z","isLockStatusChanged":false,"valid":true,"doorState":"init"}
    public String getLockStatus(String access_token,String lock_id,boolean door_status){
        Response response = callApi("get",API_GET_LOCK_STATUS_URL(lock_id),access_token,null);
        String status = AugustLockStatus.UNKNOWN;
        try {
            if (response!=null){
                String bodyStr =response.body().string() ;

                JSONObject object = new JSONObject(bodyStr);
                String state = object.getString("status");
                if (state!=null &&!state.equals("")){
                    status = determineLockStatus(state);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }



    public void getLockDetail(String access_token,String lock_id){
        callApi("get", API_GET_LOCK_URL(lock_id),access_token,null);
    }

    public void getOperableLocks(String access_token){

    }








    public class AugustLockStatus{
        public final static String LOCKED = "locked";
        public final static String UNLOCKED ="unlocked";
        public final static String UNKNOWN = "unknown";
        public final static String CLOSED = "closed";
        public final static String OPEN = "open";
    }

    private String determineLockStatus(String status){
        if (haveString(LOCKED_STATUS,status)){
            return AugustLockStatus.LOCKED;
        }else if (haveString(UNLOCKED_STATUS,status)){
            return AugustLockStatus.UNLOCKED;
        }else {
            return UNKNOWN;
        }
    }

    private String determineLockDoorStatus(String status){
        if (haveString(CLOSED_STATUS,status)){
            return AugustLockStatus.CLOSED;
        }else if (haveString(OPEN_STATUS,status)){
            return AugustLockStatus.OPEN;
        }else {
            return UNKNOWN;
        }
    }

    private  boolean haveString(String[] arr,String targetValue){
        for(String s:arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }

}

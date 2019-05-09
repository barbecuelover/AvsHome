package cn.com.broadlink.networkapi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;


import java.io.Serializable;

/**
 * using {@link #getNetworkAPIInstance(Context)} to get create or get the  NetworkAPI object
 * And call the {@link #destoryInstance()} to release the object when it's not necessary like
 * the {@link #mContext} destroy .
 */
public class NetworkAPI implements Serializable{

    private static final String TAG = "NetworkAPI";
    private  static NetworkAPI mNetworkAPIInstance;
    private  static Context mContext;

    static
    {
        System.loadLibrary("NetworkAPI");
    }

    public static  NetworkAPI getNetworkAPIInstance(Context context){

        if (mNetworkAPIInstance == null){
            mNetworkAPIInstance =new NetworkAPI();
            mContext =context;
        }
        return mNetworkAPIInstance;
    }

    /**
     * release memory ,should be called after {@link #deviceEasyConfig(String)}
     */
    public void destoryInstance()
    {
        if (mNetworkAPIInstance != null) {
            mNetworkAPIInstance = null;
        }
        if (mContext != null) {
            mContext = null;
        }
    }


    /**
     *
     * @param paramString JsonString
     * @return json result
     * {"devaddr":"192.168.1.103","mac":"34:ea:34:c2:31:98","did":"0000000000000000000034ea34c23198","status":0,"msg":"success"}
     * {"status":-4000,"msg":"fail"}
     */
    public String deviceEasyConfig(String paramString){

        WifiManager.MulticastLock localMulticastLock = ((WifiManager)mContext.getSystemService(Context.WIFI_SERVICE)).createMulticastLock("BroadLinkMulticastLock");
        localMulticastLock.acquire();
        String str = bl_easyconfig(paramString);
        localMulticastLock.release();

        Log.i(TAG, "deviceEasyConfig: result = "+str);

        return str;

    }


    private native String bl_easyconfig(String paramString);

    /**
     * cancel the deviceEasyConfig()
     *
     * {"status":0,"msg":"success"}
     * @return jsonString result
     */
    public native String deviceEasyConfigCancel();

    public native String deviceAPConfig(String paramString);

    public native String deviceGetAPList(String paramString);
}

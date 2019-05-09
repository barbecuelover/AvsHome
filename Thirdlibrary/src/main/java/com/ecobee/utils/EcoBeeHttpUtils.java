package com.ecobee.utils;

import okhttp3.OkHttpClient;

/**
 * 作者：RedKeyset on 2018/12/17 12:34
 * 邮箱：redkeyset@aliyun.com
 */
public class EcoBeeHttpUtils {
    private static EcoBeeHttpUtils instance;// 单例
    private OkHttpClient okHttpClient;

    private EcoBeeHttpUtils() {
        okHttpClient = new OkHttpClient();
    }

    public static EcoBeeHttpUtils getInstance() {
        if (instance == null) {
            synchronized (EcoBeeHttpUtils.class) {
                if (instance == null) {
                    instance = new EcoBeeHttpUtils();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getHttpObject() {
        return okHttpClient;
    }
}

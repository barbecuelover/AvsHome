package com.zw.avshome;

import android.app.Application;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class HomeApplication extends Application {

    private static HomeApplication context;
    public static boolean isAlexaLogin = false;

    public static HomeApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

}

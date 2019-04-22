package com.zw.avshome.home.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.zw.avshome.R;


public abstract class ParentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    public abstract void initView();
    public abstract void initData();
    public abstract void initEvent();
    public abstract Activity getActivity();

    /**
     * 销毁指定 Activity Class
     * @param clsList
     */
    protected void finishActivityList(Class<?>... clsList) {
        for (Class<?> aClass : clsList) {
//            ActivityManger.finishActivity(aClass);
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    protected void finishActivity() {
        finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}

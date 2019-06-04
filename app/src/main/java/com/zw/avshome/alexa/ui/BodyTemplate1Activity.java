package com.zw.avshome.alexa.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;


import com.zw.avshome.home.base.ParentActivity;
import com.zw.avshome.R;
import com.zw.avshome.alexa.interfaces.ClearTemplateListener;
import com.zw.avshome.alexa.AlexaService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class BodyTemplate1Activity extends ParentActivity {

    private String sTag = "BodyTemplate1Activity";

    private Activity mActivity;

    private TextView mMainTitle;
    private TextView mSubTitle;
    private TextView mTextField;
    private static JSONObject mJsonObject;

    private final int TIME_DELAY = 10000;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mActivity!=null){
                finishActivity();
            }
        }
    };

    public static Intent createIntent(Context context, JSONObject jsonObject) {
        mJsonObject = jsonObject;
        return new Intent(context, BodyTemplate1Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_body_template1);
        EventBus.getDefault().register(this);
        mActivity = this;
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
        mHandler.removeCallbacks(runnable);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        mMainTitle = (TextView) findViewById(R.id.mainTitle);
        mSubTitle = (TextView) findViewById(R.id.subTitle);
        mTextField = (TextView) findViewById(R.id.textField);
    }

    @Override
    public void initData() {
        configureBodyTemplate1(mJsonObject);
    }

    @Override
    public void initEvent() {
        setClearTemplateListener();
    }

    private void configureBodyTemplate1(JSONObject template) {
        try {
            if (template.has("title")) {
                JSONObject title = template.getJSONObject("title");
                if (title.has("mainTitle")) {
                    String mainTitle = title.getString("mainTitle");
                    mMainTitle.setText(mainTitle);
                    mTextField.setTextSize(22);
                }

                if (title.has("subTitle")) {
                    String subTitle = title.getString("subTitle");
                    mSubTitle.setText(subTitle);
                    mSubTitle.setTextSize(16);
                }
            }

            if (template.has("textField")) {
                String textField = template.getString("textField");
                mTextField.setText(textField);
                mTextField.setTextSize(28);
            }
        } catch (JSONException e) {
            Log.e(sTag, e.getMessage());
        }
    }

    public void setClearTemplateListener() {

        final AlexaService alexaService = AlexaService.getInstance();
        alexaService.clearTemplateInNewActivity();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alexaService.setClearTemplateListener(new ClearTemplateListener() {
                    @Override
                    public void onClearTemplate() {
                        if (mActivity != null) {
                            finishActivity();
                        }
                    }
                });
            }
        },4000);

    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(MediaPlayerStateMessage message) {
//        String mediaState = message.getMediaState();
//        if (mediaState.equals(MediaPlayerHandler.MediaStateStart)) {
//            //music start playing
//            mHandler.postDelayed(runnable,TIME_DELAY);
//        }
//    }
}

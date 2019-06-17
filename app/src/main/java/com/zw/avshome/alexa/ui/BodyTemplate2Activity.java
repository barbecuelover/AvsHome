package com.zw.avshome.alexa.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.zw.avshome.base.ParentActivity;
import com.zw.avshome.R;
import com.zw.avshome.alexa.util.DownloadImageTask;
import com.zw.avshome.alexa.interfaces.ClearTemplateListener;
import com.zw.avshome.alexa.AlexaService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BodyTemplate2Activity extends ParentActivity {

    private final String sTag = "BodyTemplate2Activity";

    private Activity mActivity;

    private TextView mMainTitle;
    private TextView mSubTitle;
    private TextView mTextField;
    private ImageView mImage;
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
        return new Intent(context, BodyTemplate2Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_body_template2);
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
        mTextField = (TextView) findViewById(R.id.textField2);
        mImage = (ImageView) findViewById(R.id.image);
    }

    @Override
    public void initData() {
        configureBodyTemplate2(mJsonObject);
    }

    @Override
    public void initEvent() {
        setClearTemplateListener();
    }

    public void downloadImage(final ImageView imageView, final String url){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new DownloadImageTask(imageView).execute(url);
            }
        });
    }

    private void configureBodyTemplate2(JSONObject template) {
        try {
            if (template.has("title")) {
                JSONObject title = template.getJSONObject("title");
                if (title.has("mainTitle")) {
                    String mainTitle = title.getString("mainTitle");
                    mMainTitle.setText(mainTitle);
                    mMainTitle.setTextSize(22);
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
                mTextField.setTextSize(16);
            }

            if (template.has("image")) {
                JSONObject image = template.getJSONObject("image");
                String imageURL = getImageUrl(image);
                downloadImage(mImage,imageURL);
            }
        } catch (JSONException e) {
            Log.e(sTag, e.getMessage());
        }
    }

    private String getImageUrl(JSONObject image) {
        String url = null;

        try {
            JSONArray sources = image.getJSONArray("sources");
            HashMap<String, String> imageMap = new HashMap<>();

            for (int j = 0; j < sources.length(); j++) {
                JSONObject next = sources.getJSONObject(j);
                String size;
                if (next.has("size")) {
                    size = next.getString("size").toUpperCase();
                } else {
                    size = "DEFAULT";
                }
                imageMap.put(size, next.getString("url"));
            }

            if (imageMap.containsKey("DEFAULT")) {
                url = imageMap.get("DEFAULT");
            } else if (imageMap.containsKey("X-LARGE")) {
                url = imageMap.get("X-LARGE");
            } else if (imageMap.containsKey("LARGE")) {
                url = imageMap.get("LARGE");
            } else if (imageMap.containsKey("MEDIUM")) {
                url = imageMap.get("MEDIUM");
            } else if (imageMap.containsKey("SMALL")) {
                url = imageMap.get("SMALL");
            } else if (imageMap.containsKey("X-SMALL")) {
                url = imageMap.get("X-SMALL");
            }
        } catch (JSONException e) {
            Log.e(sTag, e.getMessage());
        }
        return url;
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

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(MediaPlayerStateMessage message) {
//        String mediaState = message.getMediaState();
//        if (mediaState.equals(MediaPlayerHandler.MediaStateStart)) {
//            //music start playing
//            mHandler.postDelayed(runnable,TIME_DELAY);
//        }
//    }
}

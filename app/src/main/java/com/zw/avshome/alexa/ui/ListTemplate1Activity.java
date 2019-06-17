package com.zw.avshome.alexa.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zw.avshome.base.ParentActivity;
import com.zw.avshome.R;
import com.zw.avshome.alexa.interfaces.ClearTemplateListener;
import com.zw.avshome.alexa.AlexaService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListTemplate1Activity extends ParentActivity {

    private static String sTag = "ListTemplate1Activity";

    private Activity mActivity;

    private static int sNumListItems = 5; // For list template card

    private TextView mMainTitle;
    private TextView mSubTitle;
    private LinearLayout mContentList;
    private LayoutInflater mInf;

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
        return new Intent(context, ListTemplate1Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list_template1);
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
        mMainTitle = findViewById(R.id.mainTitle);
        mSubTitle = findViewById(R.id.subTitle);
        ConstraintLayout listContainer = findViewById(R.id.listContainer);
        mContentList = listContainer.findViewById(R.id.contentList);
        mInf = this.getLayoutInflater();

    }

    private void configureListTemplate1(JSONObject template) {
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

            if (template.has("listItems")) {
                JSONArray listItems = template.getJSONArray("listItems");

                // Truncate list
                int numItems = listItems.length() > sNumListItems
                        ? sNumListItems : listItems.length();

                clearLists();
                for (int j = 0; j < numItems; j++) {
                    JSONObject nextItem = listItems.getJSONObject(j);
                    String index = nextItem.has("leftTextField")
                            ? nextItem.getString("leftTextField") : "";
                    String content = nextItem.has("rightTextField")
                            ? nextItem.getString("rightTextField") : "";
                    insertListItem(index, content);
                }
            }
        } catch (JSONException e) {
            Log.e(sTag, e.getMessage());
        }
    }

    private void insertListItem(String index, String content) {
        View contentItem = mInf.inflate(R.layout.card_list_template1_item_content, mContentList, false);
        ((TextView) contentItem.findViewById(R.id.content)).setText(content);
        ((TextView) contentItem.findViewById(R.id.content)).setTextSize(28);
        ((TextView) contentItem.findViewById(R.id.index)).setText(index);
        ((TextView) contentItem.findViewById(R.id.index)).setTextSize(20);
        contentItem.findViewById(R.id.guideline1).setBackgroundColor(getResources().getColor(R.color.cardListGuideLine));
//        ((TextView) contentItem.findViewById(R.id.guideline2)).setBackgroundColor(getResources().getColor(R.color.cardListGuideLine));
        mContentList.addView(contentItem);
    }

    private void clearLists() {
        mContentList.removeAllViews();
    }

    @Override
    public void initData() {
        configureListTemplate1(mJsonObject);
    }

    @Override
    public void initEvent() {
        setClearTemplateListener();
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

package com.zw.avshome.alexa.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import com.zw.avshome.home.base.ParentActivity;
import com.zw.avshome.R;
import com.zw.avshome.alexa.util.DownloadImageTask;
import com.zw.avshome.alexa.util.MediaPlayerStateMessage;
import com.zw.avshome.alexa.impl.MediaPlayer.MediaPlayerHandler;
import com.zw.avshome.alexa.interfaces.ClearTemplateListener;
import com.zw.avshome.alexa.interfaces.PlayBackControllerListener;
import com.zw.avshome.alexa.interfaces.PlayBackToggleButtonControlerListener;
import com.zw.avshome.alexa.AlexaService;
import com.zw.avshome.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;



/**
 * stop  播放完毕关闭页面，
 * 通知  贴到后台
 */
public class RenderPlayerInfoActivity extends ParentActivity implements View.OnClickListener {

    private final String sTag = "RenderPlayeroActivity";

    private TextView mHeader;
    private TextView mDivider;
    private TextView mHeaderSubtext1;
    private TextView mTitle;
    private TextView mTitleSubtext1;
    private TextView mTitleSubtext2;
    private ImageView mPartnerLogo;
    private ImageView mArt;
    private Button mPlayPauseLeft1, mPlayPauseRight1;
    private ToggleButton mControlPlayPause, mPlayPauseLeft11, mPlayPauseRight11, mPlayPauseRight2, mPlayPauseLeft2;
    private TextView mProgressTime, mEndTime;
    private ProgressBar mProgress;
    private static final int SHOW_PROGRESS = 0;
    private ProgressHandler mProgressHandler;
    private StringBuilder mStringBuilder;
    private Formatter mFormatter;
    private String mJsonObject;
    private boolean hasLength;


    public static Intent createIntent(Context context) {
        return new Intent(context, RenderPlayerInfoActivity.class);
    }

    private static PlayBackControllerListener mPlayBackControllerListener;

    public static void setPlayBackControllerListener(PlayBackControllerListener listener) {
        mPlayBackControllerListener = listener;
    }

    private static PlayBackToggleButtonControlerListener mPlayBackToggleButtonControlerListener;

    public static void setmPlayBackToggleButtonControlerListener(PlayBackToggleButtonControlerListener listener) {
        mPlayBackToggleButtonControlerListener = listener;
    }

    AlexaService alexaService = AlexaService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_render_player_info);
        EventBus.getDefault().register(this);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        mHeader = findViewById(R.id.header);
        mDivider = findViewById(R.id.headerDivider);
        mHeaderSubtext1 = findViewById(R.id.headerSubtext1);
        mTitle = findViewById(R.id.title);
        mTitleSubtext1 = findViewById(R.id.titleSubtext1);
        mTitleSubtext2 = findViewById(R.id.titleSubtext2);
        mPartnerLogo = findViewById(R.id.partnerLogo);
        mArt = findViewById(R.id.art);

        mControlPlayPause = findViewById(R.id.playpause_btn);
        mControlPlayPause.setOnClickListener(this);

        mPlayPauseLeft1 = findViewById(R.id.left_play_pause_btn1);
        mPlayPauseLeft1.setOnClickListener(this);
        mPlayPauseLeft11 = findViewById(R.id.left_play_pause_btn11);
        mPlayPauseLeft11.setOnClickListener(this);

        mPlayPauseLeft2 = findViewById(R.id.left_play_pause_btn2);
        mPlayPauseLeft2.setOnClickListener(this);

        mPlayPauseRight1 = findViewById(R.id.right_play_pause_btn1);
        mPlayPauseRight1.setOnClickListener(this);
        mPlayPauseRight11 = findViewById(R.id.right_play_pause_btn11);
        mPlayPauseRight11.setOnClickListener(this);

        mPlayPauseRight2 = findViewById(R.id.right_play_pause_btn2);
        mPlayPauseRight2.setOnClickListener(this);

        mProgressHandler = new ProgressHandler(this);
        mProgress = findViewById(R.id.musicProgressBar);
        mEndTime = findViewById(R.id.musicTotalTime);
        mProgressTime = findViewById(R.id.musicStartTime);
        mStringBuilder = new StringBuilder();
        mFormatter = new Formatter(mStringBuilder, Locale.US);
    }

    @Override
    public void initData() {
        mJsonObject = alexaService.getmRenderPlayerJson();
        toJsonObject(mJsonObject);
    }

    @Override
    public void initEvent() {
        alexaService.setClearPlayerInfoListener(new ClearTemplateListener() {
            @Override
            public void onClearTemplate() {
                finishActivity();
                Log.i("TemplateRuntime","finish RenderPlayerInfoActivity ");
            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_play_pause_btn1:
                if (mPlayPauseLeft1.getBackground().getLevel() == 1) {
                    mPlayBackControllerListener.setPlayBackControllerListener("Previous");
                    Log.d(sTag, "Previous button click");
                } else if (mPlayPauseLeft1.getBackground().getLevel() == 2) {
                    mPlayBackControllerListener.setPlayBackControllerListener("skip back");
                    Log.d(sTag, "skip back button click");
                } else if (mPlayPauseLeft11.getBackground().getLevel() == 3) {
                    mPlayBackToggleButtonControlerListener.setPlayBackToggleButtonControlerListener("thumbDown", mPlayPauseLeft11.isChecked());
                    Log.d(sTag, "thumbDown button click");
                }
                break;
            case R.id.left_play_pause_btn11:
                if (mPlayPauseLeft11.getBackground().getLevel() == 1) {
                    mPlayBackControllerListener.setPlayBackControllerListener("Previous");
                    Log.d(sTag, "Previous button click");
                } else if (mPlayPauseLeft11.getBackground().getLevel() == 2) {
                    mPlayBackControllerListener.setPlayBackControllerListener("skip back");
                    Log.d(sTag, "skip back button click");
                } else if (mPlayPauseLeft11.getBackground().getLevel() == 3) {
                    mPlayBackToggleButtonControlerListener.setPlayBackToggleButtonControlerListener("thumbDown", mPlayPauseLeft11.isChecked());
                    Log.d(sTag, "thumbDown button click");
                }
                break;
            case R.id.left_play_pause_btn2:
                if (mPlayPauseLeft2.getBackground().getLevel() == 1) {
                    mPlayBackToggleButtonControlerListener.setPlayBackToggleButtonControlerListener("loop", mPlayPauseLeft2.isChecked());
                    Log.d(sTag, "loop button click");
                } else if (mPlayPauseLeft2.getBackground().getLevel() == 2) {
                    mPlayBackControllerListener.setPlayBackControllerListener("skip forward");
                    Log.d(sTag, "skip forward button click");
                } else if (mPlayPauseLeft2.getBackground().getLevel() == 3) {
                    mPlayBackToggleButtonControlerListener.setPlayBackToggleButtonControlerListener("thumbDown", mPlayPauseLeft2.isChecked());
                    Log.d(sTag, "thumbDown button click");
                }
                break;
            case R.id.playpause_btn:
                if (mControlPlayPause.isChecked()) {
                    mPlayBackControllerListener.setPlayBackControllerListener("play");
                } else {
                    mPlayBackControllerListener.setPlayBackControllerListener("pause");
                }
                Log.d(sTag, "playAndPause button click");
                break;
            case R.id.right_play_pause_btn1:
                if (mPlayPauseRight1.getBackground().getLevel() == 1) {
                    mPlayBackControllerListener.setPlayBackControllerListener("next");
                    Log.d(sTag, "next button click");
                } else if (mPlayPauseRight1.getBackground().getLevel() == 2) {
                    mPlayBackControllerListener.setPlayBackControllerListener("skip forward");
                    Log.d(sTag, "skip forward button click");
                } else if (mPlayPauseRight1.getBackground().getLevel() == 3) {
                    mPlayBackToggleButtonControlerListener.setPlayBackToggleButtonControlerListener("thumbUp", mPlayPauseRight11.isChecked());
                    Log.d(sTag, "thumbUp button click");
                }
                break;
            case R.id.right_play_pause_btn11:
                if (mPlayPauseRight11.getBackground().getLevel() == 1) {
                    mPlayBackControllerListener.setPlayBackControllerListener("next");
                    Log.d(sTag, "next button click");
                } else if (mPlayPauseRight11.getBackground().getLevel() == 2) {
                    mPlayBackControllerListener.setPlayBackControllerListener("skip forward");
                    Log.d(sTag, "skip forward button click");
                } else if (mPlayPauseRight11.getBackground().getLevel() == 3) {
                    mPlayBackToggleButtonControlerListener.setPlayBackToggleButtonControlerListener("thumbUp", mPlayPauseRight11.isChecked());
                    Log.d(sTag, "thumbUp button click");
                }
                break;
            case R.id.right_play_pause_btn2:
                if (mPlayPauseRight2.getBackground().getLevel() == 1) {
                    mPlayBackToggleButtonControlerListener.setPlayBackToggleButtonControlerListener("shuffle", mPlayPauseRight2.isChecked());
                    Log.d(sTag, "shuffle button click");
                    break;
                } else if (mPlayPauseRight2.getBackground().getLevel() == 2) {
                    mPlayBackControllerListener.setPlayBackControllerListener("skip forward");
                    Log.d(sTag, "skip forward button click");
                } else if (mPlayPauseRight2.getBackground().getLevel() == 3) {
                    mPlayBackToggleButtonControlerListener.setPlayBackToggleButtonControlerListener("thumbUp", mPlayPauseRight2.isChecked());
                    Log.d(sTag, "thumbUp button click");
                } else {
                    /**/
                }
                break;
            default:
                break;
        }
    }

    public void start() {
        mProgress.setMax(1000);
        mProgressHandler.sendEmptyMessage(SHOW_PROGRESS);
    }

    private static class ProgressHandler extends Handler {

        private final WeakReference<RenderPlayerInfoActivity> mController;

        ProgressHandler(RenderPlayerInfoActivity controller) {
            mController = new WeakReference<>(controller);
        }

        @Override
        public void handleMessage(Message msg) {
            RenderPlayerInfoActivity controller = mController.get();

            long pos;
            switch (msg.what) {
                case SHOW_PROGRESS:
                    pos = controller.setProgress();
                    msg = obtainMessage(SHOW_PROGRESS);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                    break;
            }
        }
    }

    private long setProgress() {
        if (alexaService.getMediaPlayer() == null) return 0;

        long position = alexaService.getMediaPlayer().getPosition();
        long duration = alexaService.getMediaPlayer().getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            } else {
                mProgress.setProgress(0);
            }
        }

        mEndTime.setText(stringForTime((int) duration));
        mEndTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        mProgressTime.setText(stringForTime((int) position));
        mProgressTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        return position;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mStringBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressHandler != null) {
            mProgressHandler.removeMessages(SHOW_PROGRESS);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MediaPlayerStateMessage message) {
        String mediaState = message.getMediaState();
        if (mediaState.equals(MediaPlayerHandler.MediaStateStart)) {
            /*update progressbar*/
            mProgressHandler.removeMessages(SHOW_PROGRESS);
            if (hasLength) {
                mProgressHandler.sendEmptyMessage(SHOW_PROGRESS);
            }
            mControlPlayPause.setChecked(false);
        } else if (mediaState.equals(MediaPlayerHandler.MediaStateStop)) {
            mControlPlayPause.setChecked(true);
            mProgressHandler.removeMessages(SHOW_PROGRESS);
        } else if (mediaState.equals(MediaPlayerHandler.MediaStatePause)) {
            //音乐暂停播放
            mControlPlayPause.setChecked(true);
            mProgressHandler.removeMessages(SHOW_PROGRESS);
        } else if (mediaState.equals(MediaPlayerHandler.MediaStateResume)) {
            //音乐Resume
            mControlPlayPause.setChecked(false);
            mProgressHandler.removeMessages(SHOW_PROGRESS);
            if (hasLength) {
                mProgressHandler.sendEmptyMessage(SHOW_PROGRESS);
            }
        }
    }

    private void toJsonObject(String espnJson) {
        goneView();
        if (StringUtil.isEmpty(espnJson)) {
            finishActivity();
            return;
        }
        try {
            JSONObject object = new JSONObject(espnJson);
            String substring = espnJson.substring(0, espnJson.indexOf(":"));
            if (substring.contains("directive")) {
                JSONObject directive = object.getJSONObject("directive");
                boolean isPayload = directive.has("payload");
                if (isPayload) {
                    JSONObject payload = directive.getJSONObject("payload");
                    //解析payload 内层数据
                    jsonAnalyzePayload(payload);
                }

            } else if (substring.contains("audioItemId") || substring.contains("content") || substring.contains("controls")) {
                //解析payload 内层数据
                jsonAnalyzePayload(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void goneView() {
        mPlayPauseLeft1.getBackground().setLevel(0);
        mPlayPauseLeft11.getBackground().setLevel(0);
        mPlayPauseLeft2.getBackground().setLevel(0);
        mControlPlayPause.setVisibility(View.GONE);
        mPlayPauseRight1.getBackground().setLevel(0);
        mPlayPauseRight11.getBackground().setLevel(0);
        mPlayPauseRight2.getBackground().setLevel(0);
    }

    /**
     * 解析payload 内层数据
     *
     * @param payload
     * @throws JSONException
     */
    private void jsonAnalyzePayload(JSONObject payload) throws JSONException {
        boolean isAudioItemId = payload.has("audioItemId");
        if (isAudioItemId) {
            String audioItemId = payload.getString("audioItemId");
        }

        //解析Content
        boolean isContent = payload.has("content");
        if (isContent) {
            JSONObject content = payload.getJSONObject("content");
            jsonAnalyzeContent(content);
        }

        //解析controls
        boolean isControls = payload.has("controls");
        if (isControls) {
            JSONArray controls = payload.getJSONArray("controls");
            jsonAnalyzeControls(controls);
        }
    }

    /**
     * 解析Content
     *
     * @param content
     * @throws JSONException
     */
    private void jsonAnalyzeContent(JSONObject content) throws JSONException {
        String header = "";
        String title = "";
        String titleSubtext1 = "";
        String titleSubtext2 = "";
        String headerSubtext1 = "";
        JSONArray sources = null;
        boolean isTitle = content.has("title");
        if (isTitle) {
            title = content.getString("title");
        }

        boolean isTitleSubtext1 = content.has("titleSubtext1");
        if (isTitleSubtext1) {
            titleSubtext1 = content.getString("titleSubtext1");
        }

        boolean isTitleSubtext2 = content.has("titleSubtext2");
        if (isTitleSubtext2) {
            titleSubtext2 = content.getString("titleSubtext2");
        }

        boolean isHeaderSubtext1 = content.has("headerSubtext1");
        if (isHeaderSubtext1) {
            headerSubtext1 = content.getString("headerSubtext1");
        }

        if (StringUtil.isEmpty(headerSubtext1)) {
            mDivider.setVisibility(View.GONE);
        } else {
            mDivider.setVisibility(View.VISIBLE);
        }

        boolean isHeader = content.has("header");
        if (isHeader) {
            header = content.getString("header");
        }

        boolean isArt = content.has("art");
        if (isArt) {
            JSONObject art = content.getJSONObject("art");
            boolean isSources = art.has("sources");
            if (isSources) {
                sources = art.getJSONArray("sources");
            }

            if (header.equals("iHeartCountry Classics")) {
                mHeader.setVisibility(View.GONE);
            } else {
                mHeader.setText(header);
                mDivider.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
                mHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
            }

            if (header.equals("iHeartCountry Classics")) {
                mHeaderSubtext1.setVisibility(View.GONE);
                mTitle.setText(header);
                mTitleSubtext1.setText(headerSubtext1);
                mTitleSubtext1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
                mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 54);
            } else {
                mTitle.setText(title);
                mTitleSubtext1.setText(titleSubtext1);
                mTitleSubtext1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
                mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 54);
                mHeaderSubtext1.setText(headerSubtext1);
                mHeaderSubtext1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
            }

            mTitleSubtext2.setText(titleSubtext2);
            mTitleSubtext2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);

            String url = getImageUrl(sources);
            downloadImage(mArt, url);

        }

        boolean isProvider = content.has("provider");
        if (isProvider) {
            JSONObject provider = content.getJSONObject("provider");

            boolean isName = provider.has("name");
            if (isName) {
                String name = provider.getString("name");
                Log.v(sTag, "jsonalyzeContent-----name:" + name);
            }

            boolean isLogo = provider.has("logo");
            if (isLogo) {
                //解析并显示Logo
                jsonAnalyzeLogo(provider);
            }
        }

        boolean hasMediaLength = content.has("mediaLengthInMilliseconds");
        if (hasMediaLength) {
            String length = content.getString("mediaLengthInMilliseconds");
            if ("0".equals(length)) {
                mEndTime.setVisibility(View.GONE);
                mProgressTime.setVisibility(View.GONE);
            } else {
                hasLength = true;
                mEndTime.setVisibility(View.VISIBLE);
                mProgressTime.setVisibility(View.VISIBLE);
                start();
            }
        }
    }

    /**
     * 解析并显示Logo
     *
     * @param provider
     * @throws JSONException
     */
    private void jsonAnalyzeLogo(JSONObject provider) throws JSONException {
        JSONObject logo = provider.getJSONObject("logo");
        boolean isSources = logo.has("sources");
        if (isSources) {
            JSONArray sources = logo.getJSONArray("sources");
            String url = getImageUrl(sources);
            if (url.contains(".svg")) {
            //    GlideToVectorYou.justLoadImage(this, Uri.parse(url), mPartnerLogo);
            } else {
                Glide.with(this).load(url).into(mPartnerLogo);

            }
        }
    }

    /**
     * 解析controls
     *
     * @param controls
     * @throws JSONException
     */
    private void jsonAnalyzeControls(JSONArray controls) throws JSONException {
        for (int j = 0; j < controls.length(); j++) {
            JSONObject controlsJSONObject = controls.getJSONObject(j);
            String type = "";
            String name = "";
            boolean enabled = false;
            boolean selected = false;

            if (controlsJSONObject.has("type")) {
                type = controlsJSONObject.getString("type");
            }

            if (controlsJSONObject.has("name")) {
                name = controlsJSONObject.getString("name");
            }

            if (controlsJSONObject.has("enabled")) {
                enabled = controlsJSONObject.getBoolean("enabled");
            }

            if (controlsJSONObject.has("selected")) {
                selected = controlsJSONObject.getBoolean("selected");
            }

            if (type.equals("BUTTON")) {
                updateControlButton(name, enabled);
            } else if (type.equals("TOGGLE")) {
                updateControlToggle(name, enabled, selected);
            }
        }
    }

    // Updates Control Button's states
    public void updateControlButton(final String name, final boolean enabled) {
        if (alexaService.getMediaPlayer() == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (name) {
                    case "PREVIOUS":
                        if (mPlayPauseLeft1.getBackground().getLevel() == 0) {
                            mPlayPauseLeft11.setEnabled(false);
                            mPlayPauseLeft1.getBackground().setLevel(1);
                            mPlayPauseLeft1.setVisibility(View.VISIBLE);
                            mPlayPauseLeft1.setEnabled(enabled);
                        }
                        break;
                    case "PLAY_PAUSE":
                        mControlPlayPause.setVisibility(View.VISIBLE);
                        mControlPlayPause.setEnabled(enabled);
                        break;
                    case "NEXT":
                        if (mPlayPauseRight1.getBackground().getLevel() == 0) {
                            mPlayPauseRight11.setEnabled(false);
                            mPlayPauseRight1.getBackground().setLevel(1);
                            mPlayPauseRight1.setVisibility(View.VISIBLE);
                            mPlayPauseRight1.setEnabled(enabled);
                        }
                        break;
                    case "SKIP_FORWARD":
                        if (mPlayPauseRight1.getBackground().getLevel() != 0) {
                            mPlayPauseRight2.getBackground().setLevel(2);
                            mPlayPauseRight2.setVisibility(View.VISIBLE);
                            mPlayPauseRight2.setEnabled(enabled);
                        } else {
                            mPlayPauseRight1.getBackground().setLevel(2);
                            mPlayPauseRight1.setVisibility(View.VISIBLE);
                            mPlayPauseRight1.setEnabled(enabled);
                        }
                        break;
                    case "SKIP_BACKWARD":
                        if (mPlayPauseLeft1.getBackground().getLevel() != 0) {
                            mPlayPauseLeft2.getBackground().setLevel(2);
                            mPlayPauseLeft2.setVisibility(View.VISIBLE);
                            mPlayPauseLeft2.setEnabled(false);
                        } else {
                            mPlayPauseLeft1.getBackground().setLevel(2);
                            mPlayPauseLeft1.setVisibility(View.VISIBLE);
                            mPlayPauseLeft1.setEnabled(false);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    // Updates Toggle's display states
    // NOTE: Disabled controls not hidden here for development visibility.
    public void updateControlToggle(final String name, final boolean enabled, final boolean selected) {
        if (alexaService.getMediaPlayer() == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (name) {
                    case "SHUFFLE":
                        mPlayPauseRight2.getBackground().setLevel(0);
                        mPlayPauseRight2.getBackground().setLevel(1);
                        mPlayPauseRight2.setVisibility(View.VISIBLE);
                        mPlayPauseRight2.setEnabled(enabled);
                        mPlayPauseRight2.setChecked(selected);
                        break;
                    case "LOOP":
                        mPlayPauseLeft2.getBackground().setLevel(0);
                        mPlayPauseLeft2.getBackground().setLevel(1);
                        mPlayPauseLeft2.setVisibility(View.VISIBLE);
                        mPlayPauseLeft2.setEnabled(enabled);
                        mPlayPauseLeft2.setChecked(selected);
                        break;
                    case "THUMBS_UP":
                        if (mPlayPauseRight1.getBackground().getLevel() != 0) {
                            mPlayPauseRight2.getBackground().setLevel(3);
                            mPlayPauseRight2.setVisibility(View.VISIBLE);
                            mPlayPauseRight2.setEnabled(enabled);
                            mPlayPauseRight2.setChecked(selected);
                        } else {
                            mPlayPauseRight1.setEnabled(false);
                            mPlayPauseRight11.getBackground().setLevel(3);
                            mPlayPauseRight11.setVisibility(View.VISIBLE);
                            mPlayPauseRight11.setEnabled(enabled);
                            mPlayPauseRight11.setChecked(selected);
                        }
                        break;
                    case "THUMBS_DOWN":
                        if (mPlayPauseLeft1.getBackground().getLevel() != 0) {
                            mPlayPauseLeft2.getBackground().setLevel(3);
                            mPlayPauseLeft2.setVisibility(View.VISIBLE);
                            mPlayPauseLeft2.setEnabled(enabled);
                            mPlayPauseLeft2.setChecked(selected);
                        } else {
                            mPlayPauseLeft1.setEnabled(false);
                            mPlayPauseLeft11.getBackground().setLevel(3);
                            mPlayPauseLeft11.setVisibility(View.VISIBLE);
                            mPlayPauseLeft11.setEnabled(enabled);
                            mPlayPauseLeft11.setChecked(selected);
                        }
                        break;
                }
            }
        });
    }

    public String getImageUrl(JSONArray sources) {
        if (sources == null) {
            return "";
        }
        String url = "";
        try {
            HashMap<String, String> imageMap = new HashMap<>();

            for (int j = 0; j < sources.length(); j++) {
                String urlStr = "";
                JSONObject next = sources.getJSONObject(j);
                String size;
                if (next.has("size")) {
                    size = next.getString("size").toUpperCase();
                } else {
                    size = "DEFAULT";
                }
                if (next.has("url")) {
                    urlStr = next.getString("url");
                }
                imageMap.put(size, urlStr);
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
            Log.d("RenderPlayerInfo", e.getMessage());
        }
        return url;
    }

    public void downloadImage(final ImageView imageView, final String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new DownloadImageTask(imageView).execute(url);
            }
        });
    }

}

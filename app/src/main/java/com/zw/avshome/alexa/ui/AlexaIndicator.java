package com.zw.avshome.alexa.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.zw.avshome.R;


public class AlexaIndicator {

    private RelativeLayout activeView;
    private LayoutInflater layoutInflater;
    private View indicatorView,otherIndicatorView;
    private WindowManager.LayoutParams mWLayoutParams = new WindowManager.LayoutParams();
    private GradientDrawable gd = new GradientDrawable();

    /*Mute color*/
    private int muteColor = Color.parseColor("#FF5D00");

    /* Disturb color*/
    private int distrubColor = Color.parseColor("#E066FF");

    /*The offlineColor color */
    private int offlineColor = Color.parseColor("#FFCC00");

    private int color1 = Color.parseColor("#97FFFF");
    private int color2 = Color.parseColor("#8EE5EE");
    private int color3 = Color.parseColor("#63B8FF");
    private int color4 = Color.parseColor("#1E90FF");
    private int color5 = Color.parseColor("#1C86EE");
    private int color6 = Color.parseColor("#FF2550F0");

    private static AlexaIndicator alexaIndicator;
    private Context mContext;
    private Activity activity;
    private Window window;

    private DisplayMetrics dm;
    private WindowManager mWindowManager;

    private AlexaIndicator(Context context) {
        mContext = context;
        activity = (Activity)mContext;
        window = activity.getWindow();
    }

    public static AlexaIndicator getInstance(Context context) {
        if (alexaIndicator == null) {
            alexaIndicator = new AlexaIndicator(context);
        }
        return alexaIndicator;
    }

    public void setAlexaIndicatorBar(){
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

//        /*Suspension window　container*/
        layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activeView = (RelativeLayout) layoutInflater.inflate(R.layout.alexa_indicator,null);

        /*indicatorView*/
        indicatorView = activeView.findViewById(R.id.alexa_indicator);
        otherIndicatorView = activeView.findViewById(R.id.alexa_other_indicator);

        gd = (GradientDrawable) mContext.getDrawable(R.drawable.alexa_state_instructions);
        float y = (float) 0.5;
        float x = (float) 0.5;
        gd.setGradientCenter(x, y);
        /*Radius value*/
        gd.setGradientRadius(width/5);
        int color[] = new int[]{color1,color2,color3,color4,color6};
        gd.setColors(color);

        indicatorView.setBackground(gd);

        /*Default not displayed*/
//        activeView.setBackgroundColor(colorEnd);
//        activeView.setAlpha((float) 0.5);
        activeView.setVisibility(View.GONE);

        /*上下左右边距*/
        mWLayoutParams.horizontalMargin = 0;
        mWLayoutParams.verticalMargin = 0;
        /*置于所有应用程序之上，状态栏之下*/
        mWLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//　android8.0 or uper ,can  write 2038
        /*透明度*/
        mWLayoutParams.format = 1;
        /*屏幕放置位置*/
        mWLayoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        /*40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）*/
        mWLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWLayoutParams.width = width;
        mWLayoutParams.height = 40;
        mWindowManager.addView(activeView, mWLayoutParams);

    }

    /*Explain:
    * mainIndicator is wakeUp, listening and Thinking state
    * otherIndicator is mute, distrub, disconnected state
    */

    public void setIndicatorState(){
        /*设置悬浮窗后所有东西都黯淡效果*/
        mWLayoutParams.dimAmount=0.4f;
        mWLayoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        /*wake Up state*/
        ScaleAnimation wakeUpAnimationIn = new ScaleAnimation(4, 1, 4, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        wakeUpAnimationIn.setDuration(1000);

        indicatorView.startAnimation(wakeUpAnimationIn);

        final ScaleAnimation listeningState = new ScaleAnimation(1, (float) 1.3, 1, (float) 1.3, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        listeningState.setDuration(200);
        listeningState.setRepeatMode(Animation.REVERSE);
        listeningState.setRepeatCount(-1);

        /*after wake up,and listening state*/
        wakeUpAnimationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                indicatorView.startAnimation(listeningState);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        setMainIndicatorView();
    }

    public void setIndicatorThinkingState(){
        mWLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        ScaleAnimation thinkingAnimationOut = new ScaleAnimation(1, (float) 1.7, 1, (float) 1.7, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        thinkingAnimationOut.setDuration(400);
        thinkingAnimationOut.setRepeatMode(Animation.REVERSE);
        thinkingAnimationOut.setRepeatCount(-1);

        indicatorView.startAnimation(thinkingAnimationOut);
        setMainIndicatorView();
    }

    /* Disconnected state*/
    public void setIndicatorDisconnectedState(){
        otherIndicatorView.setBackgroundColor(offlineColor);
        setOtherIndicatorViewColor();
    }

    /*mute state*/
    public void setIndicatorMuteState(){
        otherIndicatorView.setBackgroundColor(muteColor);
        setOtherIndicatorViewColor();
    }

    /*distrub state*/
    public void setIndicatorDistrubState(){
        otherIndicatorView.setBackgroundColor(distrubColor);
        setOtherIndicatorViewColor();
    }

    /*　clear all view state*/
    public void clearIndicator(){
        activeView.setVisibility(View.GONE);
        mWindowManager.updateViewLayout(activeView, mWLayoutParams);
    }

    /*common of mute/distrub/disconnected*/
    public void setOtherIndicatorViewColor(){
        indicatorView.setVisibility(View.GONE);
        otherIndicatorView.setVisibility(View.VISIBLE);
        activeView.setVisibility(View.VISIBLE);
        mWindowManager.updateViewLayout(activeView, mWLayoutParams);
    }


    public void setMainIndicatorView(){
        indicatorView.setVisibility(View.VISIBLE);
        otherIndicatorView.setVisibility(View.GONE);
        activeView.setVisibility(View.VISIBLE);
        mWindowManager.updateViewLayout(activeView, mWLayoutParams);
    }
    public void removeView(){
        mWindowManager.removeView(activeView);
    }
}

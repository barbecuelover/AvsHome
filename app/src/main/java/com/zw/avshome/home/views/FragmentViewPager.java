package com.zw.avshome.home.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.zw.avshome.R;

import java.util.List;


public class FragmentViewPager extends ViewPager implements View.OnTouchListener {

    /**
     * const
     */
    private static final String TAG = "FragmentViewPager";
    private static final int LOOP_MSG = 0x1001;
    private static final int LOOP_COUNT = 5000;
    /**
     * attrs
     */
    private static  int LOOP_COUNT_FRAGMENT = 3;

    private int mLoopTime;
    private boolean isLoop; //是否自动轮播
    private int mSwitchTime;
    private int mLoopMaxCount = 1;
    private boolean isSlide; //是否可以轮播滑动
    private int mCurrentIndex;

    /**
     * handle
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOOP_MSG){
                mCurrentIndex = getCurrentItem(); //重新获取index
                if (mCurrentIndex >= LOOP_COUNT / 2) {
                    mCurrentIndex++;
                }
                if (mCurrentIndex > LOOP_COUNT) {
                    mCurrentIndex = LOOP_COUNT / 2;
                }
                setCurrentItem(mCurrentIndex);

                mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);
            }
        }
    };

    public FragmentViewPager(@NonNull Context context) {
        this(context,null);
    }

    public FragmentViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FragmentViewPager);
        isLoop = ta.getBoolean(R.styleable.FragmentViewPager_banner_isloop,false);
        mLoopTime = ta.getInteger(R.styleable.FragmentViewPager_banner_looptime,2000);
        mSwitchTime = ta.getInteger(R.styleable.FragmentViewPager_banner_switchtime,600);
        mLoopMaxCount = ta.getInteger(R.styleable.FragmentViewPager_banner_loop_max_count,mLoopMaxCount);
        ta.recycle();
        setOnTouchListener(this);
        ViewPagerHelperUtils.initSwitchTime(getContext(),this,mSwitchTime);
    }

    /**
     * 为viewpage 添加fragments
     * @param fragmentList
     * @param fm
     */
    public void  setViewPageFragmentAdapter(List<Fragment> fragmentList, FragmentManager fm){

        LOOP_COUNT_FRAGMENT = fragmentList.size();

        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(fm,fragmentList);
        adapter.notifyDataSetChanged();
        setOffscreenPageLimit(0);
        setAdapter(adapter);

        int index = ViewPagerHelperUtils.LOOP_COUNT/2 % LOOP_COUNT_FRAGMENT;
        //这样能保证从第一页开始
        setCurrentItem(ViewPagerHelperUtils.LOOP_COUNT / 2 - index +LOOP_COUNT_FRAGMENT);
        //setCurrentItem(LOOP_COUNT/2);
    }

    class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList;
        public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            fragmentList =list;
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size()+LOOP_COUNT;
           // return  Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //处理position。让数组下标落在[0,fragmentList.size)中，防止越界
            position = position % fragmentList.size();
            return super.instantiateItem(container, position);

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
            if (fragmentList.size() == 3){
                instantiateItem(container,position);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mHandler.removeMessages(LOOP_MSG);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isLoop) {
            mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 当有触摸时停止
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mHandler.removeMessages(LOOP_MSG);
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP :
                if (isLoop) {
                    mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);
                }
                break;
            case MotionEvent.ACTION_MOVE :

            default:
                break;
        }
        return false;
    }


    /**
     * 手动停止
     */
    public void stopLoop(){
        if (isLoop) {
            mHandler.removeMessages(LOOP_MSG);
        }
    }

    /**
     * 手动开始
     */
    public void startLoop(){
        if (isLoop) {
            mHandler.removeMessages(LOOP_MSG);
            mHandler.sendEmptyMessageDelayed(LOOP_MSG, mLoopTime);
        }
    }
    /**
     * 如果退出了，自动停止，进来则自动开始
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (isLoop){
            if (visibility == View.VISIBLE){
                startLoop();
            }else{
                stopLoop();
            }
        }
    }

    @Override
    protected void detachAllViewsFromParent() {
        super.detachAllViewsFromParent();
        mHandler.removeCallbacksAndMessages(null);
    }

}

package com.zw.avshome.device;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.zw.avshome.R;
import com.zw.avshome.base.ParentActivity;
import com.zw.avshome.base.ParentFragment;
import com.zw.avshome.home.views.BlurringView;

public class AddNewDevicesActivity extends ParentActivity {


    private BlurringView mBgBlurView;
    private CardView mBgCardView1, mBgCardView2, mBgCardView3;
    private CardView mLeftBtn, mRightBtn;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_devices);
    }

    @Override
    public void initView() {
        mBgBlurView = findViewById(R.id.add_new_device_bg_blur_view);
        mBgCardView1 = findViewById(R.id.add_new_device_bg_card_view_1);
        mBgCardView2 = findViewById(R.id.add_new_device_bg_card_view_2);
        mBgCardView3 = findViewById(R.id.add_new_device_bg_card_view_3);
        mLeftBtn = findViewById(R.id.add_new_device_left_btn);
        mRightBtn = findViewById(R.id.add_new_device_right_btn);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePopCardItem();
            }
        });

        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = fragmentManager.getBackStackEntryCount();
                for (int i = 0; i < count; ++i) {
                    fragmentManager.popBackStack();
                }
                finishActivity();
            }
        });



    }

    @Override
    public Activity getActivity() {
        return this;
    }



    /**
     * 替换当前Fragment
     * @param fragment
     */
    public void replaceFragment(ParentFragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.add_new_device_fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        int commit = fragmentTransaction.commitAllowingStateLoss();
        showAddCardItem(commit);
    }

    private void showAddCardItem(int commit) {
        switch (commit) {
            case 0:
                mBgCardView3.setVisibility(View.VISIBLE);
                break;
            case 1:
                mBgCardView2.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBgCardView1.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     *Fragment 表示后面层级的背景
     */
    private void hidePopCardItem() {
        int count = fragmentManager.getBackStackEntryCount();
        fragmentManager.popBackStack();

        switch (count) {
            case 0:
                onBackPressed();
                break;
            case 1:
                mBgCardView1.setVisibility(View.INVISIBLE);
                mBgCardView2.setVisibility(View.INVISIBLE);
                mBgCardView3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mBgCardView1.setVisibility(View.INVISIBLE);
                mBgCardView2.setVisibility(View.INVISIBLE);
                mBgCardView3.setVisibility(View.VISIBLE);
                break;
            case 3:
                mBgCardView1.setVisibility(View.INVISIBLE);
                mBgCardView2.setVisibility(View.VISIBLE);
                mBgCardView3.setVisibility(View.VISIBLE);
                break;

        }
    }
}

package com.zw.avshome.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zw.avshome.R;
import com.zw.avshome.alexa.AlexaService;
import com.zw.avshome.home.base.ParentActivity;
import com.zw.avshome.home.base.ParentFragment;
import com.zw.avshome.settings.bean.SettingItemAdapter;
import com.zw.avshome.settings.views.BlurringView;
import com.zw.avshome.utils.NetWorkUtil;

import androidx.cardview.widget.CardView;


public class SettingsActivity extends ParentActivity {

    private BlurringView mBgBlurView;
    private CardView mBgCardView1, mBgCardView2, mBgCardView3, mSettingsCardViewContent;
    private CardView mSettingsLeftBtn, mSettingsRightBtn;
    private FragmentManager fragmentManager;
    private SettingsListFragment settingsListFragment;
    private Context context;
    private AlexaService alexaService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alexaService.initAuthProvider();
        alexaService.onResume();
    }

    @Override
    public void initView() {

        mBgBlurView = findViewById(R.id.settings_bg_blur_view);
        mBgCardView1 = findViewById(R.id.settings_bg_card_view_1);
        mBgCardView2 = findViewById(R.id.settings_bg_card_view_2);
        mBgCardView3 = findViewById(R.id.settings_bg_card_view_3);
        mSettingsLeftBtn = findViewById(R.id.settings_left_btn);
        mSettingsRightBtn = findViewById(R.id.settings_right_btn);
        mSettingsCardViewContent = findViewById(R.id.settings_card_view_content);
    }

    @Override
    public void initData() {
        context = getActivity();
        alexaService = AlexaService.getInstance();
        settingsListFragment = new SettingsListFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.settings_fragment_container, settingsListFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void initEvent() {

        mSettingsLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePopCardItem();
            }
        });

        mSettingsRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = fragmentManager.getBackStackEntryCount();
                for (int i = 0; i < count; ++i) {
                    fragmentManager.popBackStack();
                }
                finishActivity();
            }
        });

        settingsListFragment.setOnItemClickListener(new SettingItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0: //alexa login/logout
                        boolean isNetAvailable = NetWorkUtil.isNetWorkConnected(context);
                        if (!isNetAvailable){
//                            new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
//                                    .setTitleText("")
//                                    .setContentText("There is no network at present. Please connect to the network and try again…")
//                                    .setCancelText("cancel").show();
                        }else {
                            alexaService.login();

                        }

                        break;
                    case 1:
                        ThemeSelectFragment themeSelectFragment = new ThemeSelectFragment();
                        replaceFragment(themeSelectFragment);
                        break;
                    case 2: break;
                    case 3: break;
                }
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
        fragmentTransaction.replace(R.id.settings_fragment_container, fragment);
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

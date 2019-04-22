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

        settingsListFragment.setOnItemClickListener(new SettingItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0: //alexa login/logout
                        boolean isNetAvailable = NetWorkUtil.isNetWorkConnected(context);
                        if (isNetAvailable){
//                            new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
//                                    .setTitleText("")
//                                    .setContentText("There is no network at present. Please connect to the network and try again…")
//                                    .setCancelText("cancel").show();
                        }else {


                        }

                        break;
                    case 1: break;
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
}

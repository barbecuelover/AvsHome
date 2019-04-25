package com.zw.avshome.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zw.avshome.R;
import com.zw.avshome.alexa.AlexaService;
import com.zw.avshome.home.base.ParentActivity;
import com.zw.avshome.home.views.FragmentViewPager;
import com.zw.avshome.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ParentActivity implements View.OnClickListener {

    private ImageButton btnPopWindow;
    private ImageButton btnWifiStatus;
    private ImageButton btnAlexaLogin;

    private PopupWindow popupWindow;
    private View popView;
    private Context context;
    private FragmentViewPager mViewpagerContainer;
    private List<Fragment> fragmentList = new ArrayList<>();
    private HomeFragment homeFragment;
    private DeviceFragment deviceFragment;
    private AppFragment appFragment;
    AlexaService alexaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        btnPopWindow = findViewById(R.id.main_pop_window_menu);
        btnWifiStatus = findViewById(R.id.main_wifi_connect_status);
        btnAlexaLogin = findViewById(R.id.main_alexa_login_status);

        //init popWindow
        popView = getLayoutInflater().inflate(R.layout.pop_view_main_container, null, false);
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        mViewpagerContainer = findViewById(R.id.main_viewpager_container);
    }

    @Override
    public void initData() {
        context = getActivity();
        alexaService = AlexaService.getInstance();
        alexaService.start();
        homeFragment = new HomeFragment();
        deviceFragment = new DeviceFragment();
        appFragment = new AppFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(deviceFragment);
        fragmentList.add(appFragment);
        mViewpagerContainer.setViewPageFragmentAdapter(fragmentList,getSupportFragmentManager());



    }

    @Override
    protected void onResume() {
        super.onResume();
        alexaService.initAuthProvider();
        alexaService.onResume();
    }

    @Override
    public void initEvent() {
        btnAlexaLogin.setOnClickListener(this);
        btnPopWindow.setOnClickListener(this);
        btnWifiStatus.setOnClickListener(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_pop_window_menu:

                int popWidth = (int) context.getResources().getDimension(R.dimen.main_pop_window_width);
                int btnWidth = (int) context.getResources().getDimension(R.dimen.main_pop_btn_width);
                popupWindow.showAsDropDown(btnPopWindow, -(popWidth - btnWidth) / 2, 10);

                popView.findViewById(R.id.ll_add_new_device_pop_window).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        //startActivity();

                    }
                });

                popView.findViewById(R.id.ll_google_play_pop_window).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        // startActivity(new Intent(getActivity(), GooglePlayStoreActivity.class));
                    }
                });
                // Setting
                popView.findViewById(R.id.ll_setting_pop_window).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                         Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                         startActivity(intent);
                        //startActivity(HubSettingActivity.createIntent(context, llMainContainer));
                    }
                });
                break;

            case R.id.main_alexa_login_status:

                //startActivity(HubSettingActivity.createIntent(getActivity(), llMainContainer));
                break;
            case R.id.main_wifi_connect_status:
//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面


                alexaService.login();

                break;

        }
    }
}

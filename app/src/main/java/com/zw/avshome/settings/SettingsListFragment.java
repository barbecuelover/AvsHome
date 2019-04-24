package com.zw.avshome.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zw.avshome.R;
import com.zw.avshome.home.base.ParentFragment;
import com.zw.avshome.settings.bean.SettingItem;
import com.zw.avshome.settings.bean.SettingItemAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.zw.avshome.HomeApplication.isAlexaLogin;

public class SettingsListFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private List<SettingItem> list = new ArrayList<>();
    private     SettingItemAdapter adapter;
    private SettingItemAdapter.OnItemClickListener onItemClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.fragment_settings_list, null, false);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void initView() {
        recyclerView = view.findViewById(R.id.settings_list_fragment_recycler_view);
        GridLayoutManager gridlayoutManager = new GridLayoutManager(context, 2);
        gridlayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridlayoutManager);


    }

    @Override
    public void initData() {

        list.clear();
        SettingItem alexaItem = new SettingItem(isAlexaLogin?"AVS Logout":"AVS Login",R.mipmap.setting_item_alexa);
        SettingItem themeItem= new SettingItem("Theme",R.mipmap.setting_item_theme);
        SettingItem settingsItem = new SettingItem("Settings",R.mipmap.setting_item_settings);
        SettingItem d5FirmwareItem = new SettingItem("D5 FW",R.mipmap.setting_item_d5_update);
        list.add(alexaItem);
        list.add(themeItem);
        list.add(settingsItem);
        list.add(d5FirmwareItem);
        adapter= new SettingItemAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);



    }

    @Override
    public void initEvent() {

        if (onItemClickListener!=null){
            adapter.setOnItemClickListener(onItemClickListener);
        }

    }

    public void setOnItemClickListener(SettingItemAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}

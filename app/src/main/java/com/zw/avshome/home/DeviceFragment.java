package com.zw.avshome.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zw.avshome.R;
import com.zw.avshome.device.bean.BaseDevice;
import com.zw.avshome.home.adapter.DeviceTypeAdapter;
import com.zw.avshome.base.ParentFragment;
import com.zw.avshome.manager.DevicesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DeviceFragment extends ParentFragment {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DevicesManager devicesManager;
    private HashMap<String, ArrayList<BaseDevice>> deviceList = new HashMap<>();
    private HashSet<String> typeListSet = new HashSet<>();
    private List<String> typeList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home_devices, container, false);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void initView() {
        recyclerView = findViewById(R.id.device_fragment_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void initData() {
        getAllDevices();
        DeviceTypeAdapter typeAdapter = new DeviceTypeAdapter(context,typeList,deviceList);
        recyclerView.setAdapter(typeAdapter);

    }

    @Override
    public void initEvent() {

    }

    private void getAllDevices() {

        devicesManager = DevicesManager.getInstance();
        typeListSet.clear();
        //利用元素不重复，获取类别
        for (BaseDevice baseDevice : devicesManager.getDeviceList()) {
            String deviceType = baseDevice.getDeviceType();
            typeListSet.add(deviceType);
        }

        typeList.clear();
        //HashSet不便于操作
        typeList.addAll(typeListSet);

        deviceList.clear();
        //利用类别获取每一类对应的 BaseDevice集合
        for (String listData : typeListSet) {
            ArrayList<BaseDevice> baseDevices = new ArrayList<>();
            for (BaseDevice baseDevice : devicesManager.getDeviceList()) {
                if (baseDevice.getDeviceType().equals(listData)) {
                    baseDevices.add(baseDevice);
                }
            }
            deviceList.put(listData, baseDevices);
        }
    }
}

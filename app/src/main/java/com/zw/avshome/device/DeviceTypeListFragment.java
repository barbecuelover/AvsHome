package com.zw.avshome.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zw.avshome.R;
import com.zw.avshome.device.bean.BaseDevice;
import com.zw.avshome.base.ParentFragment;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zw
 * Date: 19-6-17
 */
public class DeviceTypeListFragment extends ParentFragment {

    private View view;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private DeviceTypeListFragment deviceTypeListFragment;
    private HashMap<String, ArrayList<BaseDevice>> deviceList = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_add_device_type_list, null);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void initView() {
        recyclerView = view.findViewById(R.id.recycler_view_device_type_list);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}

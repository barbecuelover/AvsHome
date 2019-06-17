package com.zw.avshome.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zw.avshome.R;
import com.zw.avshome.device.bean.BaseDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zw
 * Date: 19-6-17
 */
public class DeviceTypeAdapter extends RecyclerView.Adapter<DeviceTypeAdapter.TypeViewHolder>{


    private Context context;
    private List<String> deviceType;
    private HashMap<String, ArrayList<BaseDevice>> deviceList;
    private LayoutInflater inflater;
    private LinearLayoutManager gridlayoutManager;


    public DeviceTypeAdapter(Context context, List<String> deviceType, HashMap<String, ArrayList<BaseDevice>> deviceList) {
        this.context = context;
        this.deviceType = deviceType;
        this.deviceList = deviceList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_device_type_list,null);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        holder.mTypeName.setText(deviceType.get(position));
        ArrayList<BaseDevice> devicesListInfo = deviceList.get(deviceType.get(position));

        // 横屏显示
        gridlayoutManager = new GridLayoutManager(context, 6);
        gridlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.mRecycleView.setLayoutManager(gridlayoutManager);

        DeviceItemAdapter deviceItemAdapter = new DeviceItemAdapter(context, devicesListInfo);
        holder.mRecycleView.setAdapter(deviceItemAdapter);
        deviceItemAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return deviceType.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder{

        TextView mTypeName;
        RecyclerView mRecycleView;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTypeName = itemView.findViewById(R.id.type_name);
            this.mRecycleView = itemView.findViewById(R.id.type_list_gridView);
        }
    }
}

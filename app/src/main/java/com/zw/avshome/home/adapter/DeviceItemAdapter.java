package com.zw.avshome.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zw.avshome.R;
import com.zw.avshome.device.bean.BaseDevice;
import com.zw.avshome.utils.Constant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 具体设备种类（灯，相机等...）下的  设备列表
 * Created by zw
 * Date: 19-6-17
 */
public class DeviceItemAdapter extends RecyclerView.Adapter<DeviceItemAdapter.DeviceItemViewHolder>{

    private Context mContext;
    private List<BaseDevice> deviceList;
    private LayoutInflater inflater;

    public DeviceItemAdapter(Context mContext, List<BaseDevice> deviceList) {
        this.mContext = mContext;
        this.deviceList = deviceList;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public DeviceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_device_type_item_list,null);
        return new DeviceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceItemViewHolder holder, int position) {
        final BaseDevice baseDevice = deviceList.get(position);
        holder.tvDeviceName.setText(baseDevice.getDeviceName());
        int drawableInt = R.mipmap.device_light;
        switch (baseDevice.getDeviceType()) {
            case Constant.Device.LOCK:
                drawableInt = R.mipmap.device_lock;
                break;
            case Constant.Device.CAMERA:
                drawableInt = R.mipmap.device_camera;
                break;
            case Constant.Device.SENSOR:
                drawableInt = R.mipmap.device_room_sensor;
                break;
            case Constant.Device.LIGHT:
                drawableInt = R.mipmap.device_light;
                break;
            case Constant.Device.SWITCH:
                drawableInt = R.mipmap.device_socket;
                break;
            default:
        }

        holder.imgDeviceIcon.setImageDrawable(mContext.getDrawable(drawableInt));

        holder.mItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseDevice.startDeviceDetailActivity(mContext);
            }
        });

        holder.mItemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class DeviceItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDeviceIcon;
        TextView tvDeviceName;
        RelativeLayout mItemContainer;

        public DeviceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgDeviceIcon = itemView.findViewById(R.id.item_device_icon);
            this.tvDeviceName = itemView.findViewById(R.id.item_device_name);
            this.mItemContainer = itemView.findViewById(R.id.item_device_container);


        }
    }
}

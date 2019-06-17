package com.zw.avshome.manager;

import android.content.Context;

import com.zw.avshome.HomeApplication;
import com.zw.avshome.device.bean.BaseDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw
 * Date: 19-6-17
 */
public class DevicesManager {
    private static final String TAG = "DevicesManager";
    private static DevicesManager mDeviceManger;
    private Context mContext;
    private List<BaseDevice> deviceList = new ArrayList<>();

    private DevicesManager(Context context) {
        mContext = context;
    }

    public static DevicesManager getInstance() {
        if (mDeviceManger == null) {
            mDeviceManger = new DevicesManager(HomeApplication.getInstance());
        }
        return mDeviceManger;
    }

    public List<BaseDevice> getDeviceList(){
        return deviceList;
    }

    /**
     * add devices after binding devices
     *
     * @param device
     */
    public void addDevice(BaseDevice device) {
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getDeviceSN().equals(device.getDeviceSN())) {
                deviceList.set(i, device);
                return;
            }
        }

        deviceList.add(device);
    }

    /**
     * remove devices after unbind devices
     *
     * @param device
     */
    public void removeDevice(BaseDevice device) {
        if (isEmpty()) {
            return;
        }
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getDeviceSN().equals(device.getDeviceSN())) {
                deviceList.remove(i);
                return;
            }
        }
    }

    /**
     * remove devices after unbind devices
     *
     * @param deviceSN
     */
    public void removeDevice(String deviceSN) {
        if (isEmpty()) {
            return;
        }
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getDeviceSN().equals(deviceSN)) {
                deviceList.remove(i);
                return;
            }
        }
    }

    public boolean isEmpty() {
        return deviceList == null || deviceList.size() == 0;
    }


    public BaseDevice getDevice(String deviceSN) {
        if (isEmpty()) {
            return null;
        }
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getDeviceSN().equals(deviceSN)) {
                return deviceList.get(i);
            }
        }
        return null;
    }

    public boolean existDevice(String deviceSN) {
        BaseDevice de = getDevice(deviceSN);
        return de != null;
    }

    /**
     * report device info to mqtt server
     *
     * @param jsonStr report content
     */
    private void reportMsg2Server(String jsonStr) {

    }


    /**
     * report all device in deviceList info  to mqtt server
     */
    public void reportAllDevicesMsg2Server() {

    }

    /**
     * report  device  info  to mqtt server
     */
    public void reportDeviceMsg2Server(BaseDevice device) {
        if (device == null) {
            return;
        }

    }

    /**
     * set duration for timer in order to get devices status
     *
     * @param reportSecond unit: second
     */
    public void setTimerDurationByUser(int reportSecond) {

    }


    /**
     * use to get device status that can not report by self ,only be used by once
     */
    private void getDevicesStatusByTimer() {


    }


}

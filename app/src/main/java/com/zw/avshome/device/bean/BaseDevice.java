package com.zw.avshome.device.bean;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

public abstract class BaseDevice implements Serializable {

    private String ip;
    private String mac;
    private int port;

    private String deviceSN;
    private String deviceName;
    private String deviceType;
    private String deviceClassType;
    private String deviceDesc;
    private String deviceIcon;

    private String roomNumber;
    private boolean isOnline;


    public BaseDevice() {
    }


    public abstract  void initSelf();
    public abstract String getReportJsonData();
    public abstract void startDeviceDetailActivity(Context context);


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDeviceSN() {
        return deviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceClassType() {
        return deviceClassType;
    }

    public void setDeviceClassType(String deviceClassType) {
        this.deviceClassType = deviceClassType;
    }

    public String getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(String deviceDesc) {
        this.deviceDesc = deviceDesc;
    }

    public String getDeviceIcon() {
        return deviceIcon;
    }

    public void setDeviceIcon(String deviceIcon) {
        this.deviceIcon = deviceIcon;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}

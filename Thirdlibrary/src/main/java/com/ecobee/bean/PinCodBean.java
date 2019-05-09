package com.ecobee.bean;

/**
 * 作者：RedKeyset on 2018/12/13 19:07
 * 邮箱：redkeyset@aliyun.com
 */
public class PinCodBean {


    /**
     * ecobeePin : kn5y
     * code : ALulWWanZHCaQo4FewAM120gmsCPnP1W
     * scope : smartWrite
     * expires_in : 9
     * interval : 30
     */

    private String ecobeePin;
    private String code;
    private String scope;
    private int expires_in;
    private int interval;

    public String getEcobeePin() {
        return ecobeePin;
    }

    public void setEcobeePin(String ecobeePin) {
        this.ecobeePin = ecobeePin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}

package com.ecobee.event;

/**
 * 作者：RedKeyset on 2018/12/13 19:10
 * 邮箱：redkeyset@aliyun.com
 */
public class SentPinCodeMessage {
    private String pinCode;
    private String oauthCode;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getOauthCode() {
        return oauthCode;
    }

    public void setOauthCode(String oauthCode) {
        this.oauthCode = oauthCode;
    }

    public SentPinCodeMessage(String pinCode, String oauthCode) {
        this.pinCode = pinCode;
        this.oauthCode = oauthCode;
    }
}

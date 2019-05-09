package com.ecobee.event;

/**
 * 作者：RedKeyset on 2018/12/13 19:43
 * 邮箱：redkeyset@aliyun.com
 */
public class SentAccessTokenMessage {
    private String accessToken;
    private String refreshToken;
    private int code;

    public SentAccessTokenMessage(String accessToken, String refreshToken, int code) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

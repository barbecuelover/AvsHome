package com.ecobee.event;

public class sentRefreshTokenMessage {
    private String refreshToken;
    private String accessToken;
    private int code;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public sentRefreshTokenMessage(String refreshToken, String accessToken, int code) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.code = code;
    }
}

package com.nestlabs.sdk.rest;

public interface StreamingClient {
    Boolean start(String accessToken);
    void stop();
}

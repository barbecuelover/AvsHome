package com.nestlabs.sdk.rest;

import com.nestlabs.sdk.WwnApiUrls;

public class RestConfig {
    private String protocol;
    private String host;
    private String port;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Creates instance of RestCofig with default configuration.
     */
    public RestConfig() {
        this(WwnApiUrls.DEFAULT_PROTOCOL, WwnApiUrls.DEFAULT_WWN_URL, WwnApiUrls.DEFAULT_PORT);
    }

    public RestConfig(String protocol, String host, String port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public String getUrl() {
        return protocol + "://" + host + (port == null || port.isEmpty() ? "" : ":" + port);
    }
}

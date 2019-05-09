package com.nestlabs.sdk.rest;

public interface BackOff {
    long nextInterval();
    void reset();
}

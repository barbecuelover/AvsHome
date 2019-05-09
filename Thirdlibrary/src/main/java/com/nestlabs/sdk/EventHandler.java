package com.nestlabs.sdk;

public interface EventHandler<T> {
    void handle(T value);
}

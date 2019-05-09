package com.nestlabs.sdk.rest;

public class MissingTokenException extends RuntimeException {

    public MissingTokenException() {
        super("Access token is empty or not provided.");
    }
}

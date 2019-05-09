package com.nestlabs.sdk.rest.parsers;

import com.nestlabs.sdk.NestException;

public class ParserException extends NestException {

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Throwable throwable) {
        super(throwable);
    }
}

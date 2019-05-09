package com.nestlabs.sdk.rest.parsers;

public interface Parser {
    void parse(String msg) throws ParserException;
}

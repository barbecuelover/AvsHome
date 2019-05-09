package com.nestlabs.sdk.rest.parsers;

public interface Mapper {
    void map(StreamingEvent event) throws ParserException;
}

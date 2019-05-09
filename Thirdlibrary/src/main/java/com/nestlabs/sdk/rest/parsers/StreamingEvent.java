package com.nestlabs.sdk.rest.parsers;

public class StreamingEvent {
    private final String eventType;
    private final String message;

    public String getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public StreamingEvent(final String eventType, final String message) {
        this.eventType = eventType;
        this.message = message;
    }
}

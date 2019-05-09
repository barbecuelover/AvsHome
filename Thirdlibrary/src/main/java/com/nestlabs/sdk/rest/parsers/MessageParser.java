package com.nestlabs.sdk.rest.parsers;

import android.support.annotation.NonNull;

public class MessageParser implements Parser {

    private final Mapper mapper;

    public MessageParser(@NonNull final Mapper mapper) {
        this.mapper = mapper;
    }

    //May contain one or more events
    @Override
    public void parse(String msg) throws ParserException {
        if (msg == null || msg.length() == 0) return;

        String[] lines = msg.split("\n");
        int i = 0;
        while(i < lines.length) {
            String currentLine = lines[i];
            if (currentLine.startsWith("{\"error\":")) {
                mapper.map(new StreamingEvent("error", currentLine));
            } else if (currentLine.startsWith("event:") && lines.length > i + 1) {
                String nextLine = lines[i + 1];

                if (currentLine.length() <= 8) {
                    throw new ParserException("Unexpected length of event line.");
                }

                if (nextLine.length() <= 7) {
                    throw new ParserException("Unexpected length of data line.");
                }

                String eventType = currentLine.substring(7); //7 = length of("event: ")
                String json = nextLine.substring(6); //6 = length of("data: ")

                mapper.map(new StreamingEvent(eventType, json));
                i++;
            }
            i++;
        }
    }
}

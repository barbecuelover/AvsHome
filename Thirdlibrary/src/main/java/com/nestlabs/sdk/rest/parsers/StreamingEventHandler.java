package com.nestlabs.sdk.rest.parsers;

import com.nestlabs.sdk.models.GlobalUpdate;

public interface StreamingEventHandler {
    void handleData(GlobalUpdate eventData);
    void handleError(ErrorMessage errorMessage);
    void handleAuthRevoked();
}

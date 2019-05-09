package com.nestlabs.sdk.rest.parsers;

import com.nestlabs.sdk.models.GlobalUpdate;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.NestListener;

import java.util.ArrayList;
import java.util.List;

public class Notifier implements StreamingEventHandler {

    private final List<NestListener> listeners = new ArrayList<>();

    public void addListener(final NestListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final NestListener listener) {
        listeners.remove(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    @Override
    public void handleData(final GlobalUpdate event) {
        for(NestListener listener : listeners) {
            if (listener instanceof NestListener.GlobalListener)
                ((NestListener.GlobalListener) listener).onUpdate(event);
            else if (listener instanceof NestListener.DeviceListener)
                ((NestListener.DeviceListener) listener).onUpdate(event.getDevices());
            else if (listener instanceof NestListener.StructureListener)
                ((NestListener.StructureListener) listener).onUpdate(event.getStructures());
            else if (listener instanceof NestListener.ThermostatListener)
                ((NestListener.ThermostatListener) listener).onUpdate(event.getThermostats());
            else if (listener instanceof NestListener.CameraListener)
                ((NestListener.CameraListener) listener).onUpdate(event.getCameras());
            else if (listener instanceof NestListener.SmokeCOAlarmListener)
                ((NestListener.SmokeCOAlarmListener) listener).onUpdate(event.getSmokeCOAlarms());
            else if (listener instanceof NestListener.MetadataListener)
                ((NestListener.MetadataListener) listener).onUpdate(event.getMetadata());
        }
    }

    @Override
    public void handleError(ErrorMessage errorMessage) {
        Boolean authError = errorMessage.getError().equals("unauthorized");

        for(NestListener listener : listeners) {
            if (listener instanceof NestListener.AuthListener && authError) {
                ((NestListener.AuthListener) listener).onAuthFailure(new NestException(errorMessage.getMessage()));
            } else if (listener instanceof NestListener.ErrorListener && !authError) {
                ((NestListener.ErrorListener) listener).onError(errorMessage);
            }
        }
    }

    @Override
    public void handleAuthRevoked() {
        for(NestListener listener : listeners) {
            if (listener instanceof NestListener.AuthListener) {
                ((NestListener.AuthListener) listener).onAuthRevoked();
            }
        }
    }
}

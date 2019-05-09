package com.nestlabs.sdk.rest.parsers;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nestlabs.sdk.models.Camera;
import com.nestlabs.sdk.models.GlobalUpdate;
import com.nestlabs.sdk.models.Metadata;
import com.nestlabs.sdk.models.SmokeCOAlarm;
import com.nestlabs.sdk.models.Structure;
import com.nestlabs.sdk.models.Thermostat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjectModelMapper implements Mapper {

    private ObjectMapper mapper = new ObjectMapper();
    private final StreamingEventHandler eventHandler;

    public ObjectModelMapper(@NonNull final StreamingEventHandler handler) {
        this.eventHandler = handler;
    }

    @SuppressWarnings("unchecked")
    private <T extends Parcelable> void deserialize(
            ObjectNode node, Map<String, List<? extends Parcelable>> lists, String key, Class<T> clazz)
            throws ParserException {
        try {
            T obj = mapper.readValue(node.toString(), clazz);
            List<T> list = (List<T>) lists.get(key);
            list.add(obj);
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }

    private <T extends Parcelable> void parse(
            ObjectNode node, Map<String, List<? extends Parcelable>> lists,
            String key, Class<T> clazz) throws ParserException {

        Iterator<String> names = node.fieldNames();
        while(names.hasNext()) {
            String name = names.next();
            JsonNode current = node.get(name);
            if (!(current instanceof ObjectNode)) continue;

            if (Constants.KEY_DEVICES.equals(name) || "data".equals(name))
                parse((ObjectNode) current, lists, null, null);
            else if (Constants.KEY_STRUCTURES.equals(name))
                parse((ObjectNode) current, lists, Constants.KEY_STRUCTURES, Structure.class);
            else if (Constants.KEY_THERMOSTATS.equals(name))
                parse((ObjectNode) current, lists, Constants.KEY_THERMOSTATS, Thermostat.class);
            else if (Constants.KEY_CAMERAS.equals(name))
                parse((ObjectNode) current, lists, Constants.KEY_CAMERAS, Camera.class);
            else if (Constants.KEY_SMOKE_CO_ALARMS.equals(name))
                parse((ObjectNode) current, lists, Constants.KEY_SMOKE_CO_ALARMS, SmokeCOAlarm.class);
            else if (Constants.KEY_METADATA.equals(name))
                deserialize((ObjectNode) current, lists, Constants.KEY_METADATA, Metadata.class);
            else if (key != null && clazz != null) {
                deserialize((ObjectNode) current, lists, key, clazz);
            }
        }
    }

    private void mapData(String json) throws ParserException  {
        List<Thermostat> thermostats = new ArrayList<>();
        List<Camera> cameras = new ArrayList<>();
        List<SmokeCOAlarm> smokeAlarms = new ArrayList<>();
        List<Structure> structures = new ArrayList<>();
        List<Metadata> metadata = new ArrayList<>();

        Map<String, List<? extends Parcelable>> lists = new HashMap<>();
        lists.put(Constants.KEY_THERMOSTATS, thermostats);
        lists.put(Constants.KEY_STRUCTURES, structures);
        lists.put(Constants.KEY_CAMERAS, cameras);
        lists.put(Constants.KEY_SMOKE_CO_ALARMS, smokeAlarms);
        lists.put(Constants.KEY_METADATA, metadata);

        try {
            JsonNode node = mapper.readTree(json);
            parse((ObjectNode) node, lists, null, null);
        } catch (IOException e) {
            throw new ParserException(e);
        }

        eventHandler.handleData(new GlobalUpdate(
                thermostats, smokeAlarms, cameras, structures,
                metadata.size() > 0 ? metadata.get(0) : null));
    }

    private void mapError(String json) throws ParserException {
        try {
            ErrorMessage error = mapper.readValue(json, ErrorMessage.class);
            eventHandler.handleError(error);
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }

    @Override
    public void map(StreamingEvent event) throws ParserException {
        switch(event.getEventType()) {
            case "put":
                mapData(event.getMessage());
                break;
            case "auth_revoked":
                eventHandler.handleAuthRevoked();
                break;
            case "error":
                mapError(event.getMessage());
                break;
        }
    }
}

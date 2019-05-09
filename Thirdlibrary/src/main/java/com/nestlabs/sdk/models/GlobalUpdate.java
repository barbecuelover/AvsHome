/*
 * Copyright 2016, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nestlabs.sdk.models;

import java.util.List;

/**
 * GlobalUpdate contains the state of all devices, structures and metadata in the Nest account when
 * a change is detected in anything. A GlobalUpdate object is returned by {@link
 * com.nestlabs.sdk.NestListener.GlobalListener#onUpdate(GlobalUpdate)} when an update occurs.
 */
public class GlobalUpdate {
    private final DeviceUpdate devices;
    private final List<Structure> mStructures;
    private final Metadata mMetadata;

    public GlobalUpdate(List<Thermostat> thermostats, List<SmokeCOAlarm> smokeCOAlarms,
                 List<Camera> cameras, List<Structure> structures, Metadata metadata) {
        devices = new DeviceUpdate(thermostats, smokeCOAlarms, cameras);
        mStructures = structures;
        mMetadata = metadata;
    }

    public final DeviceUpdate getDevices() {
        return devices;
    }

    /**
     * Returns all the {@link Thermostat} objects in the Nest account at the time of the update.
     *
     * @return all the {@link Thermostat} objects in the Nest account at the time of the update.
     */
    public final List<Thermostat> getThermostats() {
        return devices.getThermostats();
    }

    /**
     * Returns all the {@link SmokeCOAlarm} objects in the Nest account at the time of the update.
     *
     * @return all the {@link SmokeCOAlarm} objects in the Nest account at the time of the update.
     */
    public final List<SmokeCOAlarm> getSmokeCOAlarms() {
        return devices.getSmokeCOAlarms();
    }

    /**
     * Returns all the {@link Camera} objects in the Nest account at the time of the update.
     *
     * @return all the {@link Camera} objects in the Nest account at the time of the update.
     */
    public final List<Camera> getCameras() {
        return devices.getCameras();
    }

    /**
     * Returns all the {@link Structure} objects in the Nest account at the time of the update.
     *
     * @return all the {@link Structure} objects in the Nest account at the time of the update.
     */
    public final List<Structure> getStructures() {
        return mStructures;
    }

    /**
     * Returns the {@link Metadata} object in the Nest account at the time of the update.
     *
     * @return the {@link Metadata} object in the Nest account at the time of the update.
     */
    public final Metadata getMetadata() {
        return mMetadata;
    }
}

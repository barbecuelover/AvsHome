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

package com.nestlabs.sdk;

import android.support.annotation.NonNull;

import com.nestlabs.sdk.models.Camera;
import com.nestlabs.sdk.models.DeviceUpdate;
import com.nestlabs.sdk.models.GlobalUpdate;
import com.nestlabs.sdk.models.Metadata;
import com.nestlabs.sdk.models.SmokeCOAlarm;
import com.nestlabs.sdk.models.Structure;
import com.nestlabs.sdk.models.Thermostat;
import com.nestlabs.sdk.rest.parsers.ErrorMessage;

import java.util.List;

/**
 * NestListener are listeners that receive events from the WwnClient and allow a user to complete
 * actions when those events occur.
 */
public interface NestListener {

    /**
     * Listens for updates to any objects in a user's Nest account, including all devices,
     * structures and metadata.
     */
    interface GlobalListener extends NestListener {

        /**
         * Called when an update occurs on any device, structure or metadata object.
         *
         * @param update a {@link GlobalUpdate} object containing all values at the time of the
         *               update.
         */
        void onUpdate(@NonNull GlobalUpdate update);
    }

    /**
     * Listens for updates on all devices in a user's Nest account.
     */
    interface DeviceListener extends NestListener {
        /**
         * Called when an update occurs on any device object.
         *
         * @param update a {@link DeviceUpdate} object containing all devices at the time of the
         *               update.
         */
        void onUpdate(@NonNull DeviceUpdate update);
    }

    /**
     * Listens for updates to any {@link Camera} in a user's Nest account.
     */
    interface CameraListener extends NestListener {
        /**
         * Called when an update occurs on any {@link Camera} device.
         *
         * @param cameras an {@link List} of all {@link Camera} objects in the user's account
         *                at the time of the update.
         */
        void onUpdate(@NonNull List<Camera> cameras);
    }

    /**
     * Listens for updates to any {@link Thermostat} in a user's Nest account.
     */
    interface ThermostatListener extends NestListener {
        /**
         * Called when an update occurs on any {@link Thermostat} device.
         *
         * @param thermostats an {@link List} of all {@link Thermostat} objects in the user's
         *                    account at the time of the update.
         */
        void onUpdate(@NonNull List<Thermostat> thermostats);
    }

    /**
     * Listens for updates to any {@link Structure} in a user's Nest account.
     */
    interface StructureListener extends NestListener {
        /**
         * Called when an update occurs on any {@link Structure} object.
         *
         * @param structures an {@link List} of all {@link Structure} objects in the user's
         *                   account at the time of the update.
         */
        void onUpdate(@NonNull List<Structure> structures);
    }

    /**
     * Listens for updates to any {@link SmokeCOAlarm} in a user's Nest account.
     */
    interface SmokeCOAlarmListener extends NestListener {
        /**
         * Called when an update occurs on any {@link SmokeCOAlarm} device.
         *
         * @param smokeCOAlarms an {@link List} of all {@link SmokeCOAlarm} objects in the
         *                      user's account at the time of the update.
         */
        void onUpdate(@NonNull List<SmokeCOAlarm> smokeCOAlarms);
    }

    /**
     * Listens for updates to the {@link Metadata} object in a user's Nest account.
     */
    interface MetadataListener extends NestListener {
        /**
         * Called when an update occurs on the {@link Metadata} object.
         *
         * @param metadata the {@link Metadata} object in user's account at the time of the update.
         */
        void onUpdate(@NonNull Metadata metadata);
    }

    /**
     * Listens for updates to the status of authentication of {@link WwnApiUrls} to the Nest service.
     */
    interface AuthListener extends NestListener {
        /**
         * Called when the authentication with the token fails. An exception is returned that can
         * either be thrown or read to determine the cause of the error.
         *
         * @param exception a {@link NestException} object containing the error that occurred.
         */
        void onAuthFailure(NestException exception);

        /**
         * Called when a previously authenticated connection becomes unauthenticated. This usually
         * occurs if the access token is revoked or has expired.
         */
        void onAuthRevoked();
    }

    interface ErrorListener extends NestListener {

        void onError(ErrorMessage errorMessage);
    }
}

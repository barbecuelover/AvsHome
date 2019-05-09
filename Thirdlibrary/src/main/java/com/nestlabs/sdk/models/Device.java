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

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Device represents any Nest device. All devices (e.g. {@link Thermostat}, {@link Camera}, {@link
 * SmokeCOAlarm}) should extend Device and thus will contain all properties that Device contains.
 */
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device implements Parcelable {
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_LOCALE = "locale";
    public static final String KEY_SOFTWARE_VERSION = "software_version";
    public static final String KEY_STRUCTURE_ID = "structure_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_NAME_LONG = "name_long";
    public static final String KEY_LAST_CONNECTION = "last_connection";
    public static final String KEY_IS_ONLINE = "is_online";
    public static final String KEY_WHERE_ID = "where_id";

    @JsonProperty(KEY_DEVICE_ID)
    String mDeviceId;

    @JsonProperty(KEY_LOCALE)
    String mLocale;

    @JsonProperty(KEY_SOFTWARE_VERSION)
    String mSoftwareVersion;

    @JsonProperty(KEY_STRUCTURE_ID)
    String mStructureId;

    @JsonProperty(KEY_NAME)
    String mName;

    @JsonProperty(KEY_NAME_LONG)
    String mNameLong;

    @JsonProperty(KEY_LAST_CONNECTION)
    String mLastConnection;

    @JsonProperty(KEY_IS_ONLINE)
    boolean mIsOnline;

    @JsonProperty(KEY_WHERE_ID)
    String mWhereId;

    public Device() {}

    protected Device(Parcel in) {
        mDeviceId = in.readString();
        mLocale = in.readString();
        mSoftwareVersion = in.readString();
        mStructureId = in.readString();
        mName = in.readString();
        mNameLong = in.readString();
        mLastConnection = in.readString();
        mIsOnline = Utils.readBoolean(in);
        mWhereId = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Device) {
            Device device = (Device) obj;
            return device.toString().equals(this.toString());
        }
        return false;
    }

    /**
     * Returns the unique identifier of this device.
     *
     * @return the unique identifier of this device.
     */
    @JsonGetter(KEY_DEVICE_ID)
    public String getDeviceId() {
        return mDeviceId;
    }

    /**
     * Returns the locale for this device, if set.
     *
     * @return the locale for this device, if set.
     */
    @JsonGetter(KEY_LOCALE)
    public String getLocale() {
        return mLocale;
    }

    /**
     * Returns the current software version that this device has installed.
     *
     * @return the current software version that this device has installed.
     */
    @JsonGetter(KEY_SOFTWARE_VERSION)
    public String getSoftwareVersion() {
        return mSoftwareVersion;
    }

    /**
     * Returns the id of the structure that this device is contained in.
     *
     * @return the id of the structure that this device is contained in.
     */
    @JsonGetter(KEY_STRUCTURE_ID)
    public String getStructureId() {
        return mStructureId;
    }

    /**
     * Returns an abbreviated version of the user's name for this device.
     *
     * @return an abbreviated version of the user's name for this device.
     */
    @JsonGetter(KEY_NAME)
    public String getName() {
        return mName;
    }

    /**
     * Returns a verbose version of the user's name for this device.
     *
     * @return a verbose version of the user's name for this device.
     */
    @JsonGetter(KEY_NAME_LONG)
    public String getNameLong() {
        return mNameLong;
    }

    /**
     * Returns the timestamp (in ISO-8601 format) when the device last connected to the Nest.
     *
     * @return the timestamp (in ISO-8601 format) when the device last connected to the Nest.
     */
    @JsonGetter(KEY_LAST_CONNECTION)
    public String getLastConnection() {
        return mLastConnection;
    }

    /**
     * Returns whether the device is online.
     *
     * @return whether the device is online.
     */
    @JsonGetter(KEY_IS_ONLINE)
    public boolean isOnline() {
        return mIsOnline;
    }

    /**
     * Returns a unique, Nest-generated identifier that represents the display name of the device.
     *
     * @return a unique, Nest-generated identifier that represents the display name of the device.
     */
    @JsonGetter(KEY_WHERE_ID)
    public String getWhereId() {
        return mWhereId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDeviceId);
        dest.writeString(mLocale);
        dest.writeString(mSoftwareVersion);
        dest.writeString(mStructureId);
        dest.writeString(mName);
        dest.writeString(mNameLong);
        dest.writeString(mLastConnection);
        Utils.writeBoolean(dest, mIsOnline);
        dest.writeString(mWhereId);
    }
}

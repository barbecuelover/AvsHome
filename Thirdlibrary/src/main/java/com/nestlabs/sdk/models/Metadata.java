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
 * Metadata holds the information related to your Nest client.
 */
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata implements Parcelable {
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_CLIENT_VERSION = "client_version";

    @JsonProperty(KEY_ACCESS_TOKEN)
    private String mAccessToken;

    @JsonProperty(KEY_CLIENT_VERSION)
    private long mClientVersion;

    public Metadata() {}

    public Metadata(Parcel in) {
        mAccessToken = in.readString();
        mClientVersion = in.readLong();
    }

    /**
     * Returns the access token associated with your Nest API connection.
     *
     * @return the access token associated with your Nest API connection.
     */
    @JsonGetter(KEY_ACCESS_TOKEN)
    public String getAccessToken() {
        return mAccessToken;
    }

    /**
     * Returns the last user-authorized version of a product. The client version increments every
     * time a change is made to the permissions in a product. You can use this to make sure that a
     * user has the right permissions for certain features.
     * <p/>
     * See here for more information: https://goo.gl/J4RPAc
     *
     * @return the last user-authorized version of a product.
     */
    @JsonGetter(KEY_CLIENT_VERSION)
    public long getClientVersion() {
        return mClientVersion;
    }

    public static final Creator<Metadata> CREATOR = new Creator<Metadata>() {
        @Override
        public Metadata createFromParcel(Parcel in) {
            return new Metadata(in);
        }

        @Override
        public Metadata[] newArray(int size) {
            return new Metadata[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAccessToken);
        dest.writeLong(mClientVersion);
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Metadata) {
            Metadata metadata = (Metadata) obj;
            return metadata.toString().equals(this.toString());
        } else {
            return false;
        }
    }
}

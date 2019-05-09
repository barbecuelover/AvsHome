/*
 * Copyright 2015, Google Inc.
 * Copyright 2014, Nest Labs Inc.
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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * NestToken contains the access token and expiry duration associated with an authenticated user.
 */
public final class NestToken implements Parcelable {

    static final String KEY_TOKEN = "access_token";
    static final String KEY_EXPIRES_IN_SECS = "expires_in";

    @JsonProperty(KEY_TOKEN)
    private String mToken;

    @JsonProperty(KEY_EXPIRES_IN_SECS)
    private long mExpiresInSecs;

    public NestToken() {}

    /**
     * Create a new NestToken.
     *
     * @param token     the access token.
     * @param expiresIn the number of seconds until the token expires.
     */
    public NestToken(String token, long expiresIn) {
        mToken = token;
        mExpiresInSecs = expiresIn;
    }

    private NestToken(Parcel in) {
        mToken = in.readString();
        mExpiresInSecs = in.readLong();
    }

    /**
     * Returns the access token. Use the access token to authenticate with the WWN API.
     *
     * @return the access token.
     */
    public String getToken() {
        return mToken;
    }

    /**
     * Returns the number of seconds until the token expires.
     *
     * @return the number of seconds until the token expires.
     */
    public long getExpiresIn() {
        return mExpiresInSecs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NestToken) {
            NestToken token = (NestToken) obj;
            return token.toString().equals(this.toString());
        }
        return false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mToken);
        dest.writeLong(mExpiresInSecs);
    }

    public static final Creator<NestToken> CREATOR =
            new Creator<NestToken>() {
                @Override
                public NestToken createFromParcel(Parcel source) {
                    return new NestToken(source);
                }

                @Override
                public NestToken[] newArray(int size) {
                    return new NestToken[size];
                }
            };
}

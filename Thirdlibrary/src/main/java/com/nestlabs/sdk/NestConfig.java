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

package com.nestlabs.sdk;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * NestConfig holds the various configuration values needed to launch the OAuth 2.0 flow.
 */
public final class NestConfig implements Parcelable {
    public static final String KEY_CLIENT_ID = "client_id";
    public static final String KEY_CLIENT_SECRET = "client_secret";
    public static final String KEY_REDIRECT_URL = "redirect_url";
    public static final String KEY_STATE_VALUE = "state_value";

    private String mClientID;
    private String mStateValue;
    private String mClientSecret;
    private String mRedirectURL;

    private NestConfig(Builder builder) {
        mClientID = builder.mBuilderClientID;
        mStateValue = builder.mBuilderStateValue;
        mClientSecret = builder.mBuilderClientSecret;
        mRedirectURL = builder.mBuilderRedirectURL;
    }

    private NestConfig(Parcel in) {
        mClientID = in.readString();
        mStateValue = in.readString();
        mClientSecret = in.readString();
        mRedirectURL = in.readString();
    }

    /**
     * Returns the client id.
     *
     * @return the client id.
     */
    public String getClientID() {
        return mClientID;
    }

    /**
     * Returns the state value. This is randomly generated for each {@link NestConfig}.
     *
     * @return the state value.
     */
    public String getStateValue() {
        return mStateValue;
    }

    /**
     * Returns the client secret. Keep this secret safe.
     *
     * @return the client secret.
     */
    public String getClientSecret() {
        return mClientSecret;
    }

    /**
     * Returns the redirect URL. Must match the redirect URL set in the Nest developer portal.
     *
     * @return the redirect URL.
     */
    public String getRedirectURL() {
        return mRedirectURL;
    }

    /**
     * Builder for creating {@link NestConfig} objects.
     */
    public static class Builder {
        String mBuilderClientID;
        String mBuilderRedirectURL;
        String mBuilderStateValue;
        String mBuilderClientSecret;

        /**
         * Sets the client id.
         *
         * @param id the client id.
         * @return the {@link Builder} instance.
         */
        public Builder clientID(String id) {
            mBuilderClientID = id;
            return this;
        }

        /**
         * Sets the client secret.
         *
         * @param secret the client secret.
         * @return the {@link Builder} instance.
         */
        public Builder clientSecret(String secret) {
            mBuilderClientSecret = secret;
            return this;
        }

        /**
         * Sets the redirect URL. Must match the redirect URL set in the Nest developer portal.
         *
         * @param url the redirect url.
         * @return the {@link Builder} instance.
         */
        public Builder redirectURL(String url) {
            mBuilderRedirectURL = url;
            return this;
        }

        /**
         * Sets the state value. This method be called directly, as the state value is automatically
         * and randomly generated when {@link Builder#build()} is called.
         *
         * @param state the state value.
         * @return the {@link Builder} instance.
         */
        private Builder stateValue(String state) {
            mBuilderStateValue = state;
            return this;
        }

        /**
         * Builds and returns the new {@link NestConfig} object.
         *
         * @return the new {@link NestConfig} object.
         */
        public NestConfig build() {
            // Create random state value on each creation
            stateValue("app-state" + System.nanoTime() + "-" + Math.abs(new Random().nextInt()));
            return new NestConfig(this);
        }
    }

    @Override
    public String toString() {
        try {
            JSONObject json = new JSONObject();
            json.put(KEY_CLIENT_ID, mClientID);
            json.put(KEY_CLIENT_SECRET, mClientSecret);
            json.put(KEY_REDIRECT_URL, mRedirectURL);
            json.put(KEY_STATE_VALUE, mStateValue);
            return json.toString();
        } catch (JSONException e) {
            return super.toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof NestConfig) {
            NestConfig t = (NestConfig) obj;
            return t.toString().equals(this.toString());
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mClientID);
        dest.writeString(mStateValue);
        dest.writeString(mClientSecret);
        dest.writeString(mRedirectURL);
    }

    public static final Creator<NestConfig> CREATOR =
            new Creator<NestConfig>() {
                @Override
                public NestConfig createFromParcel(Parcel source) {
                    return new NestConfig(source);
                }

                @Override
                public NestConfig[] newArray(int size) {
                    return new NestConfig[size];
                }
            };
}

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
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides utilities methods for various common operations within this library.
 */
public final class Utils {

    private static final ObjectMapper sMapper = new ObjectMapper();

    /**
     * Writes a boolean value to a Parcel.
     *
     * @param out   the Parcel to write to.
     * @param value the boolean value to write.
     */
    static void writeBoolean(Parcel out, boolean value) {
        out.writeInt(value ? 1 : 0);
    }

    /**
     * Reads a boolean value from a Parcel.
     *
     * @param in the Parcel to read.
     * @return the boolean value read from the Parcel.
     */
    static boolean readBoolean(Parcel in) {
        return in.readInt() != 0;
    }

    /**
     * Returns the object in a JSON string representation if possible. If this fails, it will return
     * the superclass' string representation of the object.
     *
     * @param obj the object to convert.
     * @return a string representation of the object.
     */
    static String toString(Object obj) {
        try {
            return sMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.getClass().getSuperclass().toString();
        }
    }

    /**
     * Returns whether any of the provided Strings are empty (null or zero-length).
     *
     * @param args strings to check for emptiness.
     * @return true if any of the provided strings is null or is zero-length, false otherwise.
     */
    public static boolean isAnyEmpty(String... args) {
        if (args == null) {
            return false;
        }

        for (String s : args) {
            if (TextUtils.isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds a path incrementally.
     */
    public static class PathBuilder {
        private final StringBuilder mBuilder;

        /**
         * Creates a new PathBuilder.
         */
        public PathBuilder() {
            mBuilder = new StringBuilder();
        }

        /**
         * Appends a string to the path.
         *
         * @param entry string to append to the path.
         * @return the PathBuilder. Allows for chaining multiple appends.
         */
        public PathBuilder append(@NonNull String entry) {
            mBuilder.append("/").append(entry);
            return this;
        }

        /**
         * Returns the resulting path.
         *
         * @return the resulting path.
         */
        public String build() {
            return mBuilder.toString();
        }
    }
}

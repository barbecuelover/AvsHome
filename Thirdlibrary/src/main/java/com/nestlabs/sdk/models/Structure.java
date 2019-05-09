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

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Structure represents and contains all properties of a Nest structure.
 */
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Structure implements Parcelable {
    public static final String KEY_STRUCTURE_ID = "structure_id";
    public static final String KEY_THERMOSTATS = "thermostats";
    public static final String KEY_SMOKE_CO_ALARMS = "smoke_co_alarms";
    public static final String KEY_CAMERAS = "cameras";
    public static final String KEY_DEVICES = "devices";
    public static final String KEY_AWAY = "away";
    public static final String KEY_NAME = "name";
    public static final String KEY_COUNTRY_CODE = "country_code";
    public static final String KEY_POSTAL_CODE = "postal_code";
    public static final String KEY_PEAK_PERIOD_START_TIME = "peak_period_start_time";
    public static final String KEY_PEAK_PERIOD_END_TIME = "peak_period_end_time";
    public static final String KEY_TIME_ZONE = "time_zone";
    public static final String KEY_ETA = "eta";
    public static final String KEY_RHR_ENROLLMENT = "rhr_enrollment";
    public static final String KEY_WHERES = "wheres";

    @JsonProperty(KEY_STRUCTURE_ID)
    private String mStructureId;

    @JsonProperty(KEY_THERMOSTATS)
    private ArrayList<String> mThermostats;

    @JsonProperty(KEY_SMOKE_CO_ALARMS)
    private ArrayList<String> mSmokeCoAlarms;

    @JsonProperty(KEY_CAMERAS)
    private ArrayList<String> mCameras;

    @JsonProperty(KEY_DEVICES)
    private LinkedHashMap<String, Object> mDevices;

    @JsonProperty(KEY_AWAY)
    private String mAway;

    @JsonProperty(KEY_NAME)
    private String mName;

    @JsonProperty(KEY_COUNTRY_CODE)
    private String mCountryCode;

    @JsonProperty(KEY_POSTAL_CODE)
    private String mPostalCode;

    @JsonProperty(KEY_PEAK_PERIOD_START_TIME)
    private String mPeakPeriodStartTime;

    @JsonProperty(KEY_PEAK_PERIOD_END_TIME)
    private String mPeakPeriodEndTime;

    @JsonProperty(KEY_TIME_ZONE)
    private String mTimeZone;

    @JsonProperty(KEY_ETA)
    private ETA mEta;

    @JsonProperty(KEY_RHR_ENROLLMENT)
    private boolean mRhrEnrollment;

    @JsonProperty(KEY_WHERES)
    private LinkedHashMap<String, Where> mWheres;

    public Structure() {}

    public Structure(Parcel in) {
        mStructureId = in.readString();
        mThermostats = in.createStringArrayList();
        mSmokeCoAlarms = in.createStringArrayList();
        mCameras = in.createStringArrayList();
        mDevices = new LinkedHashMap<>();
        in.readMap(mDevices, LinkedHashMap.class.getClassLoader());
        mAway = in.readString();
        mName = in.readString();
        mCountryCode = in.readString();
        mPostalCode = in.readString();
        mPeakPeriodStartTime = in.readString();
        mPeakPeriodEndTime = in.readString();
        mTimeZone = in.readString();
        mEta = in.readParcelable(ETA.class.getClassLoader());
        mRhrEnrollment = Utils.readBoolean(in);
        mWheres = new LinkedHashMap<>();
        in.readMap(mWheres, LinkedHashMap.class.getClassLoader());
    }

    /**
     * Returns the ID number of the structure.
     *
     * @return the ID number of the structure.
     */
    @JsonGetter(KEY_STRUCTURE_ID)
    public String getStructureId() {
        return mStructureId;
    }

    /**
     * Returns the list of thermostats in the structure.
     *
     * @return the list of thermostats in the structure.
     */
    @JsonGetter(KEY_THERMOSTATS)
    public ArrayList<String> getThermostats() {
        return mThermostats;
    }

    /**
     * Returns the list of smoke+CO alarms in the structure.
     *
     * @return the list of smoke+CO alarms in the structure.
     */
    @JsonGetter(KEY_SMOKE_CO_ALARMS)
    public ArrayList<String> getSmokeCoAlarms() {
        return mSmokeCoAlarms;
    }

    /**
     * Returns the list of cameras in the structure.
     *
     * @return the list of cameras in the structure.
     */
    @JsonGetter(KEY_CAMERAS)
    public ArrayList<String> getCameras() {
        return mCameras;
    }

    /**
     * Returns an object containing $company and $product_type information.
     * <p/>
     * More info: https://developer.nest.com/documentation/api-reference/overview#devices2
     *
     * @return an object containing $company and $product_type information.
     */
    @JsonGetter(KEY_DEVICES)
    public LinkedHashMap<String, Object> getDevices() {
        return mDevices;
    }

    /**
     * Returns the away state of the structure. In order for a structure to be in the Auto-Away
     * state, all devices must also be in Auto-Away state. When any device leaves the Auto-Away
     * state, then the structure also leaves the Auto-Away state.
     * <p/>
     * Values can be "home", "away", or "auto-away"
     *
     * @return the away state of the structure.
     */
    @JsonGetter(KEY_AWAY)
    public String getAway() {
        return mAway;
    }

    /**
     * Returns the user-defined name of the structure.
     *
     * @return the user-defined name of the structure.
     */
    @JsonGetter(KEY_NAME)
    public String getName() {
        return mName;
    }

    /**
     * Returns the country code, in ISO 3166 alpha-2 format.
     *
     * @return the country code, in ISO 3166 alpha-2 format.
     */
    @JsonGetter(KEY_COUNTRY_CODE)
    public String getCountryCode() {
        return mCountryCode;
    }

    /**
     * Returns the postal or zip code, depending on the country.
     *
     * @return the postal or zip code.
     */
    @JsonGetter(KEY_POSTAL_CODE)
    public String getPostalCode() {
        return mPostalCode;
    }

    /**
     * Returns the start time of the Energy rush hour event, in ISO 8601 format.
     *
     * @return the start time of the Energy rush hour event, in ISO 8601 format.
     */
    @JsonGetter(KEY_PEAK_PERIOD_START_TIME)
    public String getPeakPeriodStartTime() {
        return mPeakPeriodStartTime;
    }

    /**
     * Returns the end time of the Energy rush hour event, in ISO 8601 format.
     *
     * @return the end time of the Energy rush hour event, in ISO 8601 format.
     */
    @JsonGetter(KEY_PEAK_PERIOD_END_TIME)
    public String getPeakPeriodEndTime() {
        return mPeakPeriodEndTime;
    }

    /**
     * Returns the time zone at the structure, in IANA time zone format.
     *
     * @return the time zone at the structure, in IANA time zone format.
     */
    @JsonGetter(KEY_TIME_ZONE)
    public String getTimeZone() {
        return mTimeZone;
    }

    /**
     * Returns the ETA object that can be set on a structure.
     * <p/>
     * More info: https://developer.nest.com/documentation/cloud/eta-guide
     *
     * @return the ETA object that can be set on a structure.
     */
    @JsonGetter(KEY_ETA)
    public ETA getEta() {
        return mEta;
    }

    /**
     * Returns the Rush Hour Rewards enrollment status.
     * <p/>
     * More info: http://support.nest.com/article/What-is-Rush-Hour-Rewards
     *
     * @return the Rush Hour Rewards enrollment status.
     */
    @JsonGetter(KEY_RHR_ENROLLMENT)
    public boolean getRhrEnrollment() {
        return mRhrEnrollment;
    }

    /**
     * Returns an object containing where identifiers for devices in the structure.
     * <p/>
     * More info: https://developer.nest.com/documentation/cloud/how-to-structures-object#wheres
     *
     * @return an object containing where identifiers for devices in the structure.
     */
    @JsonGetter(KEY_WHERES)
    public LinkedHashMap<String, Where> getWheres() {
        return mWheres;
    }

    public static final Creator<Structure> CREATOR = new Creator<Structure>() {
        @Override
        public Structure createFromParcel(Parcel in) {
            return new Structure(in);
        }

        @Override
        public Structure[] newArray(int size) {
            return new Structure[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mStructureId);
        dest.writeStringList(mThermostats);
        dest.writeStringList(mSmokeCoAlarms);
        dest.writeStringList(mCameras);
        dest.writeMap(mDevices);
        dest.writeString(mAway);
        dest.writeString(mName);
        dest.writeString(mCountryCode);
        dest.writeString(mPostalCode);
        dest.writeString(mPeakPeriodStartTime);
        dest.writeString(mPeakPeriodEndTime);
        dest.writeString(mTimeZone);
        dest.writeParcelable(mEta, flags);
        Utils.writeBoolean(dest, mRhrEnrollment);
        dest.writeMap(mWheres);
    }

    /**
     * Where is an object containing where identifiers for devices in the structure.
     */
    public static class Where implements Parcelable {

        public static final String KEY_WHERE_ID = "where_id";
        public static final String KEY_NAME = "name";

        @JsonProperty(KEY_WHERE_ID)
        private String mWhereId;

        @JsonProperty(KEY_NAME)
        private String name;

        public Where() {}

        protected Where(Parcel in) {
            mWhereId = in.readString();
            name = in.readString();
        }

        /**
         * Returns the name of the room. E.g. "Bedroom".
         *
         * @return the name of the room. E.g. "Bedroom".
         */
        @JsonGetter(KEY_NAME)
        public String getName() {
            return name;
        }

        /**
         * Returns the Where unique identifier.
         *
         * @return the Where unique identifier.
         */
        @JsonGetter(KEY_WHERE_ID)
        public String getWhereId() {
            return mWhereId;
        }

        public static final Creator<Where> CREATOR = new Creator<Where>() {
            @Override
            public Where createFromParcel(Parcel in) {
                return new Where(in);
            }

            @Override
            public Where[] newArray(int size) {
                return new Where[size];
            }
        };

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof Where) {
                Where where = (Where) obj;
                return where.toString().equals(this.toString());
            } else {
                return false;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(mWhereId);
            parcel.writeString(name);
        }

    }

    /**
     * ETA is used to let Nest know that a user is expected to return home at a specific time.
     */
    public static class ETA implements Parcelable {
        public static final String KEY_TRIP_ID = "trip_id";
        public static final String KEY_EST_ARRIVAL_WINDOW_BEGIN = "estimated_arrival_window_begin";
        public static final String KEY_EST_ARRIVAL_WINDOW_END = "estimated_arrival_window_end";

        @JsonProperty(KEY_TRIP_ID)
        private String mTripId;

        @JsonProperty(KEY_EST_ARRIVAL_WINDOW_BEGIN)
        private String mEstimatedArrivalWindowBegin;

        @JsonProperty(KEY_EST_ARRIVAL_WINDOW_END)
        private String mEstimatedArrivalWindowEnd;

        public ETA() {}

        public ETA(String tridId, String estArrivalWindowBegin, String estArrivalWindowEnd) {
            mTripId = tridId;
            mEstimatedArrivalWindowBegin = estArrivalWindowBegin;
            mEstimatedArrivalWindowEnd = estArrivalWindowEnd;
        }

        public ETA(Parcel in) {
            mTripId = in.readString();
            mEstimatedArrivalWindowBegin = in.readString();
            mEstimatedArrivalWindowEnd = in.readString();
        }

        /**
         * Returns a unique identifier for this ETA instance.
         *
         * @return a unique identifier for this ETA instance.
         */
        @JsonGetter(KEY_TRIP_ID)
        public String getTripId() {
            return mTripId;
        }

        /**
         * Returns the beginning time of the estimated arrival window, in ISO 8601 format.
         *
         * @return the beginning time of the estimated arrival window, in ISO 8601 format.
         */
        @JsonGetter(KEY_EST_ARRIVAL_WINDOW_BEGIN)
        public String getEstimatedArrivalWindowBegin() {
            return mEstimatedArrivalWindowBegin;
        }

        /**
         * Returns the end time of the estimated arrival window, in ISO 8601 format.
         *
         * @return the end time of the estimated arrival window, in ISO 8601 format.
         */
        @JsonGetter(KEY_EST_ARRIVAL_WINDOW_END)
        public String getEstimatedArrivalWindowEnd() {
            return mEstimatedArrivalWindowEnd;
        }

        public static final Creator<ETA> CREATOR = new Creator<ETA>() {
            @Override
            public ETA createFromParcel(Parcel in) {
                return new ETA(in);
            }

            @Override
            public ETA[] newArray(int size) {
                return new ETA[size];
            }
        };

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof ETA) {
                ETA eta = (ETA) obj;
                return eta.toString().equals(this.toString());
            } else {
                return false;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(mTripId);
            parcel.writeString(mEstimatedArrivalWindowBegin);
            parcel.writeString(mEstimatedArrivalWindowEnd);
        }
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Structure) {
            Structure structure = (Structure) o;
            return structure.toString().equals(this.toString());
        } else {
            return false;
        }
    }
}

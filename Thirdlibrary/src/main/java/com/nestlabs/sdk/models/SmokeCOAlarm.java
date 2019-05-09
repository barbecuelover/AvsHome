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
 * SmokeCOAlarm represents and contains all properties of a Nest smoke+CO alarm device.
 */
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SmokeCOAlarm extends Device implements Parcelable {

    public static final String KEY_BATTERY_HEALTH = "battery_health";
    public static final String KEY_CO_ALARM_STATE = "co_alarm_state";
    public static final String KEY_SMOKE_ALARM_STATE = "smoke_alarm_state";
    public static final String KEY_IS_MANUAL_TEST_ACTIVE = "is_manual_test_active";
    public static final String KEY_LAST_MANUAL_TEST_TIME = "last_manual_test_time";
    public static final String KEY_UI_COLOR_STATE = "ui_color_state";

    @JsonProperty(KEY_BATTERY_HEALTH)
    private String mBatteryHealth;

    @JsonProperty(KEY_CO_ALARM_STATE)
    private String mCoAlarmState;

    @JsonProperty(KEY_SMOKE_ALARM_STATE)
    private String mSmokeAlarmState;

    @JsonProperty(KEY_IS_MANUAL_TEST_ACTIVE)
    private boolean mIsManualTestActive;

    @JsonProperty(KEY_LAST_MANUAL_TEST_TIME)
    private String mLastManualTestTime;

    @JsonProperty(KEY_UI_COLOR_STATE)
    private String mUiColorState;

    public SmokeCOAlarm() {}

    protected SmokeCOAlarm(Parcel in) {
        super(in);
        mBatteryHealth = in.readString();
        mCoAlarmState = in.readString();
        mSmokeAlarmState = in.readString();
        mIsManualTestActive = Utils.readBoolean(in);
        mLastManualTestTime = in.readString();
        mUiColorState = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mBatteryHealth);
        dest.writeString(mCoAlarmState);
        dest.writeString(mSmokeAlarmState);
        Utils.writeBoolean(dest, mIsManualTestActive);
        dest.writeString(mLastManualTestTime);
        dest.writeString(mUiColorState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SmokeCOAlarm> CREATOR = new Creator<SmokeCOAlarm>() {
        @Override
        public SmokeCOAlarm createFromParcel(Parcel in) {
            return new SmokeCOAlarm(in);
        }

        @Override
        public SmokeCOAlarm[] newArray(int size) {
            return new SmokeCOAlarm[size];
        }
    };

    /**
     * Returns the battery life/health, an estimate of remaining battery power level.
     * <p/>
     * Values: "ok", "replace"
     *
     * @return the battery life/health, an estimate of remaining battery power level.
     */
    @JsonGetter(KEY_BATTERY_HEALTH)
    public String getBatteryHealth() {
        return mBatteryHealth;
    }

    /**
     * Returns the CO alarm status.
     * <p/>
     * Values: "ok", "warning", "emergency"
     *
     * @return the CO alarm status.
     */
    @JsonGetter(KEY_CO_ALARM_STATE)
    public String getCOAlarmState() {
        return mCoAlarmState;
    }

    /**
     * Returns the smoke alarm status.
     * <p/>
     * Values: "ok", "warning", "emergency"
     *
     * @return the smoke alarm status.
     */
    @JsonGetter(KEY_SMOKE_ALARM_STATE)
    public String getSmokeAlarmState() {
        return mSmokeAlarmState;
    }

    /**
     * Returns the device status by color in the Nest app UI. It is an aggregate condition for
     * battery+smoke+co states, and reflects the actual color indicators displayed in the Nest app.
     * <p/>
     * Values: "gray", "green", "yellow", "red"
     *
     * @return the device status by color in the Nest app UI.
     */
    @JsonGetter(KEY_UI_COLOR_STATE)
    public String getUIColorState() {
        return mUiColorState;
    }

    /**
     * Returns the state of the manual smoke and CO alarm test.
     *
     * @return the state of the manual smoke and CO alarm test.
     */
    @JsonGetter(KEY_IS_MANUAL_TEST_ACTIVE)
    public boolean getIsManualTestActive() {
        return mIsManualTestActive;
    }

    /**
     * Returns the timestamp of the last successful manual smoke+CO alarm test, in ISO 8601 format.
     *
     * @return the timestamp of the last successful manual smoke+CO alarm test, in ISO 8601 format.
     */
    @JsonGetter(KEY_LAST_MANUAL_TEST_TIME)
    public String getLastManualTestTime() {
        return mLastManualTestTime;
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof SmokeCOAlarm) {
            SmokeCOAlarm smokeCOAlarm = (SmokeCOAlarm) obj;
            return smokeCOAlarm.toString().equals(this.toString());
        } else {
            return false;
        }
    }
}

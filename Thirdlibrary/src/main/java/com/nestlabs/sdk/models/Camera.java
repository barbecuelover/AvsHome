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
import java.util.List;

/**
 * Camera represents a single Nest Camera device. It contains all information associated with a
 * Camera device.
 */
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Camera extends Device implements Parcelable {
    public static final String KEY_IS_STREAMING = "is_streaming";
    public static final String KEY_IS_AUDIO_INPUT_ENABLED = "is_audio_input_enabled";
    public static final String KEY_LAST_IS_ONLINE_CHANGE = "last_is_online_change";
    public static final String KEY_IS_VIDEO_HISTORY_ENABLED = "is_video_history_enabled";
    public static final String KEY_WEB_URL = "web_url";
    public static final String KEY_APP_URL = "app_url";
    public static final String KEY_LAST_EVENT = "last_event";
    public static final String KEY_IS_PUBLIC_SHARE_ENABLED = "is_public_share_enabled";
    public static final String KEY_ACTIVITY_ZONES = "activity_zones";
    public static final String KEY_PUBLIC_SHARE_URL = "public_share_url";
    public static final String KEY_SNAPSHOT_URL = "snapshot_url";
    private static final String TAG = "Camera";

    @JsonProperty(KEY_IS_STREAMING)
    private boolean mIsStreaming;

    @JsonProperty(KEY_IS_AUDIO_INPUT_ENABLED)
    private boolean mIsAudioInputEnabled;

    @JsonProperty(KEY_LAST_IS_ONLINE_CHANGE)
    private String mLastIsOnlineChange;

    @JsonProperty(KEY_IS_VIDEO_HISTORY_ENABLED)
    private boolean mIsVideoHistoryEnabled;

    @JsonProperty(KEY_WEB_URL)
    private String mWebUrl;

    @JsonProperty(KEY_APP_URL)
    private String mAppUrl;

    @JsonProperty(KEY_LAST_EVENT)
    private LastEvent mLastEvent;

    @JsonProperty(KEY_IS_PUBLIC_SHARE_ENABLED)
    private boolean mIsPublicShareEnabled;

    @JsonProperty(KEY_ACTIVITY_ZONES)
    private List<ActivityZone> mActivityZones;

    @JsonProperty(KEY_PUBLIC_SHARE_URL)
    private String mPublicShareUrl;

    @JsonProperty(KEY_SNAPSHOT_URL)
    private String mSnapshotUrl;

    public Camera() {
        mActivityZones = new ArrayList<>();
    }

    protected Camera(Parcel in) {
        super(in);
        mActivityZones = new ArrayList<>();
        mIsStreaming = Utils.readBoolean(in);
        mIsAudioInputEnabled = Utils.readBoolean(in);
        mLastIsOnlineChange = in.readString();
        mIsVideoHistoryEnabled = Utils.readBoolean(in);
        mWebUrl = in.readString();
        mAppUrl = in.readString();
        mIsPublicShareEnabled = Utils.readBoolean(in);
        in.readTypedList(mActivityZones, ActivityZone.CREATOR);
        mPublicShareUrl = in.readString();
        mSnapshotUrl = in.readString();
        mLastEvent = LastEvent.CREATOR.createFromParcel(in);
    }

    public static final Creator<Camera> CREATOR = new Creator<Camera>() {
        @Override
        public Camera createFromParcel(Parcel in) {
            return new Camera(in);
        }

        @Override
        public Camera[] newArray(int size) {
            return new Camera[size];
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
        } else if (obj instanceof Camera) {
            Camera otherCamera = (Camera) obj;
            return otherCamera.toString().equals(this.toString());
        } else {
            return false;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        Utils.writeBoolean(dest, mIsStreaming);
        Utils.writeBoolean(dest, mIsAudioInputEnabled);
        dest.writeString(mLastIsOnlineChange);
        Utils.writeBoolean(dest, mIsVideoHistoryEnabled);
        dest.writeString(mWebUrl);
        dest.writeString(mAppUrl);
        Utils.writeBoolean(dest, mIsPublicShareEnabled);
        dest.writeTypedList(mActivityZones);
        dest.writeString(mPublicShareUrl);
        dest.writeString(mSnapshotUrl);
        mLastEvent.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Returns whether the camera is currently streaming.
     *
     * @return true if the camera is streaming, false otherwise.
     */
    @JsonGetter(KEY_IS_STREAMING)
    public boolean isStreaming() {
        return mIsStreaming;
    }

    /**
     * Returns whether audio input is enabled on the camera.
     *
     * @return true if the camera has audio input enabled, false otherwise.
     */
    @JsonGetter(KEY_IS_AUDIO_INPUT_ENABLED)
    public boolean isAudioInputEnabled() {
        return mIsAudioInputEnabled;
    }

    /**
     * Returns a timestamp of the last time that the camera changed its isOnline state.
     *
     * @return A timestamp of the last time that the camera changed its isOnline state.
     */
    @JsonGetter(KEY_LAST_IS_ONLINE_CHANGE)
    public String getLastIsOnlineChange() {
        return mLastIsOnlineChange;
    }

    /**
     * Returns whether video history is enabled on the camera.
     *
     * @return true if the camera has video history enabled, false otherwise.
     */
    @JsonGetter(KEY_IS_VIDEO_HISTORY_ENABLED)
    public boolean isVideoHistoryEnabled() {
        return mIsVideoHistoryEnabled;
    }

    /**
     * Returns a web URL (deep link) to the live camera feed at home.nest.com.
     *
     * @return A web URL (deep link) to the live camera feed at home.nest.com.
     */
    @JsonGetter(KEY_WEB_URL)
    public String getWebUrl() {
        return mWebUrl;
    }

    /**
     * Returns an app URL (deep link) to the live camera feed in the Nest app.
     *
     * @return An app URL (deep link) to the live camera feed in the Nest app.
     */
    @JsonGetter(KEY_APP_URL)
    public String getAppUrl() {
        return mAppUrl;
    }

    /**
     * Returns whether public sharing is enabled on the camera.
     *
     * @return True if public sharing is enabled on the camera.
     */
    @JsonGetter(KEY_IS_PUBLIC_SHARE_ENABLED)
    public boolean getIsPublicShareEnabled() {
        return mIsPublicShareEnabled;
    }

    /**
     * Returns a list of HashMaps that contain an activity zone.
     *
     * @return ArrayList of HashMaps that contain an activity zone.
     */
    @JsonGetter(KEY_ACTIVITY_ZONES)
    public List<ActivityZone> getActivityZones() {
        return mActivityZones;
    }

    /**
     * Returns a URL of the shared public stream.
     *
     * @return A string URL of the shared public stream.
     */
    @JsonGetter(KEY_PUBLIC_SHARE_URL)
    public String getPublicShareUrl() {
        return mPublicShareUrl;
    }

    /**
     * Returns a URL to this camera's snapshots.
     *
     * @return A string URL to this camera's snapshots.
     */
    @JsonGetter(KEY_SNAPSHOT_URL)
    public String getSnapshotUrl() {
        return mSnapshotUrl;
    }

    /**
     * Returns a LastEvent object containing information about the last event.
     *
     * @return A LastEvent object representing the data captured in the last event.
     */
    @JsonGetter(KEY_LAST_EVENT)
    public LastEvent getLastEvent() {
        return mLastEvent;
    }

    /**
     * LastEvent contains information about the last event that triggered a notification. In order
     * to capture last event data, the Nest Cam must have a Nest Aware with Video History
     * subscription.
     */
    public static class LastEvent implements Parcelable {
        public static final String KEY_HAS_SOUND = "has_sound";
        public static final String KEY_HAS_MOTION = "has_motion";
        public static final String KEY_HAS_PERSON = "has_person";
        public static final String KEY_START_TIME = "start_time";
        public static final String KEY_END_TIME = "end_time";
        public static final String KEY_URLS_EXPIRE_TIME = "urls_expire_time";
        public static final String KEY_WEB_URL = "web_url";
        public static final String KEY_APP_URL = "app_url";
        public static final String KEY_IMAGE_URL = "image_url";
        public static final String KEY_ANIMATED_IMAGE_URL = "animated_image_url";
        public static final String KEY_ACTIVITY_ZONE_IDS = "activity_zone_ids";

        @JsonProperty(KEY_HAS_SOUND)
        private boolean mHasSound;

        @JsonProperty(KEY_HAS_MOTION)
        private boolean mHasMotion;

        @JsonProperty(KEY_HAS_PERSON)
        private boolean mHasPerson;

        @JsonProperty(KEY_START_TIME)
        private String mStartTime;

        @JsonProperty(KEY_END_TIME)
        private String mEndTime;

        @JsonProperty(KEY_URLS_EXPIRE_TIME)
        private String mUrlsExpireTime;

        @JsonProperty(KEY_WEB_URL)
        private String mWebUrl;

        @JsonProperty(KEY_APP_URL)
        private String mAppUrl;

        @JsonProperty(KEY_IMAGE_URL)
        private String mImageUrl;

        @JsonProperty(KEY_ANIMATED_IMAGE_URL)
        private String mAnimatedImageUrl;

        @JsonProperty(KEY_ACTIVITY_ZONE_IDS)
        private List<String> mActivityZoneIds;

        public LastEvent() {
            mActivityZoneIds = new ArrayList<>();
        }

        public LastEvent(Parcel in) {
            this();
            mHasSound = Utils.readBoolean(in);
            mHasMotion = Utils.readBoolean(in);
            mHasPerson = Utils.readBoolean(in);
            mStartTime = in.readString();
            mEndTime = in.readString();
            mUrlsExpireTime = in.readString();
            mWebUrl = in.readString();
            mAppUrl = in.readString();
            mImageUrl = in.readString();
            mAnimatedImageUrl = in.readString();
            in.readStringList(mActivityZoneIds);
        }

        public static final Creator<LastEvent> CREATOR = new Creator<LastEvent>() {
            @Override
            public LastEvent createFromParcel(Parcel in) {
                return new LastEvent(in);
            }

            @Override
            public LastEvent[] newArray(int size) {
                return new LastEvent[size];
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
            } else if (obj instanceof LastEvent) {
                LastEvent event = (LastEvent) obj;
                return event.toString().equals(this.toString());
            } else {
                return false;
            }
        }

        /**
         * Returns whether sound was detected in the last event.
         *
         * @return true if sound was detected, false if sound was not detected.
         */
        @JsonGetter(KEY_HAS_SOUND)
        public boolean getHasSound() {
            return mHasSound;
        }

        /**
         * Returns whether motion was detected in the last event.
         *
         * @return true if motion was detected, false if motion was not detected.
         */
        @JsonGetter(KEY_HAS_MOTION)
        public boolean getHasMotion() {
            return mHasMotion;
        }

        /**
         * Returns whether person was detected in the last event.
         *
         * @return true if person was detected, false if motion was not detected.
         */
        @JsonGetter(KEY_HAS_PERSON)
        public boolean getHasPerson() {
            return mHasPerson;
        }

        /**
         * Returns the time of the start of the event.
         *
         * @return Event start time, in ISO 8601 format.
         */
        @JsonGetter(KEY_START_TIME)
        public String getStartTime() {
            return mStartTime;
        }

        /**
         * Returns the time of the end of the event.
         *
         * @return Event end time, in ISO 8601 format.
         */
        @JsonGetter(KEY_END_TIME)
        public String getEndTime() {
            return mEndTime;
        }

        /**
         * Returns the time that the URLs expire, in ISO 8601 format.
         *
         * @return the time that the URLs expire, in ISO 8601 format.
         */
        @JsonGetter(KEY_URLS_EXPIRE_TIME)
        public String getUrlsExpireTime() {
            return mUrlsExpireTime;
        }

        /**
         * Returns a web URL (deep link) to the last sound or motion event at home.nest.com. Used to
         * display the last recorded event, and requires user to be signed in to the account.
         *
         * @return A web URL (deep link) to the last sound or motion event.
         */
        @JsonGetter(KEY_WEB_URL)
        public String getWebUrl() {
            return mWebUrl;
        }

        /**
         * Returns a Nest app URL (deep link) to the last sound or motion event. Used to display the
         * last recorded event, and requires user to be signed in to the account.
         *
         * @return An app URL (deep link) to the last sound or motion event.
         */
        @JsonGetter(KEY_APP_URL)
        public String getAppUrl() {
            return mAppUrl;
        }

        /**
         * Returns a URL (link) to the image file captured for a sound or motion event.
         *
         * @return A URL (link) to the image file captured for a sound or motion event.
         */
        @JsonGetter(KEY_IMAGE_URL)
        public String getImageUrl() {
            return mImageUrl;
        }

        /**
         * Returns a URL (link) to the gif file captured for a sound or motion event.
         *
         * @return A URL (link) to the gif file captured for a sound or motion event.
         */
        @JsonGetter(KEY_ANIMATED_IMAGE_URL)
        public String getAnimatedImageUrl() {
            return mAnimatedImageUrl;
        }

        /**
         * Returns a list of zone ids that detected motion during the last event.
         *
         * @return An ArrayList of zone ids that detected motion during the last event.
         */
        @JsonGetter(KEY_ACTIVITY_ZONE_IDS)
        public List<String> getActivityZoneIds() {
            return mActivityZoneIds;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            Utils.writeBoolean(parcel, mHasSound);
            Utils.writeBoolean(parcel, mHasMotion);
            Utils.writeBoolean(parcel, mHasPerson);
            parcel.writeString(mStartTime);
            parcel.writeString(mEndTime);
            parcel.writeString(mUrlsExpireTime);
            parcel.writeString(mWebUrl);
            parcel.writeString(mAppUrl);
            parcel.writeString(mImageUrl);
            parcel.writeString(mAnimatedImageUrl);
            parcel.writeStringList(mActivityZoneIds);
        }
    }

    public static class ActivityZone implements Parcelable {
        public static final String KEY_NAME = "name";
        public static final String KEY_ID = "id";

        @JsonProperty(KEY_NAME)
        private String mName;

        @JsonProperty(KEY_ID)
        private String mId;

        protected ActivityZone() {
            mName = "";
            mId = "";
        }

        protected ActivityZone(Parcel in) {
            mName = in.readString();
            mId = in.readString();
        }

        @JsonGetter(KEY_NAME)
        public String getName() {
            return mName;
        }

        @JsonGetter(KEY_ID)
        public String getId() {
            return mId;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mName);
            dest.writeString(mId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ActivityZone> CREATOR = new Creator<ActivityZone>() {
            @Override
            public ActivityZone createFromParcel(Parcel in) {
                return new ActivityZone(in);
            }

            @Override
            public ActivityZone[] newArray(int size) {
                return new ActivityZone[size];
            }
        };
    }
}


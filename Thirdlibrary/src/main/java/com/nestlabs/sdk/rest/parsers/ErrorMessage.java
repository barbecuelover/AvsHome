package com.nestlabs.sdk.rest.parsers;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessage implements Parcelable {

    @JsonProperty("error")
    private String error;

    @JsonProperty("type")
    private String type;

    @JsonProperty("message")
    private String message;

    public String getError() {
        return error;
    }

    public ErrorMessage setError(String error) {
        this.error = error;
        return this;
    }

    public String getType() {
        return type;
    }

    public ErrorMessage setType(String type) {
        this.type = type;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorMessage() { }

    protected ErrorMessage(Parcel in) {
        error = in.readString();
        type = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(error);
        dest.writeString(type);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ErrorMessage> CREATOR = new Creator<ErrorMessage>() {
        @Override
        public ErrorMessage createFromParcel(Parcel in) {
            return new ErrorMessage(in);
        }

        @Override
        public ErrorMessage[] newArray(int size) {
            return new ErrorMessage[size];
        }
    };
}

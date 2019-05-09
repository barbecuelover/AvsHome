package com.nestlabs.sdk.setters;

import android.support.annotation.NonNull;

import com.nestlabs.sdk.Callback;
import com.nestlabs.sdk.models.Camera;
import com.nestlabs.sdk.models.Utils;
import com.nestlabs.sdk.rest.RestClient;
import com.nestlabs.sdk.rest.parsers.Constants;

public class CameraSetter {
    private static String getPath(@NonNull String cameraId) {
        return new Utils.PathBuilder().append(Constants.KEY_DEVICES)
                .append(Constants.KEY_CAMERAS)
                .append(cameraId)
                .build();
    }

    private final RestClient restClient;
    public CameraSetter(@NonNull final RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Sets the {@link Camera} streaming status on or off.
     *
     * @param cameraId    The unique identifier of the camera.
     * @param isStreaming true to turn streaming on, false to turn streaming off.
     * @param callback    A {@link Callback} to receive whether the change was successful.
     */
    public void setIsStreaming(@NonNull String cameraId, boolean isStreaming, Callback callback) {
        restClient.writeBoolean(getPath(cameraId), Camera.KEY_IS_STREAMING, isStreaming, callback);
    }

    /**
     * Sets the {@link Camera} streaming status on or off.
     *
     * @param cameraId    The unique identifier of the camera.
     * @param isStreaming true to turn streaming on, false to turn streaming off.
     */
    public void setIsStreaming(@NonNull String cameraId, boolean isStreaming) {
        setIsStreaming(cameraId, isStreaming, null);
    }
}

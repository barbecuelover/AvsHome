package com.nestlabs.sdk.setters;

import android.support.annotation.NonNull;

import com.nestlabs.sdk.Callback;
import com.nestlabs.sdk.models.Structure;
import com.nestlabs.sdk.models.Utils;
import com.nestlabs.sdk.rest.RestClient;
import com.nestlabs.sdk.rest.parsers.Constants;

public class StructureSetter {

    private static String getPath(@NonNull String structureId) {
        return new Utils.PathBuilder()
                .append(Constants.KEY_STRUCTURES)
                .append(structureId).build();
    }

    private final RestClient restClient;
    public StructureSetter(@NonNull final RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Sets the state of the structure. In order for a structure to be in the Auto-Away state, all
     * devices must also be in Auto-Away state. When any device leaves the Auto-Away state, then the
     * structure also leaves the Auto-Away state.
     *
     * @param structureId The unique identifier for the {@link Structure}.
     * @param awayState   The state of the structure. Values can be "home", "away", or "auto-away".
     * @param callback    A {@link Callback} to receive whether the change was successful.
     */
    public void setAway(@NonNull String structureId, String awayState, Callback callback) {
        restClient.writeString(getPath(structureId), Structure.KEY_AWAY, awayState, callback);
    }

    /**
     * Sets the state of the structure. In order for a structure to be in the Auto-Away state, all
     * devices must also be in Auto-Away state. When any device leaves the Auto-Away state, then the
     * structure also leaves the Auto-Away state.
     *
     * @param structureId The unique identifier for the {@link Structure}.
     * @param awayState   The state of the structure. Values can be "home", "away", or "auto-away".
     */
    public void setAway(@NonNull String structureId, String awayState) {
        setAway(structureId, awayState, null);
    }

    /**
     * Sets the ETA on a structure. It is used to let Nest know that a user is expected to return
     * home at a specific time.
     *
     * @param structureId The unique identifier for the {@link Structure}.
     * @param eta         The {@link Structure.ETA} object containing the ETA values.
     * @param callback    A {@link Callback} to receive whether the change was successful.
     */
    public void setEta(@NonNull String structureId, Structure.ETA eta, Callback callback) {
        restClient.writeString(getPath(structureId), Structure.KEY_ETA, eta.toString(), callback);
    }

    /**
     * Sets the ETA on a structure. It is used to let Nest know that a user is expected to return
     * home at a specific time.
     *
     * @param structureId The unique identifier for the {@link Structure}.
     * @param eta         The {@link Structure.ETA} object containing the ETA values.
     */
    public void setEta(@NonNull String structureId, Structure.ETA eta) {
        setEta(structureId, eta, null);
    }
}

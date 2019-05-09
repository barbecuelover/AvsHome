package com.nestlabs.sdk.setters;

import android.support.annotation.NonNull;

import com.nestlabs.sdk.Callback;
import com.nestlabs.sdk.models.Thermostat;
import com.nestlabs.sdk.models.Utils;
import com.nestlabs.sdk.rest.RestClient;
import com.nestlabs.sdk.rest.parsers.Constants;

public class ThermostatSetter {

    private static String getPath(@NonNull String thermostatId) {
        return new Utils.PathBuilder()
                .append(Constants.KEY_DEVICES)
                .append(Constants.KEY_THERMOSTATS)
                .append(thermostatId)
                .build();
    }

    private final RestClient restClient;
    public ThermostatSetter(@NonNull final RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Sets the desired temperature, in full degrees Fahrenheit (1&deg;F). Used when hvac_mode =
     * "heat" or "cool".
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The desired temperature in full degrees Fahrenheit.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureF(@NonNull String thermostatId, long temperature, Callback callback) {
        restClient.writeLong(getPath(thermostatId), Thermostat.KEY_TARGET_TEMP_F, temperature, callback);
    }

    /**
     * Sets the desired temperature, in full degrees Fahrenheit (1&deg;F). Used when hvac_mode =
     * "heat" or "cool".
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The desired temperature in full degrees Fahrenheit.
     */
    public void setTargetTemperatureF(@NonNull String thermostatId, long temperature) {
        setTargetTemperatureF(thermostatId, temperature, null);
    }

    /**
     * Sets the desired temperature, in half degrees Celsius (0.5&deg;C). Used when hvac_mode =
     * "heat" or "cool".
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The desired temperature, in half degrees Celsius (0.5&deg;C).
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureC(@NonNull String thermostatId, double temperature, Callback callback) {
        restClient.writeDouble(getPath(thermostatId), Thermostat.KEY_TARGET_TEMP_C, temperature, callback);
    }

    /**
     * Sets the desired temperature, in half degrees Celsius (0.5&deg;C). Used when hvac_mode =
     * "heat" or "cool".
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The desired temperature, in half degrees Celsius (0.5&deg;C).
     */
    public void setTargetTemperatureC(@NonNull String thermostatId, double temperature) {
        setTargetTemperatureC(thermostatId, temperature, null);
    }

    /**
     * Sets the minimum target temperature, displayed in whole degrees Fahrenheit (1&deg;F). Used
     * when hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The minimum desired temperature, displayed in whole degrees Fahrenheit.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureLowF(@NonNull String thermostatId, long temperature, Callback callback) {
        restClient.writeLong(getPath(thermostatId), Thermostat.KEY_TARGET_TEMP_LOW_F, temperature, callback);
    }

    /**
     * Sets the minimum target temperature, displayed in whole degrees Fahrenheit (1&deg;F). Used
     * when hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The minimum desired temperature, displayed in whole degrees Fahrenheit.
     */
    public void setTargetTemperatureLowF(@NonNull String thermostatId, long temperature) {
       setTargetTemperatureLowF(thermostatId, temperature, null);
    }

    /**
     * Sets the minimum target temperature, displayed in half degrees Celsius (0.5&deg;C). Used when
     * hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The minimum target temperature, displayed in half degrees Celsius.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureLowC(@NonNull String thermostatId, double temperature, Callback callback) {
        restClient.writeDouble(getPath(thermostatId), Thermostat.KEY_TARGET_TEMP_C, temperature, callback);
    }

    /**
     * Sets the minimum target temperature, displayed in half degrees Celsius (0.5&deg;C). Used when
     * hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The minimum target temperature, displayed in half degrees Celsius.
     */
    public void setTargetTemperatureLowC(@NonNull String thermostatId, double temperature) {
        setTargetTemperatureLowC(thermostatId, temperature, null);
    }

    /**
     * Sets the maximum target temperature, displayed in whole degrees Fahrenheit (1&deg;F). Used
     * when hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The maximum desired temperature, displayed in whole degrees Fahrenheit.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureHighF(@NonNull String thermostatId, long temperature, Callback callback) {
        restClient.writeLong(getPath(thermostatId), Thermostat.KEY_TARGET_TEMP_HIGH_F, temperature, callback);
    }

    /**
     * Sets the maximum target temperature, displayed in whole degrees Fahrenheit (1&deg;F). Used
     * when hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The maximum desired temperature, displayed in whole degrees Fahrenheit.
     */
    public void setTargetTemperatureHighF(@NonNull String thermostatId, long temperature) {
        setTargetTemperatureHighF(thermostatId, temperature, null);
    }

    /**
     * Sets the maximum target temperature, displayed in half degrees Celsius (0.5&deg;C). Used when
     * hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The maximum target temperature, displayed in half degrees Celsius.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureHighC(@NonNull String thermostatId, double temperature, Callback callback) {
        restClient.writeDouble(getPath(thermostatId), Thermostat.KEY_TARGET_TEMP_HIGH_C, temperature, callback);
    }

    /**
     * Sets the maximum target temperature, displayed in half degrees Celsius (0.5&deg;C). Used when
     * hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The maximum target temperature, displayed in half degrees Celsius.
     */
    public void setTargetTemperatureHighC(@NonNull String thermostatId, double temperature) {
        setTargetTemperatureHighC(thermostatId, temperature, null);
    }

    /**
     * Sets the HVAC system heating/cooling modes. For systems with both heating and cooling
     * capability, set this value to "heat-cool" (Heat / Cool mode) to get the best experience.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param mode         The heating/cooling mode. Values can be "heat", "cool", "heat-cool", or
     *                     "off".
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setHVACMode(@NonNull String thermostatId, String mode, Callback callback) {
        restClient.writeString(getPath(thermostatId), Thermostat.KEY_HVAC_MODE, mode, callback);
    }

    /**
     * Sets the HVAC system heating/cooling modes. For systems with both heating and cooling
     * capability, set this value to "heat-cool" (Heat / Cool mode) to get the best experience.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param mode         The heating/cooling mode. Values can be "heat", "cool", "heat-cool", or
     *                     "off".
     */
    public void setHVACMode(@NonNull String thermostatId, String mode) {
        setHVACMode(thermostatId, mode, null);
    }

    /**
     * Sets whether the fan timer is engaged; used with fanTimerTimeout to turn on the fan for a
     * (user-specified) preset duration.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param isActive     true if the fan timer is to be engaged, false if the fan timer should be
     *                     disengaged.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setFanTimerActive(@NonNull String thermostatId, boolean isActive, Callback callback) {
        restClient.writeBoolean(getPath(thermostatId), Thermostat.KEY_FAN_TIMER_ACTIVE, isActive, callback);
    }

    /**
     * Sets whether the fan timer is engaged; used with fanTimerTimeout to turn on the fan for a
     * (user-specified) preset duration.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param isActive     true if the fan timer is to be engaged, false if the fan timer should be
     *                     disengaged.
     */
    public void setFanTimerActive(@NonNull String thermostatId, boolean isActive) {
        setFanTimerActive(thermostatId, isActive, null);
    }

    /**
     * Sets the thermostat scale to Fahrenheit or Celsius; used with temperature display.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param tempScale    A string for temperature scale. "F" for Fahrenheit, "C" for Celsius.
     */
    public void setTemperatureScale(@NonNull String thermostatId, String tempScale, Callback callback) {
        restClient.writeString(getPath(thermostatId), Thermostat.KEY_TEMP_SCALE, tempScale, callback);
    }

    /**
     * Sets the thermostat label.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param label        A string for the custom label.
     */
    public void setLabel(@NonNull String thermostatId, String label, Callback callback) {
        restClient.writeString(getPath(thermostatId), Thermostat.KEY_LABEL, label, callback);
    }
}

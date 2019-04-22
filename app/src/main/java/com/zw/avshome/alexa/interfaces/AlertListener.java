package com.zw.avshome.alexa.interfaces;

import com.amazon.aace.alexa.Alerts;

public interface AlertListener {
    void setAlertCreateListener(String alertToken, String detailedInfo);

    void setAlertDeleteListener(String alertToken);

    void setAlertStateChangeListener(String alertToken, Alerts.AlertState state, String reason);
}

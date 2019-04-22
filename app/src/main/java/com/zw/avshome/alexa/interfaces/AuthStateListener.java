package com.zw.avshome.alexa.interfaces;

import com.amazon.aace.alexa.AuthProvider;

public interface AuthStateListener {
    void alexaAuthState(AuthProvider.AuthState authState, AuthProvider.AuthError authError);
}

package com.nestlabs.sdk;

import android.app.Activity;
import android.content.Intent;


import com.nestlabs.sdk.models.NestToken;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Oauth2FlowHandler {

    private static final String KEY_ACCESS_TOKEN = "access_token_key";
    private static final String REVOKE_TOKEN_PATH = "oauth2/access_tokens/";
    private static final String KEY_CLIENT_METADATA = "client_metadata_key";

    private NestConfig oauth2Config;
    private final OkHttpClient httpClient;

    Oauth2FlowHandler(final OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Sets the Nest configuration values used for authentication.
     *
     * @param clientId     The Nest client ID.
     * @param clientSecret The Nest client secret.
     * @param redirectUrl  The Nest redirect URL.
     */
    public void setConfig(String clientId, String clientSecret, String redirectUrl) {
        oauth2Config = new NestConfig.Builder().clientID(clientId)
                .clientSecret(clientSecret)
                .redirectURL(redirectUrl)
                .build();
    }

    /**
     * Returns a {@link NestConfig} object containing the currently set credentials. If there are no
     * credentials set, returns null.
     *
     * @return a {@link NestConfig} object containing current config values, or null if unset.
     */
    public NestConfig getConfig() {
        return oauth2Config;
    }

    /**
     * Clears the currently stored credentials.
     */
    public void clearConfig() {
        oauth2Config = null;
    }

    /**
     * Start an {@link Activity} which will guide a user through the authentication process.
     *
     * @param activity    the {@link Activity} return the result. Typically the current {@link
     *                    Activity}.
     * @param requestCode the request code for which a result will be returned.
     */
    public void launchAuthFlow(Activity activity, int requestCode) {
        final Intent authFlowIntent = new Intent(activity, NestAuthActivity.class);
        authFlowIntent.putExtra(KEY_CLIENT_METADATA, oauth2Config);
        activity.startActivityForResult(authFlowIntent, requestCode);
    }
    public void launchAuthFlow(Activity activity, Fragment fragment, int requestCode){
        final Intent authFlowIntent = new Intent(activity, NestAuthActivity.class);
        authFlowIntent.putExtra(KEY_CLIENT_METADATA, oauth2Config);
        if (fragment!=null){
            fragment.startActivityForResult(authFlowIntent, requestCode);
        }else {
            activity.startActivityForResult(authFlowIntent, requestCode);
        }
    }
    /**
     * Returns a {@link NestToken} embedded in the {@link Intent} that is returned in the result
     * from {@link #launchAuthFlow(Activity, int)}.
     *
     * @param intent the intent to retrieve the NestToken from.
     * @return the {@link NestToken}, if it was contained in the {@link Intent}, otherwise null.
     */
    public NestToken getAccessTokenFromIntent(Intent intent) {
        return intent.getParcelableExtra(KEY_ACCESS_TOKEN);
    }

    /**
     * Revokes a {@link NestToken} from the Nest API.
     *
     * @param token    The token to revoke.
     * @param callback A callback for the result of the revocation.
     */
    public void revokeToken(NestToken token, @NonNull final Callback callback) {

        Request request = new Request.Builder().url(
                WwnApiUrls.AUTHORIZATION_SERVER_URL + REVOKE_TOKEN_PATH + token.getToken()).delete().build();

        httpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(new NestException("Request to revoke token failed.", e));
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    callback.onFailure(
                            new ServerException("Token revocation failed: " + response.toString()));
                } else callback.onSuccess();
            }
        });
    }
}

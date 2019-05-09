package com.nestlabs.sdk;

import android.support.annotation.NonNull;

import com.nestlabs.sdk.models.NestToken;
import com.nestlabs.sdk.setters.CameraSetter;
import com.nestlabs.sdk.rest.parsers.MessageParser;
import com.nestlabs.sdk.rest.parsers.Notifier;
import com.nestlabs.sdk.rest.parsers.ObjectModelMapper;
import com.nestlabs.sdk.rest.parsers.Parser;
import com.nestlabs.sdk.rest.RestConfig;
import com.nestlabs.sdk.rest.RestStreamClient;
import com.nestlabs.sdk.setters.StructureSetter;
import com.nestlabs.sdk.setters.ThermostatSetter;
import com.nestlabs.sdk.rest.StreamingClient;
import com.nestlabs.sdk.rest.RestClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class WwnClient {

    private final RestClient restClient;
    private final StreamingClient streamingClient;
    private final Notifier notifier = new Notifier();

    public final CameraSetter cameras;
    public final ThermostatSetter thermostats;
    public final StructureSetter structures;
    public final Oauth2FlowHandler oauth2;

    public WwnClient() {
        this(new RestConfig(), new ExceptionHandler() {
            @Override
            public void handle(NestException value) { }
        });
    }

    public WwnClient(ExceptionHandler exceptionHandler) {
        this(new RestConfig(), exceptionHandler);
    }

    /**
     * Creates a new instance of the {@link WwnClient}.
     */
    public WwnClient(RestConfig restConfig, ExceptionHandler exceptionHandler) {
        Parser messageParser = new MessageParser(new ObjectModelMapper(notifier));

        OkHttpClient httpClient = new OkHttpClient();
        OkHttpClient streamingHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        oauth2 = new Oauth2FlowHandler(httpClient);
        restClient = new RestClient(httpClient, restConfig, messageParser);
        streamingClient = new RestStreamClient.Builder(streamingHttpClient, restConfig, messageParser)
                .setExceptionHandler(exceptionHandler)
                .build();

        cameras = new CameraSetter(restClient);
        structures = new StructureSetter(restClient);
        thermostats = new ThermostatSetter(restClient);
    }

    /**
     * Requests authentication with a {@link NestToken}.
     *
     * @param token    the NestToken to authenticate with
     */
    public void startWithToken(@NonNull NestToken token) {
        startWithToken(token.getToken());
    }

    /**
     * Requests authentication with a raw token.
     *
     * @param token        the token String to authenticate with
     */
    public void startWithToken(@NonNull String token) {
        restClient.setToken(token);
        streamingClient.start(token);
    }

    public void stop() {
        notifier.removeAllListeners();
        streamingClient.stop();
    }

    public void addListener(final NestListener listener) {
        notifier.addListener(listener);
    }

    public void removeListener(final NestListener listener) {
        notifier.removeListener(listener);
    }

    public void removeAllListeners() {
        notifier.removeAllListeners();
    }

}

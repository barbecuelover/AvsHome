package com.nestlabs.sdk.rest;

import android.support.annotation.NonNull;

import com.nestlabs.sdk.Callback;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.ServerException;
import com.nestlabs.sdk.rest.parsers.Parser;
import com.nestlabs.sdk.rest.parsers.ParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {

    private final String baseApiUrl;
    private String redirectApiUrl = null;
    private String token = null;
    private final Parser parser;
    private final OkHttpClient httpClient;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Callback callbackStub = new Callback() {
        @Override
        public void onSuccess() { }
        @Override
        public void onFailure(NestException exception) { }
    };

    public RestClient(@NonNull OkHttpClient httpClient, @NonNull final RestConfig restConfig, @NonNull final Parser parser) {
        this.httpClient = httpClient;
        this.parser = parser;
        baseApiUrl = restConfig.getUrl();
    }

    public void setToken(String token) {
        if (token == null || token.length() == 0) {
            throw new MissingTokenException();
        }
        this.token = token;
    }

    public void writeLong(String path, String field, long value, Callback callback) {
        write(path, field, String.format(Locale.US, "%d", value), callback);
    }

    public void writeDouble(String path, String field, double value, Callback callback) {
        write(path, field, String.format(Locale.US, "%f", value), callback);
    }

    public void writeString(String path, String field, String value, Callback callback) {
        write(path, field, "\"" + value + "\"", callback);
    }

    public void writeBoolean(String path, String field, Boolean value, Callback callback) {
        write(path, field, value.toString(), callback);
    }

    private void write(final String path, final String field, final String value, final Callback callback) {
        if (token == null || token.length() == 0) {
            throw new MissingTokenException();
        }

        final Callback internalCallback = callback == null ? callbackStub : callback;

        String apiUrl = redirectApiUrl == null ? baseApiUrl : redirectApiUrl;
        String body = "{\"" + field + "\": " + value + "}";
        Request request = new Request.Builder()
                .url(apiUrl + path)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .put(RequestBody.create(JSON, body))
                .build();

        httpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Reset redirect url if WWN host goes offline
                if (e instanceof UnknownHostException) {
                    redirectApiUrl = null;
                    write(path, field, value, callback);
                    return;
                }
                internalCallback.onFailure(new NestException("Write request failed.", e));
            }

            private Boolean handleRedirect(Response response) {
                if (response.code() != 307) return false;

                String location = response.headers().get("Location");
                if (location == null) return false;

                try {
                    URL url = new URL(location);
                    redirectApiUrl = url.getProtocol() + "://" + url.getAuthority();
                } catch (MalformedURLException e) {
                    //Notify client about the failure and exit response handler
                    internalCallback.onFailure(new NestException(e));
                    return true;
                }

                write(path, field, value, callback);
                return true;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (handleRedirect(response)) return; //Redirect if 307

                if (response.code() < 200 || response.code() >= 500) {
                    internalCallback.onFailure(new ServerException("Unexpected server response. Error code: " + response.code()));
                } else {
                    try {
                        parser.parse(response.body().source().readUtf8());
                        internalCallback.onSuccess();
                    } catch (ParserException ex) {
                        internalCallback.onFailure(ex);
                    }
                }
                response.body().close();
            }
        });
    }
}

package com.nestlabs.sdk.rest;

import android.support.annotation.NonNull;

import com.nestlabs.sdk.EventHandler;
import com.nestlabs.sdk.ExceptionHandler;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.rest.parsers.Parser;
import com.nestlabs.sdk.rest.parsers.ParserException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class RestStreamClient implements StreamingClient {

    private static final Long DEFAULT_BYTE_COUNT = 2048L;

    private String token = null;
    private Boolean started = false;
    private final String apiUrl;
    private final Parser parser;
    private final OkHttpClient httpClient;
    private final RetryExecutor retryExecutor;
    private final ExceptionHandler exceptionHandler;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private RestStreamClient(Builder builder) {
        this.parser = builder.getParser();
        this.httpClient = builder.getHttpClient();
        this.exceptionHandler = builder.getExceptionHandler();
        this.apiUrl = builder.getRestConfig().getUrl();
        this.retryExecutor = new RetryExecutor(builder.getBackOff());
    }

    @Override
    public Boolean start(String accessToken) {
        if (started) return false;
        if (accessToken == null || accessToken.length() == 0) {
            throw new MissingTokenException();
        }

        token = accessToken;
        executorService.execute(new Reader());
        started = true;
        return true;
    }

    @Override
    public void stop() {
        started = false;
        retryExecutor.reset();
        retryExecutor.cancel();
        httpClient.dispatcher().cancelAll();
    }

    private class Reader implements Runnable {

        private String accumulator = "";
        private String segment(String buf) {
            if (buf.endsWith("\n") || buf.endsWith("}")) {
                String msg = accumulator + buf;
                accumulator = "";
                return msg;
            } else accumulator = accumulator + buf;

            return null;
        }

        @Override
        public void run() {
            Request request = new Request.Builder()
                    .url(apiUrl + "?auth=" + token)
                    .addHeader("Accept", "text/event-stream")
                    .build();

            Response response = null;
            try {
                response = httpClient.newCall(request).execute();

                Buffer buffer = new Buffer();
                while (!response.body().source().exhausted()) {
                    long count = response.body().source().read(buffer, DEFAULT_BYTE_COUNT);
                    if (count > 0) {
                        String msg = segment(buffer.readUtf8());
                        if (msg != null) {
                            try {
                                parser.parse(msg);
                            } catch (ParserException ex) {
                                exceptionHandler.handle(ex);
                            } catch (Exception ex) {
                                //notify client and ignore downstream exceptions
                                exceptionHandler.handle(new NestException(ex));
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                exceptionHandler.handle(new NestException(ex));
            } finally {

                if (response != null) response.body().close();

                if (started) {
                    retryExecutor.schedule(new EventHandler<String>() {
                        @Override
                        public void handle(String accessToken) {
                            RestStreamClient.this.start(accessToken);
                        }
                    }, RestStreamClient.this.token);
                }
            }
        }
    }

    public static class Builder {
        private final Parser parser;
        private final RestConfig restConfig;
        private final OkHttpClient httpClient;
        private BackOff backOff = new FibonacciBackOff.Builder().build();
        private ExceptionHandler exceptionHandler;

        Parser getParser() {
            return parser;
        }

        RestConfig getRestConfig() {
            return restConfig;
        }

        OkHttpClient getHttpClient() {
            return httpClient;
        }

        BackOff getBackOff() {
            return backOff;
        }

        public Builder setBackOff(@NonNull final BackOff backOff) {
            this.backOff = backOff;
            return this;
        }

        ExceptionHandler getExceptionHandler() {
            return exceptionHandler;
        }

        public Builder setExceptionHandler(@NonNull final ExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        public Builder(@NonNull final OkHttpClient httpClient,
                       @NonNull final RestConfig restConfig, @NonNull final Parser parser) {
            this.parser = parser;
            this.restConfig = restConfig;
            this.httpClient = httpClient;
        }

        public RestStreamClient build() {
            return new RestStreamClient(this);
        }
    }
}

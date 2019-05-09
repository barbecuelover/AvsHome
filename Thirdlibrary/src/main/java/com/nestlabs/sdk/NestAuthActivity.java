/*
 * Copyright 2015, Google Inc.
 * Copyright 2014, Nest Labs Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nestlabs.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ecs.thirdlibrary.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nestlabs.sdk.models.NestToken;
import com.nestlabs.sdk.models.Utils;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * NestAuthActivity is an Activity used by WwnClient to complete the OAuth 2.0 flow for authorization
 * of a user with the Nest API. When created, it launches a WebView that will open the Nest auth
 * page and get authorization from the user for the selected permissions of the Wwn API.
 */
public class NestAuthActivity extends Activity {
    private static final String TAG = "NestAuthActivity";
    private static final String KEY_CLIENT_METADATA = "client_metadata_key";
    private static final String KEY_ACCESS_TOKEN = "access_token_key";
    private static final String QUERY_PARAM_CODE = "code";
    private static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int MAX_PROGRESS = 100;

    private ProgressBar mProgressBar;
    private NestConfig mNestConfig;
    private OkHttpClient mHttpClient;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.nest_auth_webview_layout);

        WebView clientWebView = findViewById(R.id.auth_webview);
        mProgressBar = findViewById(R.id.webview_progress);
        mNestConfig = getIntent().getParcelableExtra(KEY_CLIENT_METADATA);

        // If any args are empty, they are invalid and we should finish.
        if (mNestConfig == null || Utils.isAnyEmpty(mNestConfig.getRedirectURL(),
                mNestConfig.getClientID(),
                mNestConfig.getStateValue())) {
            finishWithResult(RESULT_CANCELED, null);
            return;
        }

        mHttpClient = new OkHttpClient();

        clientWebView.setWebChromeClient(new ProgressChromeClient());
        clientWebView.setWebViewClient(new RedirectClient());

        String url = String.format(WwnApiUrls.CLIENT_CODE_URL, mNestConfig.getClientID(),
                mNestConfig.getStateValue());
        WebSettings webSettings = clientWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        clientWebView.loadUrl(url);
    }

    /**
     * Finishes this activity, returning the resulting token and result code back to the parent
     * Activity.
     *
     * @param result A code representing the result state of the authorization flow.
     * @param token  The NestToken returned. This token will be used to authorize API requests.
     */
    private void finishWithResult(int result, NestToken token) {
        final Intent intent = new Intent();
        intent.putExtra(KEY_ACCESS_TOKEN, token);
        setResult(result, intent);
        finish();
    }

    /**
     * An indefinite progress spinner to show while loading the WebView.
     */
    private class ProgressChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            final int currentVisibility = mProgressBar.getVisibility();
            if (newProgress < MAX_PROGRESS && currentVisibility != View.VISIBLE) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else if (newProgress >= 80 && currentVisibility != View.GONE) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * The RedirectClient intercepts the redirect url and captures the code returned by Nest. This
     * code is then traded in for an access token. The access token is then returned from this
     * Activity back to the parent Activity.
     */
    private class RedirectClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.startsWith(mNestConfig.getRedirectURL())) {
                return false;
            }

            // Get the code from the resulting URL.
            String mCode = Uri.parse(url).getQueryParameter(QUERY_PARAM_CODE);
            if (mCode == null) {
                finishWithResult(RESULT_CANCELED, null);
                return true;
            }

            // Use the code to get an access token.
            String formattedUrl = String.format(WwnApiUrls.ACCESS_URL, mCode,
                    mNestConfig.getClientID(),
                    mNestConfig.getClientSecret());
            RequestBody body = RequestBody.create(TYPE_JSON, "");
            final Request request = new Request.Builder()
                    .url(formattedUrl)
                    .post(body)
                    .build();

            mHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Failed to get token.", e);
                    finishWithResult(RESULT_CANCELED, null);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (!response.isSuccessful()) {
                        finishWithResult(RESULT_CANCELED, null);
                        return;
                    }
                    try {
                        String body = response.body().string();
                        NestToken token = OBJECT_MAPPER.readValue(body, NestToken.class);
                        finishWithResult(RESULT_OK, token);
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to parse response for token.", e);
                        finishWithResult(RESULT_CANCELED, null);
                    }
                }
            });
            return true;
        }
    }
}

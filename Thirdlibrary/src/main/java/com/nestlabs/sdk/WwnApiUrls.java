/*
 * Copyright 2016, Google Inc.
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

public final class WwnApiUrls {
    public static final String DEFAULT_PROTOCOL = "https";
    public static final String DEFAULT_WWN_URL = "developer-api.nest.com";
    public static final String DEFAULT_PORT = "";
    private static final String BASE_AUTHORIZATION_URL = "https://home.nest.com/";
    static String AUTHORIZATION_SERVER_URL = "https://api.home.nest.com/";
    static final String ACCESS_URL = AUTHORIZATION_SERVER_URL
            + "oauth2/access_token?code=%s&client_id=%s&client_secret=%s"
            + "&grant_type=authorization_code";

    static final String CLIENT_CODE_URL = BASE_AUTHORIZATION_URL
            + "login/oauth2?client_id=%s&state=%s";
}

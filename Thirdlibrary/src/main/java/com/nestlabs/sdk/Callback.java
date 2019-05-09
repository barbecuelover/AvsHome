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

/**
 * Callback is used when setting values using the WwnClient. You can pass in a new instance of an
 * anonymous class implementing Callback and get notified when the result of setting the value is
 * returned.
 */
public interface Callback {

    /**
     * onSuccess is called when the action succeeded without error.
     */
    void onSuccess();

    /**
     * onFailure is called when the action failed due to some error.
     *
     * @param exception the returned exception with a message describing the reason for failure.
     */
    void onFailure(NestException exception);
}

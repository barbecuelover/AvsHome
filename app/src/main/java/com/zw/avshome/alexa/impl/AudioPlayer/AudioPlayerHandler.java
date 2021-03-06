/*
 * Copyright 2017-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.zw.avshome.alexa.impl.AudioPlayer;

import com.amazon.aace.alexa.AudioPlayer;
import com.amazon.aace.alexa.MediaPlayer;
import com.amazon.aace.alexa.Speaker;
import com.zw.avshome.alexa.impl.MediaPlayer.MediaPlayerHandler;


public class AudioPlayerHandler extends AudioPlayer {

    public AudioPlayerHandler(MediaPlayer mediaPlayer, Speaker speaker) {
        super(mediaPlayer, speaker);
    }

    public AudioPlayerHandler(MediaPlayerHandler mediaPlayer) {
        this(mediaPlayer, mediaPlayer.getSpeaker());
    }
}

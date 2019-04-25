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

package com.zw.avshome.alexa.impl.SpeechRecognizer;

import android.content.Context;
import android.media.AudioRecord;

import com.amazon.aace.alexa.SpeechRecognizer;
import com.zw.avshome.alexa.impl.Common.AudioInputManager;


import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpeechRecognizerHandler extends SpeechRecognizer
        implements AudioInputManager.AudioInputConsumer {

    private static final String sTag = "SpeechRecognizer";

    private final AudioInputManager mAudioInputManager;
    private AudioCueObservable mAudioCueObservable = new AudioCueObservable();
    private final ExecutorService mExecutor = Executors.newFixedThreadPool(1);
    private AudioRecord mAudioInput;
    private boolean mWakeWordEnabled;
    private boolean mAllowStopCapture = false; // Only true if holdToTalk() returned true

    private final Context mContext;

    public SpeechRecognizerHandler(AudioInputManager audioInputManager,
                                   Context context,
                                   boolean wakeWordSupported,
                                   boolean wakeWordEnabled) {
        super(wakeWordSupported && wakeWordEnabled);
        mAudioInputManager = audioInputManager;
        mContext = context;
        mWakeWordEnabled = wakeWordEnabled;

//        setupGUI( wakeWordSupported );
    }

    @Override
    public boolean startAudioInput() {
        return mAudioInputManager.startAudioInput(this);
    }

    @Override
    public boolean stopAudioInput() {
        return mAudioInputManager.stopAudioInput(this);
    }

    @Override
    public boolean wakewordDetected(String wakeWord) {
        mAudioCueObservable.playAudioCue(AudioCueState.START_VOICE);
        return true;
    }

    @Override
    public void endOfSpeechDetected() {
        mAudioCueObservable.playAudioCue(AudioCueState.END);
    }

    public void onTapToTalk() {
        if (tapToTalk()) mAudioCueObservable.playAudioCue(AudioCueState.START_TOUCH);
    }

    public void onHoldToTalk() {
        mAllowStopCapture = false;
        if (holdToTalk()) {
            mAllowStopCapture = true;
            mAudioCueObservable.playAudioCue(AudioCueState.START_TOUCH);
        }
    }

    public void onReleaseHoldToTalk() {
        if (mAllowStopCapture) stopCapture();
        mAllowStopCapture = false;
    }

    @Override
    public String getAudioInputConsumerName() {
        return "SpeechRecognizer";
    }

    @Override
    public void onAudioInputAvailable(byte[] buffer, int size) {
        write(buffer, size); // Write audio samples to engine
    }


    public enum AudioCueState {START_TOUCH, START_VOICE, END}

    public static class AudioCueObservable extends Observable {

        void playAudioCue(AudioCueState state) {
            setChanged();
            notifyObservers(state);
        }
    }

    public void addObserver(Observer observer) {
        if (mAudioCueObservable == null) mAudioCueObservable = new AudioCueObservable();
        mAudioCueObservable.addObserver(observer);
    }
}

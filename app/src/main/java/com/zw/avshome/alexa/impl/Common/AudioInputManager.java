package com.zw.avshome.alexa.impl.Common;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;



/**
 * @c AudioInputManager is the owner of Android @c AudioRecord object. It facilitates feeding microphone
 * input (from @c AudioRecord) to multiple clients simultaneously (or to an individual client if there
 * is only one client).
 */
public class AudioInputManager {
    private static final String sTag = "AudioInputManager";

    // All audio input consumers expect PCM 16 data @ 16 Khz. We divide this consumption into 10 ms
    // chunks. It comes out at 160 samples every 10 ms to reach 16000 samples (in a second).
    private static final int sSamplesToCollectInOneCycle = 160;
    private static final int sBytesInEachSample = 2; // PCM 16 = 2 bytes per sample
    private static final int sSampleRateInHz = 16000; //16 khz
    private static final int sAudioFramesInBuffer = 5; // Create large enough buffer for 5 audio frames.

    //    private final LoggerHandler mLogger;
    private AudioRecord mAudioInput;
    private AudioReaderRunnable mReaderRunnable;
    private List<AudioInputConsumer> mAudioInputConsumers;
    private final ExecutorService mExecutor = Executors.newFixedThreadPool(1);

    /**
     * Interface implemented by clients who would like to be notified of microphone audio input (that
     * is read from @c mAudioInput).
     */
    public interface AudioInputConsumer {
        /**
         * Returns the name of audio input consumer. Currently used only for logging.
         */
        String getAudioInputConsumerName();

        /**
         * Notifies @c AudioInputConsumer that additional data is available from microphone.
         *
         * @param buffer Data buffer where audio samples are available.
         * @param size   Number of bytes made available in this cycle.
         */
        void onAudioInputAvailable(byte[] buffer, int size);
    }

    public AudioInputManager() {
        mAudioInput = createAudioInput();
        mAudioInputConsumers = new ArrayList<AudioInputConsumer>();
    }

    /**
     * Allows @c AudioInputConsumer to request capturing audio samples from microphone. If microphone
     * audio sample capture is already in progress (because it is requested by other consumers before),
     * then simply adds the given @c AudioInputConsumer in its observers list to send notifications
     * whenever additional audio samples are available.
     *
     * @param consumer @c AudioInputConsumer who will be notified whenever new audio samples are read
     *                 from microphone
     * @return @c true if method succeed, @c false otherwise.
     */
    public boolean startAudioInput(AudioInputConsumer consumer) {
       Log.i(sTag, "Start recording request received from " + consumer.getAudioInputConsumerName());

        synchronized (mAudioInputConsumers) {
            mAudioInputConsumers.add(consumer);
        }

        if (mReaderRunnable != null && mReaderRunnable.isRunning()) {
            Log.i(sTag, "Audio recording already in progress");
            return true;
        }

        return startAudioInput();
    }

    /**
     * Allows @c AudioInputConsumer to request ceasing the microphone input capture loop. The microphone
     * reading loop will only be stopped if none of the other @c AudioInputConsumer(s) are left in the
     * list who have earlier requested to capture the microphone input by calling @c startAudioInput.
     *
     * @param consumer @c AudioInputConsumer who no longer wishes to receive audio input samples from
     *                 microphone.
     * @return @c true if method succeed, @c false otherwise.
     */
    public boolean stopAudioInput(AudioInputConsumer consumer) {
        Log.i(sTag, "Stop recording request received from " + consumer.getAudioInputConsumerName());

        int consumersLeft = 0;
        synchronized (mAudioInputConsumers) {
            mAudioInputConsumers.remove(consumer);
            consumersLeft = mAudioInputConsumers.size();
        }

        if (consumersLeft == 0) {
            Log.i(sTag, "Stopping recording for the last client " + consumer.getAudioInputConsumerName());
            return stopAudioInput();
        }

        Log.i(sTag, "Audio recording wouldnt be stopped on account of remaining clients");
        return true;
    }

    private boolean startAudioInput() {
        if (mAudioInput == null) {
            Log.w(sTag,
                    "Cannot start audio input. AudioRecord could not be created");
            return false;
        }

        if (mAudioInput.getState() != AudioRecord.STATE_INITIALIZED) {
            // Retry AudioRecord initialization.
            mAudioInput = createAudioInput();
            if (mAudioInput.getState() != AudioRecord.STATE_INITIALIZED) {
                Log.w(sTag, "Cannot initialize AudioRecord");
                return false;
            }
        }

        return startRecording();
    }

    private boolean stopAudioInput() {
        if (mAudioInput == null) {
            Log.w(sTag,
                    "stopAudioInput() called but AudioRecord was never initialized");
            return false;
        }

        // Cancel the audio reader and stop recording
        if (mReaderRunnable != null) mReaderRunnable.cancel();
        try {
            mAudioInput.stop();
        } catch (IllegalStateException e) {
            Log.e(sTag, "AudioRecord cannot stop recording. Error: "
                    + e.getMessage());
            return false;
        }

        return true;
    }

    private boolean startRecording() {
        if (mReaderRunnable != null && mReaderRunnable.isRunning()) {
            Log.i(sTag,
                    "startMonitoringWakeWord() called but AudioRecorder thread is already running");
            return false;
        } else {
            // Start audio recording
            try {
                mAudioInput.startRecording();
            } catch (IllegalStateException e) {
                Log.e(sTag, "AudioRecord cannot start recording. Error: "
                        + e.getMessage());
                return false;
            }

            // Read recorded audio samples and pass to engine
            try {
                mExecutor.submit(mReaderRunnable = new AudioReaderRunnable()); // Submit the audio reader thread
            } catch (RejectedExecutionException e) {
                Log.e(sTag,
                        "Audio reader task cannot be scheduled for execution. Error: "
                                + e.getMessage());
                return false;
            }
            return true;
        }
    }

    private void notifyDataAvailableToAudioInputConsumers(byte[] buffer, int size) {
        synchronized (mAudioInputConsumers) {
            for (AudioInputConsumer consumer : mAudioInputConsumers) {
                consumer.onAudioInputAvailable(buffer, size);
            }
        }
    }

    private AudioRecord createAudioInput() {
        AudioRecord audioRecord = null;
        try {
            int minBufferSize = AudioRecord.getMinBufferSize(
                    sSampleRateInHz,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            int bufferSize = minBufferSize + (
                    sAudioFramesInBuffer * sSamplesToCollectInOneCycle * sBytesInEachSample);
            audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC, sSampleRateInHz,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);
        } catch (IllegalArgumentException e) {
            Log.e(sTag, "Cannot create audio input. Error: "
                    + e.getMessage());
        }
        return audioRecord;
    }

    //
    // AudioReader class
    //

    private class AudioReaderRunnable implements Runnable {

        private boolean mRunning = true;
        private byte[] mBuffer = new byte[sSamplesToCollectInOneCycle * sBytesInEachSample];

        void cancel() {
            mRunning = false;
        }

        boolean isRunning() {
            return mRunning;
        }

        @Override
        public void run() {
            int size;

            while (mRunning) {
                size = mAudioInput.read(mBuffer, 0, mBuffer.length);
                if (size > 0 && mRunning) {
                    notifyDataAvailableToAudioInputConsumers(mBuffer, size);
                }
            }
        }
    }
}

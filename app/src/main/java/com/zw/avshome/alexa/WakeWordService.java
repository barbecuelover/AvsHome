package com.zw.avshome.alexa;

import android.content.Context;
import android.os.Handler;

import ai.kitt.snowboy.AppResCopy;
import ai.kitt.snowboy.audio.AudioDataSaver;
import ai.kitt.snowboy.audio.RecordingThread;

public class WakeWordService {

    private RecordingThread recordingThread;

    private static WakeWordService wakeWordService;
    private Context mContext;

    private WakeWordService(Context context) {
        mContext = context;
    }

    public static WakeWordService getInstance(Context context) {
        if (wakeWordService == null) {
            wakeWordService = new WakeWordService(context);
        }
        return wakeWordService;
    }

    public void init(Handler handle) {
        AppResCopy.copyResFromAssetsToSD(mContext);
        recordingThread = new RecordingThread(handle, new AudioDataSaver());
    }

    public void startRecording() {
        recordingThread.startRecording();
    }

    public void stopRecording() {
        recordingThread.stopRecording();
    }

    public void onDestroy() {
        recordingThread.stopRecording();
    }
}

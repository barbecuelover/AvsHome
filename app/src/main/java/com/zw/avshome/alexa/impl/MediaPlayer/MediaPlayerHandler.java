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

package com.zw.avshome.alexa.impl.MediaPlayer;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amazon.aace.alexa.Speaker;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.zw.avshome.alexa.impl.PlaybackController.PlaybackControllerHandler;
import com.zw.avshome.utils.VolumeUtil;

//import org.greenrobot.eventbus.EventBus;

import java.io.FileOutputStream;
import java.io.IOException;



public class MediaPlayerHandler extends com.amazon.aace.alexa.MediaPlayer {

    private static final String sTag = "MediaPlayer";
    private static final String sFileName = "alexa_media"; // Note: not thread safe
    private static final String AUDIOPLAYER = "Audio Player";

    //    private final Activity mActivity;
    private final Context mContext;
    //    private final LoggerHandler mLogger;
    private final String mName;
    private final SpeakerHandler mSpeaker;
    private final MediaSourceFactory mMediaSourceFactory;
    private PlaybackControllerHandler mPlaybackController;
    private SimpleExoPlayer mPlayer;
    private AudioManager mAudioManager;
    public static String MediaStateStart = "START";
    public static String MediaStatePause = "PAUSE";
    public static String MediaStateStop = "STOP";
    public static String MediaStateResume = "RESUME";
    private OnPlaybackListener mPlaybackListener;

    public MediaPlayerHandler(Context context,
                              String name,
                              @Nullable Speaker.Type speakerType,
                              @Nullable PlaybackControllerHandler controller) {
        mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        mName = name;
        mSpeaker = new SpeakerHandler(speakerType);
        mMediaSourceFactory = new MediaSourceFactory(mContext, mName);

        if (controller != null) {
            mPlaybackController = controller;
            mPlaybackController.setMediaPlayer(this);
        }
        initializePlayer();

    }

    private void initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, new DefaultTrackSelector());
        mPlayer.addListener(new PlayerEventListener());
        mPlayer.setPlayWhenReady(false);
    }

    private void resetPlayer() {
        mPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
        mPlayer.setPlayWhenReady(false);
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.getPlayWhenReady()
                && (mPlayer.getPlaybackState() == Player.STATE_BUFFERING
                || mPlayer.getPlaybackState() == Player.STATE_READY);
    }

    public boolean isPlayEnd() {
        return (mPlayer != null && mPlayer.getPlaybackState() == Player.STATE_ENDED);
    }

    public long getDuration() {
        long duration = mPlayer.getDuration();
        return duration != C.TIME_UNSET ? duration : 0;
    }

    public Speaker getSpeaker() {
        return mSpeaker;
    }

    //
    // Handle playback directives from Engine
    //

    @Override
    public boolean prepare() {
//        Log.d( sTag, String.format( "(%s) Handling prepare()", mName ) );
        resetPlayer();

        try (FileOutputStream os = mContext.openFileOutput(sFileName, Context.MODE_PRIVATE)) {
            byte[] buffer = new byte[4096];
            int size;
            while (!isClosed()) {
                while ((size = read(buffer)) > 0) os.write(buffer, 0, size);
            }
        } catch (IOException e) {
            Log.e(sTag, e.toString());
            return false;
        }

        try {
            Uri uri = Uri.fromFile(mContext.getFileStreamPath(sFileName));
            MediaSource mediaSource = mMediaSourceFactory.createFileMediaSource(uri);
            mPlayer.prepare(mediaSource, true, false);
            return true;
        } catch (Exception e) {
            Log.e(sTag, e.getMessage());
            String message = e.getMessage() != null ? e.getMessage() : "";
            mediaError(MediaError.MEDIA_ERROR_UNKNOWN, message);
            return false;
        }
    }

    @Override
    public boolean prepare(String url) {
        Log.d(sTag, String.format("(%s) Handling prepare(url)", mName));
        resetPlayer();
        Uri uri = Uri.parse(url);
        try {
            MediaSource mediaSource = mMediaSourceFactory.createHttpMediaSource(uri);
            mPlayer.prepare(mediaSource, true, false);
            return true;
        } catch (Exception e) {
            String message = e.getMessage() != null ? e.getMessage() : "";
            Log.e(sTag, message);
            mediaError(MediaError.MEDIA_ERROR_UNKNOWN, message);
            return false;
        }
    }

    @Override
    public boolean play() {
        Log.d(sTag, String.format("(%s) Handling play()", mName));
        mPlayer.setPlayWhenReady(true);
        return true;
    }

    @Override
    public boolean stop() {
        Log.d(sTag, String.format("(%s) Handling stop()", mName));
        if (!mPlayer.getPlayWhenReady()) {
            // Player is already not playing. Notify Engine of stop
            onPlaybackStopped();
        } else mPlayer.setPlayWhenReady(false);
        return true;
    }

    @Override
    public boolean pause() {
        Log.d(sTag, String.format("(%s) Handling pause()", mName));
//        if (mName.equals(AUDIOPLAYER)) {
//            EventBus.getDefault().post(new MediaPlayerStateMessage(MediaStatePause));
//        }
        mPlayer.setPlayWhenReady(false);
        return true;
    }

    @Override
    public boolean resume() {
        Log.d(sTag, String.format("(%s) Handling resume()", mName));
//        if (mName.equals(AUDIOPLAYER)) {
//            EventBus.getDefault().post(new MediaPlayerStateMessage(MediaStateResume));
//        }
        mPlayer.setPlayWhenReady(true);
        return true;
    }

    @Override
    public boolean setPosition(long position) {
        Log.d(sTag, String.format("(%s) Handling setPosition(%s)", mName, position));
        mPlayer.seekTo(position);
        return true;
    }

    @Override
    public long getPosition() {
        return Math.abs(mPlayer.getCurrentPosition());
    }

    //
    // Handle ExoPlayer state changes and notify Engine
    //

    public interface OnPlaybackListener {
        void onPlaybackStartedOrBuffering();
    }

    public void setOnPlaybackListener(OnPlaybackListener listener) {
        mPlaybackListener = listener;
    }

    private void onPlaybackStarted() {
        Log.d(sTag, String.format("(%s) Media State Changed. STATE: PLAYING", mName));
//        if (mName.equals(AUDIOPLAYER)) {
//            EventBus.getDefault().post(new MediaPlayerStateMessage(MediaStateStart));
//        }
        if (mPlaybackListener != null) {
            mPlaybackListener.onPlaybackStartedOrBuffering();
        }
        mediaStateChanged(MediaState.PLAYING);
        if (mPlaybackController != null) {
            mPlaybackController.start();
        }
    }

    private void onPlaybackStopped() {
        Log.d(sTag, String.format("(%s) Media State Changed. STATE: STOPPED", mName));
//        if (mName.equals(AUDIOPLAYER)) {
//            EventBus.getDefault().post(new MediaPlayerStateMessage(MediaStateStop));
//        }
        mediaStateChanged(MediaState.STOPPED);
        if (mPlaybackController != null) {
            mPlaybackController.stop();
        }
    }

    private void onPlaybackFinished() {
        if (isRepeating()) {
            mPlayer.seekTo(0);
            mPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        } else {
            mPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
            if (mPlaybackController != null) {
                mPlaybackController.reset();
            }
            Log.d(sTag, String.format("(%s) Media State Changed. STATE: STOPPED", mName));
//            if (mName.equals(AUDIOPLAYER)) {
//                EventBus.getDefault().post(new MediaPlayerStateMessage(MediaStateStop));
//            }
            mediaStateChanged(MediaState.STOPPED);
        }
    }

    private void onPlaybackBuffering() {
        Log.d(sTag, String.format("(%s) Media State Changed. STATE: BUFFERING", mName));
        if (mPlaybackListener != null) {
            mPlaybackListener.onPlaybackStartedOrBuffering();
        }
        mediaStateChanged(MediaState.BUFFERING);
    }

    //
    // ExoPlayer event listener
    //
    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_ENDED:
                    if (playWhenReady) onPlaybackFinished();
                    break;
                case Player.STATE_READY:
                    if (playWhenReady) onPlaybackStarted();
                    else onPlaybackStopped();
                    break;
                case Player.STATE_BUFFERING:
                    if (playWhenReady) onPlaybackBuffering();
                    break;
                default:
                    // Disregard other states
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            String message;
            if (e.type == ExoPlaybackException.TYPE_SOURCE) {
                message = "ExoPlayer Source Error: " + e.getSourceException().getMessage();
            } else if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                message = "ExoPlayer Renderer Error: " + e.getRendererException().getMessage();
            } else if (e.type == ExoPlaybackException.TYPE_UNEXPECTED) {
                message = "ExoPlayer Unexpected Error: " + e.getUnexpectedException().getMessage();
            } else {
                message = e.getMessage();
            }
            Log.e(sTag, "PLAYER ERROR: " + message);
            mediaError(MediaError.MEDIA_ERROR_INTERNAL_DEVICE_ERROR, message);
        }
    }

    //
    // SpeakerHandler
    //

    public class SpeakerHandler extends Speaker {

        //        private SeekBar mVolumeControl;
        private TextView mMuteButton;
        private byte mVolume = 50;
        private boolean mIsMuted = false;

        SpeakerHandler(@Nullable Speaker.Type type) {
            super();
            if (type == Speaker.Type.AVS_SPEAKER) {
                // Link mute button to synced speakers only
//                mMuteButton = mActivity.findViewById( R.id.muteSpeakerButton );
            }
            setupUIVolumeControls(type);
        }

        @Override
        public boolean setVolume(byte volume) {
            if (mVolume == volume)
                return true;
            Log.i(sTag, String.format("(%s) Handling setVolume(%s)", mName, volume));
            mVolume = volume;
            if (mIsMuted) {
                mPlayer.setVolume(0);
                updateUIVolume((byte) 0);
            } else {
                float channelVolume = volume / 100f;
                mPlayer.setVolume(channelVolume);

                switch (mName){
                    case "Notifications":
                        int oldNotificationInt = mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                        byte oldNotificationsByte = VolumeUtil.eightSegmentsToAvs(oldNotificationInt);
                        int newNotificationsInt = VolumeUtil.avsToEightSegments(volume);

                        if (volume != oldNotificationsByte) {
                            mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, newNotificationsInt, AudioManager.FLAG_PLAY_SOUND);
                        }
                        break;

                    case "Alerts":
                        int oldAlertsInt = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                        byte oldAlertsByte = VolumeUtil.eightSegmentsToAvs(oldAlertsInt);
                        int newAlertsInt = VolumeUtil.avsToEightSegments(volume);
                        if (volume != oldAlertsByte) {
                            mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, newAlertsInt, AudioManager.FLAG_PLAY_SOUND);
                        }
                        break;

                    case "Speech Synthesizer":
                    case "Audio Player":

                        int oldMusicInt = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        byte oldMusicByte = VolumeUtil.fifteenSegmentsToAvs(oldMusicInt);
                        int newMusicInt = VolumeUtil.avsToFifteenSegments(volume);
                        if (volume != oldMusicByte) {
                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newMusicInt, AudioManager.FLAG_SHOW_UI);
                        }
                        break;
                }

                updateUIVolume(volume);
            }
            return true;
        }

        public boolean syncVolume(byte volume){
            if (mVolume == volume)
                return true;
            Log.i(sTag, String.format("(%s) Handling syncVolume(%s)", mName, volume));
            mVolume = volume;
            if (mIsMuted) {
                mPlayer.setVolume(0);
                updateUIVolume((byte) 0);
            } else {
                float channelVolume = volume / 100f;
                mPlayer.setVolume(channelVolume);
            }
            return true;
        }

        @Override
        public boolean adjustVolume(byte value) {

            int newSys = 5;
            switch (mName){
                case "Notifications":
                    newSys = VolumeUtil.avsToEightSegments((byte) (mVolume + value));
                    mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, newSys, AudioManager.FLAG_PLAY_SOUND);
                    break;
                case "Alerts":
                    newSys = VolumeUtil.avsToEightSegments((byte) (mVolume + value));
                    mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, newSys, AudioManager.FLAG_PLAY_SOUND);
                    break;
                case "Speech Synthesizer":
                case "Audio Player":
                    newSys = VolumeUtil.avsToFifteenSegments((byte) (mVolume + value));
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newSys, AudioManager.FLAG_SHOW_UI);
                    break;
            }
            return setVolume((byte) (mVolume + value));
        }

        @Override
        public byte getVolume() {
            if (mIsMuted) return 0;
            else return mVolume;
        }

        @Override
        public boolean setMute(boolean mute) {
            if (mute && !mIsMuted) {
                Log.i(sTag, String.format("Handling mute (%s)", mName));
                updateMuteButton(mute);
            } else if (!mute && mIsMuted) {
                Log.i(sTag, String.format("Handling unmute (%s)", mName));
                updateMuteButton(mute);
            }

            mIsMuted = mute;
            if (mute) {
                mPlayer.setVolume(0);
                updateUIVolume((byte) 0);

                if (mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0 && ("Audio Player".equals(mName)|| "Speech Synthesizer".equals(mName))) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
                }

            } else {
                mPlayer.setVolume(mVolume / 100f);
                updateUIVolume(mVolume);
                if ("Audio Player".equals(mName) ||"Speech Synthesizer".equals(mName)){
                    byte oldMusicByte = VolumeUtil.fifteenSegmentsToAvs(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    int newSys = VolumeUtil.avsToFifteenSegments(mVolume);
                    if (mVolume != oldMusicByte) {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newSys, AudioManager.FLAG_SHOW_UI);
                    }
                }
            }
            return true;
        }

        @Override
        public boolean isMuted() {
            return mIsMuted;
        }

        private void setupUIVolumeControls(@Nullable final Speaker.Type type) {
            if (type == Speaker.Type.AVS_SPEAKER) {
//                mVolumeControl = mActivity.findViewById( R.id.speakerVolume );
            } else {
//                mVolumeControl = mActivity.findViewById( R.id.alertsVolume );
            }

            updateUIVolume(mVolume);
            updateMuteButton(mIsMuted);

//            mVolumeControl.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser ) {}
//
//                @Override
//                public void onStartTrackingTouch( SeekBar seekBar ) {}
//
//                @Override
//                public void onStopTrackingTouch( SeekBar seekBar ) {
//                    int progress = seekBar.getProgress();
//                    if ( type != Speaker.Type.AVS_SPEAKER ) {
//                        localVolumeSet( ( byte ) progress );
//                    } else {
//                        // Unmute before setting volume
//                        if ( mIsMuted ) {
//                            updateMuteButton( false );
//                            localMuteSet( false );
//                        }
//                        localVolumeSet( ( byte ) progress );
//                    }
//                }
//            });

            // Prevent parent view from intercepting touch events
//            mVolumeControl.setOnTouchListener( new View.OnTouchListener() {
//                @Override
//                public boolean onTouch( View v, MotionEvent event ) {
//                    int action = event.getAction();
//                    switch( action ) {
//                        case MotionEvent.ACTION_DOWN:
//                            v.getParent().requestDisallowInterceptTouchEvent( true );
//                            break;
//
//                        case MotionEvent.ACTION_UP:
//                            v.getParent().requestDisallowInterceptTouchEvent( false );
//                            break;
//                    }
//
//                    // Handle SeekBar touch events
//                    v.onTouchEvent( event );
//                    return true;
//                }
//            });

            if (mMuteButton != null) {
                mMuteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateMuteButton(!mIsMuted);
                        Log.d(sTag, String.format("Calling localMuteSet(%s)", !mIsMuted));
                        localMuteSet(!mIsMuted);
                    }
                });
            }
        }

        private void updateMuteButton(final boolean isMuted) {
            if (mMuteButton != null) {
//                mActivity.runOnUiThread( new Runnable() {
//                    @Override
//                    public void run() {
////                        if ( !isMuted ) mMuteButton.setText( R.string.volume_mute );
////                        else mMuteButton.setText( R.string.volume_unmute );
//                    }
//                });
            }
        }

        private void updateUIVolume(final byte vol) {
//            mActivity.runOnUiThread( new Runnable() {
//                @Override
//                public void run() {
////                    mVolumeControl.setProgress( vol );
//                }
//            });
        }
    }
}

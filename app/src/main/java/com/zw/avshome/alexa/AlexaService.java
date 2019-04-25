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

package com.zw.avshome.alexa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;

import com.amazon.aace.alexa.Alerts;
import com.amazon.aace.alexa.AlexaClient;
import com.amazon.aace.alexa.AlexaProperties;
import com.amazon.aace.alexa.Speaker;
import com.amazon.aace.alexa.config.AlexaConfiguration;
import com.amazon.aace.carControl.CarControlConfiguration;
import com.amazon.aace.communication.config.AlexaCommsConfiguration;
import com.amazon.aace.core.CoreProperties;
import com.amazon.aace.core.Engine;
import com.amazon.aace.core.config.EngineConfiguration;
import com.amazon.aace.localSkillService.config.LocalSkillServiceConfiguration;
import com.amazon.aace.localVoiceControl.config.LocalVoiceControlConfiguration;
import com.amazon.aace.logger.Logger;
import com.amazon.aace.logger.config.LoggerConfiguration;
import com.amazon.aace.storage.config.StorageConfiguration;
import com.amazon.aace.vehicle.config.VehicleConfiguration;
import com.zw.avshome.HomeApplication;
import com.zw.avshome.alexa.impl.Alerts.AlertsHandler;
import com.zw.avshome.alexa.impl.AlexaClient.AlexaClientHandler;
import com.zw.avshome.alexa.impl.AudioPlayer.AudioPlayerHandler;
import com.zw.avshome.alexa.impl.AuthProvider.AuthProviderHandler;
import com.zw.avshome.alexa.impl.Common.AudioInputManager;
import com.zw.avshome.alexa.impl.MediaPlayer.MediaPlayerHandler;
import com.zw.avshome.alexa.impl.Notifications.NotificationsHandler;
import com.zw.avshome.alexa.impl.PlaybackController.PlaybackControllerHandler;
import com.zw.avshome.alexa.impl.SpeechRecognizer.SpeechRecognizerHandler;
import com.zw.avshome.alexa.impl.SpeechSynthesizer.SpeechSynthesizerHandler;
import com.zw.avshome.alexa.impl.TemplateRuntime.TemplateRuntimeHandler;
import com.zw.avshome.alexa.interfaces.AlertListener;
import com.zw.avshome.alexa.interfaces.AlexaClientConnectStateListener;
import com.zw.avshome.alexa.interfaces.AvsStateChangeListener;
import com.zw.avshome.alexa.interfaces.ClearTemplateListener;
import com.zw.avshome.alexa.interfaces.ClientConnectStateListener;
import com.zw.avshome.alexa.interfaces.PlayBackControllerListener;
import com.zw.avshome.alexa.interfaces.PlayBackToggleButtonControlerListener;
import com.zw.avshome.alexa.interfaces.PlayerInfoListener;
import com.zw.avshome.alexa.interfaces.StateChangeListener;
import com.zw.avshome.alexa.interfaces.TemplateListener;
import com.zw.avshome.alexa.ui.BodyTemplate1Activity;
import com.zw.avshome.alexa.ui.BodyTemplate2Activity;
import com.zw.avshome.alexa.ui.ListTemplate1Activity;
import com.zw.avshome.alexa.ui.RenderPlayerInfoActivity;
import com.zw.avshome.alexa.ui.WeatherActivity;
import com.zw.avshome.utils.Constant;
import com.zw.avshome.utils.SharePreUtil;
import com.zw.avshome.utils.StringUtil;
import com.zw.avshome.utils.VolumeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AlexaService {

    private static final String TAG = "AlexaService";
    private static final String APP_CONFIG_JSON = "app_config.json";

    private static AlexaService alexaService;
    private Context mContext;
    /* Shared Preferences */
    private SharedPreferences mAuthSP;

    private String clientId,clientSecret, productId ,productDsn;

    private final int CLEAR_PLAYER_INFO_WAIT_TIME = 5000;
    private Intent mediaPlayerServiceIntent;
    private ClearTemplateListener mClearPlayInfoListener;
    private CountDownTimer playerInfoTimer;

    private String mRenderPlayerJson;
    private String androidId;
    /* dialogStateChanged callback*/
    private StateChangeListener mStateChangeListener;
    // Common
    private AudioInputManager mAudioInputManager;
    // Alexa
    private AlertsHandler mAlerts;
    private AlexaClientHandler mAlexaClient;
    private AudioPlayerHandler mAudioPlayer;
    private AuthProviderHandler mAuthProvider;

    private NotificationsHandler mNotifications;

    private PlaybackControllerHandler mPlaybackController;
    private SpeechRecognizerHandler mSpeechRecognizer;
    private SpeechSynthesizerHandler mSpeechSynthesizer;
    private TemplateRuntimeHandler mTemplateRuntime;
    private MediaPlayerHandler mAudioPlayerHandler;
    private MediaPlayerHandler mSpeechSynthesizerHandler;
    private Speaker audioSpeaker;
    private List<Speaker> speakerList = new ArrayList<>();

    private AudioManager mAudioManager;
    // Core
    private Engine mEngine;



    private String mCurrentAudioItemId;


    private AlexaService(Context context) {
        mContext = context;

        initAuthKey();
        mAuthProvider = new AuthProviderHandler(mContext);

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        initPlayerInfoTimer();
    }

    public static AlexaService getInstance() {
        if (alexaService == null) {
            alexaService = new AlexaService(HomeApplication.getInstance());
        }
        return alexaService;
    }

    public void start() {
        try {
            startEngine();
        } catch (RuntimeException e) {
            Log.e(TAG, "Could not StartEngine. Reason: " + e.getMessage());
        }
    }

    private void startEngine() throws RuntimeException {

        Log.d(TAG, "AlexaService startEngine!!!");

        EngineConfiguration[] engineConfigurations = getEngineConfiguration();
        boolean configureSucceeded = mEngine.configure(engineConfigurations);
        if (!configureSucceeded) throw new RuntimeException("Engine configuration failed");

        // Create the platform implementation handlers and register them with the engine

        /*init mAudioInputManager*/
        mAudioInputManager = new AudioInputManager();


        // AlexaClient
        if (!mEngine.registerPlatformInterface(
                mAlexaClient = new AlexaClientHandler(mContext)
        )
        ) throw new RuntimeException("Could not register AlexaClient platform interface");


        // PlaybackController
        if (!mEngine.registerPlatformInterface(
                mPlaybackController = new PlaybackControllerHandler(mContext)
        )
        )
            throw new RuntimeException("Could not register PlaybackController platform interface");

        // SpeechRecognizer
        boolean wakeWordSupported =
                mEngine.getProperty(AlexaProperties.WAKEWORD_SUPPORTED).equals("true");
        if (!mEngine.registerPlatformInterface(
                mSpeechRecognizer = new SpeechRecognizerHandler(
                        mAudioInputManager,
                        mContext,
                        wakeWordSupported,
                        true
                )
        )
        )
            throw new RuntimeException("Could not register SpeechRecognizer platform interface");

        // AudioPlayer
        if (!mEngine.registerPlatformInterface(
                mAudioPlayer = new AudioPlayerHandler(
                        mAudioPlayerHandler = new MediaPlayerHandler(
                                mContext,
                                "Audio Player",
                                Speaker.Type.AVS_SPEAKER,
                                mPlaybackController)
                )
        )
        ) throw new RuntimeException("Could not register AudioPlayer platform interface");


        // SpeechSynthesizer
        if (!mEngine.registerPlatformInterface(
                mSpeechSynthesizer = new SpeechSynthesizerHandler(
                        mSpeechSynthesizerHandler = new MediaPlayerHandler(
                                mContext,
                                "Speech Synthesizer",
                                Speaker.Type.AVS_SPEAKER,
                                null)
                )
        )
        )
            throw new RuntimeException("Could not register SpeechSynthesizer platform interface");

        // TemplateRuntime
        if (!mEngine.registerPlatformInterface(
                mTemplateRuntime = new TemplateRuntimeHandler(mPlaybackController)
        )
        )
            throw new RuntimeException("Could not register TemplateRuntime platform interface");

        // Alerts
        if (!mEngine.registerPlatformInterface(
                mAlerts = new AlertsHandler(
                        mContext,
                        new MediaPlayerHandler(
                                mContext,
                                "Alerts",
                                Speaker.Type.AVS_ALERTS,
                                null
                        )
                )
        )
        ) throw new RuntimeException("Could not register Alerts platform interface");


        // AuthProvider
        if (!mEngine.registerPlatformInterface(
                mAuthProvider
        )
        ) throw new RuntimeException("Could not register AuthProvider platform interface");


        // Notifications
        if (!mEngine.registerPlatformInterface(
                mNotifications = new NotificationsHandler(
                        mContext,
                        new MediaPlayerHandler(
                                mContext,
                                "Notifications",
                                Speaker.Type.AVS_ALERTS,
                                null
                        )
                )
        )
        ) throw new RuntimeException("Could not register Notifications platform interface");


        // Start the engine
        if (!mEngine.start()) throw new RuntimeException("Could not start engine");

        mAuthProvider.onInitialize();
        Log.d(TAG, "AlexaService: startEngine, SUCCESS!!!");

        setAVSListeners();

        setDefaultSettings();

    }


    /**
     * 设置默认配置,Alexa 和 system同步音量 ,勿扰模式等
     */
    private void setDefaultSettings() {

    }

    private void setAVSListeners() {


        /**
         * get alexa client state : include, LISTENING IDLE SPEAKING THINKING
         */
        mAlexaClient.setAvsStateChangeListener(new AvsStateChangeListener() {
            @Override
            public void setAvsStateChangeListener(Object state) {
                mStateChangeListener.setStateChangeListener(state);
                Log.d(TAG, state.toString());
            }
        });

        /**
         * get alexa client connect state
         */
        mAlexaClient.setAlexaClientConnectStateChange(new AlexaClientConnectStateListener() {
            @Override
            public void setAlexaClientConnectStateListener(Object state) {
                mClientConnectStateListener.setClientConnectStateListener(state);
                Log.d(TAG, state.toString());
            }
        });

        /**All Template module
         * 为所有模板
         */
        mTemplateRuntime.setTemplateListener(new TemplateListener() {
            @Override
            public void alexaTemplateListener(String type, JSONObject template) {
                /*重新封装template,替换特殊字符*/
                String tmp = template.toString().replace("\\/", "/");
                Log.v(TAG, "alexaTemplateListener --Json:\n" + tmp);
                try {
                    JSONObject mTemplate = new JSONObject(tmp);
                    switch (type) {
                        case "BodyTemplate1":
                            mContext.startActivity(BodyTemplate1Activity.createIntent(mContext, mTemplate));
                            break;
                        case "BodyTemplate2":
                            mContext.startActivity(BodyTemplate2Activity.createIntent(mContext, mTemplate));
                            break;
                        case "ListTemplate1":
                            mContext.startActivity(ListTemplate1Activity.createIntent(mContext, mTemplate));
                            break;
                        case "WeatherTemplate":
                            mContext.startActivity(WeatherActivity.createIntent(mContext, mTemplate));
                            break;
                        case "LocalSearchListTemplate1":
                            /* */
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mTemplateRuntime.setPlayerInfoListener(new PlayerInfoListener() {
            @Override
            public void onRenderPlayerInfo(JSONObject playerTmp) {
                try {
                    playerInfoTimer.cancel();
                    String audioItemId = playerTmp.getString("audioItemId");
                    // Log only if audio item has changed
                    if (!audioItemId.equals(mCurrentAudioItemId)) {
                        mCurrentAudioItemId = audioItemId;
                        Log.i(TAG, "playerTmp=" + playerTmp);
                        mRenderPlayerJson = playerTmp.toString().replace("\\/", "/");
                        Log.d(TAG, mRenderPlayerJson.toString());
//                        if ((!flagPlayerInfoActivityInFront) && flagPlayerNotificationShow) {
//                            // play in background ,do not start activity
//                        } else {
//                            mContext.startActivity(RenderPlayerInfoActivity.createIntent(mContext));
//                        }
                        mContext.startActivity(RenderPlayerInfoActivity.createIntent(mContext));
                        mContext.startService(mediaPlayerServiceIntent);
                    }
                } catch (Exception e) {
                    /**/
                }

            }

            @Override
            public void onClearPlayerInfo() {
                playerInfoTimer.cancel();
                playerInfoTimer.start();

            }
        });

        /*playback controler*/
        RenderPlayerInfoActivity.setPlayBackControllerListener(new PlayBackControllerListener() {
            @Override
            public void setPlayBackControllerListener(String str) {
                sentMediaCommand(str);
                switch (str) {
                    case "Previous":
                        mPlaybackController.previousButtonPressed();
                        break;
                    case "play":
                        mPlaybackController.playButtonPressed();
                    case "pause":
                        mPlaybackController.pauseButtonPressed();
                        break;
                    case "next":
                        mPlaybackController.nextButtonPressed();
                        break;
                    case "skip forward":
                        mPlaybackController.skipForwardButtonPressed();
                        break;
                    case "skip back":
                        mPlaybackController.skipBackwardButtonPressed();
                        break;
                    default:
                        break;
                }
            }
        });

        RenderPlayerInfoActivity.setmPlayBackToggleButtonControlerListener(new PlayBackToggleButtonControlerListener() {
            @Override
            public void setPlayBackToggleButtonControlerListener(String str, boolean bl) {
                switch (str) {
                    case "loop":
                        /*待完善。。。*/
                        mPlaybackController.loopTogglePressed(bl);
                        break;
                    case "shuffle":
                        /*待完善。。。*/
                        mPlaybackController.shuffleTogglePressed(bl);
                        break;
                    case "thumbDown":
                        mPlaybackController.thumbsDownTogglePressed(bl);
                        break;
                    case "thumbUp":
                        mPlaybackController.thumbsUpTogglePressed(bl);
                        break;
                    default:
                        break;
                }
            }
        });


        /**
         * Alert  Notification Alarm
         */
        mAlerts.setAlertListener(new AlertListener() {
            @Override
            public void setAlertCreateListener(String alertToken, String detailedInfo) {
                try {
                    String tmp = detailedInfo.replace("\\/", "/");
                    JSONObject mTemplate = new JSONObject(tmp);
                    Log.d(TAG, mTemplate.toString());

                    String timer = mTemplate.getString("time");
                    String type = mTemplate.getString("type");
                    switch (type) {
                        case "TIMER":
//                            if (!alertToken.equals(mCurrentAlertToken)) {
//                                mCurrentAlertToken = alertToken;
//                                /*500mm is for Reduce error*/
//                                long times = TimeUtil.getBetween(stringToLong(timer), System.currentTimeMillis() - 500);
//                                TimerInfo timerInfo = new TimerInfo(alertToken, timer, (int) times);
//                                Log.d(TAG, alertToken);
//                                timerInfoDBUtils.saveInfo(timerInfo);
//                            }
//                            EventBus.getDefault().post(new RefreshTimerMessage());
//                            mContext.startActivity(AlertTimerActivity.createIntent(mContext));
                            Log.d(TAG, "timer activity created");
                            break;
                        case "REMINDER":
//                            try {
//                                if (!alertToken.equals(mCurrentAlertToken)) {
//                                    mCurrentAlertToken = alertToken;
//                                    String reminderTime = mTemplate.getString("time");
//                                    String reminderContent = mTemplate.getString("label");
//                                    RemindInfo remindInfo = new RemindInfo(alertToken, reminderTime, reminderContent);
//                                    remindInfoDBUtils.saveInfo(remindInfo);
//                                    mContext.startActivity(AlertReminderActivity.createIntent(mContext));
//                                    Log.d(TAG, "reminder activity created");
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            EventBus.getDefault().post(new RefreshTimerMessage());
                            break;
                        case "ALARM":
//                            if (!alertToken.equals(mCurrentAlertToken)) {
//                                mCurrentAlertToken = alertToken;
//                                String alarmTime = mTemplate.getString("time");
//                                AlarmInfo alarmInfo = new AlarmInfo(alertToken, alarmTime);
//                                alarmInfoDBUtils.saveInfo(alarmInfo);
//                                mContext.startActivity(AlertAlarmActivity.createIntent(mContext));
//                                Log.d(TAG, "alarm activity created");
//                            }
//                            EventBus.getDefault().post(new RefreshTimerMessage());
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setAlertDeleteListener(String alertToken) {
//                timerInfoDBUtils.deleteInfo(alertToken);
//                remindInfoDBUtils.deleteInfo(alertToken);
//                alarmInfoDBUtils.deleteInfo(alertToken);
//                EventBus.getDefault().post(new RefreshTimerMessage());
//                Log.d(TAG, alertToken + " : alert deleted!!!");
            }

            @Override
            public void setAlertStateChangeListener(String alertToken, Alerts.AlertState state, String reason) {

            }
        });

    }



    private void initAuthKey() {

        mAuthSP = mContext.getSharedPreferences(Constant.AlexaAuthConfig.SP_FILE_NAME,Context.MODE_PRIVATE);

        clientId = mAuthSP.getString(Constant.AlexaAuthConfig.SP_KEY_CLIENT_ID, "");
        clientSecret = mAuthSP.getString(Constant.AlexaAuthConfig.SP_KEY_CLIENT_SECRET, "");
        productId = mAuthSP.getString(Constant.AlexaAuthConfig.SP_KEY_PRODUCT_ID, "");
        productDsn =mAuthSP.getString(Constant.AlexaAuthConfig.SP_KEY_PRODUCT_DSN, "");

        if (StringUtil.isEmpty(clientId,true) || StringUtil.isEmpty(clientSecret,true) || StringUtil.isEmpty(productId,true) || StringUtil.isEmpty(productDsn,true) ){
            //Retrieve device config from assets/app_config.json and update preferences
            JSONObject configJsonStr = getConfigFromFile("config");
            if (configJsonStr != null) {
                try {
                    clientId = configJsonStr.getString("clientId");
                    clientSecret = configJsonStr.getString("clientSecret");
                    productId = configJsonStr.getString("productId");
                    //DSN 是读取的手机SN 不是配置文件中的。
                    productDsn = getAndroidId(mContext);
                } catch (JSONException e) {
                    Log.d(TAG, "Missing device info in app_config.json");
                }
            }
            //保存到SP
            SharedPreferences.Editor editor = mAuthSP.edit();
            editor.putString(Constant.AlexaAuthConfig.SP_KEY_CLIENT_ID, clientId);
            editor.putString(Constant.AlexaAuthConfig.SP_KEY_CLIENT_SECRET, clientSecret);
            editor.putString(Constant.AlexaAuthConfig.SP_KEY_PRODUCT_ID, productId);
            editor.putString(Constant.AlexaAuthConfig.SP_KEY_PRODUCT_DSN, productDsn);
            editor.apply();
        }

    }


    private String getAndroidId(Context mContext) {
        return Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private JSONObject getConfigFromFile(String key) {
        JSONObject obj = null;
        try (
                InputStream is = mContext.getAssets().open(APP_CONFIG_JSON)
        ) {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "UTF-8");
            obj = new JSONObject(json);
        } catch (Exception e) {
            Log.e(TAG, String.format("Cannot read %s from assets directory. Error: %s",
                    APP_CONFIG_JSON, e.getMessage()));
        }

        JSONObject config = null;
        if (obj != null) {
            try {
                config = obj.getJSONObject(key);
            } catch (JSONException e) {
                Log.e(TAG, "No device config specified in " + APP_CONFIG_JSON);
            }
        }
        return config;
    }


    private EngineConfiguration[] getEngineConfiguration() {

        // Copy certs to the cache directory
        File cacheDir = mContext.getCacheDir();
        File appDataDir = new File(cacheDir, "appdata");
        File certsDir = new File(appDataDir, "certs");

        try {
            String[] certAssets = mContext.getAssets().list("certs");
            for (String next : certAssets) {
                copyAsset("certs/" + next, new File(certsDir, next), false);
            }
        } catch (IOException e) {
            Log.e(TAG, "Cannot copy certs to cache directory. Error: " + e.getMessage());
        }


        AlexaConfiguration.TemplateRuntimeTimeout[] timeoutList = new AlexaConfiguration.TemplateRuntimeTimeout[]{
                new AlexaConfiguration.TemplateRuntimeTimeout(AlexaConfiguration.TemplateRuntimeTimeoutType.DISPLAY_CARD_TTS_FINISHED_TIMEOUT, 8000),
                new AlexaConfiguration.TemplateRuntimeTimeout(AlexaConfiguration.TemplateRuntimeTimeoutType.DISPLAY_CARD_AUDIO_PLAYBACK_FINISHED_TIMEOUT, 8000),
                new AlexaConfiguration.TemplateRuntimeTimeout(AlexaConfiguration.TemplateRuntimeTimeoutType.DISPLAY_CARD_AUDIO_PLAYBACK_STOPPED_PAUSED_TIMEOUT, 1800000)
        };

        EngineConfiguration [] configurations =  new EngineConfiguration[]{
                //AlexaConfiguration.createCurlConfig( certsDir.getPath(), "wlan0" ), Uncomment this line to specify the interface name to use by AVS.
                AlexaConfiguration.createCurlConfig(certsDir.getPath()),
                AlexaConfiguration.createDeviceInfoConfig(productDsn, clientId, productId),
                AlexaConfiguration.createMiscStorageConfig(appDataDir.getPath() + "/miscStorage.sqlite"),
                AlexaConfiguration.createCertifiedSenderConfig(appDataDir.getPath() + "/certifiedSender.sqlite"),
                AlexaConfiguration.createAlertsConfig(appDataDir.getPath() + "/alerts.sqlite"),
                AlexaConfiguration.createSettingsConfig(appDataDir.getPath() + "/settings.sqlite"),
                AlexaConfiguration.createNotificationsConfig(appDataDir.getPath() + "/notifications.sqlite"),
                StorageConfiguration.createLocalStorageConfig(appDataDir.getPath() + "/localStorage.sqlite"),
                // AlexaConfiguration.createDeviceSettingsConfig(appDataDir.getPath() + "/deviceSettings.sqlite"),
                LoggerConfiguration.createSyslogSinkConfig("syslog", Logger.Level.VERBOSE),

                AlexaCommsConfiguration.createCommsConfig(certsDir.getPath()),
                //AlexaConfiguration.createTemplateRuntimeTimeoutConfig( timeoutList )
                // Example Vehicle Config
                VehicleConfiguration.createVehicleInfoConfig(new VehicleConfiguration.VehicleProperty[]{
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.MAKE, "Amazon"),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.MODEL, "AmazonCarOne"),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.TRIM, "Advance"),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.YEAR, "2025"),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.GEOGRAPHY, "US"),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.VERSION, String.format(
                                "Vehicle Software Version 1.0 (Auto SDK Version %s)", mEngine.getProperty(CoreProperties.VERSION))),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.OPERATING_SYSTEM, "Android 8.1 Oreo API Level 26"),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.HARDWARE_ARCH, "Armv8a"),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.LANGUAGE, "en-US"),
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.MICROPHONE, "Single, roof mounted"),
                        // If this list is left blank, it will be fetched by the engine using amazon default endpoint
                        new VehicleConfiguration.VehicleProperty(VehicleConfiguration.VehiclePropertyType.COUNTRY_LIST, "US,GB,IE,CA,DE,AT,IN,JP,AU,NZ,FR")
                }),
                LocalVoiceControlConfiguration.createIPCConfig(appDataDir.getPath(), LocalVoiceControlConfiguration.SocketPermission.ALL, appDataDir.getPath(), LocalVoiceControlConfiguration.SocketPermission.ALL, "127.0.0.1", appDataDir.getPath()),
                //AlexaConfiguration.createTemplateRuntimeTimeoutConfig( timeoutList ),
                LocalSkillServiceConfiguration.createLocalSkillServiceConfig(appDataDir.getPath() + "/LSS.socket", appDataDir.getPath() + "/ER.socket"),
                CarControlConfiguration.createCarControlConfig(appDataDir.getPath() + "/ApplianceDB.sqlite")
        };

        return configurations;

    }

    private void sentMediaCommand(String str) {
        switch (str) {
            case "Previous":
                mPlaybackController.previousButtonPressed();
                break;
            case "play":
                mPlaybackController.playButtonPressed();
                break;
            case "pause":
                mPlaybackController.pauseButtonPressed();
                break;
            case "next":
                mPlaybackController.nextButtonPressed();
                break;
            case "skip forward":
                mPlaybackController.skipForwardButtonPressed();
                break;
            case "skip back":
                mPlaybackController.skipBackwardButtonPressed();
                break;
            default:
                break;
        }
    }

    private void copyAsset(String assetPath, File destFile, boolean force) {
        if (!destFile.exists() || force) {
            if (destFile.getParentFile().exists() || destFile.getParentFile().mkdirs()) {
                // Copy the asset to the dest path
                try (
                        InputStream is = mContext.getAssets().open(assetPath);
                        OutputStream os = new FileOutputStream(destFile)
                ) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        os.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                }
            } else {
                Log.d(TAG, "Could not create cache directory: "
                        + destFile.getParentFile());
            }
        } else {
            Log.d(TAG, String.format("Skipping existing file in cache: %s to: %s",
                    assetPath, destFile));
        }
    }


    // Alexa Auth 相关 -->begin

    public void initAuthProvider() {
        mAuthProvider.onInitialize();
    }

    public void onResume() {
        if (mAuthProvider != null) mAuthProvider.onResume();
    }


    public void login() {
        mAuthProvider.login();
    }

    public void logout() {
        mAuthProvider.logout();
        SharedPreferences.Editor editor = mAuthSP.edit();
        editor.putString(Constant.AlexaAuthConfig.SP_KEY_USER_NAME, "");
        editor.putString(Constant.AlexaAuthConfig.SP_KEY_USER_ID, "");
        editor.putString(Constant.AlexaAuthConfig.SP_KEY_USER_EMAIL, "");
        editor.apply();
    }

    public String getUseremailInfo() {
        return mAuthSP.getString(Constant.AlexaAuthConfig.SP_KEY_USER_EMAIL, "");
    }

    // Alexa Auth 相关 -->end

    public MediaPlayerHandler getMediaPlayer() {
        return mPlaybackController.getMediaPlayer();
    }

    public boolean isMediaPlayerPlaying() {
        if (mAudioPlayerHandler != null) {
            return mAudioPlayerHandler.isPlaying();
        }
        return false;
    }


    public void clearTemplateInNewActivity() {
        if (mTemplateRuntime != null) {
            mTemplateRuntime.clearTemplate();
        }
    }

    public void clearPlayerInfoTimeOff() {
        Log.i(TAG, "clear Player Info Time Off");

        mContext.stopService(mediaPlayerServiceIntent);
        if (mClearPlayInfoListener != null) {
            mClearPlayInfoListener.onClearTemplate();
        }
        mCurrentAudioItemId = "";
    }


    public AlexaClient.ConnectionStatus getAlexaConnectionStatus() {
        return mAlexaClient.getConnectionStatus();
    }


    private void initPlayerInfoTimer() {
        playerInfoTimer = new CountDownTimer(CLEAR_PLAYER_INFO_WAIT_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                clearPlayerInfoTimeOff();
            }
        };
    }


    public String getRenderPlayerJson() {
        return mRenderPlayerJson;
    }

    public void setClearTemplateListener(ClearTemplateListener clearTemplateListener) {
        mTemplateRuntime.setClearTemplateListener(clearTemplateListener);
    }

    public void setClearPlayerInfoListener(ClearTemplateListener clearTemplateListener) {
        mClearPlayInfoListener = clearTemplateListener;
    }

    /* connectionStatusChanged callack*/
    private ClientConnectStateListener mClientConnectStateListener;

    public void setClientConnectStateChange(ClientConnectStateListener listener) {
        mClientConnectStateListener = listener;
    }

    public void setStateChangeListener(StateChangeListener listener) {
        mStateChangeListener = listener;
    }

}

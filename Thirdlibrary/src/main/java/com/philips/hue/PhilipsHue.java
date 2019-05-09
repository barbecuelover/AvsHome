package com.philips.hue;

import android.content.Context;
import android.util.Log;

import com.philips.lighting.hue.sdk.wrapper.HueLog;
import com.philips.lighting.hue.sdk.wrapper.Persistence;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeConnection;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeConnectionCallback;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeConnectionType;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeStateUpdatedCallback;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeStateUpdatedEvent;
import com.philips.lighting.hue.sdk.wrapper.connection.ConnectionEvent;
import com.philips.lighting.hue.sdk.wrapper.connection.FoundDevicesCallback;
import com.philips.lighting.hue.sdk.wrapper.discovery.BridgeDiscovery;
import com.philips.lighting.hue.sdk.wrapper.discovery.BridgeDiscoveryCallback;
import com.philips.lighting.hue.sdk.wrapper.discovery.BridgeDiscoveryResult;
import com.philips.lighting.hue.sdk.wrapper.domain.Bridge;
import com.philips.lighting.hue.sdk.wrapper.domain.BridgeBuilder;
import com.philips.lighting.hue.sdk.wrapper.domain.HueError;
import com.philips.lighting.hue.sdk.wrapper.domain.ReturnCode;
import com.philips.lighting.hue.sdk.wrapper.domain.device.Device;
import com.philips.lighting.hue.sdk.wrapper.domain.device.light.LightPoint;
import com.philips.lighting.hue.sdk.wrapper.knownbridges.KnownBridge;
import com.philips.lighting.hue.sdk.wrapper.knownbridges.KnownBridges;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 作者：RedKeyset on 2018/11/14 18:53
 * 邮箱：redkeyset@aliyun.com
 */
public class PhilipsHue {
    private static final String TAG = "PhilipsHue";
    private final Context mContext;
    private static volatile PhilipsHue instance = null;
    private BridgeDiscovery bridgeDiscovery;
    private List<BridgeDiscoveryResult> bridgeDiscoveryResults;
    public Bridge bridge;

    static {
        // Load the huesdk native library before calling any SDK method
        System.loadLibrary("huesdk");
    }

    PhilipsHue(Context context) {
        mContext = context;
        // Configure the storage location and log level for the Hue SDK
        Persistence.setStorageLocation(mContext.getFilesDir().getAbsolutePath(), "HueQuickStart");
        HueLog.setConsoleLogLevel(HueLog.LogLevel.INFO);
    }

    /**
     * 发现并连接桥
     */
    public void discoveryAndConnectBridge(IPhilipsHueInfoListener philipsHueInfo) {
        String bridgeIp = getLastUsedBridgeIp();
        if (bridgeIp == null) {
            startBridgeDiscovery(philipsHueInfo);
        } else {
            connectToBridge(bridgeIp, new IPhilipsConnectToBridgeListener() {
                @Override
                public void BridgeConnectionEventLinsing(BridgeConnection bridgeConnection, ConnectionEvent connectionEvent) {

                }

                @Override
                public void ReturnCodeLinsing(ReturnCode returnCode) {

                }

                @Override
                public void BridgeStateLinsing(Bridge bridge, BridgeStateUpdatedEvent bridgeStateUpdatedEvent) {

                }
            });
        }
    }

    /**
     * 获取最后连接过 桥的信息
     *
     * @return ip地址
     */
    public String getLastUsedBridgeIp() {
        List<KnownBridge> bridges = KnownBridges.getAll();

        if (bridges.isEmpty()) {
            return null;
        }

        return Collections.max(bridges, new Comparator<KnownBridge>() {
            @Override
            public int compare(KnownBridge a, KnownBridge b) {
                return a.getLastConnected().compareTo(b.getLastConnected());
            }
        }).getIpAddress();
    }

    private IPhilipsConnectToBridgeListener connectToBridgeListener;

    /**
     * 已知桥 IP 连接桥
     *
     * @param bridgeIp
     */
    public void connectToBridge(String bridgeIp, IPhilipsConnectToBridgeListener connectToBridgeListener) {
        this.connectToBridgeListener = connectToBridgeListener;
        stopBridgeDiscovery();
        disconnectFromBridge();

        setBridgeBuilder(bridgeIp);

        ReturnCode returnCode = bridge.connect();
        connectToBridgeListener.ReturnCodeLinsing(returnCode);

        Log.v(TAG, "Bridge IP: " + bridgeIp);
        Log.v(TAG, "Connecting to bridge...");
    }

    private void setBridgeBuilder(String bridgeIp) {
        bridge = new BridgeBuilder("app name", "device name")
                .setIpAddress(bridgeIp)
                .setConnectionType(BridgeConnectionType.LOCAL)
                .setBridgeConnectionCallback(bridgeConnectionCallback)
                .addBridgeStateUpdatedCallback(bridgeStateUpdatedCallback)
                .build();
    }

    public void stopBridgeDiscovery() {
        if (bridgeDiscovery != null) {
            bridgeDiscovery.stop();
            bridgeDiscovery = null;
        }
    }

    private void disconnectFromBridge() {
        if (bridge != null) {
            bridge.disconnect();
            bridge = null;
        }
    }

    /**
     * Start the bridge discovery search
     */
    public void startBridgeDiscovery(IPhilipsHueInfoListener iPhilipsHueInfoListener) {
        this.iPhilipsHueInfoListener = iPhilipsHueInfoListener;
        disconnectFromBridge();
        bridgeDiscovery = new BridgeDiscovery();
        // ALL Include [UPNP, IPSCAN, NUPNP] but in some nets UPNP and NUPNP is not working properly
        bridgeDiscovery.search(BridgeDiscovery.BridgeDiscoveryOption.ALL, bridgeDiscoveryCallback);
        Log.v(TAG, "Scanning the network for hue bridges...");
    }

    private BridgeConnectionCallback bridgeConnectionCallback = new BridgeConnectionCallback() {
        @Override
        public void onConnectionEvent(BridgeConnection bridgeConnection, ConnectionEvent connectionEvent) {
            Log.i(TAG, "Connection event: " + connectionEvent);
            if (connectToBridgeListener != null) {
                connectToBridgeListener.BridgeConnectionEventLinsing(bridgeConnection, connectionEvent);
            }

        }

        @Override
        public void onConnectionError(BridgeConnection bridgeConnection, List<HueError> list) {
            for (HueError error : list) {
                Log.e(TAG, "Connection error: " + error.toString());
            }
        }
    };

    private BridgeStateUpdatedCallback bridgeStateUpdatedCallback = new BridgeStateUpdatedCallback() {
        @Override
        public void onBridgeStateUpdated(Bridge bridge, BridgeStateUpdatedEvent bridgeStateUpdatedEvent) {
            Log.i(TAG, "Bridge state updated event: " + bridgeStateUpdatedEvent);
            if (connectToBridgeListener != null) {
                connectToBridgeListener.BridgeStateLinsing(bridge, bridgeStateUpdatedEvent);
            }
        }
    };


    public IPhilipsHueInfoListener iPhilipsHueInfoListener;
    /**
     * The callback that receives the results of the bridge discovery
     */
    private BridgeDiscoveryCallback bridgeDiscoveryCallback = new BridgeDiscoveryCallback() {
        @Override
        public void onFinished(final List<BridgeDiscoveryResult> results, final ReturnCode returnCode) {
            // Set to null to prevent stopBridgeDiscovery from stopping it
            // results List Data
            if (returnCode == ReturnCode.SUCCESS) {
                bridgeDiscoveryResults = results;
                iPhilipsHueInfoListener.BridgeDiscoveryLinsing(bridgeDiscoveryResults);
            } else if (returnCode == ReturnCode.STOPPED) {
                Log.i(TAG, "Bridge discovery stopped.");
            } else {
                Log.v(TAG, "Error doing bridge discovery:");
            }

            bridgeDiscovery = null;
        }
    };

    private IPhilipsHueLightsListener hueLightsListener;

    public void searchHueLights(IPhilipsHueLightsListener lightsListener) {
        this.hueLightsListener = lightsListener;
        bridge.findNewDevices(BridgeConnectionType.LOCAL, new FoundDevicesCallback() {
            @Override
            public void onDevicesFound(Bridge bridge, List<Device> list, List<HueError> list1) {
                hueLightsListener.LightsSearchFoundingLinsing(bridge, list, list1);
            }

            @Override
            public void onDeviceSearchFinished(Bridge bridge, List<HueError> list) {
                List<LightPoint> lights = bridge.getBridgeState().getLights();
                hueLightsListener.LightsSearchFinishedLinsing(lights, list);
            }
        });

    }

    public static PhilipsHue getInstance(Context context) {
        if (instance == null) {
            synchronized (PhilipsHue.class) {
                if (instance == null) {
                    instance = new PhilipsHue(context);
                }
            }
        }
        return instance;
    }
}

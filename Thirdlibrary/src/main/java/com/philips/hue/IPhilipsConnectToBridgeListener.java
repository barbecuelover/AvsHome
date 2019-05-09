package com.philips.hue;

import com.philips.lighting.hue.sdk.wrapper.connection.BridgeConnection;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeStateUpdatedEvent;
import com.philips.lighting.hue.sdk.wrapper.connection.ConnectionEvent;
import com.philips.lighting.hue.sdk.wrapper.domain.Bridge;
import com.philips.lighting.hue.sdk.wrapper.domain.ReturnCode;

/**
 * 作者：RedKeyset on 2018/11/15 16:46
 * 邮箱：redkeyset@aliyun.com
 */
public interface IPhilipsConnectToBridgeListener {
    void BridgeConnectionEventLinsing(BridgeConnection bridgeConnection, ConnectionEvent connectionEvent);
    void ReturnCodeLinsing(ReturnCode returnCode);
    void BridgeStateLinsing(Bridge bridge, BridgeStateUpdatedEvent bridgeStateUpdatedEvent);
}

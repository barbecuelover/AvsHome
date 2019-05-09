package com.philips.hue;

import com.philips.lighting.hue.sdk.wrapper.discovery.BridgeDiscoveryResult;
import com.philips.lighting.hue.sdk.wrapper.domain.Bridge;
import com.philips.lighting.hue.sdk.wrapper.domain.HueError;
import com.philips.lighting.hue.sdk.wrapper.domain.device.Device;
import com.philips.lighting.hue.sdk.wrapper.domain.device.light.LightPoint;

import java.util.List;

/**
 * 作者：RedKeyset on 2018/11/15 11:19
 * 邮箱：redkeyset@aliyun.com
 */
public interface IPhilipsHueInfoListener {
    void BridgeDiscoveryLinsing(List<BridgeDiscoveryResult> results);
}

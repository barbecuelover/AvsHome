package com.ecobee.event;

import com.ecobee.bean.GetTemperatureInfoBean; /**
 * 作者：RedKeyset on 2018/12/14 17:18
 * 邮箱：redkeyset@aliyun.com
 */
public class SentTemperatureInfoMessage {

    private GetTemperatureInfoBean temperatureInfoBean;
    private int code;

    public SentTemperatureInfoMessage(GetTemperatureInfoBean temperatureInfoBean, int code) {
        this.temperatureInfoBean = temperatureInfoBean;
        this.code = code;
    }

    public GetTemperatureInfoBean getTemperatureInfoBean() {
        return temperatureInfoBean;
    }

    public void setTemperatureInfoBean(GetTemperatureInfoBean temperatureInfoBean) {
        this.temperatureInfoBean = temperatureInfoBean;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

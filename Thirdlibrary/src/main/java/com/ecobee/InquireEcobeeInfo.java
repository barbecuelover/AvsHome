package com.ecobee;

import com.ecobee.bean.CodeMessageBean;
import com.ecobee.bean.GetTemperatureInfoBean;
import com.ecobee.event.SentTemperatureInfoMessage;
import com.ecobee.utils.EcoBeeHttpUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class InquireEcobeeInfo {
    public InquireEcobeeInfo() {
    }

    /**
     * 获取 Sensor 相关信息
     */
    public void getRemoeSensorDevices(final String refreshToken, final String accessToken) {
        final Request request = new Request.Builder()
                .url(ConstantUrls.Authorization.getCurrentTemperature)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("charset", "UTF-8")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        EcoBeeHttpUtils.getInstance().getHttpObject().newCall(request).enqueue(new Callback() {

            private GetTemperatureInfoBean temperatureInfoBean;

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String JsonString = response.body().string();
                    Gson gsonCode = new Gson();
                    CodeMessageBean codeMessageBean = gsonCode.fromJson(JsonString, CodeMessageBean.class);
                    int code = codeMessageBean.getStatus().getCode();
                    if (code == 0) {
                        Gson gson = new Gson();
                        temperatureInfoBean = gson.fromJson(JsonString, GetTemperatureInfoBean.class);
                    } else if (code == 14) {
                        Oauth2Certification.RefreshAccessToken(refreshToken);
                    }
                    EventBus.getDefault().post(new SentTemperatureInfoMessage(temperatureInfoBean, code));
                } catch (IOException e) {

                }
            }
        });
    }
}

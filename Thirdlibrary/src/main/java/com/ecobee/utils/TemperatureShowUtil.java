package com.ecobee.utils;

import android.graphics.Color;
import android.os.Build;

import java.util.Optional;

import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**
 * 作者：RedKeyset on 2018/12/14 11:31
 * 邮箱：redkeyset@aliyun.com
 */
public class TemperatureShowUtil {
    // 是否转换成 摄氏度，默认是华氏度
    private static boolean isCelsius = false;

    /**
     * 将华氏度转换成摄氏度
     * 保留 两位1位小数
     *
     * @param fahrenheit
     * @return
     */
    public static String showTemperature(String fahrenheit) {
        Integer strNum = StringToInt(fahrenheit);
        // 如果 不能匹配数值，返回null ，此处return
        if (String.valueOf(strNum).equals("null")){
            return "";
        }else if (StringUtil.isEmpty(String.valueOf(strNum))) {
            return "";
        } else {
            if (isCelsius) {
                if (strNum != null) {
                    double value = (strNum - 320) * 5 / 90;
                    return String.format("%.1f", value).toString() + "℃";
                } else {
                    return "";
                }
            }
            return (strNum / 10) + "℉";
        }
    }

    /**
     * 利用正则验证 String是否是 数值
     * 不能匹配则返回null
     *
     * @param str
     * @return
     */
    public static Integer StringToInt(String str) {
        if (str == null) {
            return null;
        } else {
            String strNum = str.trim();
            if (strNum.matches("\\d+")) {
                return Integer.parseInt(strNum);
            } else {
                return null;
            }
        }
    }

    public static int showTemperColor(String cuTemperature) {
        if (StringUtil.isEmpty(cuTemperature)) {
            return Color.argb(250, 240, 255, 255);
        } else {
            int temperaNum = Integer.parseInt(cuTemperature) / 10;
            if (temperaNum <= 20) {
                return Color.argb(250, 240, 255, 255);
            } else if (temperaNum > 20 && temperaNum <= 50) {
                return Color.argb(255, 0, 205, 205);
            } else if (temperaNum > 50 && temperaNum <= 68) {
                return Color.argb(255, 0, 205, 205);
            } else if (temperaNum > 68 && temperaNum <= 86) {
                return Color.argb(255, 238, 118, 0);
            } else if (temperaNum > 86 && temperaNum <= 100) {
                return Color.argb(255, 238, 64, 0);
            } else {
                return Color.argb(255, 238, 0, 0);
            }
        }
    }
}

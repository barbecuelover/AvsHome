package com.github.mob41.blapi;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：RedKeyset on 2018/9/25 02:36
 * 邮箱：redkeyset@aliyun.com
 */
public class LogUtils {
    public static Boolean isDebug = true;
    private static Boolean LogWarn = true;
    private static Boolean LogErr = true;

    protected static String getData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss:SSS");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static void LogDebug(String str) {
        if (isDebug){
            System.out.print("---时间：" + getData() + "----LogDebug内容：" + str);
        }
    }

    public static void LogDebug(String str1, Object str2) {
        if (isDebug){
            System.out.print("---时间：" + getData() + "----LogDebug内容：" + str1 + "----" + String.valueOf(str2));
        }
    }

    public static void LogWarn(String str) {
        if (LogWarn){
            System.out.print("---时间：" + getData() + "----LogWarn内容：" + str);
        }
    }

    public static void LogErr(String str) {
        if (LogErr){
            System.out.print("---时间：" + getData() + "----LogErr内容：" + str);
        }
    }

    public static void LogErr(String str1,Object str2) {
        if (LogErr){
            System.out.print("---时间：" + getData() + "----LogErr内容：" + str1 + "----" + String.valueOf(str2));
        }
    }
}

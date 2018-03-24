package com.ccy.lnb.en.utils;

import android.content.Context;
import android.os.Build;

public class Params {

    //判断sdk是否大约23
    public static boolean isSDK23() {

        //版本判断
        if (Build.VERSION.SDK_INT >= 23)
            return true;
        else
            return false;
    }


    //判断sdk是否大约23
    public static boolean isSDK18() {
        //版本判断
        if (Build.VERSION.SDK_INT >= 18)
            return true;
        else
            return false;
    }


    /**
     * 退出应用程序
     */
    public static void appExit(Context context) {
        try {
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid()); //获取PID
        } catch (Exception e) {
            LogUtil.e("APP退出异常:", e.getMessage());
        }
    }
}

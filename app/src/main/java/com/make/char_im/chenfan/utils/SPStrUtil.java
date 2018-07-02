package com.make.char_im.chenfan.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的key值
 * Created by chen on 2017/4/14.
 */

public class SPStrUtil {

    public static final String SETTING_INFO = "settingInfo";


    public static final String DISPLAY_DIRECTION = "displayDirection";//展示方向
    public static final String SWITCH_SPEED = "switchSpeed";//播放速度
    public static final String DEFAULT_MESS = "defaultMessage";//默认消息
    public static final String SWITCH_DEFAULT_MESS = "switchDefaultMessage";//默认消息开关
    public static final String DISPLAY_BRIGHT = "brightness";//显示的亮度
    public static final String SWITCH_AUTO_BRIGHT = "automaticAdjustmentBrightness";//自动调节亮度开关


    //蓝牙配置
    public static final String AUTO_CONNECT_BLUETOOTH = "自动连接蓝牙";//是否自动连接
    public static final String CONNECT_BLUETOOTH_NAME = "connectBluetoothName";//连接的蓝牙设备的名字
    public static final String CONNECT_BLUETOOTH_ADDRESS ="connectBluetoothAddress";//连接的蓝牙设备的address



    public static final String IS_HAVE_MY_PHRASE ="isHaveMyPhrase";//是否有我的短语



    // 设置相关信息
    /**
     * 保存String
     */
    public static void setStringPreferences(Context context, String key,
                                            String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 读取文件字符串
     */
    public static String getStringPreference(Context context, String key,
                                             String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * 保存int
     */
    public static void setIntPreference(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取int
     */
    public static int getIntPreference(Context context, String key,
                                       int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 保存bool
     *
     */
    public static void setBooleanPreference(Context context, String key,
                                            boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取bool
     */
    public static boolean getBooleanPreference(Context context, String key,
                                               boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SETTING_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

}

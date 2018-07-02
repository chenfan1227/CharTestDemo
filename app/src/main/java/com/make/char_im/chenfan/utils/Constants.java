package com.make.char_im.chenfan.utils;

/**
 * 常量
 * Created by chen on 2017/4/10.
 */

public class Constants {

    public static final String DISCONNECTED_BLUE_TOOTH = "DISCONNECTED";//蓝牙断开

    public static final String CONNECTED_BLUE_TOOTH = "CONNECTED";//蓝牙连接

    public static final String SWITCH_STATUS_BLUE_TOOTH = "SWITCHSTATUS";//蓝牙开关状态

    //蓝牙设备的sn
    public static String BluetoothSendSN = "00000000000000";//0：发送消息传的sn
    //是否通过蓝牙发送消息
    public static String isBluetoothSendMessage = "0";//0：是直接发送消息，1：是蓝牙发送消息

    public static String RESULT = "";//蓝牙发送消息返回结果
    public static String RESULT_OKL = "OKL";//蓝牙发送消息返回结果


    public static boolean IS_CONNECT_BLUETOOTH = false;//是否连接了蓝牙，默认没有

    public static int SEND_I_STATUS = 1;//蓝牙发送I指令


    public static final String IS_PERMISSION = "IS_PERMISSION";//第一次进来申请权限

    public static final String CAMERA = "android.permission.CAMERA";
    public static final String PHONE = "android.permission.CALL_PHONE";

    public static final String LOCATION1 = "android.permission.ACCESS_FINE_LOCATION";
    public static final String LOCATION2 = "android.permission.ACCESS_COARSE_LOCATION";

    public static final String LOOK_PHOTO = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final String SAY = "android.permission.RECORD_AUDIO";


    public static final String SAVE1 = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String SAVE2 = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String SAVE3 = "android.permission.WRITE_SETTINGS";


    public  static int  APP_DEFAULT=128; //app默认消息长度

}

package com.make.char_im.chenfan.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BluetoothUtil {


    //判断是否有该权限
    public static boolean getPermission(Context mc, String packageName) {
        PackageManager pm = mc.getPackageManager();
        return (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", packageName));
    }


    public static boolean getConntectBluetoothStatus() {
        if ("0".equals(Constants.isBluetoothSendMessage)) {
            return false;
        }
        return true;
    }

    public static String isUseBluetoothSet() {
        if ("0".equals(Constants.isBluetoothSendMessage)) {//1是通用设置是直接设置
            return "1";
        }
        return "0";//0是通用设置用蓝牙设置
    }


    public static String getConntectBuletoothSN() {
        if (Constants.BluetoothSendSN == null)
            return "00000000000000";
        return Constants.BluetoothSendSN;
    }




    public static boolean getMoblePermission(Context context, String... str) {
        if (!Params.isSDK23())
            return true;
        else
            return isHavePermission(context, str);
    }

    public static boolean isHavePermission(Context context, String... str) {
        for (int i = 0; i < str.length; i++) {
            int p1 = ContextCompat.checkSelfPermission(context, str[i]);
            if (p1 == PackageManager.PERMISSION_GRANTED) {
                return true;//有权限
            }
        }
        return false;//无权限
    }

    /**
     * 打开相机权限
     */
    public static boolean isCAMERA(Context mc) {
        return getMoblePermission(mc, Constants.CAMERA);
    }

    /**
     * 打电话
     */
    public static boolean isCALLPHONE(Context mc) {
        return getMoblePermission(mc, Constants.PHONE);
    }

    /**
     * 定位
     */
    public static boolean isLOCATION(Context mc) {
        return getMoblePermission(mc, Constants.LOCATION1, Constants.LOCATION2);
    }

    /**
     * 蓝牙扫描定位
     */
    public static boolean isBluetoothLOCATION(Context mc) {
        return getMoblePermission(mc, Constants.LOCATION2);
    }

    /**
     * 查看手机照片
     */
    public static boolean isLookPhoto(Context mc) {
        return getMoblePermission(mc, Constants.LOOK_PHOTO);
    }

    /**
     * 麦克风
     */
    public static boolean isSay(Context mc) {
        return getMoblePermission(mc, Constants.SAY);
    }


    /**
     * 存储空间
     */
    public static boolean isSaveAPP(Context mc) {
        return getMoblePermission(mc, Constants.SAVE1, Constants.SAVE2);
    }


    public static String DoubleToString(double money) {
        BigDecimal bd = new BigDecimal(String.valueOf(money));
        String m = bd.toPlainString();
        DecimalFormat decimalFormat;
        try {
            int index = m.indexOf(".");
            if (index == -1) {
                decimalFormat = new DecimalFormat("###0");//格式化设置
            } else {
                String b = m.substring(index + 1, m.length());
                if (m.endsWith(".0") || m.endsWith(".00") || m.endsWith(".000")) {
                    decimalFormat = new DecimalFormat("###0");//格式化设置
                } else {
                    int length = b.length();
                    if (length == 1) {
                        decimalFormat = new DecimalFormat("###0.0");//格式化设置
                    } else if (length == 2) {
                        decimalFormat = new DecimalFormat("###0.00");//格式化设置
                    } else {
                        decimalFormat = new DecimalFormat("###0.000");//格式化设置
                    }
                }
            }
            return decimalFormat.format(money);
        } catch (Exception e) {
            decimalFormat = new DecimalFormat("###0.000");//格式化设置
        }
        return decimalFormat.format(money);
    }

}

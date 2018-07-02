package com.make.char_im.chenfan.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 字符串处理及得到的工具类
 * Created by chen on 2016/12/5.
 */
public class StringUtil {

    /**
     * 字符串gbk编码处理
     * @param content --字符串
     * @return --字节长度
     */
    public static int gbkStr(String content){
        try {
            return content.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取Android唯一标识（唯一序列号）
     * @param context--
     * @return --返回唯一id
     */
    public static String getOnlyId(Context context){
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        String szI = TelephonyMgr.getDeviceId();
        return szI;
    }


}

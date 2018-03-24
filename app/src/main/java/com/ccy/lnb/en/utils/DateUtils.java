package com.ccy.lnb.en.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils extends android.text.format.DateUtils {
    public static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat defaultDateFormatM = new SimpleDateFormat("MM-dd HH:mm:ss");

    public static String getDateString(long time, DateFormat dateFormat) {
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    //得到当前时间
    public static String getCurrentDateString() {
        return getDateString(System.currentTimeMillis(), defaultDateFormat);
    }

    //得到当前时间(不包括年)
    public static String getCurrentTime() {
        return getDateString(System.currentTimeMillis(), defaultDateFormatM);
    }

}

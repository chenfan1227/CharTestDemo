package com.make.char_im.chenfan.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.make.char_im.chenfan.R;


/**
 * 自定义悬浮窗
 * Created by chen on 16/12/14.
 */

public class DiyToast {
    private static TextView toastRoot;
    private static Toast toast;
    private static LinearLayout toastLayout;

    private DiyToast() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;//设定是否显示悬浮窗，默认显示


    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, String message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }


    /**
     * 自定义短时间toast
     * @param context
     * @param text
     */
    public static void showDiyShort(Context context, String text) {
        if (null == toastRoot) {
            toastLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.toast_diy, null);
            toastRoot = (TextView) toastLayout.findViewById(R.id.toast_diy_tv);
        }
        if (null == toast) {
            toast = new Toast(context);
            toast.setView(toastLayout);
            toast.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(context, 55));
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toastRoot.setText(text);
        toast.show();
    }

    /**
     * 自定义长时间toast
     * @param context
     * @param text
     */
    public static void showDiyLong(Context context, String text) {
        if (null == toastRoot) {
            toastLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.toast_diy, null);
            toastRoot = (TextView) toastLayout.findViewById(R.id.toast_diy_tv);
        }
        if (null == toast) {
            toast = new Toast(context);
            toast.setView(toastLayout);
            toast.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(context, 55));
        }
        toast.setDuration(Toast.LENGTH_LONG);
        toastRoot.setText(text);
        toast.show();
    }
}

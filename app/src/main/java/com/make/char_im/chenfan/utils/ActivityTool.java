package com.make.char_im.chenfan.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.make.char_im.chenfan.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActivityTool {
    private static Map<String, WeakReference<View>> map;
    private static View.OnClickListener listener;

    static {
        map = new HashMap<>();
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
    }

    /**
     * add loading view to activity
     *
     * @param activity－－
     * @return －－
     */
    public static View showLoading(Context activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.loading, null);
        showLoading((Activity) activity, view, false);
        return view;
    }

    /**
     * add loading view to activity
     *
     * @param activity－－
     * @param layoutId－－
     * @return －－
     */
    public static View showLoading(Activity activity, int layoutId, boolean dimBackground) {
        View view = LayoutInflater.from(activity).inflate(layoutId, null);
        showLoading(activity, view, dimBackground);
        return view;
    }

    /**
     * add loading view to activity
     *
     * @param activity－－
     * @param view－－
     * @return －－
     */
    public static boolean showLoading(Activity activity, View view) {
        return showLoading(activity, view, true);
    }

    /**
     * add loading view to activity
     *
     * @param activity－－
     * @param view－－
     * @return －－
     */
    public static boolean showLoading(Activity activity, View view, boolean dimBackground) {
        View layout = packingView(activity, view, dimBackground, true);
        return showView(activity, layout, -1, -1, Gravity.CENTER);
    }

    /**
     * packing a view to framelayout
     *
     * @param activity－－
     * @param view－－
     * @param dimBackground－－
     * @return －－
     */
    private static View packingView(Activity activity, View view, boolean dimBackground, boolean interceptTouch) {
        FrameLayout layout = new FrameLayout(activity);
        FrameLayout.LayoutParams lps = new FrameLayout.LayoutParams(-2, -2);
        lps.gravity = Gravity.CENTER;
        layout.addView(view, lps);
        if (dimBackground) {
            layout.setBackgroundColor(0x88000000);
        }
        if (interceptTouch) {
            layout.setOnClickListener(listener);
        }
        return layout;
    }

    /**
     * add emptyView to activity
     *
     * @param activity－－
     * @param layoutId－－
     * @return －－
     */
    public static View showEmpty(Context activity, int layoutId) {
        View view = LayoutInflater.from(activity).inflate(layoutId, null);
        showEmpty((Activity) activity, view);
        return view;
    }

    /**
     * add emptyView to activity
     *
     * @param activity－－
     * @param view－－
     * @return －－
     */
    public static boolean showEmpty(Activity activity, View view) {
        return showView(activity, packingView(activity, view, false, false), -1, -1, Gravity.CENTER);
    }

    /**
     * add view to activity
     *
     * @param activity－－
     * @param view－－
     * @param width－－
     * @param height－－
     * @param gravity－－
     * @return －－
     */
    public static boolean showView(Activity activity, View view, int width, int height, int gravity) {
        dismiss(activity);
        //
        FrameLayout.LayoutParams lps = new FrameLayout.LayoutParams(width, height);
        lps.gravity = gravity;
        activity.addContentView(view, lps);
        // clear reference
        try {
            clearEmptyReference();
        } catch (Exception e) {

        }
        // save reference
        WeakReference<View> weakView = new WeakReference<View>(view);
        map.put(activity.getClass().getSimpleName(), weakView);
        //
        return true;
    }

    /**
     * clear reference
     */
    private static void clearEmptyReference() {
        Set<String> keys = map.keySet();
        for (String key : keys) {
            WeakReference<View> weakReference = map.get(key);
            if (null != weakReference && null == weakReference.get()) {
                map.remove(key);
            }
        }
    }

    /**
     * remove view from activity
     */
    public static boolean dismiss(Context activity) {
        WeakReference<View> weakView = map.remove(((Activity) activity).getClass().getSimpleName());
        if (null == weakView) return false;
        //
        View view = weakView.get();
        // view未被回收
        if (null != view) {
            View parent = (View) view.getParent();
            if (null != parent)
                try {
                    ViewGroup parentGroup = (ViewGroup) parent;
                    parentGroup.removeView(view);
                } catch (Exception e) {
                }
        }
        return true;
    }

    /**
     * set gravity and margin
     *
     * @param activity－－
     * @param gravity－－
     * @param leftMargin－－
     * @param topMargin－－
     * @param rightMargin－－
     * @param bottomMargin－－
     */
    public static void setGravityAndMargins(Activity activity, int gravity, int leftMargin,
                                            int topMargin, int rightMargin, int bottomMargin) {
        WeakReference<View> weakReference = map.get(activity.getClass().getSimpleName());
        if (null != weakReference) {
            View view = weakReference.get();
            if (null != view) {
                view = ((FrameLayout) view).getChildAt(0);
                FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) view.getLayoutParams();
                lps.gravity = gravity;
                lps.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                view.setLayoutParams(lps);
            }
        }
    }
}

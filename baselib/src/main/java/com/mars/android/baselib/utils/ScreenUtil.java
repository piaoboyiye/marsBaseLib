package com.mars.android.baselib.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {
    /**
     * 获取设备的显示
     *
     * @return 返回 设备的Display 对象
     */
    public static Display getDisplay(Context context) {
        Display display = null;
        final WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        display = windowManager.getDefaultDisplay();
        return display;
    }

    /**
     * 获取屏幕的宽度
     *
     * @return 返回 屏幕的宽度 出现异常情况返回-1
     */
    public static int getScreenWidth(Context context) {
        int width = -1;
        Display display = null;
        display = getDisplay(context);
        if (display != null) {
            DisplayMetrics metric = new DisplayMetrics();
            display.getMetrics(metric);
            width = metric.widthPixels;
        }

        return width;
    }

    /**
     * 获取屏幕的高度
     *
     * @return 返回 屏幕的高度 出现异常情况返回-1
     */
    public static int getScreenHeight(Context context) {
        int height = -1;
        Display display = null;
        display = getDisplay(context);
        if (display != null) {
            DisplayMetrics metric = new DisplayMetrics();
            display.getMetrics(metric);
            height = metric.heightPixels;
        }

        return height;
    }

    /**
     * 获取屏幕的密度
     *
     * @return 返回 屏幕的密度 出现异常情况返回-1
     */
    public float getScreenDensity(Context context) {
        float density = -1;
        Display display = null;
        display = getDisplay(context);
        if (display != null) {
            DisplayMetrics metric = new DisplayMetrics();
            display.getMetrics(metric);
            density = metric.density;
        }

        return density;
    }

    /**
     * 获取屏幕的密度
     *
     * @return 返回 屏幕的密度 出现异常情况返回-1
     */
    public float getScreenDensityDpi(Context context) {
        int densityDpi = -1;
        Display display = null;
        display = getDisplay(context);
        if (display != null) {
            DisplayMetrics metric = new DisplayMetrics();
            display.getMetrics(metric);
            densityDpi = metric.densityDpi;
        }

        return densityDpi;
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

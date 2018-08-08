package com.mars.android.baselib.utils;


import android.content.Context;

import com.mars.android.baselib.BaseApplication;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class UmengUtil {
    private static boolean isDebug = false;

    public static void onEvent(String key) {
        if (!isDebug) {
            MobclickAgent.onEvent(BaseApplication.mContext, key);
        }
    }

    public static void onResume(Context mContext) {
        if (!isDebug) {
            MobclickAgent.onResume(mContext);
        }
    }

    public static void onPause(Context mContext) {
        if (!isDebug) {
            MobclickAgent.onPause(mContext);
        }
    }

    public static void onPageStart(String key) {
        if (!isDebug) {
            MobclickAgent.onPageStart(key);
        }
    }

    public static void onPageEnd(String key) {
        if (!isDebug) {
            MobclickAgent.onPageEnd(key);
        }
    }

    public static void init() {
        UMConfigure.init(BaseApplication.mContext,BaseAppUtil.getAppMetaData("UMENG_APPKEY")
                , BaseAppUtil.getAppMetaData("UMENG_CHANNEL"), UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setScenarioType(BaseApplication.mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);
    }

    public static void setDebug() {
        isDebug = true;
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setCatchUncaughtExceptions(false);
    }
}

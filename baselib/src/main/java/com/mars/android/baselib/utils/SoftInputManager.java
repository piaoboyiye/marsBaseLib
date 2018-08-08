package com.mars.android.baselib.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.mars.android.baselib.BaseApplication;


/**
 * 软键盘管理器
 */
public class SoftInputManager {
    /**
     * 隱藏软键盘
     *
     * @param mContext
     */
    public static void hideSoftInput(Activity mContext) {
        try {
            ((InputMethodManager) BaseApplication.mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mContext.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            IMMLeaks.fixFocusedViewLeak(mContext.getApplication());
        } catch (Exception e) {
        }
    }

    /**
     * 显示软键盘
     *
     * @param mContext
     */
    public static void showSoftInput(Activity mContext) {
        try {
            ((InputMethodManager) BaseApplication.mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

}

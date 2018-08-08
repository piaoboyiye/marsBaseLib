package com.mars.android.baselib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.mars.android.baselib.BaseApplication;


public class ToastAlone extends Toast {

    private static Toast mToast = null;

    public ToastAlone(Context context) {
        super(context);
    }

    public static void show(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (null == mToast) {
            mToast = Toast.makeText(BaseApplication.mContext, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }

    public static void show(int textRid) {
        if (null == mToast) {
            mToast = Toast.makeText(BaseApplication.mContext, textRid, Toast.LENGTH_SHORT);
        }
        mToast.setText(textRid);
        mToast.show();
    }

}
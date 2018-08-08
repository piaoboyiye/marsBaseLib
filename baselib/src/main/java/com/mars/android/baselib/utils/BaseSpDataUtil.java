package com.mars.android.baselib.utils;


import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.mars.android.baselib.bean.LocationBean;
import com.mars.android.baselib.constant.BaseSpConstants;

public class BaseSpDataUtil {
    private static LocationBean locationBean = null;

    public static String getlocalJson(String tag) {
        try {
            String timeStr = SPUtil.getInstance().getString(tag);
            if (!TextUtils.isEmpty(timeStr)) {
                AES aes = new AES(AES.AES_KEY);
                return new String(aes.decrypt(Base64.decode(timeStr.getBytes(), Base64.NO_WRAP)));
            }
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return null;
    }

    public static void saveLocalJson(String tag, String jsonStr) {
        try {
            AES aes = new AES(AES.AES_KEY);
            String encodeStr = Base64.encodeToString(aes.encrypt(jsonStr.getBytes()), Base64.NO_WRAP);
            SPUtil.getInstance().putString(tag, encodeStr);
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
    }


    public static void saveLocationInfo(LocationBean bean) {
        locationBean = bean;
        saveLocalJson(BaseSpConstants.LOCATION_INFO, new Gson().toJson(bean));
    }

    public static LocationBean getLocationInfo() {
        if (locationBean == null) {
            String jsonStr = getlocalJson(BaseSpConstants.LOCATION_INFO);
            LocationBean bean = new Gson().fromJson(jsonStr, LocationBean.class);
            locationBean = bean;
        }
        return locationBean;
    }
}

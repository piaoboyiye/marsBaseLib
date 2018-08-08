package com.mars.android.baselib.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mars.android.baselib.BaseApplication;
import com.mars.android.baselib.bean.CommonCityBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CityUtil {
    private static List<CommonCityBean> totalCity = null;

    private static void initCityData() {
        try {
            InputStream is = BaseApplication.mContext.getAssets().open("cmn_citys.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            totalCity = new Gson().fromJson(stringBuffer.toString(), new TypeToken<List<CommonCityBean>>() {
            }.getType());
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
    }

    public static List<CommonCityBean> getProvinces() {
        if (totalCity == null) {
            initCityData();
        }
        List<CommonCityBean> datas = new ArrayList<>();
        for (CommonCityBean bean : totalCity) {
            if (bean.level_type == 1) {
                datas.add(bean);
            }
        }
        return datas;
    }

    public static List<CommonCityBean> getCitys(String provinceId) {
        if (totalCity == null) {
            initCityData();
        }
        List<CommonCityBean> datas = new ArrayList<>();
        for (CommonCityBean bean : totalCity) {
            if (provinceId.equals(bean.parent_id) && bean.level_type == 2) {
                datas.add(bean);
            }
        }
        return datas;
    }

    public static List<CommonCityBean> getCountys(String cityId) {
        if (totalCity == null) {
            initCityData();
        }
        List<CommonCityBean> datas = new ArrayList<>();
        for (CommonCityBean bean : totalCity) {
            if (cityId.equals(bean.parent_id) && bean.level_type == 3) {
                datas.add(bean);
            }
        }
        return datas;
    }
}

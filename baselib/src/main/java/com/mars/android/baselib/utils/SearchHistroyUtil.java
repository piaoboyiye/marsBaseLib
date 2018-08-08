package com.mars.android.baselib.utils;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mars.android.baselib.constant.BaseSpConstants;

import java.util.ArrayList;
import java.util.List;

public class SearchHistroyUtil {
    private static ArrayList<String> histroyList = null;

    public static ArrayList<String> getHistoryList() {
        if (histroyList == null) {
            String userJson = BaseSpDataUtil.getlocalJson(BaseSpConstants.SEARCH_HISTROY);
            if (!TextUtils.isEmpty(userJson)) {
                histroyList = new Gson().fromJson(userJson, new TypeToken<List<String>>() {
                }.getType());
            } else {
                histroyList = new ArrayList<>();
            }
        }
        return histroyList;
    }

    public static void cleanHistroy() {
        getHistoryList();
        histroyList.clear();
        saveSp();
    }

    private static void saveSp() {
        BaseSpDataUtil.saveLocalJson(BaseSpConstants.SEARCH_HISTROY, new Gson().toJson(histroyList));
    }

    public static void saveHistroy(String history) {
        getHistoryList();
        if (histroyList.size() == 10) {
            histroyList.remove(9);
        }
        int index = histroyList.indexOf(history);
        if (index != -1) {
            histroyList.remove(index);
        }
        histroyList.add(0, history);
        saveSp();
    }
}

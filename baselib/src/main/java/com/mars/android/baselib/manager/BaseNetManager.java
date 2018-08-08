package com.mars.android.baselib.manager;


import com.mars.android.baselib.net.RetrofitClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class BaseNetManager {

    protected static Retrofit retrofit;
    public static String host = "";

    protected static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = RetrofitClient.getInstance().getDefaultRetrofit(host);
        }
        return retrofit;
    }

    protected static Retrofit getCustomeRetrofit(OkHttpClient.Builder builder) {
        if (retrofit == null) {
            retrofit = RetrofitClient.getInstance().getCustomRetrofit(host, builder);
        }
        return retrofit;
    }

}

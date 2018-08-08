package com.mars.android.baselib;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import com.mars.android.baselib.net.RetrofitClient;
import com.mars.android.baselib.utils.LogUtil;
import com.mars.android.baselib.utils.UmengUtil;

public class BaseApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        UmengUtil.init();
    }

    public void showLog() {
        LogUtil.showLog = true;
        RetrofitClient.showHttpLog = true;
        UmengUtil.setDebug();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}

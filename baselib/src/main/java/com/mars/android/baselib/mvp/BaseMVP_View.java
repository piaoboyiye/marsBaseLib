package com.mars.android.baselib.mvp;

import android.content.Context;

public interface BaseMVP_View {
    void showMessage(String message);

    void startLoading();

    void stopLoading();

    void noData();

    void noNetWork();

    Context getActivityContext();
}

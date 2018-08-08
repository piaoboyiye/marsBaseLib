package com.mars.android.baselib.net;


import com.mars.android.baselib.utils.LogUtil;
import com.mars.android.baselib.utils.NetUtil;

import rx.Subscriber;

public abstract class NetSubscription<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        LogUtil.printeException(e);
        if (!NetUtil.isNetEnable()) {
            noNetWork();
        } else {
            String mess = "服务器异常";
            onFail(mess);
        }
        onFinish();
    }

    @Override
    public void onNext(T o) {
        onSuccess(o);
        onFinish();
    }

    @Override
    public abstract void onStart();

    public abstract void onSuccess(T o);

    public abstract void onFail(String message);

    public abstract void onFinish();

    public abstract void noNetWork();


}

package com.mars.android.baselib.mvp;


import com.mars.android.baselib.event.BaseEvent;
import com.mars.android.baselib.event.RxBus;
import com.mars.android.baselib.net.RXCallController;

import rx.Subscription;
import rx.functions.Action1;

public abstract class BaseMVP_Presenter<T extends BaseMVP_View> {
    protected T mView;
    protected RXCallController rxController;
    protected Subscription rxSubscriber;

    public BaseMVP_Presenter(T mView) {
        this.mView = mView;
        rxController = new RXCallController();
        registRxBus();
    }

    public RXCallController getRxController() {
        return rxController;
    }

    public void addRxCall(Subscription call) {
        rxController.addRxCall(call);
    }

    public void onDestroy() {
        unRegistRxBus();
        rxController.cleanRxCalls();
    }

    public void registRxBus() {
        if (rxSubscriber == null) {
            rxSubscriber = RxBus.getDefault().toObservable(BaseEvent.class).subscribe(new Action1<BaseEvent>() {
                @Override
                public void call(BaseEvent baseEvent) {
                    eventCallBack(baseEvent);
                }
            });
        }
    }

    public void unRegistRxBus() {
        if (rxSubscriber != null && !rxSubscriber.isUnsubscribed()) {
            rxSubscriber.unsubscribe();
            rxSubscriber = null;
        }
    }

    protected abstract void eventCallBack(BaseEvent baseEvent);

}

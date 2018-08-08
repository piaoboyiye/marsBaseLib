package com.mars.android.baselib.net;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class RXCallController {

    private List<Subscription> callList;

    public void addRxCall(Subscription call) {
        if (call == null) {
            return;
        }
        if (callList == null) {
            callList = new ArrayList<>();
        }
        callList.add(call);
    }

    public void cleanRxCalls() {
        if (callList != null) {
            for (Subscription call : callList) {
                if (call != null && !call.isUnsubscribed()) {
                    call.unsubscribe();
                }
            }
            callList.clear();
        }
    }
}

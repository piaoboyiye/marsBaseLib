package com.mars.android.baselib.widget;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;

public class SendCodeButton extends Button {
    private Handler mHandler;
    private Runnable mRunnale;
    private static final int MAX_TIME = 60;
    private int currTime = 0;
    private boolean handlerStart = false;

    public SendCodeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        setText("发送验证码");
        mHandler = new Handler();
        mRunnale = new Runnable() {
            @Override
            public void run() {
                setEnabled(false);
                setText("已发送 " + (MAX_TIME - currTime) + "秒");
                if (currTime++ <= MAX_TIME) {
                    if (mHandler != null) {
                        mHandler.postDelayed(mRunnale, 1000);
                    }
                } else {
                    currTime = 0;
                    stop();
                    setEnabled(true);
                    setText("发送验证码");
                }
            }
        };
    }

    public void start() {
        if (!handlerStart) {
            handlerStart = true;
            mHandler.post(mRunnale);
        }
    }

    public void stop() {
        if (handlerStart) {
            handlerStart = false;
            mHandler.removeCallbacks(mRunnale);
        }
    }
}

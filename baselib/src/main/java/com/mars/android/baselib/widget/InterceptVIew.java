package com.mars.android.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class InterceptVIew extends LinearLayout {


    public InterceptVIew(Context context) {
        this(context, null);
    }

    public InterceptVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
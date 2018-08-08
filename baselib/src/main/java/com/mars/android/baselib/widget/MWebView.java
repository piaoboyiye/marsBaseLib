package com.mars.android.baselib.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MWebView extends WebView {
    public MWebView(Context context) {
        super(context);
    }

    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.getScrollY() <= 0){
                    this.scrollTo(getScrollX(), 1);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

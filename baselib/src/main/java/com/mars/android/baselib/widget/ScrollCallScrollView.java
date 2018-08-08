package com.mars.android.baselib.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollCallScrollView extends ScrollView {
    private LoadMoreCallBack callBack;

    public void setLoadMoreCallBack(LoadMoreCallBack callBack) {
        this.callBack = callBack;
    }

    public ScrollCallScrollView(Context context) {
        this(context, null);
    }

    public ScrollCallScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callBack != null) {
            callBack.onScrollChange(l, t);
        }
    }

    public interface LoadMoreCallBack {
        void onScrollChange(int x, int y);
    }
}

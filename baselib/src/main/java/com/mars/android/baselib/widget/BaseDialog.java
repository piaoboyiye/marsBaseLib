package com.mars.android.baselib.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mars.android.baselib.R;

import butterknife.ButterKnife;


public abstract class BaseDialog extends Dialog {
    protected Context mContext;

    public BaseDialog(Context context) {
        this(context, false, R.style.BaseDialog);
    }

    public BaseDialog(Context context, boolean showSoft) {
        this(context, showSoft, R.style.BaseDialog);
    }

    public BaseDialog(Context context, boolean showSoft, int themeResId) {
        super(context, themeResId);
        mContext = context;
        if (showSoft) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        setContentView(getContentViewResId());
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initView();
        initData();
        setListner();
    }


    public void setGravity(int gravity) {
        getWindow().setGravity(gravity);
    }

    protected abstract int getContentViewResId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListner();


}

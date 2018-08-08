package com.mars.android.baselib.widget;

import android.app.Dialog;
import android.content.Context;

import com.mars.android.baselib.R;


public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.ProgressDialog);
        setContentView(R.layout.layout_loading_dialog);
        setCanceledOnTouchOutside(false);
    }
}

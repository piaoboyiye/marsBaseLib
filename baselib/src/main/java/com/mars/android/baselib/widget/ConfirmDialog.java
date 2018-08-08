package com.mars.android.baselib.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mars.android.baselib.R;


public class ConfirmDialog extends Dialog {

    TextView dialogTitle;
    Button btn_do, btn_cancle;
    private DialogCallBack callBack;

    public ConfirmDialog(Context context) {
        super(context, R.style.BaseDialog);
        setContentView(R.layout.layout_confirmdialog);
        dialogTitle = (TextView) findViewById(R.id.dialog_title);
        btn_do = (Button) findViewById(R.id.dialog_ok);
        btn_cancle = (Button) findViewById(R.id.dialog_cancle);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        btn_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (callBack != null) {
                    callBack.onOk();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (callBack != null) {
                    callBack.onCancle();
                }
            }
        });
    }

    public void setDialogCallBack(DialogCallBack callBack) {
        this.callBack = callBack;
    }

    public void setTitle(String title) {
        dialogTitle.setText(title);
    }

    public void setTitle(int titleRes) {
        dialogTitle.setText(titleRes);
    }

    public interface DialogCallBack {
        void onOk();

        void onCancle();
    }
}

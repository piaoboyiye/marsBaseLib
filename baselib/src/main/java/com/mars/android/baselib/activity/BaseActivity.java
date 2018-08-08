package com.mars.android.baselib.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mars.android.baselib.constant.BaseConstants;
import com.mars.android.baselib.mvp.BaseMVP_Presenter;
import com.mars.android.baselib.mvp.BaseMVP_View;
import com.mars.android.baselib.utils.BaseAppUtil;
import com.mars.android.baselib.utils.LogUtil;
import com.mars.android.baselib.utils.SoftInputManager;
import com.mars.android.baselib.utils.ToastAlone;
import com.mars.android.baselib.utils.UmengUtil;
import com.mars.android.baselib.widget.LoadingDialog;
import com.mars.android.baselib.widget.statusbarutil.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends BaseMVP_Presenter> extends FragmentActivity implements BaseMVP_View {
    protected Context mContext;
    protected T mPresenter;
    protected LoadingDialog loadingDialog;
    protected int statusBarColor = BaseConstants.base_statusbar_color;
    protected int statusBarAlpha = 0;
    protected boolean needSetStatusBar = true;
    protected boolean needBackExit = false;

    Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getContentViewResId());
        loadingDialog = new LoadingDialog(mContext);
        unbinder=  ButterKnife.bind(this);
        mPresenter = createPresenter();
        initView();
        initData();
        setListener();
        setStatusBarColor(statusBarColor, statusBarAlpha);
    }

    public void setStatusBarColor(int colorRes, int alpha) {
        if (needSetStatusBar) {
            statusBarColor = colorRes;
            statusBarAlpha = alpha;
            StatusBarUtil.setColor(this, getResources().getColor(statusBarColor), statusBarAlpha);
        }
    }

    public void needSetStatusBar(boolean needSetStatusBar) {
        this.needSetStatusBar = needSetStatusBar;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoftInputManager.hideSoftInput(this);
        UmengUtil.onPause(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtil.onResume(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        unbinder.unbind();
        stopLoading();
    }

    @Override
    public void noData() {

    }

    @Override
    public void noNetWork() {

    }

    @Override
    public void startLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (loadingDialog != null && !loadingDialog.isShowing()) {
                        loadingDialog.show();
                    }
                } catch (Exception e) {
                    LogUtil.printeException(e);
                }
            }
        });
    }

    @Override
    public void stopLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                } catch (Exception e) {
                    LogUtil.printeException(e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (needBackExit) {
            if (BaseAppUtil.exitApp()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void startActivity(Class clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(Class clazz, Bundle mBundle) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, clazz);
        if (mBundle != null) {
            mIntent.putExtras(mBundle);
        }
        startActivity(mIntent);
    }

    public void startActivityForResult(Class clazz, int requestCode) {
        startActivityForResult(clazz, null, requestCode);
    }

    public void startActivityForResult(Class clazz, Bundle mBundle, int requestCode) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, clazz);
        if (mBundle != null) {
            mIntent.putExtras(mBundle);
        }
        startActivityForResult(mIntent, requestCode);
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showMessage(String message) {
        ToastAlone.show(message);
    }

    protected abstract int getContentViewResId();

    protected abstract T createPresenter();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();
}

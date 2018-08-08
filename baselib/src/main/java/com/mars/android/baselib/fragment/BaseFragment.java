package com.mars.android.baselib.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mars.android.baselib.constant.BaseConstants;
import com.mars.android.baselib.mvp.BaseMVP_Presenter;
import com.mars.android.baselib.mvp.BaseMVP_View;
import com.mars.android.baselib.utils.ToastAlone;
import com.mars.android.baselib.widget.LoadingDialog;
import com.mars.android.baselib.widget.statusbarutil.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T extends BaseMVP_Presenter> extends Fragment implements BaseMVP_View {
    protected Context mContext;
    protected ViewGroup rootView;
    protected T mPresenter;
    protected LoadingDialog loadingDialog;
    protected int statusBarColor = BaseConstants.base_statusbar_color;
    protected boolean isFullScreen = false;
    protected int statusBarAlpha = 0;

    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(getRootViewResource(), container, false);
        unbinder=  ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = createPresenter();
        loadingDialog = new LoadingDialog(mContext);
        initView();
        initData();
        setListener();
        changeStatusBar();
    }

    //must call in initView
    public void setStatusBarColor(int colorRes, int alpha) {
        statusBarColor = colorRes;
        statusBarAlpha = alpha;
        StatusBarUtil.setColor(getActivity(), getResources().getColor(statusBarColor), statusBarAlpha);
    }

    //must call in initView
    public void setStatusBarFullScreen() {
        isFullScreen = true;
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onFragmentShow();
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
        startActivityForResult(new Intent(mContext, clazz), requestCode);
    }

    protected void onFragmentShow() {
        changeStatusBar();
    }

    private void changeStatusBar() {
        if (isFullScreen) {
            setStatusBarFullScreen();
        } else {
            setStatusBarColor(statusBarColor, statusBarAlpha);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        unbinder.unbind();
        stopLoading();
    }



    @Override
    public void startLoading() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void stopLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void noData() {

    }

    @Override
    public void noNetWork() {

    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void showMessage(String message) {
        ToastAlone.show(message);
    }

    protected abstract int getRootViewResource();

    protected abstract T createPresenter();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();

}

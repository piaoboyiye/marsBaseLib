package com.mars.android.baselib.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    protected Context mContext;

    public BaseViewHolder(LayoutInflater mInflater, ViewGroup parent, int resId) {
        super(mInflater.inflate(resId, parent, false));
        ButterKnife.bind(this, itemView);
        mContext = mInflater.getContext();
        initView();
    }

    protected abstract void initView();

    public abstract void setData(T data);
}

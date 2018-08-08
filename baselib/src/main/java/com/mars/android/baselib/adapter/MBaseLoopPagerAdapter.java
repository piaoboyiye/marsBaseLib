package com.mars.android.baselib.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class MBaseLoopPagerAdapter<T> extends PagerAdapter {
    protected Context mContext;
    protected List<T> data;
    protected LayoutInflater mInflater;

    public MBaseLoopPagerAdapter(Context context) {
        this.mContext = context;
        if (data == null) {
            data = new ArrayList<T>();
        }
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = getView(getRealPosition(position));
        container.addView(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return layout;
    }

    public abstract View getView(int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data.size() > 0 ? Integer.MAX_VALUE : 0;
    }

    public int getRealCount() {
        return data.size();
    }

    public int getRealPosition(int position) {
        int realCount = data.size();
        if (realCount == 0)
            return 0;
        return position % realCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setData(List<T> data) {
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        return data.get(position);
    }

    public List<T> getData() {
        return this.data;
    }

    public void addData(List<T> data) {
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        this.data.clear();
        notifyDataSetChanged();
    }

}

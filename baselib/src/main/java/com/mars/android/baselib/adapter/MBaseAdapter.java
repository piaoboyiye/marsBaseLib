package com.mars.android.baselib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.List;

public abstract class MBaseAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> data;
	protected LayoutInflater mInflater;

	public MBaseAdapter(Context context) {
		this.mContext = context;
		if (data == null) {
			data = new ArrayList<T>();
		}
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setData(List<T> data) {
		if (data != null) {
			this.data.clear();
			this.data.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addData(List<T> data) {
		if (data != null) {
			this.data.addAll(data);
			notifyDataSetChanged();
		}
	}

	public List<T> getData() {
		return this.data;
	}

	public void clearData() {
		this.data.clear();
		notifyDataSetChanged();
	}
	
}

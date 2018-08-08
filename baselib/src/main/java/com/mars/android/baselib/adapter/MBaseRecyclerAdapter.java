package com.mars.android.baselib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class MBaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context mContext;
    protected List<T> data;
    protected LayoutInflater mInflater;
    protected RecyclerViewCallBack callBack;


    public MBaseRecyclerAdapter(Context context) {
        this.mContext = context;
        if (data == null) {
            data = new ArrayList<T>();
        }
        mInflater = LayoutInflater.from(context);
    }

    public void setRecyclerViewCallBack(RecyclerViewCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setData(List<T> data) {
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        int position = getItemCount();
        addItem(position, item);
    }

    public void addItem(int position, T item) {
        this.data.add(position, item);
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        int position = this.data.indexOf(item);
        removeItem(position);
    }

    public void removeItem(int position) {
        this.data.remove(position);
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (holder != null && holder.itemView != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null) {
                        callBack.onItemClick(position);
                    }
                }
            });
        }
        onBindViewHolderEX(holder, position);
    }

    protected abstract void onBindViewHolderEX(VH holder, final int position);

    public boolean containsItem(T item) {
        return this.data.contains(item);
    }

    public T getItem(int position) {
        return data.get(position);
    }

    public List<T> getData() {
        return this.data;
    }

    public void clearData() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public interface RecyclerViewCallBack {
        void onItemClick(int position);
    }
}

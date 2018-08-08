package com.mars.android.baselib.widget;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mars.android.baselib.R;
import com.mars.android.baselib.adapter.MBasePagerAdapter;
import com.mars.android.baselib.adapter.MBaseRecyclerAdapter;
import com.mars.android.baselib.utils.LogUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class GridPageView extends LinearLayout {

    private ViewPager gridPage;
    private ViewPageSelect gridSelect;
    private TypePageAdapter mPageAdapter;

    private MBaseRecyclerAdapter itemAdapter;

    public GridPageView(Context context) {
        this(context, null);
    }

    public GridPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_grid_pageview, this, true);
        gridPage = (ViewPager) findViewById(R.id.ad_page);
        gridSelect = (ViewPageSelect) findViewById(R.id.ad_select);
        initData();
    }

    private void initData() {
        mPageAdapter = new TypePageAdapter(getContext());
        gridPage.setAdapter(mPageAdapter);

        gridPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                gridSelect.setPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class TypePageAdapter extends MBasePagerAdapter<List> {

        public TypePageAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position) {
            TypePageView view = new TypePageView(mContext);
            view.setData(getItem(position));
            return view;
        }
    }

    class TypePageView extends RecyclerView {
        MBaseRecyclerAdapter adapter = null;

        public TypePageView(Context context) {
            super(context);
            GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
            setLayoutManager(layoutManager);
            try {
                Class clazz = itemAdapter.getClass();
                Constructor c1 = clazz.getDeclaredConstructor((new Class[]{Context.class}));
                adapter = (MBaseRecyclerAdapter) c1.newInstance(getContext());
            } catch (Exception e) {
                LogUtil.printeException(e);
            }
            setAdapter(adapter);
        }

        public void setData(List data) {
            if (adapter != null) {
                adapter.setData(data);
            }
        }
    }

    public void setAdapter(MBaseRecyclerAdapter adAdapter) {
        this.itemAdapter = adAdapter;
    }

    public void showPageSelect(boolean show) {
        if (!show) {
            gridSelect.setVisibility(GONE);
        } else {
            gridSelect.setVisibility(VISIBLE);
        }
    }

    public void dataReady() {
        if (itemAdapter != null) {
            List totalData = itemAdapter.getData();
            if (totalData != null && totalData.size() > 0) {
                int size = totalData.size();
                int z = size / 8;
                int y = size % 8;
                if (y != 0) {
                    z++;
                }
                List datas = new ArrayList<>();
                for (int i = 0; i < z; i++) {
                    List temp = new ArrayList<>();
                    int end = (i + 1) * 8;
                    if (end > size) {
                        end = size;
                    }
                    temp.addAll(totalData.subList(i * 8, end));
                    datas.add(temp);
                }
                mPageAdapter.setData(datas);
                gridSelect.setSize(z);
            }
        }
    }
}

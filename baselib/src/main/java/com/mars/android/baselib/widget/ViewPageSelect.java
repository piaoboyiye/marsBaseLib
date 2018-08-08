package com.mars.android.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.mars.android.baselib.R;
import com.mars.android.baselib.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class ViewPageSelect extends LinearLayout {

    private int size;
    private int oldPositon = 0;
    private List<ImageView> adPoints = new ArrayList<>();
    private Context context;
    private int selectIconRes = R.drawable.yuan_selected;
    private int unSelectIconRes = R.drawable.yuan_unselected;

    public ViewPageSelect(Context context) {
        this(context, null);
    }

    public ViewPageSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setSize(int size) {
        this.size = size;
        init();

    }

    public void setSelectIconRes(int selectIconRes) {
        this.selectIconRes = selectIconRes;
    }

    public void setUnSelectIconRes(int unSelectIconRes) {
        this.unSelectIconRes = unSelectIconRes;
    }

    public void setPosition(int position) {
        try {
            adPoints.get(position).setImageResource(selectIconRes);
            adPoints.get(oldPositon).setImageResource(unSelectIconRes);
            oldPositon = position;
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
    }

    private void init() {
        oldPositon = 0;
        adPoints.clear();
        this.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView adView = new ImageView(context);
            if (i == 0) {
                adView.setImageResource(selectIconRes);
            } else {
                adView.setImageResource(unSelectIconRes);
            }
            adView.setScaleType(ScaleType.FIT_XY);
            adView.setMinimumHeight(dip2px(context, 8));
            adView.setMinimumWidth(dip2px(context, 8));
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0);
            lp.setMargins(5, 5, 5, 5);
            adView.setLayoutParams(lp);
            adPoints.add(adView);
            this.addView(adView);
        }
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
package com.mars.android.baselib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mars.android.baselib.R;

import java.util.ArrayList;


public class AutoBreakViewGroup extends ViewGroup {
    private static final String TAG = "AutoBreakViewGroup";
    private int mTextBackground;
    private int mTextColor;
    private int mTextSize;
    private int mTextViewPadding;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private Context mContext;
    private AutoBreakCallBack callBack;

    public AutoBreakViewGroup(Context context) {
        super(context);
        init(context);
    }

    public void setAutoBreakCallBack(AutoBreakCallBack callBack) {
        this.callBack = callBack;
    }

    public AutoBreakViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AutoBreakViewGroup, 0, 0);
            mVerticalSpacing = (int) a.getDimension(R.styleable.AutoBreakViewGroup_autobreakviewverticalSpacing, 0);
            mHorizontalSpacing = (int) a.getDimension(R.styleable.AutoBreakViewGroup_autobreakviewhorizontalSpacing, 0);
            mTextViewPadding = (int) a.getDimension(R.styleable.AutoBreakViewGroup_autobreakviewtextPadding, 0);
            mTextSize = (int) a.getDimension(R.styleable.AutoBreakViewGroup_autobreakviewtextSize, 0);
            mTextColor = a.getColor(R.styleable.AutoBreakViewGroup_autobreakviewtextColor, 0);
            mTextBackground = a.getResourceId(R.styleable.AutoBreakViewGroup_autobreakviewtextBackground, 0);
            a.recycle();
        }
    }

    public void setSpacing(int horizontalSpacing, int verticalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
        mVerticalSpacing = verticalSpacing;
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        Log.e(TAG, "modeWidth= " + modeWidth);
        Log.e(TAG, "sizeWidth= " + sizeWidth);
        Log.e(TAG, "modeHeight= " + modeHeight);
        Log.e(TAG, "sizeHeight= " + sizeHeight);

        //如果当前ViewGroup的宽高为wrap_content的情况
        int width = 0;//自己测量的 宽度
        int height = 0;//自己测量的高度
        //记录每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        //获取子view的个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            //子View占据的宽度
            int childWidth = child.getMeasuredWidth() + mVerticalSpacing;
            //子View占据的高度
            int childHeight = child.getMeasuredHeight() + mVerticalSpacing;
            //换行时候
            if (lineWidth + childWidth > sizeWidth) {
                //对比得到最大的宽度
                width = Math.max(width, lineWidth);
                //重置lineWidth
                lineWidth = childWidth;
                //记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {//不换行情况
                //叠加行宽
                lineWidth += childWidth;
                //得到最大行高
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //处理最后一个子View的情况
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        //高度wrap_content
        setMeasuredDimension(sizeWidth,
                modeHeight == MeasureSpec.AT_MOST ? height - mVerticalSpacing : sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int mTotalHeight = 0;
        int mTotalWidth = 0;
        int mTempHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            mTempHeight = (measureHeight > mTempHeight) ? measureHeight : mTempHeight;
            if ((measuredWidth + mTotalWidth + mHorizontalSpacing) > width) {
                mTotalWidth = 0;
                mTotalHeight += (mTempHeight + mVerticalSpacing);
                mTempHeight = 0;
                childView.layout(mTotalWidth, mTotalHeight, measuredWidth + mTotalWidth, mTotalHeight + measureHeight);
                mTotalWidth += (measuredWidth);
            } else {
                if (i == 0) {
                    childView.layout(mTotalWidth, mTotalHeight, measuredWidth + mTotalWidth, mTotalHeight + measureHeight);
                    mTotalWidth += (measuredWidth);
                } else {
                    childView.layout(mTotalWidth + mHorizontalSpacing, mTotalHeight, measuredWidth + mTotalWidth + mHorizontalSpacing, mTotalHeight + measureHeight);
                    mTotalWidth += (measuredWidth + mHorizontalSpacing);
                }
            }
        }
    }

    public void setData(ArrayList<String> mData) {
        for (int i = 0; i < mData.size(); i++) {
            TextView textView = new TextView(mContext);
            final String keyword = mData.get(i);
            textView.setText(keyword);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setTextColor(mTextColor);
            textView.setLayoutParams(new
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setBackgroundResource(mTextBackground);
            textView.setPadding(mTextViewPadding, mTextViewPadding, mTextViewPadding, mTextViewPadding);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null) {
                        callBack.onItemClick(keyword);
                    }
                }
            });
            addView(textView);
        }
    }

    public interface AutoBreakCallBack {
        void onItemClick(String keyword);
    }
}

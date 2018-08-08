package com.mars.android.baselib.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mars.android.baselib.R;


public class LoadMoreFooter extends LinearLayout implements BaseLoadMoreFooter {

    private TextView mText;
    private LinearLayout mContainer;
    private ProgressBar progressBar;
    private int currState = STATE_COMPLETE;

    public LoadMoreFooter(Context context) {
        this(context, null);
    }

    public LoadMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.refresh_listview_footer, this, false);
        addView(mContainer);

        mText = (TextView) findViewById(R.id.listview_foot_more);
        progressBar = (ProgressBar) findViewById(R.id.listview_foot_progress);
        setVisibility(View.GONE);
    }

    @Override
    public int getState() {
        return currState;
    }

    @Override
    public void setState(int state) {
        currState = state;
        switch (state) {
            case STATE_LOADING:
                mText.setText(getContext().getText(R.string.refresh_footer_loading));
                progressBar.setVisibility(VISIBLE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(getContext().getText(R.string.refresh_footer_loading_done));
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(getContext().getText(R.string.refresh_footer_nomore));
                progressBar.setVisibility(GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}

package com.mars.android.baselib.widget;


import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.mars.android.baselib.R;
import com.mars.android.baselib.adapter.MBaseLoopPagerAdapter;


public class LoopBannerView extends LinearLayout {

    private ViewPager adPage;
    private ViewPageSelect adSelect;

    private Handler adHandler;
    private Runnable adRunnable;
    private int adPosition = 0;
    private boolean handlerStart = false;
    private MBaseLoopPagerAdapter adAdapter;
    private int rate_time = 2000;

    public LoopBannerView(Context context) {
        this(context, null);
    }

    public LoopBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_loop_banner, this, true);
        adPage = (ViewPager) findViewById(R.id.ad_page);
        adSelect = (ViewPageSelect) findViewById(R.id.ad_select);
        initData();
    }

    private void initData() {
        if (adHandler == null) {
            adHandler = new Handler();
        }
        if (adRunnable == null) {
            adRunnable = new Runnable() {
                @Override
                public void run() {
                    if (adAdapter != null) {
                        adPage.setCurrentItem(adPosition++);
                        if (adHandler != null) {
                            adHandler.postDelayed(adRunnable, rate_time);
                        }
                    }
                }
            };
        }

        adPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (adAdapter != null) {
                    adPosition = position;
                    adSelect.setPosition(adAdapter.getRealPosition(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setAdapter(MBaseLoopPagerAdapter adAdapter) {
        this.adAdapter = adAdapter;
        adPage.setAdapter(adAdapter);
    }

    public void setRateTime(int millSecond) {
        this.rate_time = millSecond;
    }

    public void start() {
        if (adAdapter != null) {
            if (!handlerStart) {
                adSelect.setSize(adAdapter.getRealCount());
                handlerStart = true;
                adHandler.postDelayed(adRunnable, rate_time);
            }
        }
    }

    public void stop() {
        if (handlerStart) {
            handlerStart = false;
            adHandler.removeCallbacks(adRunnable);
        }
    }
}

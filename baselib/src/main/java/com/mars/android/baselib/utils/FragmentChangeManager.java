package com.mars.android.baselib.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;

/**
 * Fragment切换管理器
 */
public class FragmentChangeManager {

    private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    public TabInfo mLastTab;
    private int mContainerId;
    private FragmentManager fragmentManager;
    private Context context;
    private boolean needDetach;

    public FragmentChangeManager(Context context, FragmentManager fragmentManager, int containerId) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.mContainerId = containerId;
    }

    public void setNeedDetach(boolean isDetach) {
        this.needDetach = isDetach;
    }

    public void addFragment(String tag, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(tag, clss, args);
        mTabs.put(tag, info);
        Fragment old_fragment = fragmentManager.findFragmentByTag(tag);
        if (old_fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(old_fragment);
            ft.commitAllowingStateLoss();
        }
    }

    public Fragment onFragmentChanged(String tabId) {
        TabInfo newTab = mTabs.get(tabId);
        if (mLastTab != newTab) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    if (needDetach) {
                        ft.detach(mLastTab.fragment);
                    } else {
                        ft.hide(mLastTab.fragment);
                    }
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(context, newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    if (needDetach) {
                        ft.attach(newTab.fragment);
                    } else {
                        ft.show(newTab.fragment);
                    }
                }
            }

            mLastTab = newTab;
            ft.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();

        }
        return newTab.fragment;
    }

    public class TabInfo {
        public final String tag;
        public final Class<?> clss;
        public final Bundle args;
        public Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }
}

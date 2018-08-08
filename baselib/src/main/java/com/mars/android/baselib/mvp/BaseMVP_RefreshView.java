package com.mars.android.baselib.mvp;


import java.util.List;

public interface BaseMVP_RefreshView<T> extends BaseMVP_View {
    void refreshData(List<T> data);

    void loadMoreData(List<T> data);

    void noMoreData();
}

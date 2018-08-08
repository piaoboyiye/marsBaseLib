package com.mars.android.baselib.widget.refresh;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class XRecyclerView extends RecyclerView {
    private boolean isLoadingData = false;
    private boolean isNoMore = false;
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private WrapAdapter mWrapAdapter;
    private float mLastY = -1;
    private static final float DRAG_RATE = 2;
    private LoadingListener mLoadingListener;
    private RefreshHeader mRefreshHeader;
    private LoadMoreFooter mLoadMoreFooter;
    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    private static final int TYPE_REFRESH_HEADER = 10000;
    private static final int TYPE_FOOTER = 10001;
    private static final int HEADER_INIT_INDEX = 10002;
    private static List<Integer> sHeaderTypes = new ArrayList<>();
    private View mEmptyView;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mRefreshHeader = new RefreshHeader(getContext());
        mLoadMoreFooter = new LoadMoreFooter(getContext());
    }

    public void addHeaderView(View view) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(view);
    }

    //根据header的ViewType判断是哪个header
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    //判断一个type是否为HeaderType
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    //判断是否是XRecyclerView保留的itemViewType
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_FOOTER || sHeaderTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    public void refreshComplete() {
        mRefreshHeader.refreshComplete();
        setNoMore(false);
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        mLoadMoreFooter.setState(LoadMoreFooter.STATE_COMPLETE);
    }

    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        mLoadMoreFooter.setState(isNoMore ? LoadMoreFooter.STATE_NOMORE : LoadMoreFooter.STATE_COMPLETE);
    }

    public void reset() {
        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    public void refresh() {
        if (pullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(BaseRefreshHeader.STATE_REFRESHING);
            mLoadingListener.onRefresh();
        }
    }


    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled) {
            mLoadMoreFooter.setState(LoadMoreFooter.STATE_COMPLETE);
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && !isNoMore && mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true;
                mLoadMoreFooter.setState(LoadMoreFooter.STATE_LOADING);
                mLoadingListener.onLoadMore();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                }
                break;
            default:
                mLastY = -1; // reset
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public boolean isOnTop() {
        if (mRefreshHeader.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHeader(int position) {
        int countP = 0;
        if (pullRefreshEnabled) {
            countP = 1;
        }
        return position >= countP && position < mHeaderViews.size() + countP;
    }

    public boolean isFooter(int position) {
        if (loadingMoreEnabled) {
            return position == mWrapAdapter.getItemCount() - 1;
        } else {
            return false;
        }
    }

    public boolean isRefreshHeader(int position) {
        if (pullRefreshEnabled) {
            return position == 0;
        } else {
            return false;
        }
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getTopCount() {
        int countP = 0;
        if (pullRefreshEnabled) {
            countP = 1;
        }
        return getHeadersCount() + countP;
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (pullRefreshEnabled) {
                    emptyCount++;
                }
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    XRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    XRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }


    public class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        public WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder(mRefreshHeader);
            } else if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mLoadMoreFooter);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            int adjPosition = position - getTopCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        @Override
        public int getItemCount() {
            int countT = 0;
            if (pullRefreshEnabled) {
                countT++;
            }
            if (loadingMoreEnabled) {
                countT++;
            }
            if (adapter != null) {
                return getHeadersCount() + adapter.getItemCount() + countT;
            } else {
                return getHeadersCount() + countT;
            }
        }

        @Override
        public int getItemViewType(int position) {

            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                int countP = 0;
                if (pullRefreshEnabled) {
                    countP = 1;
                }
                position = position - countP;
                return sHeaderTypes.get(position);
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }
            int adjPosition = position - getTopCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition >= 0 || adjPosition < adapterCount) {
                    int adViewType = adapter.getItemViewType(adjPosition);
                    if (isReservedItemViewType(adViewType)) {
                        throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
                    } else {
                        return adapter.getItemViewType(adjPosition);
                    }
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return adapter.getItemId(position);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }


        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

}

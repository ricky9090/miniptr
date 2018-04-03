package me.rickyxe.miniptr;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 使用{@link SwipeRefreshLayout}实现的下拉刷新控件，实现较为轻量。<br>
 * 内部仅可包含一个RecyclerView子控件，并实现{@link AbsRecyclerAdapter}子类以填充数据。<br>
 * 加载更多的View需单独提供，不与内部RecyclerView或Adapter耦合。<br>
 */
public class MiniPtrLayout extends SwipeRefreshLayout {

    public int pageSize = 20;
    public boolean hasMore = false;
    public boolean loading = false;

    private RecyclerView mRecyclerView;

    private View loadMoreView;

    private OnLoadAndRefreshListener mOnLoadListener;

    public MiniPtrLayout(Context context) {
        super(context);
    }

    public MiniPtrLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        findRecyclerView();
        super.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!loading && mOnLoadListener != null) {
                    mOnLoadListener.onRefresh();
                }
            }
        });
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        throw new RuntimeException("USE PtrSwipeRefreshLayout::setOnLoadListener INSTEAD!!!");
    }

    private void findRecyclerView() {
        if (getChildCount() > 0) {
            View target = getChildAt(0);
            if (target instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) target;
                mRecyclerView.clearOnScrollListeners();  // 防止重复添加listener
                mRecyclerView.addOnScrollListener(new PtrOnScrollListener());
            }
        }
    }

    public OnLoadAndRefreshListener getOnLoadListener() {
        return mOnLoadListener;
    }

    /**
     * 设置加载监听器
     * @param ltn
     */
    public void setOnLoadListener(OnLoadAndRefreshListener ltn) {
        this.mOnLoadListener = ltn;
    }

    public View getLoadMoreView() {
        return loadMoreView;
    }

    /**
     * 设置加载更多对应自定义View
     * @param loadMoreView 用于显示加载更多文字/图片的View
     */
    public void setLoadMoreView(View loadMoreView) {
        this.loadMoreView = loadMoreView;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int size) {
        this.pageSize = size;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    /**
     * 设置是否还有更多数据，仅当有更多数据时，加载回调函数才会生效
     * @param more 是否有更多数据
     */
    public void setHasMore(boolean more) {
        this.hasMore = more;
    }

    private boolean isLoading() {
        return loading;
    }

    private void setLoading(boolean isLoading) {
        this.loading = isLoading;
    }

    /**
     * 加载完毕后，设置数据状态
     * @param emptyResult 数据是否为空
     * @param more 是否有更多数据需要加载
     */
    public void loadFinish(boolean emptyResult, boolean more) {
        loading = false;
        hasMore = more;
        if (loadMoreView != null) {
            loadMoreView.setVisibility(View.GONE);
        }
    }

    class PtrOnScrollListener extends RecyclerView.OnScrollListener {

        RecyclerView.LayoutManager layoutManager;
        int lastVisibleItem = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (recyclerView.getAdapter() == null) {
                lastVisibleItem = -1;
                return;
            }
            if (!loading
                    && (lastVisibleItem + 1) == mRecyclerView.getAdapter().getItemCount()) {
                if (loading || !hasMore) {
                    return;
                }

                if (mOnLoadListener != null) {
                    mOnLoadListener.onLoadMore();
                    if (loadMoreView != null) {
                        loadMoreView.setVisibility(View.VISIBLE);
                    }
                    loading = true;
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView.getAdapter() == null) {
                lastVisibleItem = -1;
                return;
            }
            if (layoutManager == null) {
                layoutManager = recyclerView.getLayoutManager();
            }

            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] spanItem = ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null);
                lastVisibleItem = spanItem[0];
            }
        }
    }

    /**
     * 列表的加载监听器
     */
    public interface OnLoadAndRefreshListener {
        /**
         * 加载更多回调函数
         */
        void onLoadMore();

        /**
         * 下拉刷新回调函数
         */
        void onRefresh();
    }


}

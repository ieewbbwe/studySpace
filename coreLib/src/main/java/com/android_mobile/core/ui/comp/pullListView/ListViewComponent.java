package com.android_mobile.core.ui.comp.pullListView;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.android_mobile.core.BasicActivity;
import com.android_mobile.core.R;
import com.android_mobile.core.base.BaseComponent;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

public class ListViewComponent extends BaseComponent {

    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;

    public ListViewComponent(BasicActivity activity, int resId) {
        super(activity, resId);
    }

    public ListViewComponent(BasicActivity activity, View v) {
        super(activity, v);
    }

    @Override
    public int onCreate() {
        return R.layout.comp_list_view;
    }

    @Override
    public void initComp() {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.comp_refresh_bga_layout);
        mRecycleView = (RecyclerView) findViewById(R.id.comp_data_rv);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(activity, true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRecycleView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        mRecycleView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecycleView.setAdapter(adapter);
    }

    public void setDivider(Drawable divider, int orientation) {
        mRecycleView.addItemDecoration(new DividerItemDecoration(divider, orientation));
    }

    public void addHeader(View headerView) {
        mRefreshLayout.setCustomHeaderView(headerView, true);
    }

    public void setListener(BGARefreshLayout.BGARefreshLayoutDelegate listener) {
        mRefreshLayout.setDelegate(listener);
    }

    public void endLoadMore() {
        mRefreshLayout.endLoadingMore();
    }

    public void endRefresh() {
        mRefreshLayout.endRefreshing();
    }

    public void doRefresh() {
        mRefreshLayout.beginRefreshing();
    }

    public RecyclerView getRecycleView() {
        return mRecycleView;
    }

    /**
     * 切换layoutManager
     * <p/>
     * 为了保证切换之后页面上还是停留在当前展示的位置，记录下切换之前的第一条展示位置，切换完成之后滚动到该位置
     * 另外切换之后必须要重新刷新下当前已经缓存的itemView，否则会出现布局错乱（俩种模式下的item布局不同），
     * RecyclerView提供了swapAdapter来进行切换adapter并清理老的itemView cache
     *
     * @param layoutManager
     */
    public void switchLayoutManager(final RecyclerView.LayoutManager layoutManager) {
        if (mRecycleView == null) {
            throw new IllegalStateException("RecycleView is null!");
        }
        if (layoutManager instanceof GridLayoutManager) {
            mRecycleView.addItemDecoration(new DividerGridItemDecoration(activity));
        } else {
            mRecycleView.removeItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.getLayoutManager().scrollToPosition(firstVisiblePosition);
    }

    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    public int getFirstVisiblePosition() {
        int position;
        RecyclerView.LayoutManager layoutManager = mRecycleView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager sgLayoutManager = (StaggeredGridLayoutManager) mRecycleView.getLayoutManager();
            int[] lastPositions = sgLayoutManager.findFirstVisibleItemPositions(new int[sgLayoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     *
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int position : positions) {
            minPosition = Math.min(minPosition, position);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    public int getLastVisiblePosition() {
        int position;
        RecyclerView.LayoutManager layoutManager = mRecycleView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager sgLayoutManager = (StaggeredGridLayoutManager) mRecycleView.getLayoutManager();
            int[] lastPositions = sgLayoutManager.findLastVisibleItemPositions(new int[sgLayoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = mRecycleView.getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

}
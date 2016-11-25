package com.webber.topnew.topnews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android_mobile.core.adapter.BasicRecycleAdapter;
import com.android_mobile.core.adapter.OnItemChildClickListener;
import com.android_mobile.core.adapter.OnItemChildLongClickListener;
import com.android_mobile.core.ui.EmptyLayout;
import com.android_mobile.core.ui.comp.pullListView.ILoadMoreViewListener;
import com.android_mobile.core.ui.comp.pullListView.ListViewComponent;
import com.webber.topnew.NFragment;
import com.webber.topnew.R;
import com.webber.topnew.adapter.TopNewsAdapter;
import com.webber.topnew.net.ApiConstants;
import com.webber.topnew.net.ApiFactory;
import com.webber.topnew.net.NetUtils;
import com.webber.topnew.net.OnSimpleRequestCallback;
import com.webber.topnew.net.request.TopNewsRequest;
import com.webber.topnew.net.response.TopNewsResponse;

import java.util.List;

import butterknife.Bind;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mxh on 2016/11/23.
 * Describe：头条新闻
 */

public class TopNewsFragment extends NFragment {

    private static final int PAGE_OFFSET = 10;
    private static int mCurrentPage = 0;
    @Bind(R.id.top_news_fl)
    FrameLayout topNewsFl;
    private ListViewComponent mListComp;
    private TopNewsAdapter mAdapter;

    @Override
    protected int create() {
        return R.layout.fragment_top_news;
    }

    @Override
    protected void initComp() {
        super.initComp();
        setTitle(R.string.title_top_news);
        mListComp = new ListViewComponent(activity, topNewsFl);
        mAdapter = new TopNewsAdapter(mListComp.getRecycleView(), R.layout.layout_top_news);
        mListComp.setAdapter(mAdapter);
        activity.mEmptyLl.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                switch (childView.getId()) {
                    case R.id.top_news_center_title_tv:
                        toast(mAdapter.getItem(position).getUrl());
                        break;
                    case R.id.top_news_iv:
                        toast("点击图片");
                        break;
                }
            }
        });
        mAdapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
                toast("长按了第" + position + "个操作符");
                return true;
            }
        });
        mListComp.setListener(new ILoadMoreViewListener() {
            @Override
            public void startRefresh() {
                mCurrentPage = 0;
                requestTopNewsData();
            }

            @Override
            public void startLoadMore() {
                mCurrentPage++;
                requestTopNewsData();
            }
        });
    }

    @Override
    protected void initData() {
        mListComp.doRefresh();
    }

    /**
     * 请求新闻数据
     */
    private void requestTopNewsData() {
        TopNewsRequest mTopNewsRequest = new TopNewsRequest(ApiConstants.KEY_WX, PAGE_OFFSET, mCurrentPage + 1);
        ApiFactory.getNewsAPI().getTopNews(NetUtils.getParams(mTopNewsRequest))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Response<TopNewsResponse>>bindToLifecycle())
                .subscribe(new OnSimpleRequestCallback<Response<TopNewsResponse>>(activity) {
                    @Override
                    public void onResponse(Response<TopNewsResponse> response) {
                        complete();
                        updateUI(response.body());
                    }

                    @Override
                    public void onFinish() {
                        complete();
                    }
                });
    }

    private void complete() {
        activity.mEmptyLl.setErrorType(EmptyLayout.STATE_HIDE_LAYOUT);
        mListComp.endLoadMore();
        mListComp.endRefresh();
    }

    private void updateUI(TopNewsResponse body) {
        if (mCurrentPage == 0) {
            mAdapter.clear();
        }
        mAdapter.setState(judgeListState(body.newslist));
        mAdapter.addMoreData(body.newslist);
    }

    /**
     * 判断当前列表的状态
     *
     * @param newslist 列表数据
     */
    private int judgeListState(List<TopNewsResponse.TopNewsItem> newslist) {
        if ((mAdapter.getItemCount() + newslist.size()) == 0) {// 空页面
            return BasicRecycleAdapter.STATE_EMPTY_ITEM;
        } else if ((newslist.size() == 0 || (newslist.size() < PAGE_OFFSET && mCurrentPage == 0))) {// 没有更多
            return BasicRecycleAdapter.STATE_NO_MORE;
        } else {// 可以加载更多
            return BasicRecycleAdapter.STATE_LOAD_MORE;
        }
    }
}

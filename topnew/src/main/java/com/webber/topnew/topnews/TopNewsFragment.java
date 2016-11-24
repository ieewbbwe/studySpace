package com.webber.topnew.topnews;

import android.widget.FrameLayout;
import android.widget.Toast;

import com.android_mobile.core.ui.comp.pullListView.ILoadMoreViewListener;
import com.android_mobile.core.ui.comp.pullListView.ListViewComponent;
import com.webber.topnew.NFragment;
import com.webber.topnew.R;
import com.webber.topnew.adapter.TopNewsAdapter;
import com.webber.topnew.net.ApiConstants;
import com.webber.topnew.net.ApiFactory;
import com.webber.topnew.net.NetUtils;
import com.webber.topnew.net.OnProgressRequestCallback;
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
    private static final int mPage = 1;
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
    }

    @Override
    protected void initListener() {
        mListComp.setListener(new ILoadMoreViewListener() {
            @Override
            public void startRefresh() {
                Toast.makeText(activity, "show", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void startLoadMore() {

            }
        });
    }

    @Override
    protected void initData() {
       // requestTopNewsData();
    }

    /**
     * 请求新闻数据
     */
    private void requestTopNewsData() {
        TopNewsRequest mTopNewsRequest = new TopNewsRequest(ApiConstants.KEY_WX, PAGE_OFFSET, mPage);
        ApiFactory.getNewsAPI().getTopNews(NetUtils.getParams(mTopNewsRequest))
                .compose(this.<Response<List<TopNewsResponse>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OnProgressRequestCallback<Response<List<TopNewsResponse>>>(getContext()) {
                    @Override
                    public void onResponse(Response<List<TopNewsResponse>> response) {
                        Toast.makeText(activity, response.body().size(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

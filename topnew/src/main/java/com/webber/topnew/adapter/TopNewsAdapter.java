package com.webber.topnew.adapter;

import android.support.v7.widget.RecyclerView;

import com.android_mobile.core.adapter.BasicRecycleAdapter;
import com.android_mobile.core.adapter.ViewHolderHelper;
import com.android_mobile.core.helper.image.ImageLoadFactory;
import com.webber.topnew.R;
import com.webber.topnew.net.response.TopNewsResponse;

/**
 * Created by mxh on 2016/11/24.
 * Describe：头条列表适配器
 */

public class TopNewsAdapter extends BasicRecycleAdapter<TopNewsResponse.TopNewsItem> {

    public TopNewsAdapter(RecyclerView recyclerView, int itemLayoutId) {
        super(recyclerView, itemLayoutId);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, TopNewsResponse.TopNewsItem model) {
        viewHolderHelper.setText(R.id.top_news_center_title_tv, model.getTitle())
                .setText(R.id.top_news_center_content_tv, model.getCtime());
        ImageLoadFactory.getInstance().getImageLoadHandler().displayImage(viewHolderHelper.getConvertView().getContext()
                , model.getPicUrl(), viewHolderHelper.getImageView(R.id.top_news_iv));
    }

    @Override
    protected void setItemChildListener(ViewHolderHelper viewHolderHelper) {
        super.setItemChildListener(viewHolderHelper);
        viewHolderHelper.setItemChildClickListener(R.id.top_news_center_title_tv);
        viewHolderHelper.setItemChildClickListener(R.id.top_news_iv);
        viewHolderHelper.setItemChildLongClickListener(R.id.top_new_operate_tv);
    }
}

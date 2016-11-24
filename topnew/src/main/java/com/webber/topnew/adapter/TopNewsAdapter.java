package com.webber.topnew.adapter;

import android.support.v7.widget.RecyclerView;

import com.android_mobile.core.adapter.BasicRecycleAdapter;
import com.android_mobile.core.adapter.ViewHolderHelper;

/**
 * Created by mxh on 2016/11/24.
 * Describe：头条列表适配器
 */

public class TopNewsAdapter extends BasicRecycleAdapter {

    public TopNewsAdapter(RecyclerView recyclerView, int itemLayoutId) {
        super(recyclerView, itemLayoutId);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, Object model) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}

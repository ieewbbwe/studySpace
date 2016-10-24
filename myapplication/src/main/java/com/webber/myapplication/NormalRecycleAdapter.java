package com.webber.myapplication;

import android.support.v7.widget.RecyclerView;

import com.android_mobile.core.adapter.BasicRecycleAdapter;
import com.android_mobile.core.adapter.ViewHolderHelper;

/**
 * Created by mxh on 2016/10/24.
 * Describe:
 */
public class NormalRecycleAdapter extends BasicRecycleAdapter<BuyerInfoModel> {

    public NormalRecycleAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_normal);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, BuyerInfoModel model) {
        viewHolderHelper
                .setText(R.id.tv_item_normal_title, model.buyerFirstName)
                .setText(R.id.tv_item_normal_detail, model.buyerLastName);
    }

    @Override
    protected void setItemChildListener(ViewHolderHelper viewHolderHelper) {
        super.setItemChildListener(viewHolderHelper);
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_normal_delete);
        viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_normal_delete);
    }

}

package com.android_mobile.core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public abstract class BasicRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context ctx;
    protected List<?> list;
    OnItemClickListener mListener;

    public BasicRecycleAdapter() {
    }

    public BasicRecycleAdapter(Context context, List<?> list) {
        this.ctx = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mListener.onItemClick(v, pos);
                }
            });
        }
    }

    public void setList(List list) {
        this.list = list;
    }

    /**
     * 設置條目點擊
     *
     * @param listener 條目點擊監聽
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public List<?> getList() {
        return list;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}

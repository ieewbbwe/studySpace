package com.android_mobile.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BasicAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context cxt;
    protected List<?> list;

    public BasicAdapter(Context context, List<?> list) {
        this.cxt = context;
        mInflater = LayoutInflater.from(cxt);
        this.list = list;
    }

    public void setList(List<?> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

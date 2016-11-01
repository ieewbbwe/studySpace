package com.webber.webberdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        initRecycleView();
    }

    private void initRecycleView() {
        RecyclerView mLoadMoreRv = (RecyclerView) findViewById(R.id.load_more_rv);
        //创建布局管理器 LinearLayoutManager为线性布局，支持横向、纵向。
        //               GridLayoutManager()为网格布局。
        //               StaggeredGridLayoutManager为瀑布流布局
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mLoadMoreRv.setLayoutManager(mLayoutManager);
        //添加条目间距 RecycleView 没有提供类似ListView的divider方法，间距需要自己画，也就意味着我们也可以使用各种自定义的间距样式
        mLoadMoreRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //创建适配器及数据
        List<String> mData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mData.add("item" + i);
        }
        MyAdapter myAdapter = new MyAdapter(mData);
        mLoadMoreRv.setAdapter(myAdapter);
    }

    //RecycleView 适配器，泛型可以传自己定义的ViewHolder
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<String> list;

        MyAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_simple_text, parent, false));
        }

        //这个方法拿到绑定的ViewHolder 在此处处理数据
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mItemTv.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mItemTv;

            MyViewHolder(View itemView) {
                super(itemView);
                mItemTv = (TextView) itemView.findViewById(R.id.item_recycle_tv);
            }
        }
    }
}

package com.webber.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

import com.android_mobile.core.adapter.OnItemChildClickListener;
import com.android_mobile.core.adapter.OnItemChildLongClickListener;
import com.android_mobile.core.adapter.OnRVItemClickListener;
import com.android_mobile.core.base.BaseActivity;
import com.android_mobile.core.net.IBasicAsyncTask;
import com.android_mobile.core.ui.comp.pullListView.ListViewComponent;
import com.android_mobile.core.utiles.Lg;
import com.android_mobile.core.utiles.TimerUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class MainActivity extends BaseActivity {

    private ListViewComponent loadComponent;
    private NormalRecycleAdapter normalRecycleAdapter;
    private List<BuyerInfoModel> buyerInfoModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //navigationBar.hidden();
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT > 21) {
            toolbar.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void initComp() {
        Lg.print("webber", "timeUtileTest:" + TimerUtils.formatTime(System.currentTimeMillis()));
        loadComponent = new ListViewComponent(this, findViewById(R.id.home_list_fl));
        normalRecycleAdapter = new NormalRecycleAdapter(loadComponent.getRecycleView());
        loadComponent.setAdapter(normalRecycleAdapter);
    }

    @Override
    protected void initListener() {
        normalRecycleAdapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
                Lg.print("webber", "onItemChildLongClick:" + position);
                return true;
            }
        });
        normalRecycleAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Lg.print("webber", "onRVItemClick:" + position);

            }
        });
        normalRecycleAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                Lg.print("webber", "onItemChildClick:" + position);
            }
        });

        loadComponent.setListener(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                async(new IBasicAsyncTask() {
                    @Override
                    public void callback(Object result) {
                        loadComponent.endRefresh();
                        if (result != null) {
                            User user = (User) result;
                            //normalRecycleAdapter.addItem(0, user.purchaseInfo);
                            normalRecycleAdapter.clear();
                            normalRecycleAdapter.addNewData(buyerInfoModels);
                            Lg.print("webber", "user:" + user.toString());
                        }
                    }
                }, new BaseRequest(), new PersionInfoService());
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadComponent.endLoadMore();
                        normalRecycleAdapter.addMoreData(buyerInfoModels);
                    }
                }, 2000);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        loadComponent.doRefresh();
        buyerInfoModels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            buyerInfoModels.add(new BuyerInfoModel("buyerFirstName" + i, "buyerLastName" + i));
        }
    }


}

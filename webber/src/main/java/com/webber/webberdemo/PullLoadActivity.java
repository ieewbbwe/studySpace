package com.example.pullloaddemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ArrayAdapter;

import com.webber.webberdemo.MyPullLoadView;
import com.webber.webberdemo.R;

import java.util.ArrayList;
import java.util.List;

public class PullLoadActivity extends Activity {
    private static final int REQUEST_OK = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_load);
        init();
    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REQUEST_OK:
                    List<String> datas= (List<String>) msg.obj;
                    infos.addAll(0,datas);
                    adapter.notifyDataSetChanged();
                    mpv.finishRefresh();
                    break;

                default:
                    break;
            }
        };
    };
    private void init() {
        mpv = (MyPullLoadView) findViewById(R.id.mpv);
        initDate();
        adapter = new ArrayAdapter<String>(PullLoadActivity.this,
                android.R.layout.simple_list_item_1, infos);
        mpv.setAdapter(adapter);
        mpv.setListener(new MyPullLoadView.OnRefreshListener() {

            @Override
            public void OnLoadingMore() {
                //添加一些数据
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        List<String> datas = new ArrayList<String>();
                        datas.add("我是模拟加载的数据");
                        datas.add("我赵日天不服！！！");
                        handler.obtainMessage(REQUEST_OK, datas).sendToTarget();
                    }
                }).start();
            }

            @Override
            public void OnLoadingFoot() {

            }
        });
    }
    List<String> infos;
    private MyPullLoadView mpv;
    private ArrayAdapter<String> adapter;
    private void initDate() {
        infos = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            String str = "我是数据" + i;
            infos.add(str);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}

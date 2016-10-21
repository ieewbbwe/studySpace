package com.webber.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android_mobile.core.base.BaseActivity;
import com.android_mobile.core.net.IBasicAsyncTask;
import com.android_mobile.core.utiles.Lg;
import com.android_mobile.core.utiles.TimerUtils;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        async(new IBasicAsyncTask() {
            @Override
            public void callback(Object result) {
                if (result != null) {
                    User user = (User) result;
                    Lg.print("webber", "user:" + user.toString());
                }
            }
        }, new BaseRequest(), new PersionInfoService());
    }

}

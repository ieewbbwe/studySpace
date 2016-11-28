package com.webber.webberdemo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.recycle_bt)
    Button recycleBt;
    @Bind(R.id.snack_bt)
    Button snackBt;
    @Bind(R.id.bottom_elevation_6)
    View bottom6;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
        initListener();
        View v = findViewById(R.id.test_tv);
        v.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.d("webber", "onPreDraw");
                return true;
            }
        });
        v.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                Log.d("webber", "onDraw");
            }
        });

        //forTest();

        bottom6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void forTest() {
        for (int i = 0; i < 3; i++) {
            Log.d("webber", "第" + i + "次循環");
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    Log.d("webber", "相等： i:" + i + "j:" + j);
                    continue;
                } else {
                    Log.d("webber", "i:" + i + "j:" + j);
                }
            }
        }
    }

    private void initListener() {
        recycleBt.setOnClickListener(this);
        snackBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recycle_bt:
                Intent intent = new Intent(this, RecycleViewActivity.class);
                startActivity(intent);
                break;
            case R.id.snack_bt:
                startActivity(new Intent(MainActivity.this, ActivityOne.class));
                overridePendingTransition(R.anim.push_bottom_in, R.anim.push_left_out);
                break;
        }
    }
}

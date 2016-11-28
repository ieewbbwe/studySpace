package com.webber.webberdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityOne extends AppCompatActivity {

    @Bind(R.id.one_bt)
    Button oneBt;
    @Bind(R.id.activity_activty_one)
    RelativeLayout activityActivtyOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activty_one);
        ButterKnife.bind(this);
        oneBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityOne.this, MainActivity.class));
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                finish();
            }
        });
    }

}

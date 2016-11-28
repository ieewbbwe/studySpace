package com.webber.webberdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityTwo extends AppCompatActivity {

    @Bind(R.id.two_bt)
    Button twoBt;
    @Bind(R.id.activity_two)
    RelativeLayout activityTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        ButterKnife.bind(this);
        twoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityTwo.this, MainActivity.class));
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
    }
}

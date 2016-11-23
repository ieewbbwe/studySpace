package com.webber.topnew;

import android.os.Bundle;

import com.android_mobile.core.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by mxh on 2016/11/23.
 * Describe：
 */

public abstract class NActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    @Override
    protected void initComp() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

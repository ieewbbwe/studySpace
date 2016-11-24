package com.webber.topnew;

import com.android_mobile.core.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by mxh on 2016/11/24.
 * Describe：
 */

public abstract class NFragment extends BaseFragment {

    @Override
    protected void initComp() {
        ButterKnife.bind(this, v);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

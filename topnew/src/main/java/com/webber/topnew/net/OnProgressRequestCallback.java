package com.webber.topnew.net;

import com.android_mobile.core.BasicActivity;
import com.android_mobile.core.utiles.Lg;

import retrofit2.Response;

/**
 * Created by mxh on 2016/11/22.
 * Describe： 需要展示进度框的网络访问
 */

public abstract class OnProgressRequestCallback<T extends Response> extends OnSimpleRequestCallback<T> {

    public OnProgressRequestCallback(BasicActivity context) {
        super(context);
    }

    @Override
    public void onStart() {
        mContext.displayProgressBar();
        super.onStart();
    }

    @Override
    public void onCompleted() {
        mContext.hiddenProgressBar();
        super.onCompleted();
    }

    @Override
    public void onFinish() {
        mContext.hiddenProgressBar();
        Lg.print("network", "onFinish");
    }
}

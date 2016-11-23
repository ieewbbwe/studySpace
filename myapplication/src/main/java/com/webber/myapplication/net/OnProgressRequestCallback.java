package com.webber.myapplication.net;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Response;

/**
 * Created by mxh on 2016/11/22.
 * Describe： 需要展示进度框的网络访问
 */

public abstract class OnProgressRequestCallback<T extends Response> extends OnSimpleRequestCallback<T> {

    public OnProgressRequestCallback(Context context) {
        super(context);
    }

    @Override
    public void onStart() {
        //displayProgress
        Toast.makeText(mContext, "开始加载", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    @Override
    public void onFinish() {
        //hideProgress
        Toast.makeText(mContext, "加载结束", Toast.LENGTH_SHORT).show();
    }
}

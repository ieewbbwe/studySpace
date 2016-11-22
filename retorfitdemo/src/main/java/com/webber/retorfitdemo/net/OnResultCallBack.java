package com.webber.retorfitdemo.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.webber.retorfitdemo.R;

import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by mxh on 2016/11/22.
 * Describe：回调结果封装类
 */

public abstract class OnResultCallBack<T extends Response> extends Subscriber<T> {

    protected final Context mContext;

    public abstract void onFailed(int code, String message);

    public abstract void onException(String message);

    public abstract void onResponse(T response);

    public abstract void onFinish();

    public OnResultCallBack(Context context) {
        this.mContext = context;
    }

    @Override
    public void onStart() {
        Log.d("network", "onStart" + Thread.currentThread().getName());
        super.onStart();
        if (!isNetworkAvailable(mContext)) {
            onFailed(ApiConstants.ERROR_NO_INTERNET, mContext.getString(R.string.network_unavailable));
            onFinish();
            unsubscribe();
        }
    }

    @Override
    public void onCompleted() {
        Log.d("network", "onCompleted" + Thread.currentThread().getName());

    }

    @Override
    public void onError(Throwable e) {
        onException(e.getMessage());
        onFinish();
    }

    @Override
    public void onNext(T response) {
        Log.d("network", "onNext" + Thread.currentThread().getName());

        if (response.isSuccessful()) {
            Log.d("network", "response:" + new Gson().toJson(response));
            onResponse(response);
        } else {
            onFailed(response.code(), response.message());
        }
    }

    /**
     * 检测网络连接是否可用
     *
     * @return true 可用; false 不可用
     */
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] netinfo = cm.getAllNetworkInfo();
        if (netinfo == null) {
            return false;
        }
        for (NetworkInfo aNetinfo : netinfo) {
            if (aNetinfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

}

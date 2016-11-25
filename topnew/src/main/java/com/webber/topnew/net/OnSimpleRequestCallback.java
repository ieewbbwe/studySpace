package com.webber.topnew.net;

import android.widget.Toast;

import com.android_mobile.core.BasicActivity;
import com.android_mobile.core.utiles.Lg;

import retrofit2.Response;


/**
 * Created by mxh on 2016/11/22.
 * Describe：不需要展示进度框的网络请求, 但是出现错误会提示
 * 如果你想使用其他方式展示错误信息, 回调里重写方法即可
 */

public abstract class OnSimpleRequestCallback<T extends Response> extends OnResultCallBack<T> {

    public OnSimpleRequestCallback(BasicActivity context) {
        super(context);
    }

    @Override
    public void onFailed(int code, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Lg.print("network", String.format("onFailed code：%s->>message：%s", code, message));
    }

    @Override
    public void onException(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Lg.print("network", "onException " + message);
    }

}

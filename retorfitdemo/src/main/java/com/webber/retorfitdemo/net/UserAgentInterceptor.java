package com.webber.retorfitdemo.net;

import android.util.Log;

import com.webber.retorfitdemo.MainActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mxh on 2016/11/18.
 * Describe：自定义拦截器
 */

public class UserAgentInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request userRequest = originalRequest.newBuilder()
                //添加统一的头信息
                //.addHeader()
                .addHeader("Cookie", MainActivity.cookie)
                .addHeader("X-YahooWSSID-Authorization", MainActivity.wssid)
                .build();
        Log.d("network", "Method:" + userRequest.method());
        Log.d("network", "URL:" + userRequest.url().url());
        return chain.proceed(userRequest);
    }
}

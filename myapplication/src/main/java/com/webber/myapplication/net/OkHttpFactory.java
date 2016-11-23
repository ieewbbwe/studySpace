package com.webber.myapplication.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by mxh on 2016/11/18.
 * Describe：
 */

public class OkHttpFactory {

    private OkHttpClient client;
    private static final int TIMEOUT_READ = 30;
    private static final int TIMEOUT_CONNECTION = 30;

    private OkHttpFactory() {
        client = new OkHttpClient.Builder()
                //.cache() //缓存
                //.addInterceptor(new HttpLoggingInterceptor())//Log日志
                .addInterceptor(new UserAgentInterceptor())//自定义拦截器
                .retryOnConnectionFailure(true) //失败重连
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpClient getOkHttpClient() {
        return Holder.INSTANCE.client;
    }

    public static class Holder {
        final public static OkHttpFactory INSTANCE = new OkHttpFactory();
    }
}

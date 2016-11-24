package com.webber.topnew.net;

import com.webber.topnew.net.api.NewsApi;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mxh on 2016/11/18.
 * Describe：
 */

public enum ApiFactory {

    INSTANCE;

    private static NewsApi newsApi;

    ApiFactory() {
    }

    public static NewsApi getNewsAPI() {
        if (newsApi == null) {
            ApiFactory.newsApi = createApi(ApiConstants.BASE_URL, GsonConverterFactory.create(), NewsApi.class);
        }
        return newsApi;
    }

    private static <T> T createApi(String baseUrl, Converter.Factory factory, Class<T> t) {
        Retrofit.Builder mBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return mBuilder.client(OkHttpFactory.getOkHttpClient()).build().create(t);
    }

}

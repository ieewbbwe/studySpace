package com.webber.topnew.net.api;

import com.webber.topnew.net.response.TopNewsResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by mxh on 2016/11/24.
 * Describe：新闻信息请求接口
 */

public interface NewsApi {
    @GET("wxnew")
    Observable<Response<TopNewsResponse>> getTopNews(@QueryMap Map<String, String> map);
}

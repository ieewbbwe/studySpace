package com.webber.retorfitdemo;


import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by mxh on 2016/11/17.
 * Describe：
 */

public interface NetEngine {

    @GET("products/{id}")
    Observable<Response<BaseProduct>> getProductDetail(@Path("id") String pId, @Query("fields") String fields, @Query("id") String id);

    @GET("products/{id}")
    Observable<Response<BaseProduct>> getProductDetail(@QueryMap Map<String, String> stringMap);

    //@Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("likes")
    Observable<Response<BaseProduct>> postLikeItem(@Body RequestBody likeItem);

}

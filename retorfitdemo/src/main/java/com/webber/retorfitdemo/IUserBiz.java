package com.webber.retorfitdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mxh on 2016/10/19.
 */
public interface IUserBiz {
    @GET("profiles")
    Call<User> getProfiles();

    @GET("yql?{yql}")
    Call<QueryCommodityResponse> getProduct(@Path("yql") String yql);
}

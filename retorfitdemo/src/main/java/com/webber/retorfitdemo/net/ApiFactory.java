package com.webber.retorfitdemo.net;

import com.webber.retorfitdemo.net.api.ProductAPI;

/**
 * Created by mxh on 2016/11/18.
 * Describe：
 */

public enum ApiFactory {

    INSTANCE;

    private static ProductAPI productAPI;

    ApiFactory() {
    }

    public static ProductAPI getProductAPI() {
        if (productAPI == null) {
            productAPI = RetrofitClient.INSTANCE.getRetrofit().create(ProductAPI.class);
        }
        return productAPI;
    }

}

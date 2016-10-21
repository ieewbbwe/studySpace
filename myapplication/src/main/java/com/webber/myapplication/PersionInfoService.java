package com.webber.myapplication;

import com.android_mobile.core.enums.RequestType;
import com.android_mobile.core.net.http.Service;
import com.android_mobile.core.net.http.ServiceRequest;
import com.android_mobile.core.net.http.ServiceResponse;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by mxh on 2016/10/21.
 */
public class PersionInfoService extends Service {
    @Override
    protected ServiceResponse execute(ServiceRequest request) {
        String result = request(RequestType.GET, "https://hk.secure.shop.yahoo.com/api/m/v1/profiles", request);
        ServiceResponse response = new ServiceResponse();
        try {
            Type type = new TypeToken<User>() {
            }.getType();
            response = g.fromJson(result, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }
}

package com.webber.myapplication.net.request;

/**
 * Created by mxh on 2016/11/22.
 * Describe：
 */

public class UserRequest extends BaseRequest{
    public String id;
    public String fields;

    public UserRequest(String id, String fields) {
        this.id = id;
        this.fields = fields;
    }
}

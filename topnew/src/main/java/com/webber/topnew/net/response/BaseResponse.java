package com.webber.topnew.net.response;

import java.net.HttpURLConnection;

/**
 * Created by mxh on 2016/11/22.
 * Describe：响应基类
 */

public class BaseResponse {

    /*响应信息*/
    private String msg;
    /*响应码*/
    private int code;

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED
                || code == HttpURLConnection.HTTP_ACCEPTED || code == HttpURLConnection.HTTP_NOT_AUTHORITATIVE;
    }
}

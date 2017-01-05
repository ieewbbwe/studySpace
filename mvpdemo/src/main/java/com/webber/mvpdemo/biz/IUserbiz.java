package com.webber.mvpdemo.biz;

/**
 * Created by mxh on 2016/12/27.
 * Describe：
 */

public interface IUserbiz {
    void login(String username, String pwd, OnLoginListener onLoginListener);
}

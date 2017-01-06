package com.webber.mvpdemo.biz;

import com.webber.mvpdemo.module.User;

/**
 * Created by mxh on 2016/12/27.
 * Describe：
 */

public interface OnLoginListener {
    void onSucceed(User user);

    void onFailed();
}

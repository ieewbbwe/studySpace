package com.webber.mvpdemo.biz;

import com.webber.mvpdemo.module.User;

/**
 * Created by mxh on 2016/12/27.
 * Describe：
 */

public class UserBiz implements IUserbiz {

    @Override
    public void login(final String username, final String pwd, final OnLoginListener onLoginListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ("mxh".equals(username) && "123".equals(pwd)) {
                    onLoginListener.onSucceed(new User(username, pwd));
                } else {
                    onLoginListener.onFailed();
                }
            }
        }).start();
    }
}

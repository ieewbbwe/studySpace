package com.webber.mvpdemo.presenter;

import android.os.Handler;

import com.webber.mvpdemo.biz.OnLoginListener;
import com.webber.mvpdemo.biz.UserBiz;
import com.webber.mvpdemo.module.User;
import com.webber.mvpdemo.view.IUserLoginView;

/**
 * Created by mxh on 2016/12/27.
 * Describe：
 */

public class UserLoginPresenter {

    private UserBiz userBiz;
    private IUserLoginView userLoginView;
    private Handler handler = new Handler();

    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.userLoginView = userLoginView;
        this.userBiz = new UserBiz();
    }

    public void login() {
        userLoginView.showLoading();
        userBiz.login(userLoginView.getUserName(), userLoginView.getPwd(), new OnLoginListener() {
            @Override
            public void onSucceed(User user) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.hideLoading();
                    }
                });
            }

            @Override
            public void onFailed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.hideLoading();
                    }
                });
            }
        });
    }
}

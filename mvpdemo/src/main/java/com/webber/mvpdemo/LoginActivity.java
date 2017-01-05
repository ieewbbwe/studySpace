package com.webber.mvpdemo;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.webber.mvpdemo.presenter.UserLoginPresenter;
import com.webber.mvpdemo.view.IUserLoginView;


/**
 * Mvp Login
 * 解耦，将Activity与功能逻辑分开，在修改View或者逻辑的时候互不影响
 * Activity持有一个Presenter 中间者，Presenter中通过接口持有Activity与实体功能类的引用
 * 在presenter中处理View与module的关系
 * 1 View 可用通过presenter获取到module中的信息，例如，点击某一模块之后触发功能模块
 * 2 Module 也可以通过presenter获取到View中的元素，例如，计算时获取view上的参数
 * 添加方法的时候只需要添加接口中的对应方法
 * 修改的时候只需要修改实现类即可
 * <p>
 * 优点：功能逻辑一目了然，修改时View与module互不影响
 * 缺点：会创建很多类和口
 *
 * 适用场景：
 */
public class LoginActivity extends AppCompatActivity implements IUserLoginView {

    private TextInputLayout mNameTil;
    private TextInputEditText mNameIet;
    private TextInputEditText mPwdIet;
    private Button mLoginBt;
    private ProgressBar mLoginPb;

    private UserLoginPresenter loginPresenter = new UserLoginPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNameTil = (TextInputLayout) findViewById(R.id.username_til);
        mNameIet = (TextInputEditText) findViewById(R.id.username_iet);
        mPwdIet = (TextInputEditText) findViewById(R.id.pwd_iet);
        mLoginBt = (Button) findViewById(R.id.login_bt);
        mLoginPb = (ProgressBar) findViewById(R.id.login_pb);

        mNameTil.setErrorEnabled(true);
        mNameTil.setError("请输入用户名"); //错误提醒的文字
        mNameTil.setErrorEnabled(false);

        mPwdIet.setError("请输入密码");


        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.login();
            }
        });
    }

    @Override
    public String getUserName() {
        return mNameIet.getText().toString();
    }

    @Override
    public String getPwd() {
        return mPwdIet.getText().toString();
    }

    @Override
    public void showLoading() {
        mLoginPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoginPb.setVisibility(View.GONE);
    }
}

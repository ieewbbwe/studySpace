package com.webber.rxbidding;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private CheckBox checkBox;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.test_bt);
        textView = (TextView) findViewById(R.id.test_tv);
        checkBox = (CheckBox) findViewById(R.id.test_cb);
        editText = (EditText) findViewById(R.id.test_et);
        //demo1();
        //demo2();
        //demo3();
        demo4();
    }

    private void demo4() {

    }

    private void demo3() {
        RxCompoundButton.checkedChanges(checkBox)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        button.setEnabled(!aBoolean);
                    }
                });
    }

    private void demo2() {
        RxTextView.textChanges(editText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {

                    }
                });
    }

    //防止抖动测试
    private void demo1() {
        RxView.clicks(button).debounce(1000, TimeUnit.MILLISECONDS)//.throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Snackbar.make(button, "点击了", Snackbar.LENGTH_SHORT).show();
                        Log.d("webber", "点击了" + System.currentTimeMillis());
                    }
                });
    }
}

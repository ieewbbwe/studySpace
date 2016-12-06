package com.webber.mvpdemo;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Android 动画
 */
public class MainActivity extends AppCompatActivity {

    private ImageView mRotateIv;
    private ObjectAnimator rotateAnim;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBar();
        initRotate();
    }

    private void initRotate() {
        mRotateIv = (ImageView) findViewById(R.id.rotate_uv);
        Glide.with(this).load(R.mipmap.img1)
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                .into(mRotateIv);
        mRotateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewAnim();
                //frameAnim();
                objectAnim();

            }
        });
    }

    /**
     * 属性动画
     */
    private void objectAnim() {
        rotate();
    }

    /**
     * 旋转Iv
     */
    private void rotate() {
        demo1();
    }

    /**
     * 使用属性动画
     * ObjectAnimator
     */
    private void demo1() {
        if (rotateAnim != null && rotateAnim.isRunning()) {
            //动画正在执行
            currentTime = rotateAnim.getCurrentPlayTime();
            rotateAnim.cancel();
        } else {
            if (rotateAnim == null) {
                rotateAnim = ObjectAnimator.ofFloat(mRotateIv, "rotation", 0f, 360f);
                rotateAnim.setDuration(1000);
                rotateAnim.setInterpolator(new LinearInterpolator());
                rotateAnim.setRepeatCount(-1);//set repeat time forever
            } else {
                rotateAnim.setCurrentPlayTime(currentTime);
            }
            rotateAnim.start();
        }
    }

    private void initBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

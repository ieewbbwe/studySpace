package com.webber.uploaddemo;

import android.util.Log;

/**
 * Created by mxh on 2016/12/13.
 * Describe：
 */

public class LifeParentObject {

    public LifeParentObject() {
        Log.e("init","p构造方法");
    }

    static {
        Log.e("init","p静态代码块");
    }

    public static void MethodStatic() {
        Log.e("init","p静态方法");
    }
}

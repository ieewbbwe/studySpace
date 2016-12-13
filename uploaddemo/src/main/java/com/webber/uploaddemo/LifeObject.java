package com.webber.uploaddemo;

import android.util.Log;

/**
 * Created by mxh on 2016/12/13.
 * Describe：
 */

public class LifeObject extends LifeParentObject {

    public LifeObject() {
        Log.e("init","构造方法");
    }

    static {
        Log.e("init","静态代码块");
    }

    public static void MethodStatic() {
        Log.e("init","静态方法");
    }


}

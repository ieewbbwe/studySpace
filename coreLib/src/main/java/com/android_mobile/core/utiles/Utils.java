package com.android_mobile.core.utiles;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class Utils {
    public static Bitmap bitmap;

    public static float d2r(float degree) {
        return degree * (float) Math.PI / 180f;
    }

    public static FloatBuffer toFloatBuffer(float[] v) {
        ByteBuffer buf = ByteBuffer.allocateDirect(v.length * 4);
        buf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = buf.asFloatBuffer();
        buffer.put(v);
        buffer.position(0);
        return buffer;
    }

    public static ShortBuffer toShortBuffer(short[] v) {
        ByteBuffer buf = ByteBuffer.allocateDirect(v.length * 2);
        buf.order(ByteOrder.nativeOrder());
        ShortBuffer buffer = buf.asShortBuffer();
        buffer.put(v);
        buffer.position(0);
        return buffer;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dimen(Context context, int id) {
        return (int) context.getResources().getDimension(id);
    }

    /**
     * 检测网络连接是否可用
     *
     * @return true 可用; false 不可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] netinfo = cm.getAllNetworkInfo();
        if (netinfo == null) {
            return false;
        }
        for (NetworkInfo aNetinfo : netinfo) {
            if (aNetinfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断有无sd卡
     */
    public static boolean isSdCardAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED)
                || status.equals("/mnt/sdcard");
    }

    public static boolean isTestUser(String phone) {
        return phone.startsWith("400");
    }

    /**
     * 判断设备有功能
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 获取文件的后缀名
     */
    public static String getFileType(String fileName) {
        if (!StringUtils.isEmpty(fileName)) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                return fileName.substring(typeIndex + 1).toLowerCase();
            }
        }
        return null;
    }
}

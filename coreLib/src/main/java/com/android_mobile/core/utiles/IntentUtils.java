package com.android_mobile.core.utiles;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

/**
 * Created by mxh on 2016/11/1.
 * Describe：用于打开各类页面的工具类
 */

public class IntentUtils {

    /**
     * 判断设备是否存在功能
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 打电话
     *
     * @param context
     * @param tel
     */
    public static void intentForCall(Context context, String tel) {
        tel = tel.replaceAll("-", "");
        Intent intent = new Intent();
        // 激活源代码,添加intent对象
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "没有拨打电话功能", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 发送邮件
     */
    public static void intentForSendEmail(Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            Intent sendEmail = new Intent(Intent.ACTION_SEND);
            // sendEmail.setType("plain/text");
            sendEmail.setType("message/rfc822"); // 真机上使用这行
            sendEmail.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{content});
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, "发送邮件"); // 主题
            sendEmail.putExtra(Intent.EXTRA_TEXT, "您的内容");
            try {
                context.startActivity(
                        Intent.createChooser(sendEmail,
                                "请选择邮箱"));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context, "没有找到邮件功能！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

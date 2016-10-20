package cn.android_mobile.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import cn.android_mobile.toolkit.Lg;

public class CrashHandler implements UncaughtExceptionHandler {
	public static CrashHandler instance = new CrashHandler();
	private Context cxt;

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		return instance;
	}

	public void init(Context cxt) {
		this.cxt = cxt;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		System.out.println("*************uncaughtException");
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		// 获取跟踪的栈信息，除了系统栈信息，还把手机型号、系统版本、编译版本的唯一标示
		StackTraceElement[] trace = ex.getStackTrace();
		StackTraceElement[] trace2 = new StackTraceElement[trace.length + 3];
		System.arraycopy(trace, 0, trace2, 0, trace.length);
		trace2[trace.length + 0] = new StackTraceElement("Android", "MODEL",
				android.os.Build.MODEL, -1);
		trace2[trace.length + 1] = new StackTraceElement("Android", "VERSION",
				android.os.Build.VERSION.RELEASE, -1);
		trace2[trace.length + 2] = new StackTraceElement("Android",
				"FINGERPRINT", android.os.Build.FINGERPRINT, -1);
		// 追加信息，因为后面会回调默认的处理方法
		ex.setStackTrace(trace2);
		ex.printStackTrace(printWriter);
		// 把上面获取的堆栈信息转为字符串，打印出来
		final String stacktrace = result.toString();
		printWriter.close();
		Lg.print(stacktrace);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
//				Toast.makeText(cxt, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG)
//						.show();
				Intent intent = new Intent(cxt, SendErrorActivity.class);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        intent.putExtra("msg", stacktrace);
		        cxt.startActivity(intent);
				Looper.loop();
				BasicApplication.closeApp();
			}
		}.start();
		BasicApplication.closeApp();
	}
}

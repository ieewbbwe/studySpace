package cn.android_mobile.toolkit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

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
	public static Bitmap takeScreenshot(View view) {
		assert view.getWidth() > 0 && view.getHeight() > 0;
		Bitmap.Config config = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	public static int dimen(Context context,int id ){
		return (int)context.getResources().getDimension(id);
	}
}

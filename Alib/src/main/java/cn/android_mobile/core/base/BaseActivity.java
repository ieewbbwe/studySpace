package cn.android_mobile.core.base;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.view.View;
import android.widget.RelativeLayout;
import cn.android_mobile.core.BasicActivity;
import cn.android_mobile.core.ui.BasicComponent;
import cn.android_mobile.toolkit.Lg;

import com.nineoldandroids.animation.ObjectAnimator;

public abstract class BaseActivity extends BasicActivity {

	private int modalAnimTime = 300;

	public void pushModalComponent(BasicComponent bc, int widthDip) {
		if (bc.isDisplay() == true)
			return;
		bc.getRoot().setVisibility(View.VISIBLE);
		bc.setOffset(widthDip);
		RelativeLayout.LayoutParams imagebtn_params = (android.widget.RelativeLayout.LayoutParams) bc
				.getRoot().getLayoutParams();
		imagebtn_params.height = (int) SCREEN_HEIGHT;
		imagebtn_params.width = widthDip;
		bc.getRoot().setLayoutParams(imagebtn_params);
		ObjectAnimator
				.ofFloat(bc.getRoot(), "translationX", SCREEN_WIDTH,
						SCREEN_WIDTH - widthDip).setDuration(modalAnimTime)
				.start();
		bc.setDisplay(true);
	}

	public void popModalComponent(BasicComponent bc) {
		if (bc.isDisplay() == false)
			return;
		ObjectAnimator
				.ofFloat(bc.getRoot(), "translationX",
						SCREEN_WIDTH - bc.getOffset(), SCREEN_WIDTH)
				.setDuration(modalAnimTime).start();
		bc.setDisplay(false);
	}

	public Bitmap getCroppedBitmap(Bitmap bmp) {

		int w = bmp.getWidth();
		int h = bmp.getHeight();
		int radius = w > h ? h : w;

		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffa19774;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
				sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		android.graphics.Paint paint1 = new android.graphics.Paint();

		paint1.setColor(Color.WHITE);
		paint1.setStrokeWidth(2);
		paint1.setStyle(Style.STROKE);
		paint1.setAntiAlias(true);
		canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2,
				sbmp.getWidth() / 2 - 1, paint1);
		// if (sbmp != null)
		// {
		// sbmp.recycle();
		// sbmp = null;
		// }
		return output;
	}

	//根据图片路径 压缩图片并且覆盖成压缩后的图片
	public void compressImage(String imagePath, int w, int h) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 不把图片读到内存中,但依然可以计算出图片的大小
		BitmapFactory.decodeFile(imagePath, options);//压缩到到bitmap为null
		int height = options.outHeight;
		int width = options.outWidth;
		Lg.print("图片原始尺寸  w:"+width+" h:"+height);
		int inSampleSize = 1;
		int reqWidth = w;
		int reqHeight = h;
		Lg.print("图片缩小后尺寸  w:"+reqWidth+" h:"+reqHeight);
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			Lg.print("压缩比例  w:"+widthRatio+" h:"+heightRatio);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		// 在内存中创建bitmap对象，这个对象按照缩放大小创建的
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
		
		int degree = readPictureDegree(imagePath);
		Lg.print("照片角度："+degree);
		bitmap = rotateBitmap(bitmap, -degree);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		try {
			FileOutputStream fos = new FileOutputStream(imagePath);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Lg.print("图片压缩完成: inSampleSize:"+inSampleSize);
	}
	
	//判断照片角度
	public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
	//旋转照片
	public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress); 
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
}

package cn.android_mobile.core.ui.tween;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AView {
	private float x, y;
	private int alpha;
	private int textSize;
	public View view;
	private int backgroundcolor;

	private float radius, dx, dy;
	private int color;

	private int scrollX=0;
	private int scrollY=0;
	public int getScrollX() {
		return scrollX;
	}

	public void setScrollX(int scrollX) {
		this.scrollX = scrollX;
		if (view instanceof HorizontalScrollView) {
			((HorizontalScrollView) view).scrollTo(scrollX, scrollY);
		}else if(view instanceof ScrollView){
			((ScrollView) view).scrollTo(scrollX, scrollY);
		}else if(view instanceof ListView){
			((ListView) view).scrollTo(scrollX, scrollY);
		}
	}

	public int getScrollY() {
		return scrollY;
	}

	public void setScrollY(int scrollY) {
		this.scrollY = scrollY;
		if (view instanceof HorizontalScrollView) {
			((HorizontalScrollView) view).scrollTo(scrollX, scrollY);
		}else if(view instanceof ScrollView){
			((ScrollView) view).scrollTo(scrollX, scrollY);
		}else if(view instanceof ListView){
			((ListView) view).scrollTo(scrollX, scrollY);
		}
	}

	public AView(View v) {
		this.view = v;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getAlpha() {
		return alpha;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setX(float x) {
		this.x = x;

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		params.leftMargin = (int) x;
		view.setLayoutParams(params);
		// view.setX(x);
	}

	public void setY(float y) {
		this.y = y;

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		params.topMargin = (int) y;
		view.setLayoutParams(params);
		// view.setX(x);
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;


		if (view instanceof ImageView) {
			((ImageView) view).setAlpha((int) alpha);
		}
		// view.setX(x);
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
		if (view instanceof TextView) {
			((TextView) view).setTextSize(textSize);
		}
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		// view.setBackgroundColor(color);
	}

	public void setShadow() {
		if (view instanceof TextView) {
			((TextView) view).setShadowLayer(radius, dx, dy, color);
		}
	}

	public int getBackgroundcolor() {
		return backgroundcolor;
	}

	public void setBackgroundcolor(int backgroundcolor) {
		this.backgroundcolor = backgroundcolor;
		view.setBackgroundColor(backgroundcolor);
	}
	
	public void distroy(){
		view=null;
	}
}
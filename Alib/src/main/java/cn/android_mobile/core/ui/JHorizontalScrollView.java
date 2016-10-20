package cn.android_mobile.core.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;

public class JHorizontalScrollView extends HorizontalScrollView{

	public Object object=new Object();
	private Scroller mScroller;
	public JHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	public JHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public JHorizontalScrollView(Context context) {
		super(context);
	}
	
	private void init(Context context) {
		mScroller = new Scroller(context);
	}
	
}

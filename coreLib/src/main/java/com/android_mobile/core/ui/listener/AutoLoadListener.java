/******************************************************
 * @FileName: 【AutoLoadListener.java】
 * @Data:     【2013-7-16下午1:36:25】 
 * @author    【方珍】    
 * @Describe  【】
 * TODO
 ***************************[Begin]*******************/

package com.android_mobile.core.ui.listener;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/******************************************************
 * @FileName: 【AutoLoadListener.java】
 * @Data: 【2013-7-16下午1:36:25】
 * @author 【方珍】
 *******************/

public class AutoLoadListener extends PauseOnScrollListener {

	public AutoLoadListener(ImageLoader imageLoader, boolean pauseOnScroll,
			boolean pauseOnFling) {
		super(imageLoader, pauseOnScroll, pauseOnFling);
	}

	public static int itemHeight = 0;

	public interface AutoLoadCallBack {
		void nextPage();
	}

	private AutoLoadCallBack mCallback;

	 public AutoLoadListener(AutoLoadCallBack callback,boolean flag) {
		 super(ImageLoader.getInstance(),flag,flag);
		 this.mCallback = callback;
	 }

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		super.onScrollStateChanged(view, scrollState);
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			/*
			 * if (view.getFirstVisiblePosition() == 0) { int[] listView = new
			 * int[4]; int[] item = new int[4];
			 * view.getLocationOnScreen(listView);
			 * view.getChildAt(0).getLocationOnScreen(item); if (listView[1] ==
			 * item[1]) { Toast.makeText(view.getContext(), "滚动到顶部",
			 * Toast.LENGTH_LONG).show(); } }
			 */
			itemHeight = view.getChildAt(0).getHeight();
			if (canLodding
					&& view.getLastVisiblePosition() == view.getCount() - 1) {
				mCallback.nextPage();
			}
		}
	}

	private boolean canLodding = false;

	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		super.onScroll(arg0, arg1, arg2, arg3);
		if (arg1 + arg2 == arg3 /*&& arg1 != 0*/) {
			canLodding = true;
		} else {
			canLodding = false;
		}
	}
}

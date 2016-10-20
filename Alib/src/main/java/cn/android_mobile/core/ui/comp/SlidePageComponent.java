package cn.android_mobile.core.ui.comp;

import java.util.ArrayList;
import java.util.Stack;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.android_mobile.core.BasicActivity;
import cn.android_mobile.core.R;
import cn.android_mobile.core.base.BaseComponent;

import com.nostra13.universalimageloader.core.ImageLoader;

public class SlidePageComponent extends BaseComponent implements
		OnClickListener {
	public interface ISlidePageComponent {
		public void onItemLick(View v);
		public void onPageSelected(int pos);
	}

	
	private Stack<Bitmap> bitmaps = new Stack<Bitmap>();
	private ISlidePageComponent listener;
	public ViewPager viewpager;
	private LinearLayout points;
	private ArrayList<View> viewList;
	private ArrayList<TextView> _points;
	private PagerAdapter pagerAdapter;
	private boolean isDisplayPoint = false;
	public void setDisplayPointEnable(boolean bl) {
		isDisplayPoint = bl;
	}

	public void setListener(ISlidePageComponent listener) {
		this.listener = listener;
	}

	public SlidePageComponent(BasicActivity activity, int resId) {
		super(activity, resId);
	}

	public SlidePageComponent(BasicActivity activity, View v) {
		super(activity, v);
	}

	@Override
	public int onCreate() {
		return R.layout.component_banner_carousel;
	}

	@Override
	public void initComp() {
		viewList = new ArrayList<View>();
		_points = new ArrayList<TextView>();
		TextView tv;
		viewpager = (ViewPager) findViewById(R.id.banner_carousel_viewpager);
		points = (LinearLayout) findViewById(R.id.banner_carousel_viewpager_points);
		tv = new TextView(activity);
		tv.setBackgroundResource(R.drawable.banner_point_unselect);
		viewList.add(tv);
		points.removeAllViews();
		_points.clear();
		viewList.clear();
		for (int i = 0; i < viewList.size(); i++) {
			tv = new TextView(activity);
			tv.setBackgroundResource(R.drawable.banner_point_unselect);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					activity.dip2px(10), activity.dip2px(10));
			tv.setPadding(activity.dip2px(5), activity.dip2px(5),
					activity.dip2px(5), activity.dip2px(5));
			lp.setMargins(activity.dip2px(5), activity.dip2px(5),
					activity.dip2px(5), activity.dip2px(5));
			points.addView(tv, lp);
			_points.add(tv);
		}
		pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				if (viewList.size() > 0 && position < viewList.size()) {
					container.removeView(viewList.get(position));
				}
			}

			@Override
			public int getItemPosition(Object object) {
				return super.getItemPosition(object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList.get(position));
				return viewList.get(position);
			}
		};
		viewpager.setAdapter(pagerAdapter);

		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		pagerAdapter.notifyDataSetChanged();
		if (_points.size() > 0) {
			_points.get(0)
					.setBackgroundResource(R.drawable.banner_point_select);
		}
	}

	@Override
	public void initListener() {
		viewpager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					isTouch = true;
					break;
				case MotionEvent.ACTION_DOWN:
					isTouch = true;
					break;
				case MotionEvent.ACTION_UP:
					isTouch = false;
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	public void addPage(View v) {
		v.setOnClickListener(this);
		viewList.add(v);
		refersh();
	}
	public int getPageSize(){
		return viewList.size();
	}
	public void addPage(String imageUrl) {
		View view=LayoutInflater.from(activity).inflate(R.layout.banner_item, null);
		ImageView v = (ImageView) view.findViewById(R.id.banner_item_img);
		ImageLoader.getInstance().displayImage(imageUrl, v);
		viewList.add(view);
		refersh();
	}

	public void addPage(int... imageResId) {
		for (int i : imageResId) {
			View view=LayoutInflater.from(activity).inflate(R.layout.banner_item, null);
			ImageView v = (ImageView) view.findViewById(R.id.banner_item_img);
			if (i != -1) {
				Bitmap b = activity.readBitmap(i);
				bitmaps.add(b);
				v.setImageBitmap(b);
			}
			viewList.add(view);
		}
		refersh();
	}

	public void addPage(int imageResId, String flag) {
		if (imageResId != -1) {
			View view=LayoutInflater.from(activity).inflate(R.layout.banner_item, null);
			ImageView v = (ImageView) view.findViewById(R.id.banner_item_img);
			Bitmap b = activity.readBitmap(imageResId);
			bitmaps.add(b);
			v.setImageBitmap(b);
			viewList.add(view);
		}
		refersh();
	}

	public View addPage(int resId) {
		View v = activity.inflater.inflate(resId, null);
		v.setOnClickListener(this);
		viewList.add(v);
		refersh();
		return v;
	}

	public View geCurrenttPage() {
		return viewList.get(viewpager.getCurrentItem());
	}

	public void removeAllPages() {
		viewList.clear();
		refersh();
	}

	public void removePage(int index) {
		viewList.remove(index);
		refersh();
	}

	private void refersh() {
		if (isDisplayPoint) {
			points.removeAllViews();
			_points.clear();
			TextView tv;
			for (int i = 0; i < viewList.size(); i++) {
				tv = new TextView(activity);
				tv.setBackgroundResource(R.drawable.banner_point_unselect);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						activity.dip2px(6), activity.dip2px(6));
				tv.setPadding(activity.dip2px(5), activity.dip2px(5),
						activity.dip2px(5), activity.dip2px(5));
				lp.setMargins(activity.dip2px(5), activity.dip2px(5),
						activity.dip2px(5), activity.dip2px(5));
				points.addView(tv, lp);
				_points.add(tv);
			}
		}
		pagerAdapter.notifyDataSetChanged();
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		// int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		// int two = one * 2;// 页卡1 -> 页卡3 偏移量
		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			if (listener != null) {
				listener.onPageSelected(arg0);
			}
			if (isDisplayPoint) {
				for (TextView iv : _points) {
					iv.setBackgroundResource(R.drawable.banner_point_unselect);
				}
				_points.get(arg0).setBackgroundResource(
						R.drawable.banner_point_select);
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		for (int i = 0; i < viewList.size(); i++) {
			if (viewList.get(i) == arg0) {
				if (listener != null) {
					listener.onItemLick(viewList.get(i));
				}
			}
		}
	}

	public void hiddenPoint() {
		points.setVisibility(View.INVISIBLE);
	}

	public void onDestroy() {
		clear();
	}

	public void clear() {
		if (bitmaps.size() > 0) {
			bitmaps.pop().recycle();
		}
		bitmaps.clear();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
	private boolean loopFlag = true;
	private boolean isTouch = false;
	private int delay = 5000;
	private Runnable loopRunnable = new Runnable() {
		@Override
		public void run() {
			if (isTouch == false&&viewList.size()>0) {
				int currentItem = viewpager.getCurrentItem();
				int count = viewpager.getAdapter().getCount();
//				Lg.print(currentItem + "-" + count);
				currentItem++;
				if (currentItem >= count)
					currentItem = 0;
				viewpager.setCurrentItem(currentItem);
			}
			if (loopFlag) {
				activity.h.postDelayed(loopRunnable, delay);
			}
		}
	};
	public void startLoop(){
		loopFlag=true;
		activity.h.postDelayed(loopRunnable, delay);
	}
	public void stopLoop(){
		loopFlag=false;
		activity.h.removeCallbacks(loopRunnable);
	}
}

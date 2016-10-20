package cn.android_mobile.core.ui.comp;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import cn.android_mobile.core.ui.comp.vo.CycleData;
import cn.android_mobile.toolkit.Lg;

import com.nostra13.universalimageloader.core.ImageLoader;

public class CycleComponent extends BaseComponent implements
		OnClickListener {
	public interface ICycleComponent {
		public void onItemLick(Object obj);
		public void onPageSelected(int pos);
	}
	private ICycleComponent listener;
	public ViewPager viewpager; // 滑动组件
	private LinearLayout points; // 滑动点容器
	private View[] viewList;
	private ArrayList<CycleData> realViewList;
	private ArrayList<TextView> _points;
	private PagerAdapter pagerAdapter;
	private boolean isDisplayPoint = false;
	public void setDisplayPointEnable(boolean bl) {
		isDisplayPoint = bl;
	}

	public void setListener(ICycleComponent listener) {
		this.listener = listener;
	}

	public CycleComponent(BasicActivity activity, int resId) {
		super(activity, resId);
	}

	public CycleComponent(BasicActivity activity, View v) {
		super(activity, v);
	}

	@Override
	public int onCreate() {
		return R.layout.component_cycle_carousel;
	}

	@Override
	public void initComp() {
		realViewList = new ArrayList<CycleData>();
		viewList = new View[3];
		for (int i = 0; i < 3; i++) {
			View v = activity.inflater.inflate(R.layout.component_cycle, null);
			v.setOnClickListener(this);
			viewList[i]=v;
		}
		_points = new ArrayList<TextView>();
		viewpager = (ViewPager) findViewById(R.id.cycle_viewpager);
		points = (LinearLayout) findViewById(R.id.cycle_viewpager_points);
		pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return viewList.length;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(viewList[position]);
			}

			@Override
			public int getItemPosition(Object object) {
				return super.getItemPosition(object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList[position]);
				return viewList[position];
			}
		};
		viewpager.setAdapter(pagerAdapter);
		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		pagerAdapter.notifyDataSetChanged();
		if (_points.size() > 0) {
			_points.get(0)
					.setBackgroundResource(R.drawable.banner_point_select);
		}
		viewpager.setCurrentItem(1, false);
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
	public void setDataSource(ArrayList<CycleData> list){
		realViewList=list;
		if(list==null || list.size()==0) return;
		refersh();
	}
	public int getSize() {
		return realViewList.size();
	}

	public View geCurrenttPage() {
		return viewList[viewpager.getCurrentItem()];
	}
	private int index = 0;
	public void refersh() {
		if (isDisplayPoint) {
			points.removeAllViews();
			_points.clear();
			TextView tv;
			for (int i = 0; i < realViewList.size(); i++) {
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
		if(viewList.length!=3) return;
		ImageLoader.getInstance().displayImage(realViewList.get(left()).imgUrl, (ImageView)(viewList[0].findViewById(R.id.component_cycle_img)));
		ImageLoader.getInstance().displayImage(realViewList.get(index).imgUrl, (ImageView)(viewList[1].findViewById(R.id.component_cycle_img)));
		ImageLoader.getInstance().displayImage(realViewList.get(right()).imgUrl, (ImageView)(viewList[2].findViewById(R.id.component_cycle_img)));
		((TextView)(viewList[0].findViewById(R.id.component_cycle_title))).setText(realViewList.get(left()).title);
		((TextView)(viewList[1].findViewById(R.id.component_cycle_title))).setText(realViewList.get(index).title);
		((TextView)(viewList[2].findViewById(R.id.component_cycle_title))).setText(realViewList.get(right()).title);
		Lg.print(realViewList.get(left()));
		Lg.print(realViewList.get(index));
		Lg.print(realViewList.get(right()));
		pagerAdapter.notifyDataSetChanged();
	}

	public int right() {
		if (index == realViewList.size() - 1) {
			return 0;
		} else {
			return index + 1;
		}
	}

	public int left() {
		if ((index - 1) < 0) {
			return realViewList.size() - 1;
		} else {
			return index - 1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		// int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		// int two = one * 2;// 页卡1 -> 页卡3 偏移量
		private int position=0;
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 0) {
				Lg.print("onPageScrollStateChanged  " + arg0);
				if (position == 0) {
					Lg.print("onPageSelected " + position);
					Lg.print("左滑动");
					index = left();
					refersh();
					viewpager.setCurrentItem(1, false);
					Lg.print("index: " + index);
				} else if (position == 1) {
					// Lg.print("没有滑动");
				} else if (position == 2) {
					Lg.print("onPageSelected " + position);
					Lg.print("右滑动");
					index = right();
					refersh();
					viewpager.setCurrentItem(1, false);
					Lg.print("index: " + index);
				}

				if (listener != null) {
					listener.onPageSelected(index);
				}
				if (isDisplayPoint) {
					for (TextView iv : _points) {
						iv.setBackgroundResource(R.drawable.banner_point_unselect);
					}
					_points.get(index).setBackgroundResource(
							R.drawable.banner_point_select);
				}
				isTouch=false;
			}else{
				isTouch=true;
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			isTouch=true;
		}

		public void onPageSelected(int position) {
			this.position=position;
		}
	}

	@Override
	public void onClick(View arg0) {
		// for (int i = 0; i < viewList.size(); i++) {
		// if (viewList.get(i) == arg0) {
		 if (listener != null) {
			 listener.onItemLick(realViewList.get(index).obj);
		 }
		// }
		// }
	}

	public void hiddenPoint() {
		points.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initData() {

	}

	private boolean loopFlag = false;
	private boolean isTouch = false;
	private int delay = 5000;
	private Runnable loopRunnable = new Runnable() {
		@Override
		public void run() {
			if (isTouch == false && realViewList.size() > 0) {
				int currentItem = viewpager.getCurrentItem();
				int count = viewpager.getAdapter().getCount();
				// Lg.print(currentItem + "-" + count);
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

	public void startLoop() {
		viewpager.setCurrentItem(0);
		if(loopFlag==false){
			loopFlag = true;
			activity.h.postDelayed(loopRunnable, delay);
		}
	}

	public void stopLoop() {
		if(loopFlag==true){
			loopFlag = false;
			activity.h.removeCallbacks(loopRunnable);
		}
	}
}

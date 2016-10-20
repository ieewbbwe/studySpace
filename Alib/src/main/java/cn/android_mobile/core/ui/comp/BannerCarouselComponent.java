package cn.android_mobile.core.ui.comp;

import java.util.ArrayList;
import java.util.Stack;
import java.util.zip.Inflater;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.android_mobile.core.BasicActivity;
import cn.android_mobile.core.R;
import cn.android_mobile.core.ui.BasicComponent;
import cn.android_mobile.toolkit.Lg;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Demo
 * 
 * @Override protected void onCreate(Bundle savedInstanceState) {
 *           super.onCreate(savedInstanceState);
 *           setContentView(R.layout.activity_main); banner = new
 *           BannerCarouselComponent(this, R.id.main_layout);
 *           banner.addItem(banner.new
 *           Item("http://www.baidu.com/img/baidu_sylogo1.gif"));
 *           banner.addItem(banner.new Item(R.drawable.ic_launcher));
 *           banner.addItem(banner.new
 *           Item("http://www.baidu.com/img/baidu_sylogo1.gif"));
 *           banner.addItem(banner.new Item(R.drawable.ic_launcher));
 *           banner.addItem(banner.new
 *           Item("http://www.baidu.com/img/baidu_sylogo1.gif"));
 *           banner.addItem(banner.new Item(R.drawable.ic_launcher));
 *           banner.refersh(); }
 * @Override protected void onDestroy() { banner.onDestroy(); super.onDestroy();
 *           }
 * @author Administrator
 * 
 */
public class BannerCarouselComponent extends BasicComponent implements
		OnClickListener {
	public interface IBannerCarouselComponent {
		public void onItemLick(Item item);
		public void onPageSelected(int pos);
	}

	private IBannerCarouselComponent listener;
	public ViewPager viewpager;
	private LinearLayout points;
	private ArrayList<View> viewList = new ArrayList<View>();
	private ArrayList<TextView> _points = new ArrayList<TextView>();
	private Stack<Bitmap> bitmaps = new Stack<Bitmap>();
	private ArrayList<Item> items = new ArrayList<Item>();
	private boolean isTouch = false;
	private int delay = 5000;

	public class Item {
		public Item(String url) {
			this.url = url;
		}

		public Item(int id) {
			this.id = id;
		}

		public int id = -1;
		public String url = "";
		public Object obj = null;
	}

	public void addListener(IBannerCarouselComponent listener) {
		this.listener = listener;
	}

	public BannerCarouselComponent(BasicActivity activity, int resId) {
		super(activity, resId);
	}

	public BannerCarouselComponent(BasicActivity activity, ViewGroup v) {
		super(activity, v);
	}

	@Override
	public int onCreate() {
		return R.layout.component_banner_carousel;
	}

	@Override
	public void initComp() {
		viewpager = (ViewPager) findViewById(R.id.banner_carousel_viewpager);
		points = (LinearLayout) findViewById(R.id.banner_carousel_viewpager_points);
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

	public void addItem(Item i) {
		items.add(i);
	}

	public void removeAll() {
		loopFlag = false;
		items.clear();
		refersh();
	}

	public Item getItem() {
		return items.get(viewpager.getCurrentItem());
	}

	public void refersh() {
		refersh(5000);
	}

	public void refersh(int delay) {
		this.delay = delay;
		points.removeAllViews();
		_points.clear();
		viewList.clear();
		TextView tv;
		for (int i = 0; i < items.size(); i++) {
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
		if (_points.size() > 0) {
			_points.get(0)
					.setBackgroundResource(R.drawable.banner_point_select);
			// viewList = // 将要分页显示的View装入数组中
			for (int i = 0; i < items.size(); i++) {
				View view=LayoutInflater.from(activity).inflate(R.layout.banner_item, null);
				ImageView iv1 = (ImageView) view.findViewById(R.id.banner_item_img);
//				iv1.setScaleType(ScaleType.CENTER);
				Item item = items.get(i);
				if (item.id != -1) {
					Bitmap b = activity.readBitmap(item.id);
					bitmaps.add(b);
					iv1.setImageBitmap(b);
				} else {
					ImageLoader.getInstance().displayImage(item.url, iv1);
				}
				view.setOnClickListener(this);
				viewList.add(view);
			}
			// pagerAdapter.notifyDataSetChanged();
			viewpager.setAdapter(pagerAdapter);
			
			viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
			// viewpager.setCurrentItem(0);
			loopFlag=true;
			activity.h.postDelayed(loopRunnable, delay);
		}else{
			pagerAdapter.notifyDataSetChanged();
			viewpager.removeAllViews();
		}
	}

	private boolean loopFlag = true;
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
	private PagerAdapter pagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (viewList.size() > 0&&position<viewList.size()) {
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

	public class MyOnPageChangeListener implements OnPageChangeListener {
		// int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		// int two = one * 2;// 页卡1 -> 页卡3 偏移量
		public void onPageScrollStateChanged(int arg0) {
			
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		public void onPageSelected(int arg0) {
			if(listener!=null){
				listener.onPageSelected(arg0);
			}
			// Lg.print(arg0);
			for (TextView iv : _points) {
				iv.setBackgroundResource(R.drawable.banner_point_unselect);
			}
			_points.get(arg0).setBackgroundResource(
					R.drawable.banner_point_select);
		}
	}

	public void onDestroy() {
		clear();
	}

	public void clear() {
		if (bitmaps.size() > 0) {
			bitmaps.pop().recycle();
		}
		bitmaps.clear();
		bitmaps = null;
		activity.h.removeCallbacks(loopRunnable);
	}

	@Override
	public void onClick(View arg0) {
		for (int i = 0; i < viewList.size(); i++) {
			if (viewList.get(i) == arg0) {
				if (listener != null) {
					listener.onItemLick(items.get(i));
				}
			}
			
		}
		
	}
	
	public void stopLoop(){
		loopFlag=false;
		activity.h.removeCallbacks(loopRunnable);
	}
	
	public void hiddenPoint(){
		points.setVisibility(View.INVISIBLE);
	}
	
}

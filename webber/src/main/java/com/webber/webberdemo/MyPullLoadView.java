package com.webber.webberdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyPullLoadView extends ListView implements OnScrollListener {

	private List<String> infos;
	private int downY;
	private int firstVisibleItem;
	private View head;
	private int headHeight;
	private TextView mInfoTv;
	private View arrow;
	private TextView mTimeTv;

	private enum State {
		PULL_TO_REFRESH, RELASE_REFRESH, REFRESHING
	};

	private Enum currentState = null;
	private OnRefreshListener listener;

	public MyPullLoadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		inflate(context, R.layout.list_load_view, null);
		initHeader(context);
		initFooter(context);
		initList(context);
	}

	private void initFooter(Context context) {
		footer = inflate(context, R.layout.foot_view, null);
		footer.measure(0, 0);
		int footHeight = footer.getMeasuredHeight();
		
		footer.setPadding(0, -footHeight, 0, 0);
		addFooterView(footer);
	}

	private void initHeader(Context context) {
		head = inflate(context, R.layout.head_view, null);
		head.measure(0, 0);
		headHeight = head.getMeasuredHeight();
		head.setPadding(0, -headHeight, 0, 0);
		// 找到头部的控件
		arrow = head.findViewById(R.id.iv_arrow);
		mInfoTv = (TextView) head.findViewById(R.id.tv_info);
		mTimeTv = (TextView) head.findViewById(R.id.tv_time);
		mPb = (ProgressBar) head.findViewById(R.id.pb);
		
		
		mPb.setVisibility(View.INVISIBLE);
		currentState = State.PULL_TO_REFRESH;
		isRelease = false;
		
		addHeaderView(head);
	}

	private void initList(Context context) {
		
		setOnScrollListener(this);
	}

	/*private void initDate() {
		infos = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			String str = "我是数据" + i;
			infos.add(str);
		}
	}*/

	/**
	 * 触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveY = (int) ev.getY();
			int dY = moveY - downY;
			int paddingTop = -headHeight + dY;
			// 是head并且有下拉距离的话才改变head的padding  有BUG
			if (firstVisibleItem == 0 && dY > 0 && paddingTop<headHeight+1) {
				head.setPadding(0, paddingTop, 0, 0);
				if (paddingTop >= 0 && currentState != State.RELASE_REFRESH) {
					// 变成释放刷新
					currentState = State.RELASE_REFRESH;
					mInfoTv.setText("释放刷新");
					isRelease = true;
					changeArrow();
				}else if(paddingTop<0 && currentState!=State.PULL_TO_REFRESH){
					//变成下拉刷新
					isRelease = false;
					currentState = State.PULL_TO_REFRESH;
					mInfoTv.setText("下拉刷新");
					changeArrow();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(currentState == State.PULL_TO_REFRESH){
				//没有超过头部弹回去
				head.setPadding(0, -headHeight, 0, 0);
			}else if(currentState == State.RELASE_REFRESH){
				//当状态是释放刷新时
				mInfoTv.setText("正在加载...");
				mPb.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.INVISIBLE);
				currentState = State.REFRESHING;
				//TODO 设置不可啦
				//提供一个接口 谁用我谁给我数据
				if(listener!=null){
					listener.OnLoadingMore();
				}
			}
			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}
	public void setListener(OnRefreshListener listener){
		this.listener = listener;
	}
	public interface OnRefreshListener{
		void OnLoadingMore();
		void OnLoadingFoot();
	}
	private boolean isRelease;
	private ProgressBar mPb;
	private View footer;

	/**
	 * 改变箭头动画
	 */
	// 动画只有两种情况，下拉刷新，释放刷新
	private void changeArrow() {
		RotateAnimation ra = new RotateAnimation(isRelease ? 0 : -180, isRelease ? -180
				: -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(200);
		ra.setFillAfter(true);
		
		arrow.startAnimation(ra);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(getLastVisiblePosition()==getCount()-1){
			Log.i("test", "到底了");
			footer.setPadding(0, 0, 0, 0);
			setSelection(getCount());
			if(listener!=null){
				listener.OnLoadingFoot();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		//到底后显示加载
	}
	/**
	 * 刷新成功
	 */
	public void finishRefresh() {
		head.setPadding(0, -headHeight, 0, 0);
		arrow.setVisibility(View.VISIBLE);
		mPb.setVisibility(View.INVISIBLE);
		mInfoTv.setText("下拉刷新");
		currentState = State.PULL_TO_REFRESH;
		//更新刷新时间
		SimpleDateFormat sdf = new SimpleDateFormat();
		String format = sdf.format(new Date());
		mTimeTv.setText(format);
	}
}

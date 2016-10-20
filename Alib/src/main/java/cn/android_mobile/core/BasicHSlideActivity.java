package cn.android_mobile.core;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import cn.android_mobile.core.enums.Direction;
import cn.android_mobile.toolkit.Lg;

@SuppressLint("NewApi")
public abstract class BasicHSlideActivity extends BasicActivity implements
		View.OnTouchListener {

	private ViewStub menuStub = null;
	private ViewStub bodyStub = null;
	protected View mView = null;
	protected View bView = null;
	private LinearLayout slidRoot;
	private int mWidth = 0;
	private int navHeight = 55;
	private int basicLeft;
	private boolean isOpen = true;
	private boolean isAnimRunning = false;
	private int left;
	private int offset;
	private int moveWidth;
	private long time = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public abstract int onCreateMenuWidth();

	public void setContentView(int menuLayoutResID, int bodyLayoutResID) {
		super.setContentView(R.layout.activity_basic_slide);
		mWidth = onCreateMenuWidth();
		slidRoot = (LinearLayout) findViewById(R.id.basic_slide_root_linearlayout);
		menuStub = (ViewStub) findViewById(R.id.basic_slide_menu_relativelayout);
		bodyStub = (ViewStub) findViewById(R.id.basic_slide_body_relativelayout);
		menuStub.setLayoutResource(menuLayoutResID);
		bodyStub.setLayoutResource(bodyLayoutResID);
		mView = menuStub.inflate();
		bView = bodyStub.inflate();

		LayoutParams rLp = slidRoot.getLayoutParams();
		rLp.width = (int) SCREEN_WIDTH + mWidth;
		// rLp.height = (int) SCREEN_HEIGHT - dip2px(getNavHeight());
		rLp.height = (int) SCREEN_HEIGHT;
		slidRoot.setLayoutParams(rLp);
		LayoutParams mLp = mView.getLayoutParams();
		mLp.width = onCreateMenuWidth();
		// mLp.height = (int) SCREEN_HEIGHT - dip2px(getNavHeight());
		mView.setLayoutParams(mLp);
		LayoutParams bLp = bView.getLayoutParams();
		bLp.width = (int) SCREEN_WIDTH;
		// bLp.height = (int) SCREEN_HEIGHT - dip2px(getNavHeight());
		bView.setLayoutParams(bLp);
		// slidRoot.setOnTouchListener(this);
		// mView.setOnTouchListener(this);
		// bView.setOnTouchListener(this);
		List<View> allChildViews = getAllChildViews(bView);
		for (View view : allChildViews) {
			view.setOnTouchListener(this);
		}
		 allChildViews = getAllChildViews(mView);
		for (View view : allChildViews) {
			view.setOnTouchListener(this);
		}
	}

	public View findViewById(Direction d,int id) {
		View v = null;
		if (d == Direction.LEFT) {
			v = mView.findViewById(id);
		}else if (d == Direction.CENTER) {
			v = bView.findViewById(id);
		}
		return v;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isAnimRunning)
			return false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			basicLeft = slidRoot.getLeft();
			left = (int) event.getRawX();
			Lg.print("action_down:" + left);
			break;
		case MotionEvent.ACTION_MOVE:
			offset = basicLeft + (int) event.getRawX() - left;
			moveWidth = (int) event.getRawX() - left;
			if (offset < -mWidth) {
				slidRoot.layout(-mWidth, slidRoot.getTop(),
						(int) (SCREEN_WIDTH + mWidth),
						slidRoot.getMeasuredHeight());
			} else if (offset >= 0) {
				slidRoot.layout(0, slidRoot.getTop(),
						(int) (SCREEN_WIDTH + mWidth),
						slidRoot.getMeasuredHeight());
			} else {
				slidRoot.layout(offset, slidRoot.getTop(),
						(int) (SCREEN_WIDTH + mWidth),
						slidRoot.getMeasuredHeight());
			}
			break;
		case MotionEvent.ACTION_UP:
			Lg.print("ACTION_UP");
			Lg.print("action_move:" + moveWidth + "  offset:" + offset
					+ " left:" + slidRoot.getLeft() + "   mwidth:" + mWidth);
			// if (moveWidth > 0) {
			// ObjectAnimator.ofFloat(v, "left",0)
			// .setDuration(time).start();
			// } else {
			// ObjectAnimator.ofFloat(v, "left",-mWidth)
			// .setDuration(time).start();
			// Lg.print("action_move:" + moveWidth + "  offset:" + offset
			// + " left:" + slidRoot.getLeft());
			// }
			if (isAnimRunning == false) {
				if (moveWidth > 0) {
					if (!isOpen) {
						isOpen = true;
						if (moveWidth > mWidth)
							moveWidth = mWidth;
						TranslateAnimation ta = new TranslateAnimation(0,
								mWidth - moveWidth, 0, 0);
						ta.setInterpolator(new DecelerateInterpolator());
						ta.setDuration(time);
						ta.setFillAfter(true);
						ta.setAnimationListener(new AnimListener(slidRoot,
								new AnimParams(0, slidRoot.getTop(),
										(int) (SCREEN_WIDTH + mWidth), slidRoot
												.getMeasuredHeight())));
						slidRoot.startAnimation(ta);
					}
				} else {
					if (isOpen) {
						isOpen = false;
						if (moveWidth < -mWidth)
							moveWidth = -mWidth;
						TranslateAnimation ta = new TranslateAnimation(0,
								-mWidth - moveWidth, 0, 0);
						ta.setInterpolator(new DecelerateInterpolator());
						ta.setDuration(time);
						ta.setAnimationListener(new AnimListener(slidRoot,
								new AnimParams(-mWidth, slidRoot.getTop(),
										(int) (SCREEN_WIDTH + mWidth), slidRoot
												.getMeasuredHeight())));
						ta.setFillAfter(true);
						slidRoot.startAnimation(ta);
					}
				}
			}
			break;
		default:
			break;

		}
		return false;
	}

	public int getNavHeight() {
		return navHeight;
	}

	public void setNavHeight(int navHeight) {
		this.navHeight = navHeight;
	}

	class AnimParams {
		public int left, right, top, bottom;

		public AnimParams(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
	}

	class AnimListener implements AnimationListener {

		private AnimParams p;
		private View v;

		public AnimListener(View v, AnimParams p) {
			this.p = p;
			this.v = v;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			isAnimRunning = false;
			slidRoot.layout(p.left, p.top, p.right, p.bottom);
			slidRoot.clearAnimation();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			isAnimRunning = true;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			isAnimRunning = true;
		}
	}
}

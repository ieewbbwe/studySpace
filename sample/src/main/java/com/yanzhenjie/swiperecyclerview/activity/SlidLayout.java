package com.yanzhenjie.swiperecyclerview.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yanzhenjie.swiperecyclerview.R;

/**
 * Created by mxh on 2017/1/4.
 * Describe： 滑动删除
 */
public class SlidLayout extends FrameLayout {

    private View mContentView;
    private View mMenuView;
    private View mMenuBgView;
    private int mDownX;
    private int mDownY;
    private int mLastX;
    private boolean isIntercepted;
    private int mScaledTouchSlop;
    private int mScaledMaximumFlingVelocity;
    private int mScaledMinimumFlingVelocity;
    private boolean isMenuOpen = false;

    public SlidLayout(Context context) {
        this(context, null);
    }

    public SlidLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewGroup mMainMenuView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.item_slid, this, false);
        mContentView = mMainMenuView.findViewById(R.id.content_fl);
        mMenuBgView = mMainMenuView.findViewById(R.id.bg_iv);
        mMenuView = mMainMenuView.findViewById(R.id.delete_ll);

        ViewConfiguration mViewConfig = ViewConfiguration.get(getContext());
        mScaledTouchSlop = mViewConfig.getScaledTouchSlop();
        mScaledMinimumFlingVelocity = mViewConfig.getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = mViewConfig.getScaledMaximumFlingVelocity();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = mLastX = (int) ev.getX();
                mDownY = (int) ev.getY();
                isIntercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int disX = (int) (ev.getX() - mDownX);
                int disY = (int) (ev.getY() - mDownY);
                isIntercepted = Math.abs(disX) > mScaledTouchSlop && Math.abs(disX) > Math.abs(disY);
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }
}

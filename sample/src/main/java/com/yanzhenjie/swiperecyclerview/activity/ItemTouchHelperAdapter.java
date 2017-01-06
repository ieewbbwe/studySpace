package com.yanzhenjie.swiperecyclerview.activity;

/**
 * Created by mxh on 2017/1/5.
 * Describe：
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}

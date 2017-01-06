package com.yanzhenjie.swiperecyclerview.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.yanzhenjie.swiperecyclerview.R;

/**
 * Created by mxh on 2017/1/5.
 * Describe：
 */

public class SimpleItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private Context mContext;
    private ItemTouchHelperAdapter mAdapter;
    private Paint mShaderP = new Paint();
    private Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Shader mGradientShader;
    private int SCREEN_WIRTH;

    public SimpleItemTouchHelperCallBack() {
    }

    public SimpleItemTouchHelperCallBack(ItemTouchHelperAdapter adapter, Context context) {
        this.mAdapter = adapter;
        this.mContext = context;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        this.SCREEN_WIRTH = wm.getDefaultDisplay().getWidth();
        mGradientShader = new LinearGradient(0, 0, SCREEN_WIRTH, 0, Color.parseColor("#FF333A"),
                Color.parseColor("#cc00c8"), Shader.TileMode.REPEAT);
        p.setColor(Color.WHITE);
        p.setTextSize(40);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 支持向左滑动
        //final int swipeFlags = ItemTouchHelper.END;
        // 支持向右滑动
        final int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Log.d("andy", "onChildDraw：" + dX);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            Bitmap icon;
            icon = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_action_delete);

            if (dX <= 0) {
                float itemHeight = (float) itemView.getBottom() - (float) itemView.getTop();
                mShaderP.setShader(mGradientShader);
                // Draw Rect with varying right side, equal to displacement dX
                c.drawRect((float) itemView.getWidth() + dX, (float) itemView.getTop(), itemView.getWidth(),
                        (float) itemView.getBottom(), mShaderP);
                c.drawBitmap(icon, (float) itemView.getWidth() - SCREEN_WIRTH / 6/* - Utils.dpToPx(16)*/, (float) itemView.getTop() +
                        (itemHeight - icon.getHeight()) / 2 - 20, p);
                c.drawText("删除", (float) itemView.getWidth() - SCREEN_WIRTH / 6 + 6, (float) itemView.getTop() +
                        (itemHeight + icon.getHeight()) / 2 + 20/* - Utils.dpToPx(16)*/, p);
                icon.recycle();
            }
        }
    }
}

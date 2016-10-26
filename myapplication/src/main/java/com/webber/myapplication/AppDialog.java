package com.webber.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android_mobile.core.ui.comp.dialog.BasicDialog;

/**
 * Created by mxh on 2016/10/26.
 * Describe：
 */
public class AppDialog extends BasicDialog {
    public AppDialog(Context context) {
        super(context);
    }

    @Override
    protected int onCreate() {
        return R.layout.dialog_app;
    }

    @Override
    public void onViewCreated(View v) {
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(true);// 设置点击dialog外面的界面 关闭dialog
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        //dialogWindow.setWindowAnimations(R.style.dialog_anim);
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        lp.width = (int) (0.9 * dm.widthPixels);
        //lp.height += dm.density * 3;
        //lp.height = (int) (0.21 * dm.heightPixels);
        dialogWindow.setAttributes(lp);
    }
}

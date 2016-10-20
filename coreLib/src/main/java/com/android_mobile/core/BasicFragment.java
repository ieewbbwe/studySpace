package com.android_mobile.core;

import android.annotation.SuppressLint;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android_mobile.core.enums.CacheType;
import com.android_mobile.core.enums.ModalDirection;
import com.android_mobile.core.event.BasicEvent;
import com.android_mobile.core.event.BasicEventDispatcher;
import com.android_mobile.core.net.BasicAsyncTask;
import com.android_mobile.core.net.IBasicAsyncTask;
import com.android_mobile.core.net.IBasicAsyncTaskFinish;
import com.android_mobile.core.net.http.Service;
import com.android_mobile.core.net.http.ServiceRequest;
import com.android_mobile.core.ui.NavigationBar;
import com.android_mobile.core.ui.listener.IMediaImageListener;
import com.android_mobile.core.ui.listener.IMediaPicturesListener;
import com.android_mobile.core.ui.listener.IMediaScannerListener;
import com.android_mobile.core.ui.listener.IMediaSoundRecordListener;
import com.android_mobile.core.ui.listener.IMediaVideoListener;
import com.android_mobile.core.utiles.CacheUtil;
import com.android_mobile.core.utiles.Lg;

import java.util.List;
import java.util.Stack;

@SuppressLint("NewApi")
public abstract class BasicFragment extends Fragment implements
        IBasicAsyncTaskFinish, IBasicCoreMethod {
    public View v;
    public BasicActivity activity;
    public BasicApplication application;
    protected boolean asyncWithProgress = true;
    protected NavigationBar navigationBar = null;
    private Stack<BasicAsyncTask> tasks = new Stack<BasicAsyncTask>();
    public String name = this.getClass().getName();
    public final String TAG = this.getClass().getSimpleName();

    private void printLog() {
        Lg.print(TAG, Thread.currentThread().getStackTrace()[3]
                .getMethodName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        printLog();
        v = inflater.inflate(create(), container, false);
        activity = (BasicActivity) getActivity();
        application = (BasicApplication) activity.getApplication();
        navigationBar = activity.navigationBar;
//		Lg.print(name);
        activity.updateSkin(BasicActivity.skinColor);
        init();
        return v;
    }

    @Override
    public void async(ServiceRequest req, IBasicAsyncTask callback) {
        activity.async(req, callback);
    }

    @Override
    public void async(IBasicAsyncTask callback, ServiceRequest req, Service service) {
        activity.async(callback, req, service);
    }

    @Override
    public void async(IBasicAsyncTask callback, ServiceRequest req, Service service, CacheType cacheType) {
        activity.async(callback, req, service, cacheType);
    }

    @Override
    public void asyncTaskFinish(BasicAsyncTask task) {
        tasks.remove(task);
        if (tasks.size() == 0 && asyncWithProgress) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hiddenProgressBar();
                }
            });
        }
    }

    protected abstract int create();

    protected void init() {
        printLog();
        initComp();
        initListener();
        initData();
    }

    protected abstract void initComp();

    protected abstract void initListener();

    protected abstract void initData();


    public void pushFragment(BasicFragment f) {
        activity.pushFragment(f);
    }

    public void popBackStack() {
        activity.popFragment();
    }

    public void pushActivity(Class<?> a) {
        activity.pushActivity(a);
    }

    public void pushActivity(Intent i) {
        activity.pushActivity(i);
    }

    public void pushActivity(Class<?> a, boolean finishSelf) {
        activity.pushActivity(a, finishSelf);
    }

    public void popActivity() {
        activity.popActivity();
    }

    @Override
    public void onDestroy() {
        printLog();
        removeBasicEvent();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        printLog();
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        printLog();
        if (activity != null)
            activity.currentFragment = this;
        super.onStart();
    }

    @Override
    public void onStop() {
        printLog();
        removeBasicEvent();
        while (tasks.size() > 0) {
            cancelAsyncTask(tasks.pop());
        }
//		hiddenProgressBar();
//		cancelProgress();
        super.onStop();
    }

    public View findViewById(int id) {
        return v.findViewById(id);
    }

    // 取消异步调用
    public void cancelAsyncTask(BasicAsyncTask task) {
        activity.cancelAsyncTask(task);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        printLog();
        super.onViewCreated(view, savedInstanceState);
    }

    public void displayProgressBar() {
        activity.displayProgressBar();
    }

    public void hiddenProgressBar() {
        activity.hiddenProgressBar();
    }

    /**
     * 显示进度条
     */
    public void showProgress() {
        activity.showProgress(R.string.loading_net, true);
    }

    public void showProgress(boolean canBack) {
        activity.showProgress(R.string.loading_net, canBack);
    }

    /**
     * 取消进度条
     */
    public void cancelProgress() {
        activity.cancelProgress();
    }

    /**
     * 退出应用
     */
    public void exitAppWithToast() {
        activity.exitAppWithToast();
    }

    // 关闭软键盘
    public void closeSoftInput() {
        activity.closeSoftInput();
    }

    /**
     * 显示Toast
     */
    public void toast(int resId) {
        activity.toast(resId);
    }

    public void toast(String text) {
        activity.toast(text);
    }

    public void changeFragment(int layoutId, BasicFragment f) {
        activity.pushFragment(layoutId, f);
    }

    public void changeFragment(BasicFragment f) {
        if (activity.fragmentBodyLayoutRes != -1)
            activity.pushFragment(activity.fragmentBodyLayoutRes, f);
    }

    /* 显示Dialog的method */
    public void showDialog(String mess) {
        activity.showDialog(mess);
    }

    public void showDialog(String title, String mess) {
        activity.showDialog(title, mess);
    }

    public void setTitle(String title) {
        activity.setTitle(title);
    }

    @Override
    public void isDisplayFragmentEffect(boolean flag) {
        activity.isDisplayFragmentEffect(flag);
    }

    @Override
    public void isDisplayProgressByHttpRequest(boolean b) {
        activity.isDisplayProgressByHttpRequest(b);
    }

    @Override
    public void setEditViewClearButton(EditText et, View clear,
                                       boolean clearOnce) {
        activity.setEditViewClearButton(et, clear, clearOnce);
    }

    @Override
    public void showProgress(int resID, boolean canBack) {
        activity.showProgress(resID, canBack);
    }

    @Override
    public void showProgress(String res, boolean canBack) {
        activity.showProgress(res, canBack);
    }

    @Override
    public boolean isEmpty(Object obj) {
        return activity.isEmpty(obj);
    }

    @Override
    public void showSoftInput(EditText et) {
        activity.showSoftInput(et);
    }

    @Override
    public int getVersionCode() {
        return activity.getVersionCode();
    }

    @Override
    public void sendMailByIntent(String msg, String email) {
        activity.sendMailByIntent(msg, email);
    }

    @Override
    public boolean isNetworkAvailable() {
        return activity.isNetworkAvailable();
    }

    @Override
    public int dip2px(float dipValue) {
        return activity.dip2px(dipValue);
    }

    @Override
    public int px2dip(float pxValue) {
        return activity.px2dip(pxValue);
    }

    @Override
    public List<View> getAllChildViews() {
        return activity.getAllChildViews();
    }

    @Override
    public List<View> getAllChildViews(View view) {
        return activity.getAllChildViews(view);
    }

    @Override
    public void setMediaImageListener(IMediaImageListener listener) {
        activity.setMediaImageListener(listener);
    }

    @Override
    public void setMediaPictureListener(IMediaPicturesListener listener) {
        activity.setMediaPictureListener(listener);
    }

    @Override
    public void setMediaVideoListener(IMediaVideoListener listener) {
        activity.setMediaVideoListener(listener);
    }

    @Override
    public void setMediaSoundRecordListener(IMediaSoundRecordListener listener) {
        activity.setMediaSoundRecordListener(listener);
    }

    @Override
    public void setMediaScannerListener(IMediaScannerListener listener) {
        activity.setMediaScannerListener(listener);
    }

    @Override
    public void startPictures() {
        activity.startPictures();
    }

    @Override
    public void startCamera() {
        activity.startCamera();
    }

    @Override
    public void startVideo() {
        activity.startVideo();
    }

    @Override
    public void startSoundRecorder() {
        activity.startSoundRecorder();
    }

    @Override
    public PackageInfo getPackageInfo() {
        return activity.getPackageInfo();
    }

    @Override
    public boolean hasSdcard() {
        return activity.hasSdcard();
    }

    @Deprecated
    @Override
    public void setRootFragmentView(int layoutId) {
        activity.setRootFragmentView(layoutId);
    }

    @Override
    public void setPushFragmentLayout(int layoutId) {
        activity.setPushFragmentLayout(layoutId);
    }

    @Override
    public void pushFragment(int layoutId, BasicFragment f) {
        activity.pushFragment(layoutId, f);
    }

    @Override
    public void popFragment() {
        activity.popFragment();
    }

    @Override
    public void focus(View v) {
        activity.focus(v);
    }

    @Override
    public void displayViewAnimation(View v, int fx, float depth) {
        activity.displayViewAnimation(v, fx, depth);
    }

    @Override
    public void startScan() {
        activity.startScan();
    }

    @Override
    public void startScan(Intent i) {
        activity.startScan(i);
    }

    @Override
    public void pushFragment(BasicFragment f, boolean flag) {
        activity.pushFragment(f, flag);
    }

    @Override
    public void pushFragment(int layoutId, BasicFragment f, boolean flag) {
        activity.pushFragment(layoutId, f, flag);
    }

    @Override
    public void initModalFragment(BasicFragment f) {
        activity.initModalFragment(f);
    }

    @Override
    public void pushModalFragment(ModalDirection d, int widthDip, int time, BasicFragment f, boolean hasBackground) {
        activity.pushModalFragment(d, widthDip, time, f, true);
    }

    @Override
    public void pushModalFragment(ModalDirection d, int widthDip, int time,
                                  BasicFragment f) {
        activity.pushModalFragment(d, widthDip, time, f);

    }

    @Override
    public void pushModalFragment(ModalDirection d,
                                  int widthDip, BasicFragment f) {
        activity.pushModalFragment(d, widthDip, f);
    }

    @Override
    public void pushModalFragment(int widthDip, BasicFragment f) {
        activity.pushModalFragment(widthDip, f);
    }

    @Override
    public void pushModalFragment(BasicFragment f) {
        activity.pushModalFragment(f);
    }

    @Override
    public void pushActivityForResult(Class<?> a, int requestCode) {
        activity.pushActivityForResult(a, requestCode);
    }

    @Override
    public void setSkin(String color) {
        activity.setSkin(color);
    }

    @Override
    public void setSkin(int colorRes) {
        activity.setSkin(colorRes);
    }

    @Override
    public void updateSkin(int skinColor) {
        if (activity != null)
            activity.updateSkin(skinColor);
    }

    @Override
    public void showDialog(String title, String mess,
                           OnClickListener clickListener, boolean cancelable) {
        activity.showDialog(title, mess, clickListener, cancelable);
    }

    @Override
    public void showDialog(String title, String mess,
                           OnClickListener clickListener, OnClickListener cancelListener,
                           boolean cancelable) {
        activity.showDialog(title, mess, clickListener, cancelListener, cancelable);
    }

    @Override
    public void showDialog(String title, String mess,
                           OnClickListener clickListener, OnClickListener cancelListener) {
        activity.showDialog(title, mess, clickListener, cancelListener, true);
    }

    @Override
    public void popModalFragment() {
        activity.popModalFragment();
    }

    @Override
    public void displayProgressBar(String s) {
        activity.displayProgressBar(s);
    }

    @Override
    public void dispatchBasicEvent(BasicEvent e) {
        BasicEventDispatcher.dispatcher(e);
    }

    @Override
    public void removeBasicEvent() {
        BasicEventDispatcher.removeEventListenerByKey(this.getClass().getName());
    }

    @Override
    public Bitmap takeScreenshot(View view) {
        assert view.getWidth() > 0 && view.getHeight() > 0;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void displayHelpView(Integer... resIds) {
        if (BasicApplication.helpFlag == 100 || resIds == null
                || resIds.length == 0)
            return;
        for (int i = resIds.length - 1; i >= 0; i--) {
            activity.helpFragmentImgs.add(resIds[i]);
        }
        activity.displayHelpView(activity.helpFragmentImgs.pop());
    }

    @Override
    public int getSkinColor() {
        return BasicActivity.skinColor;
    }

    @Override
    public void startWebBrowser(String url) {
        activity.startWebBrowser(url);
    }

    @Override
    public boolean isTestUser(String phone) {
        return activity.isTestUser(phone);
    }

    @Override
    public boolean isFirstAppRunning() {
        return BasicApplication.helpFlag != 100;
    }

    @Override
    public boolean isFirstViewRunning() {
        boolean b = true;
        b = CacheUtil.getInteger("app_help_view" + this.getClass().getName()) < 0;
        CacheUtil.saveInteger("app_help_view" + this.getClass().getName(), 100);
        return b;
    }

    @Override
    public void onPause() {
        printLog();
        super.onPause();
    }


    @Override
    public void onResume() {
        printLog();
        super.onResume();
    }

    @Override
    public void startPhoneCall(String phoneNum) {
        activity.startPhoneCall(phoneNum);
    }

    @Override
    public boolean hasSkinColor() {
        return activity.hasSkinColor();
    }

    @Override
    public void pushModalView(View v, ModalDirection d, int widthDip, int time) {
        activity.pushModalView(v, d, widthDip, time);
    }

    @Override
    public void pushModalView(View v, ModalDirection d, int widthDip) {
        activity.pushModalView(v, d, widthDip);
    }

    @Override
    public void pushModalView(View v, int widthDip) {
        activity.pushModalView(v, widthDip);
    }

    @Override
    public void popModalView() {
        activity.popModalView();
    }

    @Override
    public int getStatusBarHeight() {
        return activity.getStatusBarHeight();
    }

    @Override
    public Bitmap readBitmap(int resId) {
        return activity.readBitmap(resId);
    }

    @Override
    public void setTextViewIcon(TextView tv, int l, int t, int r, int b) {
        activity.setTextViewIcon(tv, l, t, r, b);
    }

    @Override
    public Bitmap readBitmap(String path) {
        return activity.readBitmap(path);
    }
}

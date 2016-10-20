package cn.android_mobile.core;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.android_mobile.core.enums.CacheType;
import cn.android_mobile.core.enums.ModalDirection;
import cn.android_mobile.core.event.BasicEvent;
import cn.android_mobile.core.event.BasicEventDispatcher.IBasicListener;
import cn.android_mobile.core.net.BasicAsyncTask;
import cn.android_mobile.core.net.IBasicAsyncTask;
import cn.android_mobile.core.net.http.Service;
import cn.android_mobile.core.net.http.ServiceRequest;
import cn.android_mobile.core.ui.listener.IMediaImageListener;
import cn.android_mobile.core.ui.listener.IMediaPicturesListener;
import cn.android_mobile.core.ui.listener.IMediaScannerListener;
import cn.android_mobile.core.ui.listener.IMediaSoundRecordListener;
import cn.android_mobile.core.ui.listener.IMediaVideoListener;

public interface IBasicCoreMethod {
	public void isDisplayFragmentEffect(boolean flag);

	public void isDisplayProgressByHttpRequest(boolean b);

//	public void async(IBasicAsyncTask callback, String interfaceName,
//			CacheType cacheType, Object... params);

//	public void async(IBasicAsyncTask callback, String interfaceName,
//			Object... params);

	public void async(ServiceRequest req,IBasicAsyncTask callback);
	public void async(IBasicAsyncTask callback, ServiceRequest req,
			Service service, CacheType cacheType);

	public void async(IBasicAsyncTask callback, ServiceRequest req,
			Service service);

	public void setEditViewClearButton(final EditText et, final View clear,
			final boolean clearOnce);

	public void displayProgressBar();
	
	public void displayProgressBar(String s);

	public void hiddenProgressBar();

	public void showProgress();

	public void showProgress(boolean canBack);

	public void showProgress(int resID, boolean canBack);

	public void showProgress(String res, boolean canBack);

	public void cancelProgress();

	public boolean isEmpty(Object obj);

	public void exitAppWithToast();

	public void closeSoftInput();

	public void showSoftInput(EditText et);

	public int getVersionCode();

	public void cancelAsyncTask(BasicAsyncTask task);

	public void toast(int resId);

	public void toast(String text);

	public void sendMailByIntent(String msg, String email);

	public boolean isNetworkAvailable();

	public int dip2px(float dipValue);

	public int px2dip(float pxValue);

	public List<View> getAllChildViews();

	public List<View> getAllChildViews(View view);

	public void showDialog(String title, String mess);

	public void showDialog(String mess);

	@Deprecated
	public void addMediaImageListener(IMediaImageListener listener);
	public void setMediaImageListener(IMediaImageListener listener);
	@Deprecated
	public void addMediaPictureListener(IMediaPicturesListener listener);
	public void setMediaPictureListener(IMediaPicturesListener listener);
	@Deprecated
	public void addMediaVideoListener(IMediaVideoListener listener);
	public void setMediaVideoListener(IMediaVideoListener listener);
	@Deprecated
	public void addMediaSoundRecordListener(IMediaSoundRecordListener listener);
	public void setMediaSoundRecordListener(IMediaSoundRecordListener listener);
	@Deprecated
	public void addMediaScannerListener(IMediaScannerListener listener);
	public void setMediaScannerListener(IMediaScannerListener listener);

	public void startScan();

	public void startScan(Intent i);

	public void startPictures();

	public void startCamera();

	public void startVideo();

	public void startSoundRecorder();

	public PackageInfo getPackageInfo();

	public boolean hasSdcard();

	public void setTitle(String title);

	public void pushActivityForResult(Class<?> a, int requestCode);

	public void pushActivity(Class<?> a);

	public void pushActivity(Intent i);

	public void pushActivity(Class<?> a, boolean finishSelf);

	public void initModalFragment(BasicFragment f);

	public void pushModalFragment(ModalDirection d, int widthDip, int time, BasicFragment f,
            boolean hasBackground);

	public void pushModalFragment(ModalDirection d, int widthDip,
			BasicFragment f);

	public void pushModalFragment(int widthDip,BasicFragment f);

	public void pushModalFragment(BasicFragment f);

	public void popModalFragment();

	public void setRootFragmentView(int layoutId);
	public void setPushFragmentLayout(int layoutId);

	public void pushFragment(BasicFragment f);

	public void pushFragment(BasicFragment f, boolean flag);

	public void pushFragment(int layoutId, BasicFragment f);

	public void pushFragment(int layoutId, BasicFragment f, boolean flag);

	public void popFragment();

	public void focus(View v);

	public void displayViewAnimation(View v, int fx, float depth);
	
	public void setSkin(String color);
	public void setSkin(int colorRes);
	public  void updateSkin(int skinColor);
	public void showDialog(String title, String mess,
			DialogInterface.OnClickListener clickListener,boolean cancelable) ;
	public void showDialog(String title, String mess,
			DialogInterface.OnClickListener clickListener,
			DialogInterface.OnClickListener cancelListener,boolean cancelable);
	public void showDialog(String title, String mess,
			DialogInterface.OnClickListener clickListener,
			DialogInterface.OnClickListener cancelListener);
	
	public void addBasicEventListener(String type,IBasicListener listener);
	public void dispatchBasicEvent(BasicEvent e);
	public void removeBasicEvent();
	
	public  Bitmap takeScreenshot(View view);
//	public void displayShareView();
//	public void displayShareView(String msg,Bitmap bmp) ;
//	public void displayShareView(String msg,View v);
	public void displayHelpView(Integer... resIds);
	public int getSkinColor();
	public void startWebBrowser(String url);
	
	public boolean isTestUser(String phone);
	
	public boolean isFirstAppRunning();
	public boolean isFirstViewRunning();
	
	public void startPhoneCall(String phoneNum);
	public boolean hasSkinColor();
	
	public void pushModalView(View v,ModalDirection d, int widthDip, int time) ;
	public void pushModalView(View v,ModalDirection d, int widthDip) ;
	public void pushModalView(View v, int widthDip) ;
	public void popModalView() ;
	public  int getStatusBarHeight();
	public  Bitmap readBitmap(int resId);
	public  Bitmap readBitmap(String path);

	public void pushModalFragment(ModalDirection d, int widthDip, int time,
			BasicFragment f);

	public void setTextViewIcon(TextView tv, int l, int t, int r, int b);
}

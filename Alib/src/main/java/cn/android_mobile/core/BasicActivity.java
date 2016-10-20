package cn.android_mobile.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.android_mobile.core.enums.CacheType;
import cn.android_mobile.core.enums.ModalDirection;
import cn.android_mobile.core.event.BasicEvent;
import cn.android_mobile.core.event.BasicEventDispatcher;
import cn.android_mobile.core.event.BasicEventDispatcher.IBasicListener;
import cn.android_mobile.core.net.BasicAsyncTask;
import cn.android_mobile.core.net.IBasicAsyncTask;
import cn.android_mobile.core.net.IBasicAsyncTaskFinish;
import cn.android_mobile.core.net.ThreadPool;
import cn.android_mobile.core.net.http.Service;
import cn.android_mobile.core.net.http.ServiceRequest;
import cn.android_mobile.core.ui.NavigationBar;
import cn.android_mobile.core.ui.listener.IMediaImageListener;
import cn.android_mobile.core.ui.listener.IMediaPicturesListener;
import cn.android_mobile.core.ui.listener.IMediaScannerListener;
import cn.android_mobile.core.ui.listener.IMediaSoundRecordListener;
import cn.android_mobile.core.ui.listener.IMediaVideoListener;
import cn.android_mobile.toolkit.CMDExecute;
import cn.android_mobile.toolkit.CacheUtil;
import cn.android_mobile.toolkit.Lg;
import cn.android_mobile.toolkit.ViewServer;
import cn.capture.CaptureActivity;

import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

@SuppressLint("NewApi")
public abstract class BasicActivity extends FragmentActivity implements
        IBasicAsyncTaskFinish, IBasicCoreMethod {
    private String netWorkErrorMsg = "网络连接失败";
    public static String backText = "";
    private ProgressDialog progressDialog;// 加载进度条
    protected ActivityManager activityManager;
    protected DisplayMetrics metrics;
    public InputMethodManager imm;// 键盘管理
    public RelativeLayout rootView;// 根容器
    public BasicApplication application;
    public float SCREEN_WIDTH; // 设备屏幕宽
    public float SCREEN_HEIGHT;// 设备屏幕高
    public NavigationBar navigationBar;
    private LinearLayout progressBar;
    private long _firstTime = 0;
    protected Toast toast;// 提示框
    public Handler h;
    private ArrayList<BasicAsyncTask> tasks = new ArrayList<BasicAsyncTask>();
    private ViewStub bodyStub;

    protected BasicListener listener;
    public boolean fragmentChangeEffect = false;
    protected int fragmentBodyLayoutRes = -1;
    public LayoutInflater inflater;
    public Gson g = new Gson();

    private float widthDip = 0;
    private ModalDirection direction = ModalDirection.RIGHT;
    private RelativeLayout helpView;// 帮助页面 浮动 第一次运行展现，点击 关闭
    public final String TAG = this.getClass().getSimpleName();

    public RelativeLayout loadLayout;//加载网络时候出现的遮罩层
    public TextView failedText;//网络数据加载失败的时候提示语句
    public ImageView failedImage;
    public Button failedButton;
    public LinearLayout failedLayot;

    private void printLog() {
        Lg.print(TAG, Thread.currentThread().getStackTrace()[3].getMethodName());
    }

    /**
     * 配置 是否在Fragment 切换的时候 需要默认动画
     *
     * @param flag
     */
    public void isDisplayFragmentEffect(boolean flag) {
        fragmentChangeEffect = flag;
    }

    /**
     * 是否在 调用接口的时候显示加载进度条
     *
     * @param b
     */
    public void isDisplayProgressByHttpRequest(boolean b) {
        BasicAsyncTask.asyncWithProgress = b;
    }

    // @Override
    // public void async(IBasicAsyncTask callback, String interfaceName,
    // CacheType cacheType, Object... params) {
    // closeSoftInput();
    // closeViewInteraction();
    // if (BasicAsyncTask.asyncWithProgress) {
    // runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    // displayProgressBar();
    // }
    // });
    // }
    // BasicAsyncTask task = new BasicAsyncTask(callback, interfaceName,
    // cacheType);
    // tasks.add(task);
    // task.addFinishListener(this);
    // task.execute(params);
    // }
    //
    // @Override
    // public void async(IBasicAsyncTask callback, String interfaceName,
    // Object... params) {
    // async(callback, interfaceName, CacheType.BASIC_NET, params);
    // }
    @Override
    public void async(ServiceRequest req, IBasicAsyncTask callback) {

    }

    @Override
    public void async(IBasicAsyncTask callback, ServiceRequest req,
                      Service service, CacheType cacheType) {
        closeSoftInput();
        closeViewInteraction();
        if (BasicAsyncTask.asyncWithProgress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayProgressBar();
                }
            });
        }
        BasicAsyncTask task = new BasicAsyncTask(req, service, cacheType,
                callback);
        tasks.add(task);
        task.addFinishListener(this);
        task.execute();
    }

    @Override
    public void async(IBasicAsyncTask callback, ServiceRequest req,
                      Service service) {
        async(callback, req, service, CacheType.DEFAULT_NET);
    }

    /**
     * 清理 EditText
     *
     * @param et
     * @param clear
     * @param clearOnce
     */
    public void setEditViewClearButton(final EditText et, final View clear) {
        setEditViewClearButton(et, clear, true);
    }

    public void setEditViewClearButton(final EditText et, final View clear,
                                       final boolean clearOnce) {

        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (et.getText().toString().trim().length() == 0) {
                    clear.setVisibility(View.GONE);
                } else {
                    clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (clearOnce) {
                    et.setText("");
                } else {
                    String regPhoneNum = et.getText().toString().trim();
                    if (regPhoneNum.length() > 0) {
                        regPhoneNum = regPhoneNum.substring(0,
                                regPhoneNum.length() - 1);
                        et.setText(regPhoneNum);
                        // 设置edittext编辑在末尾
                        CharSequence text = et.getText();
                        if (text instanceof Spannable) {
                            Spannable spanText = (Spannable) text;
                            Selection.setSelection(spanText, text.length());
                        }
                    }
                }
            }
        });

    }

    /**
     * 异步运行完成后 从tasks 任务管理中移除
     */
    @Override
    public void asyncTaskFinish(BasicAsyncTask task) {
        tasks.remove(task);
        openViewInteraction();
        if (BasicAsyncTask.asyncWithProgress && tasks.size() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hiddenProgressBar();
                }
            });
        }
        // Lg.print("TaskFinish-> size:" + tasks.size());
    }

    public void setNetworkErrorMsg(String msg) {
        netWorkErrorMsg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        printLog();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        SCREEN_WIDTH = metrics.widthPixels;
        SCREEN_HEIGHT = metrics.heightPixels;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        activityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        application = (BasicApplication) this.getApplication();
//		if (!isNetworkAvailable()) {
//			// showDialog(R.id.application_dialog_net_error);
//			// toast("网络连接失败");
//			showDialog("", netWorkErrorMsg,
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							popActivity();
//						}
//					}, false);
//		}
        h = new Handler();
        BasicApplication.activityStack.add(this);
        fm = getSupportFragmentManager();
        inflater = getLayoutInflater();
        // initUMShare();
        super.onCreate(savedInstanceState);
        if (Lg.DEBUG) {
            ViewServer.get(this).addWindow(this);
        }
    }

    /**
     * 友盟分享
     */
    // // 首先在您的Activity中添加如下成员变量
    // protected final UMSocialService umController = UMServiceFactory
    // .getUMSocialService("com.umeng.share", RequestType.SOCIAL);
    private RelativeLayout modalViewGroup; // 用来动画 模态显示的 容器，里面可以嵌套fragment
    private RelativeLayout modalViewGroupBg;// 用来动画 模态显示的 背景遮罩层
    private TextView progressBarLabel;

    @Override
    public Bitmap takeScreenshot(View view) {
        assert view.getWidth() > 0 && view.getHeight() > 0;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                config);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    //
    // public void displayShareView() {
    // // 是否只有已登录用户才能打开分享选择页
    // umController.openShare(getActivity(), false);
    // }
    //
    // public void displayShareView(String msg, Bitmap bmp) {
    // umController.setShareContent(msg);
    // // 设置分享图片, 参数2为图片的url地址
    // umController.setShareMedia(new UMImage(this, bmp));
    // // 是否只有已登录用户才能打开分享选择页
    // umController.openShare(getActivity(), false);
    // }
    //
    // public void displayShareView(String msg, View v) {
    // umController.setShareContent(msg);
    // // 设置分享图片, 参数2为图片的url地址
    // umController.setShareMedia(new UMImage(this, takeScreenshot(v)));
    // // 是否只有已登录用户才能打开分享选择页
    // umController.openShare(getActivity(), false);
    // }
    //
    // private void initUMShare() {
    // // 设置分享内容
    // umController.setShareContent("");//
    // 友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social
    // // 设置分享图片, 参数2为图片的url地址
    // umController.setShareMedia(new UMImage(this,
    // "http://www.umeng.com/images/pic/banner_module_social.png"));
    // umController.getConfig().setShareMail(false);
    // umController.getConfig().setShareSms(false);
    // // 选择平台
    // umController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
    // // 设置qq空间 网页链接 跳转地址
    // QZoneSsoHandler.setTargetUrl("http://MiniX.IVoka.cn");
    // // 设置分享平台面板中显示的平台
    // umController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,
    // SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.RENREN,
    // SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
    // // SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
    //
    // // String appID = "wx967daebe835fbeac";
    // String appID = "wxc273c50462e3b77f";
    // /**
    // * AppID：wxc273c50462e3b77f AppSecret：658c473d4d7f9cf3e29d99effacd0b62
    // */
    // // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
    // // String appID = "wx967daebe835fbeac";
    // // 微信图文分享必须设置一个url
    // String contentUrl = "http://MiniX.IVoka.cn";
    // // 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
    // UMWXHandler wxHandler = umController.getConfig().supportWXPlatform(
    // getActivity(), appID, contentUrl);
    // wxHandler.setWXTitle("");// 友盟社会化组件还不错...
    // // 支持微信朋友圈
    // UMWXHandler circleHandler = umController.getConfig()
    // .supportWXCirclePlatform(getActivity(), appID, contentUrl);
    // circleHandler.setCircleTitle("");// 友盟社会化组件还不错... 标题
    // }

    @Override
    public void setContentView(int layoutResID) {
        printLog();
        super.setContentView(R.layout.activity_frame);
        navigationBar = new NavigationBar(this,
                (ViewStub) findViewById(R.id.title_stub));
        progressBar = (LinearLayout) findViewById(R.id.frame_progress);
        progressBarLabel = (TextView) findViewById(R.id.frame_progress_label);
        modalViewGroup = (RelativeLayout) findViewById(R.id.frame_modal_view_root);
        helpView = (RelativeLayout) findViewById(R.id.frame_help_relativelayout);
        modalViewGroupBg = (RelativeLayout) findViewById(R.id.frame_modal_view_bg);
        modalViewGroupBg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popModalFragment();
            }
        });

        helpView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (_tmpBtmp != null && !_tmpBtmp.isRecycled()) {
                    _tmpBtmp.recycle();
                }
                if (helpImgs.size() > 0) {
                    _tmpBtmp = displayHelpView(helpImgs.pop());
                } else if (helpFragmentImgs.size() > 0) {
                    _tmpBtmp = displayHelpView(helpFragmentImgs.pop());
                } else {
                    helpView.setVisibility(View.GONE);
                }
            }
        });
        bodyStub = (ViewStub) findViewById(R.id.body_stub);
        bodyStub.setLayoutResource(layoutResID);
        bodyView = bodyStub.inflate();
        rootView = (RelativeLayout) findViewById(R.id.main_root);
        //网络数据加载的时候出现的页面
        loadLayout = (RelativeLayout) findViewById(R.id.http_data_load_layout);
        failedText = (TextView) findViewById(R.id.http_data_load_text);
        failedImage = (ImageView) findViewById(R.id.http_load_failed_image);
        failedButton = (Button) findViewById(R.id.http_load_failed_button);
        failedLayot = (LinearLayout) findViewById(R.id.http_load_failed_layout);
        // 换肤功能
        skinColor = CacheUtil.getInteger("SKIN_COLOR");
        if (skinColor != -1) {
            navigationBar.layout.setBackgroundColor(skinColor);
            // bodyView.setBackgroundColor(skinColor);
        }
        initDefault();
        init();
    }

    Bitmap _tmpBtmp = null;
    ;

    public void setContentView_IOS(int layoutResID) {
        super.setContentView(R.layout.activity_frame);
        navigationBar = new NavigationBar(this,
                (ViewStub) findViewById(R.id.title_stub));
        progressBar = (LinearLayout) findViewById(R.id.frame_progress);
        bodyStub = (ViewStub) findViewById(R.id.body_stub);
        bodyStub.setLayoutResource(layoutResID);
        bodyStub.inflate();
        rootView = (RelativeLayout) findViewById(R.id.main_root);
        initDefault();
        init();
    }

    protected void initDefault() {
        if (backActivityTitle.equals("")) {
            navigationBar.leftBtn.setVisibility(View.GONE);
        } else {
            navigationBar.leftBtn.setVisibility(View.VISIBLE);
            navigationBar.leftBtn.setText(" " + backActivityTitle);
            navigationBar.leftBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // BasicActivity.this.finish();
                    popFragment();
                }
            });
        }
        navigationBar.rightBtn.setVisibility(View.GONE);
    }

    public void displayProgressBar() {
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);
        progressBarLabel.setVisibility(View.GONE);
    }

    @Override
    public void displayProgressBar(String s) {
        // Lg.print("public void displayProgressBar(String s) ");
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);
        progressBarLabel.setVisibility(View.VISIBLE);
        progressBarLabel.setText(s);
    }

    public void hiddenProgressBar() {
        progressBar.setVisibility(View.GONE);
        progressBarLabel.setVisibility(View.GONE);
        progressBarLabel.setText("");
    }

    /**
     * 显示进度条
     */
    public void showProgress() {
        showProgress(R.string.loading_net, true);
    }

    public void showProgress(boolean canBack) {
        showProgress(R.string.loading_net, canBack);
    }

    /**
     * 显示进度条
     *
     * @param resID
     * @param canBack
     */
    public void showProgress(int resID, boolean canBack) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.cancel();
        }
        progressDialog = ProgressDialog.show(this, "", getResources()
                .getString(resID));
        progressDialog.setCancelable(canBack);
    }

    /**
     * 显示进度条
     *
     * @param res
     * @param canBack
     */
    public void showProgress(String res, boolean canBack) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.cancel();
        }
        progressDialog = ProgressDialog.show(this, "", res);
        progressDialog.setCancelable(canBack);
    }

    /**
     * 取消进度条
     */
    public void cancelProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.cancel();
        }
    }

    public void cancelAllTask() {
        printLog();
        if (tasks == null || tasks.size() == 0)
            return;
        for (int i = 0; i < tasks.size(); i++) {
            cancelAsyncTask(tasks.get(i));
        }
        tasks.clear();
        // Lg.println("cancelAllTask() current tasks:" + tasks.size(), true);
    }

    @Override
    public void finish() {
        printLog();
        cancelAllTask();
        removeBasicEvent();
        BasicApplication.activityStack.remove(this);
        super.finish();
    }

    protected void init() {
        printLog();
        initComp();
        initListener();
        initData();
    }

    protected abstract void initComp();

    protected abstract void initListener();

    protected abstract void initData();

    public boolean isEmpty(Object obj) {
        boolean f = false;
        if (obj == null) {
            f = true;
        } else if (obj instanceof TextView) {
            TextView tv = (TextView) obj;
            if (TextUtils.isEmpty(tv.getText())) {
                f = true;
            }
        } else if (obj instanceof EditText) {
            EditText et = (EditText) obj;
            if (TextUtils.isEmpty(et.getText())) {
                f = true;
            }
        } else if (obj instanceof String) {
            String s = (String) obj;
            if (s.trim().equals("")) {
                f = true;
            }
        }
        return f;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        printLog();
        long secondTime = System.currentTimeMillis();
        if (secondTime - _firstTime > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
            toast("再按一次退出程序...");
            _firstTime = secondTime;// 更新firstTime
            return;
        } else {
            exitAppWithToast();
        }
    }

    public void exitAppWithToast() {
        // SPUtil.saveObject("CAR_LIST", HomeModule.getInstance().carnumList,
        // SPUtil.CAR_CACHE);
        // ActivityManager activityManager = (ActivityManager)
        // getSystemService(ACTIVITY_SERVICE);
        // activityManager.restartPackage(getPackageName());
        // activityManager.killBackgroundProcesses(getPackageName());
        CacheUtil.saveInteger("app_help_view", 100);
        ThreadPool.basicPool.shutdown();
        ThreadPool.fixedPool.shutdown();
        while (BasicApplication.activityStack.size() > 0) {
            BasicApplication.activityStack.pop().finish();
        }
        finish();
        System.gc();
        System.exit(0);
    }

    public void exitAppWithoutThis() {
        BasicApplication.activityStack.pop();
        while (BasicApplication.activityStack.size() > 0) {
            BasicApplication.activityStack.pop().finish();
        }
    }

    // 关闭软键盘
    public void closeSoftInput() {
        printLog();
        if (imm != null && imm.isActive()) {
            View focusView = getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    public void showSoftInput(EditText et) {
        imm.showSoftInput(et, 0);
    }

    /**
     * 获取当前程序的版本号
     */
    public int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }

    // 取消异步调用
    public void cancelAsyncTask(BasicAsyncTask task) {
        if (task != null && task.isCancelled() == false) {
            task.abort();
            task.cancel(true);
        }
    }

    // /**
    // * 两次退出程序
    // */
    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // if (keyCode == KeyEvent.KEYCODE_BACK) {
    // long secondTime = System.currentTimeMillis();
    // if (secondTime - _firstTime > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
    // toast("再按一次退出程序...");
    // _firstTime = secondTime;// 更新firstTime
    // return true;
    // } else {
    // exitAppWithToast();
    // }
    // }
    // return super.onKeyUp(keyCode, event);
    // }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        printLog();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isModalFragmentDisplay) {
                popModalFragment();
            } else {
                finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 显示Toast
     */
    public void toast(int resId) {
        if (toast == null) {
            toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        } else {
            // toast.cancel();
            toast.setText(resId);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public void toast(String text) {
        if (toast == null) {
            toast = Toast.makeText(this.getApplicationContext(), text,
                    Toast.LENGTH_SHORT);
        } else {
            // toast.cancel();
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * 意见反馈 邮件发送
     */
    public void sendMailByIntent(String msg, String email) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + email));// 收件人
        // data.putExtra(Intent.EXTRA_EMAIL, new
        // String[]{"fyygw10@126.com"});//更多的收件人
        // data.putExtra(Intent.EXTRA_CC, new
        // String[]{"zhenfang@wistronits.com"});//抄送
        // data.putExtra(Intent.EXTRA_BCC, new
        // String[]{"fangyygw@gmail.com"});//秘密发送
        data.putExtra(Intent.EXTRA_SUBJECT, "[意见反馈]");
        data.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(data);
    }

    /**
     * 检测网络连接是否可用
     *
     * @return true 可用; false 不可用
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] netinfo = cm.getAllNetworkInfo();
        if (netinfo == null) {
            return false;
        }
        for (int i = 0; i < netinfo.length; i++) {
            if (netinfo[i].isConnected()) {
                return true;
            }
        }
        return false;
    }

    public int dip2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @note 获取该activity所有view
     * @author liuh
     */
    public List<View> getAllChildViews() {
        View view = this.getWindow().getDecorView();
        return getAllChildViews(view);
    }

    public List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                // Lg.print(viewchild);
                allchildren.add(viewchild);
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        // Lg.print("--------");
        return allchildren;
    }

    /* 显示Dialog的method */
    protected Builder dialog;

    public Builder createDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this);
        } else {
            return dialog;
        }
        return dialog;
    }

    @Override
    public void showDialog(String title, String mess) {
        createDialog().setTitle(title).setMessage(mess)
                .setNegativeButton("确定", null).show();
    }

    @Override
    public void showDialog(String title, String mess,
                           DialogInterface.OnClickListener clickListener,
                           DialogInterface.OnClickListener cancelListener, boolean cancelable) {
        createDialog().setTitle(title).setMessage(mess)
                .setPositiveButton("确定", clickListener)
                .setNegativeButton("取消", cancelListener)
                .setCancelable(cancelable).show();
    }

    @Override
    public void showDialog(String title, String mess,
                           DialogInterface.OnClickListener clickListener,
                           DialogInterface.OnClickListener cancelListener) {
        showDialog(title, mess, clickListener, cancelListener, true);
    }

    @Override
    public void showDialog(String title, String mess,
                           DialogInterface.OnClickListener clickListener, boolean cancelable) {
        createDialog().setTitle(title).setMessage(mess)
                .setPositiveButton("确定", clickListener)
                .setCancelable(cancelable).show();
    }

    public void showDialog(String mess) {
        createDialog().setMessage(mess).setNegativeButton("确定", null).show();
    }

    private static final int RESULT_CAPTURE_IMAGE = 1;// 照相的requestCode
    private static final int REQUEST_CODE_TAKE_VIDEO = 2;// 摄像的照相的requestCode
    private static final int RESULT_CAPTURE_RECORDER_SOUND = 3;// 录音的requestCode
    private static final int RESULT_CAPTURE_PICTURES = 4;// 本地相冊
    private static final int RESULT_SCANNER = 5;// 条码扫描
    private String strImgPath = "";// 照片文件绝对路径
    private String strVideoPath = "";// 视频文件的绝对路径
    private String strRecorderPath = "";// 录音文件的绝对路径
    private IMediaImageListener iMediaImageListener = null;
    private IMediaVideoListener iMediaVideoListener = null;
    private IMediaSoundRecordListener iMediaSoundRecordListener = null;
    private IMediaPicturesListener iMediaPicturesListener = null;
    private IMediaScannerListener iMediaScannerListener = null;
    public FragmentManager fm;
    public BasicFragment currentFragment;

    @Deprecated
    @Override
    public void addMediaImageListener(IMediaImageListener listener) {
        this.iMediaImageListener = listener;
    }

    @Override
    public void setMediaImageListener(IMediaImageListener listener) {
        this.iMediaImageListener = listener;
    }

    @Deprecated
    @Override
    public void addMediaPictureListener(IMediaPicturesListener listener) {
        this.iMediaPicturesListener = listener;
    }

    @Override
    public void setMediaPictureListener(IMediaPicturesListener listener) {
        this.iMediaPicturesListener = listener;
    }

    @Deprecated
    @Override
    public void addMediaScannerListener(IMediaScannerListener listener) {
        this.iMediaScannerListener = listener;
    }

    @Override
    public void setMediaScannerListener(IMediaScannerListener listener) {
        this.iMediaScannerListener = listener;
    }

    @Deprecated
    @Override
    public void addMediaVideoListener(IMediaVideoListener listener) {
        this.iMediaVideoListener = listener;
    }

    @Override
    public void setMediaVideoListener(IMediaVideoListener listener) {
        this.iMediaVideoListener = listener;
    }

    @Deprecated
    @Override
    public void addMediaSoundRecordListener(IMediaSoundRecordListener listener) {
        this.iMediaSoundRecordListener = listener;
    }

    @Override
    public void setMediaSoundRecordListener(IMediaSoundRecordListener listener) {
        this.iMediaSoundRecordListener = listener;
    }

    /**
     * 本地相册
     */
    public void startPictures() {
        Intent getAlbum = new Intent(Intent.ACTION_PICK);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum, RESULT_CAPTURE_PICTURES);
    }

    /**
     * 照相功能
     */
    public void startCamera() {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        strImgPath = Environment.getExternalStorageDirectory().toString()
                + "/CONSDCGMPIC/";// 存放照片的文件夹
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + ".jpg";// 照片命名
        File out = new File(strImgPath);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(strImgPath, fileName);
        strImgPath = strImgPath + fileName;// 该照片的绝对路径
        Uri uri = Uri.fromFile(out);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(imageCaptureIntent, RESULT_CAPTURE_IMAGE);

    }

    /**
     * 拍摄视频
     */
    public void startVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
    }

    public void startScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, RESULT_SCANNER);
    }

    public void startScan(Intent i) {
        startActivityForResult(i, RESULT_SCANNER);
    }

    /**
     * 录音功能
     */
    public void startSoundRecorder() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/amr");
        startActivityForResult(intent, RESULT_CAPTURE_RECORDER_SOUND);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        printLog();
        super.onActivityResult(requestCode, resultCode, data);
        // Lg.print("onActivityResult+ fragment");
        switch (requestCode) {
            case BasicActivity.RESULT_CAPTURE_PICTURES:// 相册
                if (resultCode == RESULT_OK) {
                    if (iMediaPicturesListener != null) {
                        Uri selectedImage = data.getData();
                        // Log.e(TAG, selectedImage.toString());
                        if (selectedImage != null) {
                            String uriStr = selectedImage.toString();
                            String path = uriStr.substring(10, uriStr.length());
                            if (path.startsWith("com.sec.android.gallery3d")) {
                                // Log.e(TAG,
                                // "It's auto backup pic path:"+selectedImage.toString());
                                return;
                            }
                        }
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        iMediaPicturesListener.mediaPicturePath(picturePath);
                    }
                }
                break;
            case BasicActivity.RESULT_CAPTURE_IMAGE:// 拍照
                if (resultCode == RESULT_OK) {
                    if (iMediaImageListener != null) {
                        // showToast(strImgPath);
//					myImageCompress(strImgPath);
                        // imageCompress(strImgPath);
                        iMediaImageListener.mediaImagePath(strImgPath);
                    }
                }
                break;
            case BasicActivity.REQUEST_CODE_TAKE_VIDEO:// 拍摄视频
                if (resultCode == RESULT_OK) {
                    Uri uriVideo = data.getData();
                    Cursor cursor = getContentResolver().query(uriVideo, null,
                            null, null, null);
                    if (cursor.moveToNext()) {
                        /** _data：文件的绝对路径 ，_display_name：文件名 */
                        strVideoPath = cursor.getString(cursor
                                .getColumnIndex("_data"));
                        // showToast(strVideoPath);
                        if (iMediaVideoListener != null) {
                            iMediaVideoListener.mediaVideoPath(strVideoPath);
                        }
                    }
                }
                break;
            case BasicActivity.RESULT_CAPTURE_RECORDER_SOUND:// 录音
                if (resultCode == RESULT_OK) {
                    Uri uriRecorder = data.getData();
                    Cursor cursor = getContentResolver().query(uriRecorder, null,
                            null, null, null);
                    if (cursor.moveToNext()) {
                        /** _data：文件的绝对路径 ，_display_name：文件名 */
                        strRecorderPath = cursor.getString(cursor
                                .getColumnIndex("_data"));
                        // showToast(strRecorderPath);
                        if (iMediaSoundRecordListener != null) {
                            iMediaSoundRecordListener
                                    .mediaSoundRecordPath(strRecorderPath);
                        }
                    }
                }
                break;
            case BasicActivity.RESULT_SCANNER:// 拍照
                if (resultCode == RESULT_OK) {
                    if (iMediaScannerListener != null) {
                        iMediaScannerListener.mediaScannerResult(data
                                .getStringExtra("result"));
                    }
                }
                break;
        }
    }

    /**
     * 根据路径进行图片压缩，然后覆盖
     *
     * @param strImgPath2
     */
    private void imageCompress(String strImgPath2) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;// 只读边,不读内容
        Bitmap bmp = BitmapFactory.decodeFile(strImgPath2, newOpts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(strImgPath2);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按像素图片压缩，然后覆盖
     *
     * @param strImgPath2
     */
    public void myImageCompress(String strImgPath2) {
        Bitmap bmp = myReduceImage(strImgPath2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;//
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 300 && options > 10) {
            Lg.print("baos.toByteArray().length / 1024==="
                    + baos.toByteArray().length / 1024);
            Lg.print("options===" + options);
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(strImgPath2);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按宽高压缩图片
     *
     * @param path
     * @return
     */
    public Bitmap myReduceImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 400);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be + 3;
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap != null) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            System.out.println(w + "   " + h);
        }
        return bitmap;
    }

    protected void uninstallAPK(String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        this.startActivity(intent);
    }

    protected void uninstallAPK() {
        Uri uri = Uri.parse("package:" + getPackageInfo().packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        this.startActivity(intent);
    }

    /**
     * 获取当前程序的版本号
     */

    public PackageInfo getPackageInfo() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    public ArrayList<PackageInfo> getPackageInfosByLocalApp() {
        PackageManager packageManager = getPackageManager();
        final List<PackageInfo> packageInfos = packageManager
                .getInstalledPackages(0);
        ArrayList<PackageInfo> local_pkgInfoNoSys = new ArrayList<PackageInfo>();
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo packageInfo = packageInfos.get(i);
            // 获取 非系统的应用
            if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0) {
                local_pkgInfoNoSys.add(packageInfo);
            }
            // 本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
            else if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                local_pkgInfoNoSys.add(packageInfo);
            }
        }
        return local_pkgInfoNoSys;
    }

    /**
     * 判断有无sd卡
     */
    public boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)
                || status.equals("/mnt/sdcard")) {
            return true;
        } else {
            return false;
        }
    }

    public void setTitle(String title) {
        navigationBar.titleText.setText(title);
    }

    /**
     * UI交互 控制
     */
    private ArrayList<View> itViews = new ArrayList<View>();

    protected void configViewInteraction(View... v) {
        if (v == null)
            return;
        itViews.clear();
        // fore
    }

    protected void openViewInteraction() {

    }

    protected void closeViewInteraction() {

    }

    /**
     * 跳船activity 前保存当前activity 的title
     */
    public static String backActivityTitle = "";
    public View bodyView;
    private int modalAnimTime = 300;
    private boolean isModalFragmentDisplay;

    public void setBackText(String s) {
        backText = s;
    }

    public void pushActivity(Class<?> a) {
        if (isEmpty(backText)) {
            backActivityTitle = navigationBar.titleText.getText().toString()
                    .trim();
        } else {
            backActivityTitle = backText;
        }
        startActivity(new Intent(this, a));
    }

    public void pushActivity(Intent i) {
        if (isEmpty(backText)) {
            backActivityTitle = navigationBar.titleText.getText().toString()
                    .trim();
        } else {
            backActivityTitle = backText;
        }
        startActivity(i);
    }

    public void pushActivityForResult(Class<?> a, int requestCode) {
        if (isEmpty(backText)) {
            backActivityTitle = navigationBar.titleText.getText().toString()
                    .trim();
        } else {
            backActivityTitle = backText;
        }
        startActivityForResult(new Intent(this, a), requestCode);
    }

    public void pushActivityForResult(Intent a, int requestCode) {
        if (isEmpty(backText)) {
            backActivityTitle = navigationBar.titleText.getText().toString()
                    .trim();
        } else {
            backActivityTitle = backText;
        }
        startActivityForResult(a, requestCode);
    }

    public void pushActivity(Class<?> a, boolean finishSelf) {
        pushActivity(a);
        if (finishSelf) {
            backActivityTitle = "";
            this.finish();
        }
    }

    @Deprecated
    @Override
    public void setRootFragmentView(int layoutId) {
        fragmentBodyLayoutRes = layoutId;
    }

    @Override
    public void setPushFragmentLayout(int layoutId) {
        fragmentBodyLayoutRes = layoutId;
    }

    public void pushFragment(BasicFragment f) {
        pushFragment(f, false);
    }

    public void pushFragment(int layoutId, BasicFragment f) {
        pushFragment(layoutId, f, false);
    }

    @Override
    public void pushFragment(BasicFragment f, boolean flag) {
        if (currentFragment != null && f != null
                && f.name.equals(currentFragment.name))
            return;
        FragmentTransaction ft = this.fm.beginTransaction();
        if (this.fragmentChangeEffect) {
            ft.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out, android.R.anim.fade_out,
                    android.R.anim.fade_in);
        }
        ft.replace(fragmentBodyLayoutRes, f);
        if (flag) {
            ft.addToBackStack(f.name);
        } else {
        }
        ft.commit();
    }

    @Override
    public void pushFragment(int layoutId, BasicFragment f, boolean flag) {
        if (currentFragment != null && f != null
                && f.name.equals(currentFragment.name))
            return;
        FragmentTransaction ft = this.fm.beginTransaction();
        if (this.fragmentChangeEffect) {
            ft.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out, android.R.anim.fade_out,
                    android.R.anim.fade_in);
        }
        ft.replace(layoutId, f);
        if (flag) {
            ft.addToBackStack(f.name);
        }
        ft.commit();
    }

    public void popFragment() {
        Lg.print("popFragment");
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            popActivity();
        }
    }

    /**
     * 关闭系统浏览器
     *
     * @return
     */
    public boolean closeAppWithPackageName(String pkgn) {
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            Runtime.getRuntime().exec("su");
            String[] args = {"adb", "shell", "pgrep", pkgn, "|", "xargs",
                    "kill", "-9"};// "com.android.browser"
            result = cmdexe.run(args, "/sdcard/baidu");
            // Lg.print("com.android.browser   PID: "+result.trim());
            // String[] args2 = { "adb","shell", "kill","-9",result.trim()};
            // result = cmdexe.run(args2, "/sdcard/baidu");
            // Lg.print(result);
            toast(result.trim());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (result.indexOf("Success") != -1) {
            return true;
        } else {
            return false;
        }
    }

    public void popActivity() {
        BasicApplication.activityStack.pop().finish();
    }

    /**
     * 验证日期 是否 低于 今天， 返回Bool
     *
     * @param date
     * @return
     */
    public BasicActivity getActivity() {
        return this;
    }

    public boolean validDate(String date) {
        String[] ss = date.split("-");
        int sYear = Integer.parseInt(ss[0]);
        int sMonth = Integer.parseInt(ss[1]);
        int sDay = Integer.parseInt(ss[2]);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Lg.print(year + "=" + month + "=" + day);
        if (sYear >= year && sMonth >= month && sDay >= day) {
            return true;
        }
        return false;
    }

    @Override
    public void focus(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
    }

    @Override
    public void displayViewAnimation(View v, int fx, float depth) {
        // right
        if (fx == 1) {
            ObjectAnimator.ofFloat(v, "translationX", SCREEN_WIDTH,
                    SCREEN_WIDTH - depth).start();
        }
    }

    @Override
    public void pushModalFragment(BasicFragment f) {
        this.pushModalFragment(ModalDirection.RIGHT, (int) (SCREEN_WIDTH / 2),
                modalAnimTime, f);

    }

    @Override
    public void pushModalFragment(int widthDip, BasicFragment f) {
        this.pushModalFragment(ModalDirection.RIGHT, widthDip, modalAnimTime, f);
    }

    @Override
    public void pushModalFragment(ModalDirection d, int widthDip,
                                  BasicFragment f) {
        this.pushModalFragment(d, widthDip, modalAnimTime, f);
    }

    public void initModalFragment(BasicFragment f) {
        setModalFragment(f);
    }

    public void setModalFragment(BasicFragment f) {
        pushFragment(R.id.frame_modal_view_root, f, false);
    }

    @Override
    public void pushModalFragment(ModalDirection d, int widthDip, int time,
                                  BasicFragment f) {
        pushModalFragment(d, widthDip, time, f, true);
    }

    @Override
    public void pushModalFragment(ModalDirection d, int widthDip, int time,
                                  BasicFragment f, boolean hasBackground) {
        initModalFragment(f);
        isModalFragmentDisplay = true;
        this.widthDip = widthDip;
        this.modalAnimTime = time;
        this.direction = d;
        ValueAnimator colorAnim;
        if (hasBackground) {
            colorAnim = ObjectAnimator.ofInt(modalViewGroupBg,
                    "backgroundColor", /* Red */0x00000000, /* Blue */
                    0x55000000);
        } else {
            colorAnim = ObjectAnimator.ofInt(modalViewGroupBg,
                    "backgroundColor", /* Red */0x00000000, /* Blue */
                    0x00000000);
        }
        modalViewGroupBg.setVisibility(View.VISIBLE);
        modalViewGroup.setVisibility(View.VISIBLE);
        colorAnim.setDuration(modalAnimTime);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
        // AnimatorSet set = new AnimatorSet();
        // set.playTogether(
        // ObjectAnimator.ofFloat(modalViewGroup, "translationX", SCREEN_WIDTH,
        // SCREEN_WIDTH-widthDip)
        // );
        RelativeLayout.LayoutParams imagebtn_params = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (ModalDirection.RIGHT == d) {
            // Lg.print("modal  right");
            imagebtn_params.height = (int) SCREEN_HEIGHT;
            imagebtn_params.width = widthDip;
            modalViewGroup.setLayoutParams(imagebtn_params);
            ObjectAnimator
                    .ofFloat(modalViewGroup, "translationX", SCREEN_WIDTH,
                            SCREEN_WIDTH - widthDip).setDuration(modalAnimTime)
                    .start();

        } else if (ModalDirection.LEFT == d) {
            // Lg.print("modal  left");
            imagebtn_params.height = (int) SCREEN_HEIGHT;
            imagebtn_params.width = widthDip;
            modalViewGroup.setLayoutParams(imagebtn_params);
            ObjectAnimator
                    .ofFloat(modalViewGroup, "translationX", -widthDip, 0)
                    .setDuration(modalAnimTime).start();
        } else if (ModalDirection.BOTTOM == d) {
            // Lg.print("modal  bottom");
            imagebtn_params.height = widthDip;
            imagebtn_params.width = (int) SCREEN_WIDTH;
            modalViewGroup.setLayoutParams(imagebtn_params);
            ObjectAnimator
                    .ofFloat(modalViewGroup, "y",
                            SCREEN_HEIGHT - getStatusBarHeight(),
                            SCREEN_HEIGHT - getStatusBarHeight() - widthDip)
                    .setDuration(modalAnimTime).start();
        } else if (ModalDirection.TOP == d) {
            // Lg.print("modal  top");
            imagebtn_params.height = widthDip;
            imagebtn_params.width = (int) SCREEN_WIDTH;
            modalViewGroup.setLayoutParams(imagebtn_params);
            ObjectAnimator.ofFloat(modalViewGroup, "translationY", 0, widthDip)
                    .setDuration(modalAnimTime).start();
        }
        // pushFragment(R.id.frame_modal_view_root, f,false);
    }

    @Override
    public void pushModalView(View v, ModalDirection d, int widthDip, int time) {
        modalViewGroup.removeAllViews();
        RelativeLayout.LayoutParams vLp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        modalViewGroup.addView(v, vLp);
        isModalFragmentDisplay = true;
        this.widthDip = widthDip;
        this.modalAnimTime = time;
        this.direction = d;
        modalViewGroupBg.setVisibility(View.VISIBLE);
        modalViewGroup.setVisibility(View.VISIBLE);
        ValueAnimator colorAnim = ObjectAnimator.ofInt(modalViewGroupBg,
                "backgroundColor", /* Red */0x00000000, /* Blue */0x55000000);
        colorAnim.setDuration(modalAnimTime);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
        RelativeLayout.LayoutParams imagebtn_params = (android.widget.RelativeLayout.LayoutParams) modalViewGroup
                .getLayoutParams();
        if (ModalDirection.RIGHT == d) {
            // Lg.print("modal  right");
            imagebtn_params.height = (int) SCREEN_HEIGHT;
            imagebtn_params.width = widthDip;
            modalViewGroup.setLayoutParams(imagebtn_params);
            ObjectAnimator
                    .ofFloat(modalViewGroup, "translationX", SCREEN_WIDTH,
                            SCREEN_WIDTH - widthDip).setDuration(modalAnimTime)
                    .start();

        } else if (ModalDirection.LEFT == d) {
            // Lg.print("modal  left");
            imagebtn_params.height = (int) SCREEN_HEIGHT;
            imagebtn_params.width = widthDip;
            modalViewGroup.setLayoutParams(imagebtn_params);
            ObjectAnimator
                    .ofFloat(modalViewGroup, "translationX", -widthDip, 0)
                    .setDuration(modalAnimTime).start();
        } else if (ModalDirection.BOTTOM == d) {
            // Lg.print("modal  bottom: STATUS BAR HEIGHT:" +
            imagebtn_params.height = widthDip;
            imagebtn_params.width = (int) SCREEN_WIDTH;
            modalViewGroup.setLayoutParams(imagebtn_params);
            ObjectAnimator
                    .ofFloat(modalViewGroup, "y",
                            SCREEN_HEIGHT - getStatusBarHeight(),
                            SCREEN_HEIGHT - getStatusBarHeight() - widthDip)
                    .setDuration(modalAnimTime).start();
        } else if (ModalDirection.TOP == d) {
            // Lg.print("modal  top");
            imagebtn_params.height = widthDip;
            imagebtn_params.width = (int) SCREEN_WIDTH;
            modalViewGroup.setLayoutParams(imagebtn_params);
            ObjectAnimator.ofFloat(modalViewGroup, "y", -widthDip, getStatusBarHeight() * 2)
                    .setDuration(modalAnimTime).start();
        }
    }

    @Override
    public void pushModalView(View v, ModalDirection d, int widthDip) {
        pushModalView(v, d, widthDip, modalAnimTime);
    }

    @Override
    public void pushModalView(View v, int widthDip) {
        pushModalView(v, ModalDirection.BOTTOM, widthDip, modalAnimTime);
    }

    public static int skinColor = -1;

    public void updateSkin(int skinColor) {
        if (skinColor != -1) {
            navigationBar.layout.setBackgroundColor(skinColor);
            // bodyView.setBackgroundColor(skinColor);
            BasicEvent e = new BasicEvent(BasicEvent.UPDATE_SKIN);
            e.setData(skinColor);
            dispatchBasicEvent(e);
            CacheUtil.saveInteger("SKIN_COLOR", skinColor);
        }
    }

    @Override
    public void setSkin(String color) {
        skinColor = Color.parseColor(color);
        updateSkin(skinColor);
    }

    @Override
    public boolean hasSkinColor() {
        if (skinColor == -1) {
            return false;
        }
        return true;
    }

    @Override
    public void setSkin(int colorRes) {
        skinColor = colorRes;
        updateSkin(skinColor);
    }

    @Override
    public void popModalView() {
        popModalFragment();
    }

    @Override
    public void popModalFragment() {
        isModalFragmentDisplay = false;
        if (direction == ModalDirection.RIGHT) {
            ObjectAnimator
                    .ofFloat(modalViewGroup, "translationX",
                            SCREEN_WIDTH - widthDip, SCREEN_WIDTH)
                    .setDuration(modalAnimTime).start();
        } else if (direction == ModalDirection.LEFT) {
            ObjectAnimator
                    .ofFloat(modalViewGroup, "translationX", 0, -widthDip)
                    .setDuration(modalAnimTime).start();
        } else if (direction == ModalDirection.TOP) {
            ObjectAnimator.ofFloat(modalViewGroup, "y", 0, -widthDip)
                    .setDuration(modalAnimTime).start();
        } else if (direction == ModalDirection.BOTTOM) {
            ObjectAnimator
                    .ofFloat(modalViewGroup, "y",
                            SCREEN_HEIGHT - widthDip - getStatusBarHeight(),
                            SCREEN_HEIGHT - getStatusBarHeight())
                    .setDuration(modalAnimTime).start();
        }
        ValueAnimator colorAnim = ObjectAnimator.ofInt(modalViewGroupBg,
                "backgroundColor", /* Red */
                0x55000000, /* Blue */0x00000000);
        colorAnim.setDuration(modalAnimTime);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                modalViewGroupBg.setVisibility(View.GONE);
                modalViewGroup.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        colorAnim.start();
    }

    @Override
    public void addBasicEventListener(String type, IBasicListener listener) {
        BasicEventDispatcher.addEventListener(this.getClass().getName(), type,
                listener);
    }

    @Override
    public void dispatchBasicEvent(BasicEvent e) {
        BasicEventDispatcher.dispatcher(e);
    }

    @Override
    public void removeBasicEvent() {
        BasicEventDispatcher
                .removeEventListenerByKey(this.getClass().getName());
    }

    /**
     * Object 可以为View（View） int（ResID） 或者 String（url）
     */
    private Stack<Integer> helpImgs = new Stack<Integer>();
    public Stack<Integer> helpFragmentImgs = new Stack<Integer>();

    private Bitmap displayHelpView(int resId) {
        helpView.removeAllViews();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(this);
        Bitmap b = readBitmap(resId);
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setImageBitmap(b);
        helpView.addView(imageView, lp);
        helpView.bringToFront();
        helpView.setVisibility(View.VISIBLE);
        return b;
    }

    @Override
    public void displayHelpView(Integer... resIds) {
        if (isFirstViewRunning() == false) {
            return;
        }
        for (int i = resIds.length - 1; i >= 0; i--) {
            helpImgs.add(resIds[i]);
        }
        displayHelpView(helpImgs.pop());
    }

    @Override
    public int getSkinColor() {
        return skinColor;
    }

    @Override
    public void startWebBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    @Override
    public boolean isTestUser(String phone) {
        if (phone.startsWith("400")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isFirstAppRunning() {
        boolean b = true;
        if (CacheUtil.getInteger("app_help_view") < 0) {
            b = true;
        } else {
            b = false;
        }
        CacheUtil.saveInteger("app_help_view", 100);
        // Lg.print("App是否第一次运行：" + b);
        return b;
    }

    @Override
    public boolean isFirstViewRunning() {
        boolean b = true;
        if (CacheUtil.getInteger("app_help_view" + this.getClass().getName()) < 0) {
            b = true;
        } else {
            b = false;
        }
        CacheUtil.saveInteger("app_help_view" + this.getClass().getName(), 100);
        // Lg.print("Acitivy是否第一次运行：" + b);
        return b;
    }

    // 友盟 session的统计 获取正确的新增用户、活跃用户、启动次数、使用时长等基本数据
    @Override
    protected void onPause() {
        printLog();
        super.onPause();
    }

    @Override
    protected void onResume() {
        printLog();
        super.onResume();

        // if (Lg.DEBUG) {
        // ViewServer.get(this).setFocusedWindow(this);
        // }
    }

    // 判断设备有无拨打电话功能
    protected boolean isIntentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void startPhoneCall(String phoneNum) {
        if (isEmpty(phoneNum))
            return;
        // 调用拨打电话页面，android.intent.action.Call直接调用拨打电话。
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phoneNum));
        if (isIntentAvailable(intent)) {
            startActivity(intent);
        } else {
            toast("没有拨打电话功能");
        }
    }

    @Override
    public int getStatusBarHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // Class<?> c = null;
        // Object obj = null;
        // Field field = null;
        // int x = 0, statusBarHeight = 0;
        // try {
        // c = Class.forName("com.android.internal.R$dimen");
        // obj = c.newInstance();
        // field = c.getField("status_bar_height");
        // x = Integer.parseInt(field.get(obj).toString());
        // statusBarHeight = this.getResources().getDimensionPixelSize(x);
        // } catch (Exception e1) {
        // e1.printStackTrace();
        // }
        return statusBarHeight;
    }

    @Override
    public Bitmap readBitmap(int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        // opt.inSampleSize =2;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    @Override
    protected void onDestroy() {
        printLog();
        super.onDestroy();
        if (Lg.DEBUG) {
            ViewServer.get(this).removeWindow(this);
        }
    }

    @Override
    public Bitmap readBitmap(String path) {
        // myReduceImage(path);
        // BitmapFactory.Options opt = new BitmapFactory.Options();
        // opt.inPreferredConfig = Bitmap.Config.RGB_565;
        // opt.inSampleSize =2;
        // opt.inPurgeable = true;
        // opt.inInputShareable = true;
        // return BitmapFactory.decodeFile(path, opt);
        return myReduceImage(path);
    }

    /**
     * 获取圆形图片
     *
     * @param bitmap
     * @return
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public Bitmap getRoundedCornerBitmap(int res) {
        return getRoundedCornerBitmap(BitmapFactory.decodeResource(
                getResources(), res));
    }

    public void startUploadAPP(String url, String date, String msg,
                               boolean flag, UpdateManager.IUpdateManager listener) {
        new UpdateManager(this).startUpdateInfo(url, date, msg, flag, listener);
    }

    @Override
    public void setTextViewIcon(TextView tv, int l, int t, int r, int b) {
        Drawable left = null;
        Drawable right = null;
        Drawable top = null;
        Drawable bottom = null;
        if (l > 0) {
            left = getResources().getDrawable(l);
            left.setBounds(0, 0, left.getMinimumWidth(),
                    left.getMinimumHeight());
        }
        if (r > 0) {
            right = getResources().getDrawable(r);
            right.setBounds(0, 0, right.getMinimumWidth(),
                    right.getMinimumHeight());
        }
        if (t > 0) {
            top = getResources().getDrawable(t);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        }
        if (b > 0) {
            bottom = getResources().getDrawable(b);
            bottom.setBounds(0, 0, bottom.getMinimumWidth(),
                    bottom.getMinimumHeight());
        }
        tv.setCompoundDrawables(left, top, right, bottom);
    }

    /**
     * 模拟键盘的 view 推移 键盘出现时 挡住输入窗口， view 整体向上推移 包括 导航栏和 bodyview
     */
    public void pushBody(int dept) {
        ObjectAnimator.ofFloat(bodyView, "y", dept).setDuration(300).start();
        ObjectAnimator.ofFloat(navigationBar.layout, "y", dept - dip2px(55))
                .setDuration(300).start();
    }

    public void pushBackBody() {
        ObjectAnimator.ofFloat(bodyView, "y", dip2px(55)).setDuration(300)
                .start();
        ObjectAnimator.ofFloat(navigationBar.layout, "y", 0).setDuration(300)
                .start();
    }

    public ArrayList<BasicAsyncTask> getTasks() {
        return tasks;
    }
}

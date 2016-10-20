package cn.android_mobile.core;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import cn.android_mobile.core.database.DBHelper;
import cn.android_mobile.core.net.ThreadPool;
import cn.android_mobile.toolkit.CacheUtil;
import cn.android_mobile.toolkit.Lg;

public class BasicApplication extends Application {

    public final static String TAG = "PateoApplication";

    /**
     * 数据库连接类
     */
    public static DBHelper dbHelper = null;
    public static SharedPreferences mPref;
    public static int helpFlag = -1;

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private DecimalFormat df = new DecimalFormat("#.##");
    private String versionName;
    private String versionCode;
    private String appName;
    public static Stack<Activity> activityStack = new Stack<Activity>();
    public static Context context;
    public static final String FNAME = "CoreLib";
    private static final File CACHE_ROOT_DIR = new File(
            android.os.Environment.getExternalStorageDirectory(),
            FNAME);

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Lg.DEBUG == true) {
            // CrashHandler.getInstance().init(this.getApplicationContext());
        }
        mPref = getSharedPreferences("peato_sp", Context.MODE_PRIVATE);
        context = getApplicationContext();
//		 CrashHandler crashHandler = CrashHandler.getInstance();
        // Thread.setDefaultUncaughtExceptionHandler(this);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                // 设置图片以如何的编码方式显示  
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                // 设置图片的解码类型  
                .bitmapConfig(Bitmap.Config.RGB_565)
                // .decodingOptions(android.graphics.BitmapFactory.Options  
                // decodingOptions)//设置图片的解码配置  
                .considerExifParams(true)
//				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//				.bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        //图片保存系统路径
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, FNAME);
        Lg.print("CacheDir:::" + cacheDir.getPath());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .defaultDisplayImageOptions(defaultOptions)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSizePercentage(20)// 20%
                .diskCache(new UnlimitedDiskCache(cacheDir, null/* 100M */))
                .diskCacheSize(1024 * 1024 * 200)//200M
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .writeDebugLogs() // Remove for release app
                .memoryCache(new WeakMemoryCache())
                .imageDecoder(new BaseImageDecoder(false))
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        CacheUtil.context = this.getApplicationContext();
        helpFlag = CacheUtil.getInteger("app_help_view");
    }

    public static synchronized DBHelper getDBHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        }
        return dbHelper;
    }

    public static synchronized void destroyDataHelper() {
        if (null != dbHelper) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    @Override
    public void onTerminate() {
        destroyDataHelper();
        super.onTerminate();
    }

    /**
     * @param context
     */
    public static void initImageLoader(Context context) {
        //        DisplayImageOptions options = new DisplayImageOptions.Builder()
        //                .showImageOnLoading(R.drawable.com_default_pic)
        //                .showImageForEmptyUri(R.drawable.com_default_pic)
        //                .showImageOnFail(R.drawable.com_default_pic).cacheInMemory(true)
        //                .cacheOnDisc(true).build();
        //        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        //                .threadPriority(Thread.NORM_PRIORITY - 2)
        //                .denyCacheImageMultipleSizesInMemory()
        //                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        //                .memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
        //                .discCacheFileCount(100).defaultDisplayImageOptions(options)
        //                .discCacheFileNameGenerator(new Md5FileNameGenerator())
        //                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        //        ImageLoader.getInstance().init(config);
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();

    }

    public static void finishAllActivity() {
        while (activityStack.size() > 0) {
            activityStack.pop().finish();
        }
    }

    public static void closeApp() {
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
        android.os.Process.killProcess(android.os.Process.myPid());
        System.gc();
        System.exit(0);
    }

}

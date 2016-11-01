package com.android_mobile.core.helper.image;

import android.content.Context;

import com.android_mobile.core.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


/**
 * Create By mxh on 2016/3/5
 * 重写ImageLoad 方便以后修改ImageLoad
 */
public final class UniversalImageLoad extends ImageLoader {

    private static final String CACHE_IMAGE_LOADER = "images";
    private volatile static UniversalImageLoad instance;

    /**
     * Returns singleton class instance
     */
    public static UniversalImageLoad getInstance() {
        if (instance == null) {
            throw new IllegalStateException("The ImageLoad not initialize!");
        }
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (UniversalImageLoad.class) {
                if (instance == null) {
                    instance = new UniversalImageLoad(context);
                }
            }
        }
    }

    protected UniversalImageLoad(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("The ImageLoad init Context be not null!");
        }
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, CACHE_IMAGE_LOADER);
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory() //多尺寸缓存一份
                .diskCache(new UnlimitedDiskCache(cacheDir))//缓存文件位置
                .diskCacheSize(200 * 1024 * 1024)//磁盘缓存200M
                .diskCacheFileCount(500)//缓存文件数
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//缓存文件名
                .memoryCache(new LruMemoryCache(25 * 1024 * 1024))//bitmap的强引用
                .memoryCacheSize(25 * 1024 * 1024)//内存缓存 25M
                .memoryCacheSizePercentage(13)// 设置内存缓存最大大小占当前应用可用内存的百分比
                .tasksProcessingOrder(QueueProcessingType.LIFO) //任务队列
                .defaultDisplayImageOptions(UniversalDisplayOptions.create(context.getResources()
                        .getDrawable(R.mipmap.img_item_default)))// 占位符
                .build();
        super.init(configuration);
    }
}

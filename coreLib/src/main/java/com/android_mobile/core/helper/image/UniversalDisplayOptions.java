package com.android_mobile.core.helper.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import com.android_mobile.core.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


/**
 * Create by mxh on 2016/3/5
 * ImageLoad 配置信息
 */
public final class UniversalDisplayOptions extends DisplayImageOptions.Builder {

    public UniversalDisplayOptions() {
        super();
        resetViewBeforeLoading(true);  // default
        cacheOnDisc(true);
        cacheInMemory(true);
        imageScaleType(ImageScaleType.IN_SAMPLE_INT); // brucend
        bitmapConfig(Bitmap.Config.RGB_565); // default
    }

    public static DisplayImageOptions createDefultWebOption() {

        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.img_item_default)
                .showImageForEmptyUri(R.mipmap.img_item_default)
                .showImageOnFail(R.mipmap.img_item_default)
                .cacheInMemory(true)
                .cacheOnDisc(true).build();
    }

    public static DisplayImageOptions create(Drawable shouldDrawable) {
        UniversalDisplayOptions options = new UniversalDisplayOptions();
        options.showImageForEmptyUri(shouldDrawable);
        options.showImageOnLoading(shouldDrawable); // resource or drawable
        options.showImageOnFail(shouldDrawable); // resource or drawable
        return options.build();
    }

    public static DisplayImageOptions create(@DrawableRes int shouldDrawable) {
        UniversalDisplayOptions options = new UniversalDisplayOptions();
        options.showImageForEmptyUri(shouldDrawable);
        options.showImageOnLoading(shouldDrawable); // resource or drawable
        options.showImageOnFail(shouldDrawable); // resource or drawable
        return options.build();
    }
}

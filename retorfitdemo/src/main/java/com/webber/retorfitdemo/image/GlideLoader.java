package com.webber.retorfitdemo.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webber.retorfitdemo.R;

/**
 * Created by mxh on 2016/11/18.
 * Describe：图片加载器
 */

public class GlideLoader {

    public static void load(Context context, String url, ImageView iv) {
        Glide.with(context).load(url)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
    }
}

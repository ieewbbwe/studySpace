package com.android_mobile.core.ui.comp.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android_mobile.core.R;
import com.android_mobile.core.utiles.CollectionUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class LoopPagerAdapter<T> extends BasicPageAdapter<T> {
    private static final String TAG = "LoopPagerAdapter";
    private DisplayImageOptions options;//设置图片参数
    private OnImageClickListener mListener;

    public LoopPagerAdapter(Context context, List<T> list) {
        super(context, list);
        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.img_item_default)
                .imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true).build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public List<T> getList() {
        return list;
    }

    public void addImage(ArrayList images) {
        if (list.size() > 0) {
            list.clear();
        }
        if (CollectionUtils.isNotEmpty(images)) {
            list.addAll(images);
        }
        notifyDataSetChanged();
    }

    public interface OnImageClickListener {
        void onImageClick(int pos);
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.header_viewpager, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.header_imageview);
        imageView.setImageResource(R.mipmap.img_item_default);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {

    }

}

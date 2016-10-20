package cn.android_mobile.core.ui.listener;

import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public abstract class ImageListener implements ImageLoadingListener {

	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// TODO Auto-generated method stub
		
	}

}

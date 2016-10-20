package cn.android_mobile.core;

import android.view.View.OnClickListener;

public abstract class BasicListener  implements OnClickListener{
	protected BasicActivity activity;
	public BasicListener(BasicActivity activity){
		this.activity=activity;
	}
}

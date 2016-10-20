package cn.android_mobile.core.ui;

import android.view.View;
import android.view.ViewGroup;
import cn.android_mobile.core.BasicActivity;

public abstract class BasicComponent {
	protected BasicActivity activity;
	protected View view;
	private ViewGroup root;
	public BasicComponent(BasicActivity activity,int resId){
		this.activity = activity;
		view = activity.inflater.inflate(onCreate(), null);
		root = (ViewGroup) activity.findViewById(resId);
		root.addView(view);
		initComp();
		initListener();
	}
	public BasicComponent(BasicActivity activity,ViewGroup v){
		this.activity = activity;
		view = activity.inflater.inflate(onCreate(), null);
		root = v;
		root.addView(view);
		initComp();
		initListener();
	}
	public abstract int onCreate();
	public abstract void initComp();
	public abstract void initListener();
	public View findViewById(int id){
		return view.findViewById(id);
	}
	public ViewGroup getRoot(){
		return root;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public boolean isDisplay() {
		return isDisplay;
	}
	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}
	private  int offset=-1;
	private  boolean isDisplay=false;
	
}

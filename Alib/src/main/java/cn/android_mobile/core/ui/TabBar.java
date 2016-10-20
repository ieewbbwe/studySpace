package cn.android_mobile.core.ui;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.android_mobile.core.BasicFragment;

public class TabBar implements View.OnClickListener {
	public interface ITabBar{
		public void callbackFragment(BasicFragment f);
	}
	public ITabBar listener=null;
	public LinearLayout layout;
	public ArrayList<TabItem> tabs=new ArrayList<TabItem>();
	private boolean isHidden = false;
	protected View view;
	protected INavigationBar callback;
	public TabBar(LinearLayout vs) {
		this.layout = vs;
	}
	public void hidden() {
		isHidden = true;
		layout.setVisibility(View.GONE);
	}

	public void display() {
		isHidden = false;
		layout.setVisibility(View.VISIBLE);
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void addListener(INavigationBar listener) {
		callback = listener;
	}
	@Override
	public void onClick(View v) {
		
	}
	public void setBackground(int resid){
		layout.setBackgroundResource(resid);
	}
	public void setBackground(String color){
		layout.setBackgroundColor(Color.parseColor(color));
	}
	public void addTabItem(View v,final TabItem item){
		layout.addView(v);
		tabs.add(item);
		if(item.layout!=null){
			item.layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (TabItem item : tabs) {
						item.unSelected();
					}
					item.selected();
					if(listener!=null){
						listener.callbackFragment(item.fragment);
					}
				}
			});
		}
	}
}

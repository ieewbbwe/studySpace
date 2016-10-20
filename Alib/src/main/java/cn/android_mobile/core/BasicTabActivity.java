package cn.android_mobile.core;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.android_mobile.core.enums.ModalDirection;
import cn.android_mobile.core.net.IBasicAsyncTask;
import cn.android_mobile.core.net.http.ServiceRequest;
import cn.android_mobile.core.ui.TabBar;
import cn.android_mobile.core.ui.TabBar.ITabBar;
import cn.android_mobile.core.ui.TabItem;

public class BasicTabActivity extends BasicActivity implements ITabBar {

	
	protected TabBar tabBar;
	private RelativeLayout tabbarRelativeLayout;
	private ImageView itemImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_frame);
		fragmentBodyLayoutRes=R.id.activity_tab_fragment_layout;
		tabBar = new TabBar((LinearLayout)findViewById(R.id.activity_tabbar_layout));
		tabBar.listener=this;
		tabbarRelativeLayout = (RelativeLayout) findViewById(R.id.activity_tabbar_relativelayou);
		itemImg = (ImageView) findViewById(R.id.tabbar_item_bg);
	}
	
	public void setTabBackgroundResource(int resid){
		
		tabbarRelativeLayout.setBackgroundResource(resid);
	}
	public void setTabBackgroundColor(int color){
		tabbarRelativeLayout.setBackgroundColor(color);
	}
	public void setItemImageResource(int resid){
		itemImg.setImageResource(resid);
	}
	public void addTabItem(BasicFragment f,String itemName,final int itemIconSelected,int itemInconUnSelected,String textSelectedColor, String textUnSelectedColor,int bgSelectedRes,int bgUnSelectedRes){
		View v=getLayoutInflater().inflate(R.layout.activity_tab_item, null);
		LinearLayout _layout = (LinearLayout) v.findViewById(R.id.activity_tab_linearlayout);
		_layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1)); 
		final ImageView _itemIcon = (ImageView) v.findViewById(R.id.activity_tab_icon);
		final TextView _itemName = (TextView) v.findViewById(R.id.activity_tab_text);
		TabItem item=new TabItem(_layout, _itemIcon, _itemName, itemName, itemIconSelected, itemInconUnSelected, textSelectedColor, textUnSelectedColor,bgSelectedRes,bgUnSelectedRes);
		item.fragment=f;
		item.itemBg=itemImg;
		tabBar.addTabItem(v,item);
		refershItemBg();
	}
	public void addTabItem(BasicFragment f,final int itemIconSelected,int itemInconUnSelected,int bgSelectedRes,int bgUnSelectedRes){
		View v=getLayoutInflater().inflate(R.layout.activity_tab_item, null);
		LinearLayout _layout = (LinearLayout) v.findViewById(R.id.activity_tab_linearlayout);
		_layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1)); 
		final ImageView _itemIcon = (ImageView) v.findViewById(R.id.activity_tab_icon);
		final TextView _itemName = (TextView) v.findViewById(R.id.activity_tab_text);
		TabItem item=new TabItem(_layout, _itemIcon, _itemName, itemIconSelected, itemInconUnSelected,bgSelectedRes,bgUnSelectedRes);
		item.fragment=f;
		item.itemBg=itemImg;
		tabBar.addTabItem(v,item);
		refershItemBg();
	}
	public void addTabItem(BasicFragment f,String itemName,String textSelectedColor, String textUnSelectedColor,int bgSelectedRes,int bgUnSelectedRes){
		View v=getLayoutInflater().inflate(R.layout.activity_tab_item, null);
		LinearLayout _layout = (LinearLayout) v.findViewById(R.id.activity_tab_linearlayout);
		_layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1)); 
		final ImageView _itemIcon = (ImageView) v.findViewById(R.id.activity_tab_icon);
		final TextView _itemName = (TextView) v.findViewById(R.id.activity_tab_text);
		TabItem item=new TabItem(_layout, _itemIcon, _itemName, itemName, textSelectedColor, textUnSelectedColor,bgSelectedRes,bgUnSelectedRes);
		item.fragment=f;
		item.itemBg=itemImg;
		tabBar.addTabItem(v,item);
		refershItemBg();
	}
	
	public void addTabItem(BasicFragment f,String itemName,final int itemIconSelected,int itemInconUnSelected,String textSelectedColor, String textUnSelectedColor){
		View v=getLayoutInflater().inflate(R.layout.activity_tab_item, null);
		LinearLayout _layout = (LinearLayout) v.findViewById(R.id.activity_tab_linearlayout);
		_layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1)); 
		final ImageView _itemIcon = (ImageView) v.findViewById(R.id.activity_tab_icon);
		final TextView _itemName = (TextView) v.findViewById(R.id.activity_tab_text);
		TabItem item=new TabItem(_layout, _itemIcon, _itemName, itemName, itemIconSelected, itemInconUnSelected, textSelectedColor, textUnSelectedColor);
		item.fragment=f;
		item.itemBg=itemImg;
		tabBar.addTabItem(v,item);
		refershItemBg();
	}
	public void addTabItem(BasicFragment f,String itemName,String textSelectedColor, String textUnSelectedColor){
		View v=getLayoutInflater().inflate(R.layout.activity_tab_item, null);
		LinearLayout _layout = (LinearLayout) v.findViewById(R.id.activity_tab_linearlayout);
		_layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
		final ImageView _itemIcon = (ImageView) v.findViewById(R.id.activity_tab_icon);
		final TextView _itemName = (TextView) v.findViewById(R.id.activity_tab_text);
		TabItem item=new TabItem(_layout,_itemIcon, _itemName, itemName, textSelectedColor, textUnSelectedColor);
		item.fragment=f;
		item.itemBg=itemImg;
		tabBar.addTabItem(v,item);
		refershItemBg();
	}
	public void addTabItem(BasicFragment f,final int itemIconSelected,int itemInconUnSelected){
		View v=getLayoutInflater().inflate(R.layout.activity_tab_item, null);
		LinearLayout _layout = (LinearLayout) v.findViewById(R.id.activity_tab_linearlayout);
		_layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1)); 
		final ImageView _itemIcon = (ImageView) v.findViewById(R.id.activity_tab_icon);
		final TextView _itemName = (TextView) v.findViewById(R.id.activity_tab_text);
		TabItem item=new TabItem(_layout, _itemIcon,_itemName, itemIconSelected, itemInconUnSelected);
		item.fragment=f;
		item.itemBg=itemImg;
		tabBar.addTabItem(v,item);
		refershItemBg();
	}

	@Override
	protected void initComp() {
		
	}

	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
	private void refershItemBg(){
		itemImg.setLayoutParams(new RelativeLayout.LayoutParams((int) (SCREEN_WIDTH/tabBar.tabs.size()), RelativeLayout.LayoutParams.MATCH_PARENT));
	}
	public void select(BasicFragment f){
		for (TabItem item : tabBar.tabs) {
			if(item.fragment.name==f.name){
				item.selected();
			}else{
				item.unSelected();
			}
		}
		callbackFragment(f);
	}

	@Override
	public void callbackFragment(BasicFragment f) {
		this.pushFragment(R.id.activity_tab_fragment_layout, f);
	}

	@Override
	public void async(ServiceRequest req, IBasicAsyncTask callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushModalFragment(ModalDirection d, int widthDip, int time,
			BasicFragment f, boolean hasBackground) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTextViewIcon(TextView tv, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
}

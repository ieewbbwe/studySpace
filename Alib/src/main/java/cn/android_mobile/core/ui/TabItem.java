package cn.android_mobile.core.ui;

import com.nineoldandroids.animation.ObjectAnimator;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.android_mobile.core.BasicFragment;

public class TabItem {

	private ImageView iconImageView=null;
	private TextView tabNameTextView=null;
	private int iconSelectedRes=-1;
	private int iconUnSelectedRes=-1;
	private String textSelectedColor=null;
	private String textUnSelectedColor=null;
	private int bgSelectedRes=-1;
	private int bgUnSelectedRes=-1;
	public LinearLayout layout=null;
	public BasicFragment fragment=null;
	public ImageView itemBg=null;
	public TabItem(LinearLayout layout,
			ImageView iv,TextView tv,String name,
			int selectedRes,int unselectedRes,
			String textSelectedColor,String textUnselectedColor,
			int bgSelectedRes,int bgUnselectedRes){
		this.layout=layout;
		this.iconImageView=iv;
		this.tabNameTextView=tv;
		this.iconSelectedRes=selectedRes;
		this.iconUnSelectedRes=unselectedRes;
		this.textSelectedColor=textSelectedColor;
		this.textUnSelectedColor=textUnselectedColor;
		this.bgSelectedRes=bgSelectedRes;
		this.bgUnSelectedRes=bgUnselectedRes;
		this.iconImageView.setImageResource(unselectedRes);
		this.tabNameTextView.setText(name);
	}
	public TabItem(LinearLayout layout,
			ImageView iv,TextView tv,String name,
			String textSelectedColor,String textUnselectedColor,
			int bgSelectedRes,int bgUnselectedRes){
		this.layout=layout;
		this.iconImageView=iv;
		this.tabNameTextView=tv;
		this.textSelectedColor=textSelectedColor;
		this.textUnSelectedColor=textUnselectedColor;
		this.bgSelectedRes=bgSelectedRes;
		this.bgUnSelectedRes=bgUnselectedRes;
		this.tabNameTextView.setText(name);
	}
	public TabItem(LinearLayout layout,
			ImageView iv,TextView tv,
			int selectedRes,int unselectedRes,
			int bgSelectedRes,int bgUnselectedRes){
		this.layout=layout;
		this.iconImageView=iv;
		this.tabNameTextView=tv;
		this.iconSelectedRes=selectedRes;
		this.iconUnSelectedRes=unselectedRes;
		this.bgSelectedRes=bgSelectedRes;
		this.bgUnSelectedRes=bgUnselectedRes;
		this.iconImageView.setImageResource(unselectedRes);
	}
	public TabItem(LinearLayout layout,
			ImageView iv,TextView tv,String name,
			int selectedRes,int unselectedRes,
			String textSelectedColor,String textUnselectedColor){
		this.layout=layout;
		this.iconImageView=iv;
		this.tabNameTextView=tv;
		this.iconSelectedRes=selectedRes;
		this.iconUnSelectedRes=unselectedRes;
		this.textSelectedColor=textSelectedColor;
		this.textUnSelectedColor=textUnselectedColor;
		this.iconImageView.setImageResource(unselectedRes);
		this.tabNameTextView.setText(name);
	}
	public TabItem(LinearLayout layout,
			ImageView iv,TextView tv,
			int selectedRes,int unselectedRes){
		this.layout=layout;
		this.iconImageView=iv;
		this.tabNameTextView=tv;
		this.iconSelectedRes=selectedRes;
		this.iconUnSelectedRes=unselectedRes;
		this.iconImageView.setImageResource(unselectedRes);
	}
	public TabItem(LinearLayout layout,ImageView iv,
			TextView tv,String name,
			String textSelectedColor,String textUnselectedColor){
		this.layout=layout;
		this.iconImageView=iv;
		this.tabNameTextView=tv;
		this.textSelectedColor=textSelectedColor;
		this.textUnSelectedColor=textUnselectedColor;
		this.tabNameTextView.setText(name);
	}
	
	
	public void selected(){
		if(iconImageView!=null&&iconSelectedRes!=-1){
			iconImageView.setImageResource(iconSelectedRes);
		}else{
			iconImageView.setVisibility(View.GONE);
		}
		if(layout!=null&&bgSelectedRes!=-1){
			layout.setBackgroundResource(bgSelectedRes);
		}
		if(tabNameTextView!=null&&textSelectedColor!=null){
			tabNameTextView.setTextColor(Color.parseColor(textSelectedColor));
		}else{
			tabNameTextView.setVisibility(View.GONE);
		}
		
		if(itemBg!=null){
//			itemBg.setX(layout.getX());
			ObjectAnimator.ofFloat(itemBg, "translationX", layout.getX()).start();
		}
	}
	public void unSelected(){
		if(iconImageView!=null&&iconUnSelectedRes!=-1){
			iconImageView.setImageResource(iconUnSelectedRes);
		}else{
			iconImageView.setVisibility(View.GONE);
		}
		if(layout!=null&&bgUnSelectedRes!=-1){
			layout.setBackgroundResource(bgUnSelectedRes);
		}
		if(tabNameTextView!=null&&textUnSelectedColor!=null){
			tabNameTextView.setTextColor(Color.parseColor(textUnSelectedColor));
		}else{
			tabNameTextView.setVisibility(View.GONE);
		}
	}
}

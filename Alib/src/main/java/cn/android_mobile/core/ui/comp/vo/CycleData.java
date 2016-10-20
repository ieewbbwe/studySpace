package cn.android_mobile.core.ui.comp.vo;

public class CycleData {
	public String imgUrl="";
	public String title="";
	public Object obj=null;
	public CycleData(String imgUrl, String title) {
		super();
		this.imgUrl = imgUrl;
		this.title = title;
	}
	public CycleData(String imgUrl, String title,Object obj) {
		super();
		this.imgUrl = imgUrl;
		this.title = title;
		this.obj=obj;
	}
}

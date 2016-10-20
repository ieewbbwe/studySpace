package cn.android_mobile.core.enums;

public enum RequestType {
	POST("post"), GET("get");
	private String context;
	private RequestType(String context) {
		this.context = context;
	}
	public String getContext(){
		return context;
	}
}

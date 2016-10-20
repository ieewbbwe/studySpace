package cn.android_mobile.core.event;

import cn.android_mobile.core.event.BasicEventDispatcher.IBasicListener;

public class EventItem {
	public String type;
	public String key;
	public IBasicListener listener;
}

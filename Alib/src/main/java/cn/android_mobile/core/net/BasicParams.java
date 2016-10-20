package cn.android_mobile.core.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;


public class BasicParams {
	
	private List<BasicNameValuePair> params = null;
	
	public BasicParams()
	{
		params = new ArrayList<BasicNameValuePair>();
	}
	
	public void addParam(String name,String value)
	{
		BasicNameValuePair param = new BasicNameValuePair(name, value);
		params.add(param);
	}
	
	public List<BasicNameValuePair> getParams()
	{
		return params;
	}
}

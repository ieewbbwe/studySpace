package cn.android_mobile.core.net.http;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.android_mobile.core.enums.RequestType;
import cn.android_mobile.core.enums.ResponseChartset;
import cn.android_mobile.core.net.HttpClientFactory;
import cn.android_mobile.toolkit.Lg;

public abstract class Service {
	public RequestType REQUEST_TYPE = RequestType.POST;
	public ResponseChartset CHARSET = ResponseChartset.UTF_8;
	private static final int SET_CONNECTION_TIMEOUT = 60 * 1000;
	private static final int SET_SOCKET_TIMEOUT = 60 * 1000;
	protected DefaultHttpClient client = null;
	protected HttpGet requestGet = null;
	protected HttpPost requestPost = null;

	protected abstract ServiceResponse execute(ServiceRequest request);

	protected Gson g = new Gson();

	private Http http = null;

	public void cancel() {
		if (requestGet != null && requestGet.isAborted() == false) {
			requestGet.abort();
		}
		if (requestPost != null && requestPost.isAborted() == false) {
			requestPost.abort();
		}
		if (http != null) {
			http.cancel();
		}
		if (client != null) {
			client.getConnectionManager().shutdown();
		}
	}

	protected DefaultHttpClient createHttpClient() {
		if (client == null) {
			// client = new DefaultHttpClient();
			client = HttpClientFactory.getHttpClient();
		}
		return client;
	}

	protected List<BasicNameValuePair> getParams(ServiceRequest req) {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair param = null;
		Class<? extends ServiceRequest> c = req.getClass();
		Class supreClazz = c.getSuperclass();
		Field[] fields = c.getDeclaredFields();
		Field[] parentFields = supreClazz.getDeclaredFields();

		Field[] result = new Field[fields.length + parentFields.length];
		System.arraycopy(fields, 0, result, 0, fields.length);
		System.arraycopy(parentFields, 0, result, fields.length,
				parentFields.length);

		for (Field f : result) {
			f.setAccessible(true);
			try {
				String s = null;
				if (f.get(req) instanceof Integer) {
					s = String.valueOf((Integer) f.get(req));
				}
				if (f.get(req) instanceof String) {
					s = (String) f.get(req);
				}
				if (f.get(req) instanceof Double) {
					s = String.valueOf((Double) f.get(req));
				}
				if (f.get(req) instanceof Float) {
					s = String.valueOf((Float) f.get(req));
				}
				if (f.get(req) instanceof Boolean) {
					s = String.valueOf((Boolean) f.get(req));
				}
				param = new BasicNameValuePair(f.getName(), s);
				if (s != null) {
					params.add(param);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		return params;
	}

	protected String request_raw_data(RequestType requestType,
			String actionUrl, ServiceRequest req) {
		String s_result = null;
		client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(actionUrl);
		try {
			Lg.print("network", "params:" + g.toJson(req));
			httppost.setEntity(new ByteArrayEntity(g.toJson(req).getBytes()));
			HttpResponse response = client.execute(httppost);
			if (response != null
					&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				s_result = EntityUtils.toString(response.getEntity(),
						CHARSET.context);
				Lg.print("network", "charset:" + CHARSET.context);
				Lg.print("network", s_result);
				Lg.println("", true);
			}
		} catch (ClientProtocolException e) {
			/**/
		} catch (IOException e) {
			/**/
		} finally {
			cancel();
		}
		return s_result;
	}

	protected String request(RequestType requestType, String actionUrl,
			ServiceRequest req) {
		http = new Http(actionUrl, requestType.getContext());
		List<BasicNameValuePair> valueList = getParams(req);
		Lg.print("network", "URL:" + actionUrl);
		Lg.print("network", "params:" + g.toJson(valueList));
		if (valueList.size() > 0) {
			for (BasicNameValuePair value : valueList) {
				if (value.getValue().toLowerCase().indexOf("file://") != -1) {
					String[] split = value.getValue().split(";");
					for (String s : split) {
						if (!s.equals("")) {
							http.addFile(s.replaceAll("file://", ""),
									value.getName());
						}
					}
				} else {
					http.addParam(value.getName(), value.getValue());
				}
			}
		}
		String str = http.execute();
		// Lg.print("network_escape", "response:" + Escape.escape(str));
		Lg.print("network", "response:" + str);
		return str;
	}
	// protected String request(RequestType requestType, String actionUrl,
	// ServiceRequest req) {
	// client = createHttpClient();
	// HttpConnectionParams.setConnectionTimeout(client.getParams(),
	// SET_CONNECTION_TIMEOUT);
	// HttpConnectionParams.setSoTimeout(client.getParams(),
	// SET_SOCKET_TIMEOUT);
	// HttpResponse response = null;
	// String s_result = null;
	// Lg.print("network","URL:"+actionUrl);
	// Lg.print("network","params:"+g.toJson(req));
	// try {
	// if (requestType.equals(RequestType.POST)) {
	// Lg.print("network","Type:POST");
	// requestPost = new HttpPost(actionUrl);
	// MultipartEntity entity = new MultipartEntity();
	// // MultipartEntity entity = new
	// MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName(org.apache.http.protocol.HTTP.UTF_8));
	// List<BasicNameValuePair> valueList = getParams(req);
	// if (valueList.size() > 0) {
	// for (BasicNameValuePair value : valueList) {
	// if (value.getValue().toLowerCase().indexOf("file://") != -1) {
	// entity.addPart(value.getName(),
	// new FileBody(new File(value.getValue()
	// .replaceAll("file://", ""))));
	// } else {
	// entity.addPart(
	// value.getName(),
	// new StringBody(
	// value.getValue(),
	// Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
	// }
	// }
	// requestPost.setEntity(entity);
	// }
	// response = client.execute(requestPost);
	// } else {
	// Lg.print("Type:GET");
	// List<BasicNameValuePair> valueList = getParams(req);
	// String s_params = "?";
	// for (BasicNameValuePair value : valueList) {
	// if (s_params.length() > 1)
	// s_params += "&";
	// s_params += value.getName() + "=" + value.getValue();
	// }
	// requestGet = new HttpGet(actionUrl + s_params);
	// response = client.execute(requestGet);
	// }
	// if (response != null
	// && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// s_result = EntityUtils.toString(response.getEntity(),
	// CHARSET.context);
	// Lg.print("network","charset:" + CHARSET.context);
	// Lg.print("network",s_result);
	// Lg.println("", true);
	// }
	// } catch (UnsupportedEncodingException e) {
	// // e.printStackTrace();
	// } catch (ClientProtocolException e) {
	// // e.printStackTrace();
	// } catch (IOException e) {
	// // e.printStackTrace();
	// } finally {
	// cancel();
	// }
	// return s_result;
	// }
	// protected String request2(RequestType requestType, String actionUrl,
	// ServiceRequest req) {
	// client = createHttpClient();
	// HttpConnectionParams.setConnectionTimeout(client.getParams(),
	// SET_CONNECTION_TIMEOUT);
	// HttpConnectionParams.setSoTimeout(client.getParams(),
	// SET_SOCKET_TIMEOUT);
	// HttpResponse response = null;
	// String s_result = null;
	// Lg.print("URL:"+actionUrl);
	// try {
	// if (requestType.equals(RequestType.POST)) {
	// Lg.print("network","Type:POST");
	// requestPost = new HttpPost(actionUrl);
	// requestPost.setEntity(new UrlEncodedFormEntity(getParams(req),
	// HTTP.UTF_8));
	// response = client.execute(requestPost);
	// } else {
	// Lg.print("network","Type:GET");
	// List<BasicNameValuePair> valueList = getParams(req);
	// String s_params = "?";
	// for (BasicNameValuePair value : valueList) {
	// if (s_params.length() > 1)
	// s_params += "&";
	// s_params += value.getName() + "=" + value.getValue();
	// }
	// requestGet = new HttpGet(actionUrl + s_params);
	// response = client.execute(requestGet);
	// }
	// if (response != null
	// && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// s_result = EntityUtils.toString(response.getEntity(),
	// CHARSET.context);
	// Lg.print("network","charset:" + CHARSET.context);
	// }
	// } catch (UnsupportedEncodingException e) {
	// } catch (ClientProtocolException e) {
	// } catch (IOException e) {
	// } finally {
	// cancel();
	// }
	// Lg.print("network", s_result);
	// return s_result;
	// }
}
package cn.android_mobile.core.net;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import cn.android_mobile.core.enums.CacheType;
import cn.android_mobile.core.enums.RequestType;
import cn.android_mobile.core.enums.ResponseChartset;
import cn.android_mobile.core.net.http.Service;
import cn.android_mobile.core.net.http.ServiceManager;
import cn.android_mobile.core.net.http.ServiceRequest;
import cn.android_mobile.core.net.http.ServiceResponse;
import cn.android_mobile.toolkit.CacheUtil;
import cn.android_mobile.toolkit.Lg;

/**
 * http 请求封装 支持 直接 传递 URL 加参数 支持 Request Response 封装两种格式
 *
 * @author fyygw
 */
@SuppressLint("HandlerLeak")
public class BasicAsyncTask extends AsyncTask<Object, Integer, Object> {
    public static boolean asyncWithProgress = false;
    private String url;
    protected IBasicAsyncTask callback;
    private Gson g = new Gson();

    public RequestType REQUEST_TYPE = RequestType.GET;
    public ResponseChartset CHARSET = ResponseChartset.UTF_8;
    private CacheType cacheType = CacheType.DEFAULT_NET;
    private IBasicAsyncTaskFinish listener = null;

    private ServiceRequest sRequest;
    private Service sService;
    private ServiceResponse sResponse;

    protected Handler h = null;
    protected Object result = null;

    public BasicAsyncTask(ServiceRequest request, Service service,
                          CacheType cache, IBasicAsyncTask callback) {
        this.sRequest = request;
        this.sService = service;
        this.callback = callback;
        this.cacheType = cache;
    }

    public BasicAsyncTask(ServiceResponse response, Service service,
                          CacheType cache) {
        this.sService = service;
        this.cacheType = cache;
        this.sResponse = response;
    }

    public BasicAsyncTask(IBasicAsyncTask callback, String url,
                          CacheType cacheType) {
        this.url = url;
        this.callback = callback;
        this.cacheType = cacheType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 100:
                        callback.callback(result);
                        break;

                    default:
                        break;
                }
            }

        };
    }

    @Override
    protected Object doInBackground(Object... params) {
        Object obj = null;
        Lg.print(cacheType);
        if (cacheType == CacheType.DEFAULT_NET) {
            obj = _doInBackGround_net(params);
        } else if (cacheType == CacheType.DEFAULT_CACHE_NET) {
            obj = _doInBackGround_cache_net(params);
        } else if (cacheType == CacheType.DEFAULT_CACHE) {
            obj = _doInBackGround_cache(params);
        } else if (cacheType == CacheType.BASIC_NET) {
            obj = _doInBackGround_basic_net(params);
        } else if (cacheType == CacheType.BASIC_CACHE_NET) {
            obj = _doInBackGround_basic_cache_net(params);
        } else if (cacheType == CacheType.BASIC_CACHE) {
            obj = _doInBackGround_basic_cache(params);
        }
        return obj;
    }

    // 从缓存返回
    private Object _doInBackGround_basic_cache(Object[] params) {
        if (params != null && params.length % 2 != 0) {
            Lg.print("params error ...");
            return null;
        }
        String key = url + g.toJson(params);
        result = CacheUtil.getInterfaceObject(key);
        // Lg.print("从缓存获取");
        h.sendEmptyMessage(100);
        return result;
    }

    // 先从缓存 获取， 然后返回网络数据 ，返回两次
    private Object _doInBackGround_basic_cache_net(Object[] params) {
        if (params != null && params.length % 2 != 0) {
            Lg.print("params error ...");
            return null;
        }
        String key = url + g.toJson(params);
        result = CacheUtil.getInterfaceObject(key);
        // Lg.print("从缓存获取");
        BasicParams p = new BasicParams();
        if (params != null) {
            for (int i = 0; i < params.length; i += 2) {
                p.addParam(params[i].toString(), params[i + 1].toString());
            }
        }
        Lg.print("网络获取URL:" + url);
        result = request(REQUEST_TYPE.getContext(), url, p);
        CacheUtil.saveInterfaceObject(key, result);
        if (listener != null) {
            listener.asyncTaskFinish(this);
        }
        h.sendEmptyMessage(100);
        return result;
    }

    // 只从网络返回
    protected Object _doInBackGround_basic_net(Object[] params) {
        if (params != null && params.length % 2 != 0) {
            Lg.print("params error ...");
            return null;
        }
        String key = url + g.toJson(params);
        BasicParams p = new BasicParams();
        if (params != null) {
            for (int i = 0; i < params.length; i += 2) {
                p.addParam(params[i].toString(), params[i + 1].toString());
            }
        }
        Lg.print("网络获取URL:" + url);
        result = request(REQUEST_TYPE.getContext(), url, p);
        CacheUtil.saveObject(key, result);
        if (listener != null) {
            listener.asyncTaskFinish(this);
        }
        return result;
    }

    private Object _doInBackGround_cache(Object[] params) {
        String key = sService.getClass().getName() + g.toJson(sRequest)
                + CacheUtil.cacheKey;
        result = CacheUtil.getInterfaceObject(key);
        if (result != null) {
            Lg.print("从缓存获取");
            h.sendEmptyMessage(100);
        } else {
            sResponse = ServiceManager.getServiceResponse(sRequest, sService);
            result = sResponse;
            CacheUtil.saveInterfaceObject(key, result);
            Lg.print("从网络获取");
            h.sendEmptyMessage(100);
        }
        if (listener != null) {
            listener.asyncTaskFinish(this);
        }
        return result;
    }

    private Object _doInBackGround_cache_net(Object[] params) {
        String key = sService.getClass().getName() + g.toJson(sRequest)
                + CacheUtil.cacheKey;
        Lg.print("_doInBackGround_cache_net    " + key);
        result = CacheUtil.getInterfaceObject(key);
        if (result != null) {
            Lg.print("从缓存获取");
            h.sendEmptyMessage(100);
        } else {
            sResponse = ServiceManager.getServiceResponse(sRequest, sService);
            result = sResponse;
            CacheUtil.saveInterfaceObject(key, result);
            Lg.print("从网络获取");
            h.sendEmptyMessage(100);
        }
        if (listener != null) {
            listener.asyncTaskFinish(this);
        }
        return result;
    }

    private Object _doInBackGround_net(Object[] params) {
        String key = sService.getClass().getName() + g.toJson(sRequest)
                + CacheUtil.cacheKey;
        // Lg.print("_doInBackGround_net    "+key);
        sResponse = ServiceManager.getServiceResponse(sRequest, sService);
        result = sResponse;
        CacheUtil.saveInterfaceObject(key, result);
        if (listener != null) {
            listener.asyncTaskFinish(this);
        }
        h.sendEmptyMessage(100);
        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        // callback.callback(result);
    }

    @Override
    protected void onCancelled() {
        abort();
        if (sService != null) {
            sService.cancel();
        }
        super.onCancelled();
    }

    public void addFinishListener(IBasicAsyncTaskFinish fl) {
        this.listener = fl;
    }

    /**
     *
     */
    private static final int SET_CONNECTION_TIMEOUT = 60 * 1000;
    private static final int SET_SOCKET_TIMEOUT = 60 * 1000;
    private DefaultHttpClient client = null;
    private HttpGet requestGet = null;
    private HttpPost requestPost = null;

    protected String request(String method, String actionUrl, BasicParams params) {
        client = createHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(),
                SET_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(client.getParams(),
                SET_SOCKET_TIMEOUT);
        // HttpProtocolParams.setContentCharset(client.getParams(), HTTP.UTF_8);
        // HttpHost proxy = new HttpHost("10.5.38.14", 8087);
        // client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
        // proxy);

        /**
         * File file = new File(pathToOurFile); MultipartEntity mpEntity = new
         * MultipartEntity(); //文件传输 mpEntity.addPart("userfile", new
         * FileBody(file)); // <input type="file" name="userfile" /> 对应的
         * mpEntity.addPart("username",new StringBody("fangzhen",
         * Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
         * mpEntity.addPart("password",new StringBody("123456",
         * Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
         */
        HttpResponse response = null;
        String s_result = null;
        try {
            if (method.toLowerCase().equals("post")) {
                Lg.print("Type:POST");
                requestPost = new HttpPost(actionUrl);
                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE, null,
                        Charset.forName(org.apache.http.protocol.HTTP.UTF_8));
                List<BasicNameValuePair> valueList = params.getParams();
                for (BasicNameValuePair value : valueList) {
                    if (value.getValue().indexOf("file://") != -1) {
                        String[] paths = value.getValue().split(";");
                        for (String path : paths) {
                            entity.addPart(value.getName(), new FileBody(
                                    new File(path.replaceAll("file://", ""))));
                        }
                    } else {
                        entity.addPart(
                                value.getName(),
                                new StringBody(
                                        value.getValue(),
                                        Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
                    }
                }
                Lg.printJson(entity);
                requestPost.setEntity(entity);
                response = client.execute(requestPost);
            } else if (method.toLowerCase().equals("get")) {
                Lg.print("Type:GET");
                List<BasicNameValuePair> valueList = params.getParams();
                String s_params = "?";
                for (BasicNameValuePair value : valueList) {
                    if (s_params.length() > 1)
                        s_params += "&";
                    s_params += value.getName() + "=" + value.getValue();
                }
                requestGet = new HttpGet(actionUrl + s_params);
                response = client.execute(requestGet);
            }
            if (response != null
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                s_result = EntityUtils.toString(response.getEntity(),
                        CHARSET.context);
                Lg.print("charset:" + CHARSET.context);
                Lg.print(s_result);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            abort();
        }
        return s_result;
    }

    @Override
    protected void onCancelled(Object result) {
        abort();
    }

    public void abort() {
        if (requestGet != null && requestGet.isAborted() == false) {
            requestGet.abort();
        }
        if (requestPost != null && requestPost.isAborted() == false) {
            requestPost.abort();
        }
        // if (client != null) {
        // client.getConnectionManager().shutdown();
        // }
    }

    public DefaultHttpClient createHttpClient() {
        if (client == null) {
            client = HttpClientFactory.getHttpClient();
        }
        return client;
    }
}

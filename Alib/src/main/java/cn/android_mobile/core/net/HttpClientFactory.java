package cn.android_mobile.core.net;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class HttpClientFactory {
	private static DefaultHttpClient httpClient;
	private static int DEFAULT_SOCKET_TIMEOUT=1000*2;
	private static int DEFAULT_SOCKET_BUFFER_SIZE=1024;
	private static int DEFAULT_MAX_CONNECTIONS=100;
	private static int DEFAULT_HOST_CONNECTIONS=100;
	public static  DefaultHttpClient getHttpClient() {
		if(httpClient == null) {
			final HttpParams httpParams = new BasicHttpParams();  
			// timeout: get connections from connection pool
	        ConnManagerParams.setTimeout(httpParams, 1000);  
	        // timeout: connect to the server
	        HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
	        // timeout: transfer data from server
	        HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT*2); 
	        // set max connections per host
	        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));  
	        // set max total connections
	        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
	        // use expect-continue handshake
	        HttpProtocolParams.setUseExpectContinue(httpParams, true);
	        // disable stale check
	        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
	        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);  
	        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8); 
	        HttpClientParams.setRedirecting(httpParams, false);
	        // set user agent
	        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
	        HttpProtocolParams.setUserAgent(httpParams, userAgent); 	
	        // disable Nagle algorithm
	        HttpConnectionParams.setTcpNoDelay(httpParams, true); 
	        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);  
	        // scheme: http and https
	        SchemeRegistry schemeRegistry = new SchemeRegistry();  
	        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
	        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

	        ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);  
	        httpClient = new DefaultHttpClient(manager, httpParams); 
		}		
		
		return httpClient;
	}
}

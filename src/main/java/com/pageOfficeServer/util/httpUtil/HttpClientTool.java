package com.pageOfficeServer.util.httpUtil;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;

/**
 * 连接池工具类
 * 
 * @author wangcheng
 *
 */
public class HttpClientTool {

	// ms毫秒,从池中获取链接超时时间
	private static final long connectionRequestTimeout = 6000;

	// ms毫秒,建立链接超时时间
	private static final int connectTimeout = 5000;

	// ms毫秒,读取超时时间
	private static final int socketTimeout = 5000;

	// 最大总并发,很重要的参数
	private static final int maxTotal = 500;

	// 每路并发,很重要的参数
	private static final int maxPerRoute = 500;

	// 重试连接次数
	private static final int executionNumber = 10;

	// 获取连接池
	public static DefaultHttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();

		// 设置连接超时时间
		HttpConnectionParams.setConnectionTimeout(params, connectTimeout);

		// 数据超时时间
		HttpConnectionParams.setSoTimeout(params, socketTimeout);
		params.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, connectionRequestTimeout);

		// 设置访问协议
		SchemeRegistry schreg = new SchemeRegistry();
		schreg.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schreg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		// 多连接的线程安全的管理器
		PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(schreg);

		// 每个主机的最大并行链接数
		pccm.setDefaultMaxPerRoute(maxPerRoute);

		// 客户端总并行链接最大数
		pccm.setMaxTotal(maxTotal);

		DefaultHttpClient httpClient = new DefaultHttpClient(pccm, params);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= executionNumber) {
					// 如果超过最大重试次数，那么就不要继续了
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					// 不要重试SSL握手异常
					return false;
				}
				HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// 如果请求被认为是幂等的，那么就重试
					return true;
				}
				return false;
			}
		};

		httpClient.setHttpRequestRetryHandler(httpRequestRetryHandler);
		// 关闭cookie功能
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);

		return httpClient;
	}

}

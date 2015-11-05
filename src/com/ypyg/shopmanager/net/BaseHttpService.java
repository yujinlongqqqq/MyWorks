package com.ypyg.shopmanager.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.ypyg.shopmanager.thread.HttpThread;
import com.ypyg.shopmanager.util.Log_;

public class BaseHttpService implements IHttpService {

	private final static String TAG = BaseHttpService.class.getSimpleName();

	private final int mRetryMax = 2;

	private Context mContext = null;
	private INetState mNetState = null;

	/**
	 * 最大连接数
	 */
	public final static int MAX_CONNECTIONS = 100;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 30000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 30;

	/**
	 * 读取超时时间
	 */
	public final static int DEFAULT_READ_TIMEOUT = 15000;

	private final DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0, false);

	private ClientConnectionManager mConnManager;

	private final HttpParams mHttpParams;

	private static final byte[] lock = new byte[0];

	private Integer mMaxReadTimeout = DEFAULT_READ_TIMEOUT;

	// 默认的ＨTTP 端口
	private final Integer DEFAULT_HTTP_PORT = 80;
	// 默认的ＨTTPS 端口
	private final Integer DEFAULT_HTTPS_PORT = 443;
	// 默认的每地址路由数目
	private final Integer DEFAULT_MAX_PER_ROUTE = 50;

	// 默认的CMWAP服务器地址
	private final String DEFAULT_CMWAP_HOST = "10.0.0.172";

	/*
	 * aContext Context aState 网络状况的检测 aMaxReadTimeout 为读取的最大超时时间
	 */
	public BaseHttpService(Context aContext, INetState aState, Integer aMaxReadTimeout) {
		this.mContext = aContext;
		this.mNetState = aState;
		this.mMaxReadTimeout = aMaxReadTimeout;
		this.mHttpParams = buildHttpParams(DEFAULT_MAX_PER_ROUTE, mMaxReadTimeout);
	}

	private HttpParams buildHttpParams(Integer aMaxPerRoute, Integer aMaxReadTimeout) {
		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter("Content-Type", "text/html; charset=UTF-8");
		httpParams.setParameter("Accept", "*/*");
		httpParams.setParameter("User-Agent", "Android");
		httpParams.setParameter("Accept-Language", "UTF-8");
		httpParams.setParameter("Connection", "Keep-Alive"); // 长连接
		httpParams.setParameter("Content-Type", "application/octet-stream");
		httpParams.setParameter("Pragma", "no-cache");
		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, aMaxReadTimeout);

		ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
		// 增加最大连接到MAX_CONNECTIONS
		ConnManagerParams.setMaxTotalConnections(httpParams, MAX_CONNECTIONS);
		// 增加每个路由的默认最大连接到MAX_ROUTE_CONNECTIONS
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);

		connPerRoute.setDefaultMaxPerRoute(aMaxPerRoute);

		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);

		SchemeRegistry registry = new SchemeRegistry();
		Scheme aHttp = new Scheme("http", PlainSocketFactory.getSocketFactory(), DEFAULT_HTTP_PORT);
		registry.register(aHttp);
		Scheme aHttps = new Scheme("https", SSLSocketFactory.getSocketFactory(), DEFAULT_HTTPS_PORT);
		registry.register(aHttps);
		mConnManager = new ThreadSafeClientConnManager(httpParams, registry);
		return httpParams;
	}

	private void checkNetworkConnectState() throws Exception {
		synchronized (lock) {
			if (mNetState.getNetType() == INetState.NETWORK_ERROR) {
				try {
					mNetState.resetNetType(mContext);
				} catch (Exception e) {
				}
				if (mNetState.getNetType() == INetState.NETWORK_ERROR) {
					throw new Exception("检查网络");
				}
			}
		}
	}

	private void checkUniwapAccessing(Exception e) {
		synchronized (lock) {
			if (!mNetState.isUniWap()) {
				mHttpParams.removeParameter(ConnRoutePNames.DEFAULT_PROXY);
			} else if (e instanceof UnknownHostException) {
				HttpHost host = new HttpHost(DEFAULT_CMWAP_HOST, DEFAULT_HTTP_PORT);
				mHttpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
			}
		}
	}

	/**
	 * Simple {@link HttpEntityWrapper} that inflates the wrapped
	 * {@link HttpEntity} by passing it through {@link GZIPInputStream}.
	 */
	private static class InflatingEntity extends HttpEntityWrapper {
		public InflatingEntity(HttpEntity wrapped) {
			super(wrapped);
		}

		@Override
		public InputStream getContent() throws IOException {
			return new GZIPInputStream(wrappedEntity.getContent());
		}

		@Override
		public long getContentLength() {
			return -1;
		}
	}

	@Override
	public DefaultHttpClient getDefaultHttpClient() {
		synchronized (lock) {
			try {
				// 关闭过期连接
				mConnManager.closeExpiredConnections();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (mNetState.isWifi() || mNetState.is3G()) {
				mHttpParams.setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 4 * TCP_SOCKET_BUFFER_SIZE);
				mHttpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TCP_SOCKET_CONNECT_TIMEOUT);
			} else {
				mHttpParams.setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, TCP_SOCKET_BUFFER_SIZE / (mNetState.isUniWap() ? 2 : 1));
				mHttpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2 * TCP_SOCKET_CONNECT_TIMEOUT);
			}
			if (!mNetState.isUniWap()) {
				mHttpParams.removeParameter(ConnRoutePNames.DEFAULT_PROXY);
			}

			DefaultHttpClient defaultClient = new DefaultHttpClient(mConnManager, mHttpParams);
			defaultClient.setHttpRequestRetryHandler(retryHandler);

			defaultClient.addRequestInterceptor(new HttpRequestInterceptor() {
				@Override
				public void process(HttpRequest request, HttpContext context) {
					// Add header to accept gzip content
					if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
						request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
					}
				}
			});

			defaultClient.addResponseInterceptor(new HttpResponseInterceptor() {
				@Override
				public void process(HttpResponse response, HttpContext context) {
					// Inflate any responses compressed with gzip
					final HttpEntity entity = response.getEntity();
					final Header encoding = entity.getContentEncoding();
					if (encoding != null) {
						for (HeaderElement element : encoding.getElements()) {
							if (element.getName().trim().equalsIgnoreCase(ENCODING_GZIP)) {
								response.setEntity(new InflatingEntity(response.getEntity()));
								break;
							}
						}
					}
				}
			});
			return defaultClient;
		}
	}

	@Override
	public void shutdown() {
		synchronized (lock) {
			try {
				mConnManager.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean isCompressedStream(final HttpEntity aEntity) {
		final Header encoding = aEntity.getContentEncoding();
		if (encoding != null) {
			for (HeaderElement element : encoding.getElements()) {
				if (element.getName().trim().equalsIgnoreCase(ENCODING_GZIP)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSuccess(HttpResponse response) {
		if (response != null && response.getStatusLine() != null) {
			StatusLine aStatusLine = response.getStatusLine();
			int iStatusCode = aStatusLine.getStatusCode();
			Log.i(TAG, "网络层请求返回码:" + iStatusCode);
			HttpEntity aEntity = response.getEntity();
			if (null != aEntity) {
				if (aEntity.isChunked()) {
					return ((iStatusCode == HttpStatus.SC_OK) || (iStatusCode == HttpStatus.SC_PARTIAL_CONTENT));
				} else if (isCompressedStream(aEntity)) {
					return ((iStatusCode == HttpStatus.SC_OK) || (iStatusCode == HttpStatus.SC_PARTIAL_CONTENT));
				} else {
					long lContentLen = aEntity.getContentLength();
					return (((iStatusCode == HttpStatus.SC_OK) || (iStatusCode == HttpStatus.SC_PARTIAL_CONTENT)) && (lContentLen > 0) && (lContentLen <= Integer.MAX_VALUE));
				}
			}
		}
		return false;
	}

	/**
	 * 基于http GET协议的下载请求，返回response对象
	 * 
	 * @param url
	 *            下载接口地址
	 * @param begin
	 *            起始的字节数
	 * @param end
	 *            结束 的字节数
	 */
	@Override
	public HttpResponse getHttpRespWithDoGet(String aUrl, long begin, long end) throws Exception {
		HttpThread httpThread = (HttpThread) Thread.currentThread();
		try {
			if (httpThread.defaultClient == null) {
				httpThread.defaultClient = getDefaultHttpClient();
			}
			httpThread.mDefaultPost = new HttpGet(aUrl);
			// // 如果参数有效，设置range头
			// httpThread.mDefaultPost.addHeader("Range", "bytes=" + begin + "-"
			// + end);
			// 发起请求
			HttpResponse aResp = httpThread.defaultClient.execute(httpThread.mDefaultPost);
			if (aResp != null) {
				StatusLine stLine = aResp.getStatusLine();
				if (null != stLine) {
					int iCode = stLine.getStatusCode();
					if (iCode == HttpStatus.SC_NOT_FOUND || iCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
						Log.w(TAG, iCode + ",by " + aUrl);
						httpThread.abort();
						// 直接返回
						return null;
					}
				}
			} else {
				Log.w(TAG, " Response = null by url = " + aUrl);
				httpThread.abort();
			}
			if (isSuccess(aResp)) {
				return aResp;
			} else {
				if (httpThread.mDefaultPost != null && (!httpThread.mDefaultPost.isAborted())) {
					httpThread.mDefaultPost.abort();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			checkUniwapAccessing(e);

			if (!httpThread.isAbort()) {
				httpThread.abort();
			}
		}
		throw new Exception("网络错误");
	}

	private HttpResponse _getHttpRespException(String aUrl, HttpEntity aEntry, HttpThread aHttpThread) throws ClientProtocolException, IOException {
		HttpClient aClient = aHttpThread.defaultClient;
		if (aClient == null) {
			aHttpThread.defaultClient = getDefaultHttpClient();
			aClient = aHttpThread.defaultClient;
		}
		aHttpThread.mDefaultPost = new HttpPost(aUrl);
		HttpPost aPost = (HttpPost) aHttpThread.mDefaultPost;
		aPost.setEntity(aEntry);
		try {
			Log_.e("test", EntityUtils.toString(aEntry));
		} catch (Exception e) {
		}

		HttpResponse aResp = aClient.execute(aPost);

		if (aResp != null) {
			// 检查服务器http连接是否异常
			StatusLine stLine = aResp.getStatusLine();
			if (null != stLine) {
				int iCode = stLine.getStatusCode();
				if (iCode == HttpStatus.SC_NOT_FOUND || iCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					Log_.w(TAG, iCode + ",by " + aUrl);
					aHttpThread.abort();
					// 直接返回
					return null;
				}
			}
		} else {
			Log.w(TAG, " Response = null by url = " + aUrl);
			aHttpThread.abort();
		}

		if (isSuccess(aResp)) {
			return aResp;
		} else {
			if (aPost != null && (!aPost.isAborted())) {
				aPost.abort();
			}
		}
		return null;
	}

	@Override
	public HttpResponse getHttpResp(IReq aReq, HttpEntity aEntry) throws Exception {
		checkNetworkConnectState();
		HttpThread httpThread = (HttpThread) Thread.currentThread();
		int tryTimes = 0;
		while (tryTimes < mRetryMax) {
			try {
				String sUrl = aReq.getUrl();
				if (null == sUrl)
					break;
				HttpResponse aResp = _getHttpRespException(sUrl, aEntry, httpThread);
				if (null != aResp)
					return aResp;
				tryTimes++;
			} catch (Exception e) {
				e.printStackTrace();
				checkUniwapAccessing(e);
				tryTimes++;

				if (httpThread.isAbort()) {
					break;
				}
				httpThread.abort();
			}
		}

		throw new Exception("网络错误");
	}
}

package com.ypyg.shopmanager.net;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

public interface IHttpService {

	public static final int TCP_SOCKET_CONNECT_TIMEOUT = 100000;// 连接超时时间

	public static final int TCP_SOCKET_BUFFER_SIZE = 64 * 1024;// 缓冲区大小,64K

	public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding"; // 接受的编码格式

	public static final String ENCODING_GZIP = "gzip";// gzip

	public static final String GET = "GET";

	public static final String HTTP_URI = "http://";

	public static final String POST = "POST";

	public DefaultHttpClient getDefaultHttpClient();

	public void shutdown();

	public HttpResponse getHttpResp(IReq aReq, HttpEntity aEntry) throws Exception;

	public HttpResponse getHttpRespWithDoGet(String aUrl, long begin, long end) throws Exception;

}

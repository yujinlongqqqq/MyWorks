package com.ypyg.shopmanager.net;

import org.apache.http.HttpEntity;

public interface IReq {
	// 获取URL
	public String getUrl();

	// 获取缓存的Key
	public String getCacheKey();

	// 获取缓存 如果返回null 则表明缓存存在
	public Object getFromCache();

	// 从内存中获取数据
	public Object getFromMemory();

	// 获取实际的发送数据
	public HttpEntity getEntry();
}

package com.ypyg.shopmanager.loader;

import com.ypyg.shopmanager.net.IReq;
import com.ypyg.shopmanager.net.IResp;

public interface IDataLoader {

	// loadFromCache 从缓存中获取数据
	public Object loadFromCache(IReq aReq, IResp aResp) throws Exception;

	// 直接向服务器请求数据
	public Object loadFromServer(IReq aReq, IResp aResp) throws Exception;

	// 先读取缓存 再向服务器请求
	public Object load(IReq aReq, IResp aResp) throws Exception;

	public Object loadWithGet(IReq aReq, IResp aResp) throws Exception;

}

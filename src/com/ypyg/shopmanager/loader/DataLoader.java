package com.ypyg.shopmanager.loader;

import org.apache.http.HttpResponse;

import com.ypyg.shopmanager.net.IHttpService;
import com.ypyg.shopmanager.net.IReq;
import com.ypyg.shopmanager.net.IResp;

public class DataLoader implements IDataLoader {

	private IHttpService httpService = null;

	public DataLoader(IHttpService httpService) {
		this.httpService = httpService;
	}

	// 从服务器获取数据
	private Object fromServer(IReq aReq, IResp aResp) throws Exception {
		HttpResponse aResponse = httpService.getHttpResp(aReq, aReq.getEntry());
		if (null != aResponse) {
			aResp.onResponse(aReq, aResponse.getEntity().getContent());
			return aResp.getRespObject();
		}
		return null;
	}

	// 从服务器获取数据
	private Object fromServerWithGet(IReq aReq, IResp aResp) throws Exception {
		HttpResponse aResponse = httpService.getHttpRespWithDoGet(aReq.getUrl(), 0l, 0l);
		if (null != aResponse) {
			aResp.onResponse(aReq, aResponse.getEntity().getContent());
			return aResp.getRespObject();
		}
		return null;
	}

	// 读取缓存
	@Override
	public Object loadFromCache(IReq aReq, IResp aResp) throws Exception {
		Object aObj = aReq.getFromCache();
		if (null != aObj) {
			return aObj;
		}
		return null;
	}

	// 向服务器请求数据
	@Override
	public Object loadFromServer(IReq aReq, IResp aResp) throws Exception {
		return fromServer(aReq, aResp);
	}

	// 先从缓存中获取数据，再向服务器请求
	@Override
	public Object load(IReq aReq, IResp aResp) throws Exception {
		Object aObj = aReq.getFromCache();
		if (null != aObj) {
			return aObj;
		}
		return fromServer(aReq, aResp);
	}

	// get 请求
	@Override
	public Object loadWithGet(IReq aReq, IResp aResp) throws Exception {
		Object aObj = aReq.getFromCache();
		if (null != aObj) {
			return aObj;
		}
		return fromServerWithGet(aReq, aResp);
	}

}

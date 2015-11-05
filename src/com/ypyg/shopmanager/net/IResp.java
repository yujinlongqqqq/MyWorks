package com.ypyg.shopmanager.net;

import java.io.IOException;
import java.io.InputStream;

public interface IResp {
	public void onResponse(IReq aReq, InputStream aInStream)
			throws IllegalStateException, IOException;

	public Object getRespObject();

	public Class<?> getRespClass();

}

package com.ypyg.shopmanager.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.TestDataEvent;
import com.ypyg.shopmanager.util.Log_;

public class BaseResp implements IResp {

	protected Object mRespObject = null;

	protected Object getObjectFromResponse(IReq aReq, InputStream aInStream, Class<?> T) throws IllegalStateException, IOException {
		try {
			// 存储InputStream到ByteArrayOutputStream
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = aInStream.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());
			InputStream stream2 = new ByteArrayInputStream(baos.toByteArray());
			// 创建JsonReader对象
			JsonReader reader = new JsonReader(new InputStreamReader(stream1));
			String temp = inputStream2String(stream2);
			// 查看原始接收数据
			Log_.e("原始数据：", temp);
			BusProvider.get().post(new TestDataEvent("原始数据：" + temp));
			try {
				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				try {
					// 检查下行数据格式
					JsonElement jsonElement = parser.parse(reader);
					try {
						Log_.e("resp", jsonElement.toString());

					} catch (Exception e) {
						Log_.e("resp", "", e);
					}
					if (jsonElement.isJsonObject()) {
						JsonObject JsonObject = (JsonObject) jsonElement;
						Object aObj = gson.fromJson(JsonObject, T);
						String sKey = aReq.getCacheKey();
						return aObj;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				if (null != reader)
					reader.close();
			}
		} finally {
			if (null != aInStream)
				aInStream.close();
		}
		return null;
	}

	public String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	@Override
	public void onResponse(IReq aReq, InputStream aInStream) throws IllegalStateException, IOException {
		mRespObject = getObjectFromResponse(aReq, aInStream, getRespClass());
	}

	@Override
	public Object getRespObject() {
		return mRespObject;
	}

	@Override
	public Class<?> getRespClass() {
		return Object.class;
	}

}

package com.ypyg.shopmanager.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class CommenReqBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String datamode;// 数据模式
	private String data;// 加密报文
	private String keyvernum;// 密钥版本号
	private String decryptkey;// DES 解密key
	public String getDatamode() {
		return datamode;
	}
	public void setDatamode(String datamode) {
		this.datamode = datamode;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getKeyvernum() {
		return keyvernum;
	}
	public void setKeyvernum(String keyvernum) {
		this.keyvernum = keyvernum;
	}
	public String getDecryptkey() {
		return decryptkey;
	}
	public void setDecryptkey(String decryptkey) {
		this.decryptkey = decryptkey;
	}
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {
			Gson gson = new Gson();
			String sStr = gson.toJson(this);
			StringEntity aStrEntity = new StringEntity(sStr, "UTF-8");
			return aStrEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aEntity;
	}
	public CommenReqBean getObjectFromResponse(InputStream aInStream)
			throws IllegalStateException, IOException {
		try {
			// 创建JsonReader对象
			JsonReader reader = new JsonReader(new InputStreamReader(aInStream));
			try {
				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				try {
					JsonElement jsonElement = parser.parse(reader);
					// try {
					// Log_.e("test", jsonElement.toString());
					// } catch (Exception e) {
					// Log_.e("test", "", e);
					// }
					if (jsonElement.isJsonObject()) {
						JsonObject JsonObject = (JsonObject) jsonElement;
						CommenReqBean aObj = gson.fromJson(JsonObject,
								getClass());
						// String sKey = aReq.getCacheKey();
						// 如果允许缓存
						// if (null != sKey) {
						// Context aContext = EduParentsApplication
						// .getInstance().getApplicationContext();
						// ObjectCache aCache = ObjectCache
						// .getInstance(aContext);
						// if (null != aCache) {
						// aCache.put(sKey, aObj);
						// }
						// }
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
}

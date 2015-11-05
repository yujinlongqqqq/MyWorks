package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;

public class ImageDownloadReq extends BaseQueryReq {
	private final String mUrl;
	private String px = null;

	public ImageDownloadReq(String aUrl, String px) {
		this.mUrl = aUrl;
		this.px = px;
	}

	@Override
	public String getUrl() {
		if (null == mUrl)
			return null;
		if (mUrl.length() <= 0)
			return null;
		if (mUrl.startsWith("http://")) {
			return mUrl;
		}
		DataCener aCener = DataCener.getInstance();

		if (null != aCener) {
			String sDomain = aCener.getImageServUrl();
			if (AppUtil.isNull(sDomain))
				return null;
			if (!sDomain.startsWith("http://")) {
				sDomain = new String("http://" + sDomain);
			}

			StringBuilder sBuilder = new StringBuilder(sDomain);
			if (!sDomain.endsWith("/") && (!mUrl.startsWith("/"))) {
				sBuilder.append("/");
			}

			sBuilder.append(mUrl);
			if (null != mUrl && null != px)
				if (!mUrl.endsWith(px))
					sBuilder.append(px);
			String sUri = sBuilder.toString();

			return sUri;
		}
		return null;
	}

	@Override
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {

			StringEntity aStrEntity = new StringEntity("");
			aEntity = aStrEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aEntity;
	}
}

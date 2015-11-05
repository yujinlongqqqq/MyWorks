package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.ExitLoginReqBean;
import com.ypyg.shopmanager.common.DataCener;

public class ExitLoginReq extends BaseQueryReq {
	// private String token = "login" + Constants.secretKey + "index";
	private String token = "c06d7920cb09766e6a37259427d0d1";
	private final ExitLoginReqBean mReqBean = new ExitLoginReqBean();

	public ExitLoginReq(Integer uid) {
		mReqBean.setUid(uid);
	}

	@Override
	public String getUrl() {
		String mainserverurl = DataCener.getInstance().getMainServerDomain();
		StringBuilder url = new StringBuilder(mainserverurl);
		url.append(EXIT_LOGOIN_URL);
		return url.toString();
	}

	@Override
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {
			BaseReqBean.cpoyObjAttr(this.getBaseReqBean(), mReqBean, BaseReqBean.class);
			DataCener aCenter = DataCener.getInstance();
			if (null != aCenter) {
				// mReqBean.setGroup(Constants.getMobileClientGroup());
				mReqBean.setToken(token);
				// mReqBean.setOperation(action);
			}
			StringEntity aStrEntity = getStringEntity(mReqBean);
			aEntity = aStrEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aEntity;
	}
}

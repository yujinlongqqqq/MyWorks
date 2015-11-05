package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.LoginReqBean;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

public class LoginReq extends BaseQueryReq {
	// private String token = "login" + Constants.secretKey + "index";
	private String token = "c06d7920cb09766e6a37259427d0d1";
	// private String url =
	// "http://wxmall.wuyuejin.net/app_interface/login/index";
	private final LoginReqBean mReqBean = new LoginReqBean();

	public LoginReq(String username, String password, String auto) {
		super();
		mReqBean.setUsername(username);
		mReqBean.setPassword(password);
		mReqBean.setAuto(auto);
	}

	@Override
	public String getUrl() {
		String mainserverurl = DataCener.getInstance().getMainServerDomain();
		StringBuilder url = new StringBuilder(mainserverurl);
		url.append(Logoin_URL);
		return url.toString();
	}

	@Override
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {
			BaseReqBean.cpoyObjAttr(this.getBaseReqBean(), mReqBean, BaseReqBean.class);
			DataCener aCenter = DataCener.getInstance();
			if (null != aCenter) {
				mReqBean.setGroup(Constants.getMobileClientGroup());
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

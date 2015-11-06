package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.infobean.GoodinfoBean;
import com.ypyg.shopmanager.bean.reqbean.GoodAddReqBean;
import com.ypyg.shopmanager.common.DataCener;

public class GoodAddReq extends BaseQueryReq {
	private final GoodAddReqBean mReqBean = new GoodAddReqBean();

	public GoodAddReq(int uid,GoodinfoBean infobean) {
		mReqBean.setUid(uid);
		mReqBean.setInfobean(infobean);
	}

	@Override
	public String getUrl() {
		String mainserverurl = mdataCener.getMainServerDomain();
		StringBuilder url = new StringBuilder(mainserverurl);
		url.append(GOOD_ADD_URL);
		return url.toString();
	}

	@Override
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {
			BaseReqBean.cpoyObjAttr(this.getBaseReqBean(), mReqBean, BaseReqBean.class);
			DataCener aCenter = DataCener.getInstance();
			if (null != aCenter) {
				mReqBean.setToken(token);
			}
			StringEntity aStrEntity = getStringEntity(mReqBean);
			aEntity = aStrEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aEntity;
	}
}

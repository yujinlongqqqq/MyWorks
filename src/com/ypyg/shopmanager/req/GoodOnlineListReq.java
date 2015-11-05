package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.bean.GoodOnlineListReqBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

public class GoodOnlineListReq extends BaseQueryReq {
	// private String token = "login" + Constants.secretKey + "index";

	private final GoodOnlineListReqBean mReqBean = new GoodOnlineListReqBean();
	

	public GoodOnlineListReq(String catid, Long offset, Long count) {
		super();
		mReqBean.setCatid(catid);
		mReqBean.setOffset(offset);
		mReqBean.setCount(count);
		mReqBean.setUid(mdataCener.mBasicUserInfoBean.getId());

	}

	@Override
	public String getUrl() {
		String mainserverurl = mdataCener.getMainServerDomain();
		StringBuilder url = new StringBuilder(mainserverurl);
		url.append(OnlineGood_URL);
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
				// mReqBean.setUserName(aCenter.getUserName());
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

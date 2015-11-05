package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.reqbean.MemberListReqBean;
import com.ypyg.shopmanager.common.DataCener;

public class MemberListReq extends BaseQueryReq {
	private final MemberListReqBean mReqBean = new MemberListReqBean();

	public MemberListReq(String sortcat, Long offset, Long count) {
		super();
		mReqBean.setSortcat(sortcat);
		mReqBean.setOffset(offset);
		mReqBean.setCount(count);
		mReqBean.setUid(mdataCener.mBasicUserInfoBean.getId());

	}

	@Override
	public String getUrl() {
		String mainserverurl = mdataCener.getMainServerDomain();
		StringBuilder url = new StringBuilder(mainserverurl);
		url.append(VIPList_URL);
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

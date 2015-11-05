package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.reqbean.GoodSStatusReqBean;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

public class GoodSStatusReq extends BaseQueryReq {
	private final GoodSStatusReqBean mReqBean = new GoodSStatusReqBean();

	public GoodSStatusReq(String goodsid, String isonline) {
		super();
		mReqBean.setId(goodsid);
		mReqBean.setIsonline(isonline);
		mReqBean.setUid(mdataCener.mBasicUserInfoBean.getId());
	}
	@Override
	public String getUrl() {
		String mainserverurl = mdataCener.getMainServerDomain();
		StringBuilder url = new StringBuilder(mainserverurl);
		url.append(GOODDSTATE_URL);
		return url.toString();
	}
	@Override
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {
			BaseReqBean.cpoyObjAttr(this.getBaseReqBean(), mReqBean,
					BaseReqBean.class);
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

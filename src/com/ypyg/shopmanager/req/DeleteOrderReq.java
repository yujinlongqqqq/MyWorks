package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.reqbean.DeleteOrderReqBean;
import com.ypyg.shopmanager.common.DataCener;

public class DeleteOrderReq extends BaseQueryReq {
	private final DeleteOrderReqBean mReqBean = new DeleteOrderReqBean();

	public DeleteOrderReq(String catid, String orderid) {
		super();
		mReqBean.setCatid(catid);
		mReqBean.setId(orderid);
		mReqBean.setUid(mdataCener.mBasicUserInfoBean.getId());
	}

	@Override
	public String getUrl() {
		String mainserverurl = mdataCener.getMainServerDomain();
		StringBuilder url = new StringBuilder(mainserverurl);
		url.append(DeleteOrder_URL);
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

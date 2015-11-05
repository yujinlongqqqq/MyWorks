package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.reqbean.MarketGoodsListReqBean;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

public class MarketGoodsListReq extends BaseQueryReq {
	private String action = "40005";
	private final MarketGoodsListReqBean mReqBean = new MarketGoodsListReqBean();

	public MarketGoodsListReq(String id, Long offset, Long count) {
		super();
		mReqBean.setId(id);
		mReqBean.setOffset(offset);
		mReqBean.setCount(count);
	}

	@Override
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {
			BaseReqBean.cpoyObjAttr(this.getBaseReqBean(), mReqBean,
					BaseReqBean.class);
			DataCener aCenter = DataCener.getInstance();
			if (null != aCenter) {
				mReqBean.setGroup(Constants.getMobileClientGroup());
				mReqBean.setUserName(aCenter.getUserName());
				mReqBean.setToken(aCenter.getToken());
				mReqBean.setOperation(action);
			}
			StringEntity aStrEntity = getStringEntity(mReqBean);
			aEntity = aStrEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aEntity;
	}
}

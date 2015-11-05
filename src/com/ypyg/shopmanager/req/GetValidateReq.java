package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.GetValidateInfoBean;
import com.ypyg.shopmanager.bean.GetValidateReqBean;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

public class GetValidateReq extends BaseQueryReq {
	private String action = "40005";
	private final GetValidateReqBean mReqBean = new GetValidateReqBean();

	// private final GetValidateInfoBean infoBean = new GetValidateInfoBean();

	public GetValidateReq(String username) {
		super();
		mReqBean.setUsername(username);

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

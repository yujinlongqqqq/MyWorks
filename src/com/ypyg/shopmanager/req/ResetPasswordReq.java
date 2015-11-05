package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.ResetPasswordInfoBean;
import com.ypyg.shopmanager.bean.ResetPasswordReqBean;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

public class ResetPasswordReq extends BaseQueryReq {
	private String action = "40005";
	private final ResetPasswordReqBean mReqBean = new ResetPasswordReqBean();
	private final ResetPasswordInfoBean infoBean = new ResetPasswordInfoBean();

	public ResetPasswordReq(String username, String password, String validate) {
		super();

		infoBean.setUsername(username);
		infoBean.setValidate(validate);
		infoBean.setPassword(password);
		mReqBean.setInfobean(infoBean);
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

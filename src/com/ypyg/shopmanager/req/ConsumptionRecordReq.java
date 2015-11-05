package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.reqbean.ConsumptionRecordReqBean;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

public class ConsumptionRecordReq extends BaseQueryReq {
	private String action = "40005";
	private final ConsumptionRecordReqBean mReqBean = new ConsumptionRecordReqBean();

	public ConsumptionRecordReq(String id, String sort, String cat) {
		super();
		mReqBean.setId(id);
		mReqBean.setSort(sort);
		mReqBean.setCat(cat);

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

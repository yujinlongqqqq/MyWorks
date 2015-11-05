package com.ypyg.shopmanager.bean.respbean;

import com.ypyg.shopmanager.bean.BaseRespBean;
import com.ypyg.shopmanager.bean.infobean.DataRevenueInfoBean;

public class DataRevenueRespBean extends BaseRespBean {
	private static final long serialVersionUID = 1L;
	private DataRevenueInfoBean data;

	@Override
	public Object getBeanEntity() {
		return data;
	}

	public void setData(DataRevenueInfoBean data) {
		this.data = data;
	}

}

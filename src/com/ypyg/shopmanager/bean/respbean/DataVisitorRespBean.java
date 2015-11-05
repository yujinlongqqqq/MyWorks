package com.ypyg.shopmanager.bean.respbean;

import com.ypyg.shopmanager.bean.BaseRespBean;
import com.ypyg.shopmanager.bean.infobean.DataVisitorInfoBean;

public class DataVisitorRespBean extends BaseRespBean {
	private static final long serialVersionUID = 1L;
	private DataVisitorInfoBean data;

	@Override
	public Object getBeanEntity() {
		return data;
	}

	public void setData(DataVisitorInfoBean data) {
		this.data = data;
	}

}

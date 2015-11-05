package com.ypyg.shopmanager.bean.reqbean;

import com.ypyg.shopmanager.bean.BaseClientInfoBean;

public class GoodSChooseReqBean extends BaseClientInfoBean {

	private String ids;// 市场id 集合 ，用逗号分隔

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}

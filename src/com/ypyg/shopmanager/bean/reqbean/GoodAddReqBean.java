package com.ypyg.shopmanager.bean.reqbean;

import com.ypyg.shopmanager.bean.BaseClientInfoBean;
import com.ypyg.shopmanager.bean.infobean.GoodinfoBean;

public class GoodAddReqBean extends BaseClientInfoBean {
	private Integer uid;
	private GoodinfoBean infobean;
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public GoodinfoBean getInfobean() {
		return infobean;
	}

	public void setInfobean(GoodinfoBean infobean) {
		this.infobean = infobean;
	}
	
}

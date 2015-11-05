package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.BasicUserInfoBean;

public class LoginEvent extends BaseEvent {
	private BasicUserInfoBean infobean;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobean = (BasicUserInfoBean) aObj;
	}

	public BasicUserInfoBean getInfobean() {
		return infobean;
	}

}
package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.GoodInfoBean;

public class GoodDetailEvent extends BaseEvent {
	private GoodInfoBean infobeans;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobeans = (GoodInfoBean) aObj;
	}

	public GoodInfoBean getInfobeans() {
		return infobeans;
	}

}
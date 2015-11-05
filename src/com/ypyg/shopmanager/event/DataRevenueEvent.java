package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.infobean.DataRevenueInfoBean;

public class DataRevenueEvent extends BaseEvent {

	private DataRevenueInfoBean infobean;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobean = (DataRevenueInfoBean) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infobean;
	}
}
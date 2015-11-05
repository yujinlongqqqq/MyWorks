package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.infobean.DataVisitorInfoBean;

public class DataVisitorEvent extends BaseEvent {

	private DataVisitorInfoBean infoBean;

	@Override
	public void setEventEntity(Object aObj) {
		this.infoBean = (DataVisitorInfoBean) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infoBean;
	}
}
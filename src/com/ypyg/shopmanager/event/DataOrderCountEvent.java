package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.DataOrderCountinfobean;

public class DataOrderCountEvent extends BaseEvent {
	private DataOrderCountinfobean infobeans;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobeans = (DataOrderCountinfobean) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infobeans;
	}
}
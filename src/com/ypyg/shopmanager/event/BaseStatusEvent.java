package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.BaseStatusInfoBean;
import com.ypyg.shopmanager.event.BaseEvent;

public class BaseStatusEvent extends BaseEvent {

	private BaseStatusInfoBean infobean;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobean = (BaseStatusInfoBean) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infobean;
	}
}
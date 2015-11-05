package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.infobean.ServerConfigInfoBean;

public class ServerConfigEvent extends BaseEvent {

	private ServerConfigInfoBean infobean;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobean = (ServerConfigInfoBean) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infobean;
	}
}
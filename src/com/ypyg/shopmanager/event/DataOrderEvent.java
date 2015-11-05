package com.ypyg.shopmanager.event;

import java.util.List;

import com.ypyg.shopmanager.bean.OrderBean;

public class DataOrderEvent extends BaseEvent {
	private String catid;
	private List<OrderBean> infobean;

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	@Override
	public void setEventEntity(Object aObj) {
		this.infobean = (List<OrderBean>) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infobean;
	}
}
package com.ypyg.shopmanager.event;

import java.util.List;

import com.ypyg.shopmanager.bean.GoodInfoBean;

public class ConsumptionRecordEvent extends BaseEvent {
	private List<GoodInfoBean> infobeans;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobeans = (List<GoodInfoBean>) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infobeans;
	}
}
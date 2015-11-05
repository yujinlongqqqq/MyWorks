package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.MemberInfoBean;

public class MemberDetailEvent extends BaseEvent {
	private MemberInfoBean infobean;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobean = (MemberInfoBean) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infobean;
	}
}
package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.bean.MemberListinfoBean;

public class MemberListEvent extends BaseEvent {
	private String catid;
	private MemberListinfoBean infobeans;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobeans = (MemberListinfoBean) aObj;
	}

	@Override
	public Object getEventEntity() {
		return infobeans;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}
	
}
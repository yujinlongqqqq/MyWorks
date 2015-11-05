package com.ypyg.shopmanager.event;

import java.util.List;

import com.ypyg.shopmanager.bean.GoodParentsSortBean;

public class GoodSortsEvent extends BaseEvent {
	private List<GoodParentsSortBean> infobean;

	@Override
	public void setEventEntity(Object aObj) {
		this.infobean = (List<GoodParentsSortBean>) aObj;
	}

	public List<GoodParentsSortBean> getInfobean() {
		return infobean;
	}

}

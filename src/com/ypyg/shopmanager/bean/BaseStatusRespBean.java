package com.ypyg.shopmanager.bean;

public class BaseStatusRespBean extends BaseRespBean {

	private static final long serialVersionUID = 1L;
	private BaseStatusInfoBean infobean;

	@Override
	public Object getBeanEntity() {
		return infobean;
	}
}

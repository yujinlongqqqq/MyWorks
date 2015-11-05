package com.ypyg.shopmanager.bean;

import com.ypyg.shopmanager.bean.infobean.ServerConfigInfoBean;

public class ServerConfigRespBean extends BaseRespBean {

	private static final long serialVersionUID = 1L;
	private ServerConfigInfoBean infobean;

	@Override
	public Object getBeanEntity() {
		return infobean;
	}
}

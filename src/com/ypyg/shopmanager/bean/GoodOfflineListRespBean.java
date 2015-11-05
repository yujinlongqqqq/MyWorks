package com.ypyg.shopmanager.bean;

import java.util.List;

public class GoodOfflineListRespBean extends BaseRespBean {

	private static final long serialVersionUID = 1L;
	private List<GoodInfoBean> result;

	@Override
	public Object getBeanEntity() {
		return result;
	}
}

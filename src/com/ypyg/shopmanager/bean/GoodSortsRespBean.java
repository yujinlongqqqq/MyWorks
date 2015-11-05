package com.ypyg.shopmanager.bean;

import java.util.List;

public class GoodSortsRespBean extends BaseRespBean {
	private static final long serialVersionUID = 1L;
	private List<GoodParentsSortBean> result_data;

	@Override
	public Object getBeanEntity() {
		return result_data;
	}

	public void setResult(List<GoodParentsSortBean> result) {
		this.result_data = result;
	}

}

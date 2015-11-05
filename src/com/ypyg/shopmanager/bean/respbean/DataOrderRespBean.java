package com.ypyg.shopmanager.bean.respbean;

import java.util.List;

import com.ypyg.shopmanager.bean.BaseRespBean;
import com.ypyg.shopmanager.bean.OrderBean;

public class DataOrderRespBean extends BaseRespBean {
	private static final long serialVersionUID = 1L;
	private List<OrderBean> result;

	@Override
	public Object getBeanEntity() {
		return result;
	}

	public void setResult(List<OrderBean> result) {
		this.result = result;
	}

}

package com.ypyg.shopmanager.bean;

public class GoodDetailRespBean extends BaseRespBean {
    private static final long serialVersionUID = 1L;
	private GoodInfoBean result;

	@Override
	public Object getBeanEntity() {
		return result;
	}
}

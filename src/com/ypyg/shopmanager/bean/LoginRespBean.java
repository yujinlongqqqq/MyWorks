package com.ypyg.shopmanager.bean;

public class LoginRespBean extends BaseRespBean {
	private static final long serialVersionUID = 1L;

	private BasicUserInfoBean result;

	@Override
	public Object getBeanEntity() {
		return result;
	}

	public void setResult(BasicUserInfoBean result) {
		this.result = result;
	}

}

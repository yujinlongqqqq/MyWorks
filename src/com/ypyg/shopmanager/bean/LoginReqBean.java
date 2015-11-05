package com.ypyg.shopmanager.bean;

public class LoginReqBean extends BaseClientInfoBean {

	private String password;

	private String auto;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

}

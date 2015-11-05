package com.ypyg.shopmanager.bean;

public class ExitLoginReqBean extends BaseReqBean {
	private static final long serialVersionUID = 1L;
	private Integer uid;
	private String token;
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

}

package com.ypyg.shopmanager.bean;

import java.io.Serializable;

public class ResetPasswordInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;
	private String validate;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

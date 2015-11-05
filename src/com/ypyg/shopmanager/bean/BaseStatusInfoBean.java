package com.ypyg.shopmanager.bean;

import java.io.Serializable;

public class BaseStatusInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String daliymoney;
	private String daliyorder;
	private String weekorder;
	private String weekmoney;
	private String membercat;
	private String goodcat;

	public String getDaliymoney() {
		return daliymoney;
	}

	public void setDaliymoney(String daliymoney) {
		this.daliymoney = daliymoney;
	}

	public String getDaliyorder() {
		return daliyorder;
	}

	public void setDaliyorder(String daliyorder) {
		this.daliyorder = daliyorder;
	}

	public String getWeekorder() {
		return weekorder;
	}

	public void setWeekorder(String weekorder) {
		this.weekorder = weekorder;
	}

	public String getWeekmoney() {
		return weekmoney;
	}

	public void setWeekmoney(String weekmoney) {
		this.weekmoney = weekmoney;
	}

	public String getMembercat() {
		return membercat;
	}

	public void setMembercat(String membercat) {
		this.membercat = membercat;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getGoodcat() {
		return goodcat;
	}

	public void setGoodcat(String goodcat) {
		this.goodcat = goodcat;
	}

}

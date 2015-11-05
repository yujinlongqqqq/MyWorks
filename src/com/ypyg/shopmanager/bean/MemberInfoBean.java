package com.ypyg.shopmanager.bean;

import java.io.Serializable;

public class MemberInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String smallhead;
	private String id;
	private String phone;
	private String nickname;
	private String score;
	private String orders;

	// private String level;
	// private String brand;
	// private String customtag;
	// private String ordernum;
	public String getSmallhead() {
		return smallhead;
	}

	public void setSmallhead(String smallhead) {
		this.smallhead = smallhead;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

}

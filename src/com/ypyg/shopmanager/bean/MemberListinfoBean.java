package com.ypyg.shopmanager.bean;

import java.io.Serializable;
import java.util.List;

public class MemberListinfoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<MemberInfoBean> online;
	private List<MemberInfoBean> offline;

	public List<MemberInfoBean> getOnline() {
		return online;
	}

	public void setOnline(List<MemberInfoBean> online) {
		this.online = online;
	}

	public List<MemberInfoBean> getOffline() {
		return offline;
	}

	public void setOffline(List<MemberInfoBean> offline) {
		this.offline = offline;
	}

}

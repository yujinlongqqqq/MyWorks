package com.ypyg.shopmanager.bean.reqbean;

import com.ypyg.shopmanager.bean.BaseClientInfoBean;

public class GoodStatusReqBean extends BaseClientInfoBean {
	private Long id;
	private String isonline; // 0:下架；1：上架
	private Integer uid;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsonline() {
		return isonline;
	}

	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}

}

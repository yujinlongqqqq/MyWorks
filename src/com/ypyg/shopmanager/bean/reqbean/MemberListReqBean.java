package com.ypyg.shopmanager.bean.reqbean;

import com.ypyg.shopmanager.bean.BaseClientInfoBean;

public class MemberListReqBean extends BaseClientInfoBean {
	private String sortcat;
	private Integer uid;
	private Long offset;
	private Long count;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getSortcat() {
		return sortcat;
	}

	public void setSortcat(String sortcat) {
		this.sortcat = sortcat;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}

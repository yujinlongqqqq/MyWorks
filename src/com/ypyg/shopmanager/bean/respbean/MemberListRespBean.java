package com.ypyg.shopmanager.bean.respbean;

import com.ypyg.shopmanager.bean.BaseRespBean;
import com.ypyg.shopmanager.bean.MemberListinfoBean;

public class MemberListRespBean extends BaseRespBean {
	private static final long serialVersionUID = 1L;
	private MemberListinfoBean result;

	@Override
	public Object getBeanEntity() {
		return result;
	}

	public void setResult(MemberListinfoBean result) {
		this.result = result;
	}

}

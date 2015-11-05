package com.ypyg.shopmanager.bean.reqbean;

import com.ypyg.shopmanager.bean.BaseClientInfoBean;
import com.ypyg.shopmanager.bean.MemberInfoBean;

public class MemberAddReqBean extends BaseClientInfoBean {

	private MemberInfoBean infobean;

	public MemberInfoBean getInfobean() {
		return infobean;
	}

	public void setInfobean(MemberInfoBean infobean) {
		this.infobean = infobean;
	}

}

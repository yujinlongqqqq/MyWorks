package com.ypyg.shopmanager.bean.respbean;

import com.ypyg.shopmanager.bean.BaseRespBean;
import com.ypyg.shopmanager.bean.MemberInfoBean;

public class MemberDetailRespBean extends BaseRespBean {

	private static final long serialVersionUID = 1L;
	private MemberInfoBean infobean;

	@Override
	public Object getBeanEntity() {
		return infobean;
	}
}

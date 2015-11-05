package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.MemberAddRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class MemberAddResp extends BaseResp {
	@Override
	public Class<?> getRespClass() {
		return MemberAddRespBean.class;
	}
}

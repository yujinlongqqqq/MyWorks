package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.GoodStatusRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodStatusResp extends BaseResp {
	@Override
	public Class<?> getRespClass() {
		return GoodStatusRespBean.class;
	}
}

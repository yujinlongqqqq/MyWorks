package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.GoodChooseRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodChooseResp extends BaseResp {
	@Override
	public Class<?> getRespClass() {
		return GoodChooseRespBean.class;
	}
}

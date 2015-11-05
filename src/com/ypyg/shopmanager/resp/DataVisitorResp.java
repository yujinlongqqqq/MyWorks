package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.DataVisitorRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class DataVisitorResp extends BaseResp {
	@Override
	public Class<?> getRespClass() {
		return DataVisitorRespBean.class;
	}
}

package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.ExitLoginRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class ExitLoginResp extends BaseResp {
	@Override
	public Class<?> getRespClass() {
		return ExitLoginRespBean.class;
	}
}

package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.GoodSortsRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodSortsResp extends BaseResp {
	@Override
	public Class<?> getRespClass() {
		return GoodSortsRespBean.class;
	}
}

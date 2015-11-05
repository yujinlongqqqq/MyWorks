package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.ConsumptionRecordRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class ConsumptionRecordResp extends BaseResp {
	@Override
	public Class<?> getRespClass() {
		return ConsumptionRecordRespBean.class;
	}
}

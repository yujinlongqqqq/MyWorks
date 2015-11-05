package com.ypyg.shopmanager.bean.respbean;

import java.util.List;

import com.ypyg.shopmanager.bean.BaseRespBean;
import com.ypyg.shopmanager.bean.GoodInfoBean;

public class MarketGoodsListRespBean extends BaseRespBean {

	private static final long serialVersionUID = 1L;
	private List<GoodInfoBean> infobeans;

	@Override
	public Object getBeanEntity() {
		return infobeans;
	}
}

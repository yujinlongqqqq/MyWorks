package com.ypyg.shopmanager.bean.respbean;

import com.ypyg.shopmanager.bean.BaseRespBean;
import com.ypyg.shopmanager.bean.DataOrderCountinfobean;

public class DataOrderCountRespBean extends BaseRespBean {
    private static final long serialVersionUID = 1L;
    private DataOrderCountinfobean result;
    @Override
	public Object getBeanEntity() {
		return result;
	}
	public void setResult(DataOrderCountinfobean result) {
		this.result = result;
	}
    
}

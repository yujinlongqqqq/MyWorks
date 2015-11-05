package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.DataOrderCountRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class DataOrderCountResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return DataOrderCountRespBean.class;
    }
}

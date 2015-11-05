package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.BaseRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class DeleteOrderResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return BaseRespBean.class;
    }
}

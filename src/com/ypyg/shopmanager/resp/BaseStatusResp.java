
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.BaseStatusRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class BaseStatusResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return BaseStatusRespBean.class;
    }
}

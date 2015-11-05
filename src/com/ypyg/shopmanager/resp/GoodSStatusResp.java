
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.GoodSStatusRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodSStatusResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return GoodSStatusRespBean.class;
    }
}

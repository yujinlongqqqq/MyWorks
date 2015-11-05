
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.DataOrderRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class DataOrderResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return DataOrderRespBean.class;
    }
}

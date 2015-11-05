
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.DataRevenueRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class DataRevenueResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return DataRevenueRespBean.class;
    }
}

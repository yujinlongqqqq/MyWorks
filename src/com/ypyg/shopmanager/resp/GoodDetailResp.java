
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.GoodDetailRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodDetailResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return GoodDetailRespBean.class;
    }
}

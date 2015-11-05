
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.GoodUpdateRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodUpdateResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return GoodUpdateRespBean.class;
    }
}

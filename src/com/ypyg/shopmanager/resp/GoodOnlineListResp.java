
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.GoodOnlineListRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodOnlineListResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return GoodOnlineListRespBean.class;
    }
}

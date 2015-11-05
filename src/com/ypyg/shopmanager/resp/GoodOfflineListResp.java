
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.GoodOfflineListRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodOfflineListResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return GoodOfflineListRespBean.class;
    }
}

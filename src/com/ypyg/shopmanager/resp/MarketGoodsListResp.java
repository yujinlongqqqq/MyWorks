
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.MarketGoodsListRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class MarketGoodsListResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return MarketGoodsListRespBean.class;
    }
}


package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.GoodSChooseRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GoodSChooseResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return GoodSChooseRespBean.class;
    }
}

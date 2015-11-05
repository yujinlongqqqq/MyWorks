
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.ServerConfigRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class ServerConfigResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return ServerConfigRespBean.class;
    }
}

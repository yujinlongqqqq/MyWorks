
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.GetValidateRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class GetValidateResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return GetValidateRespBean.class;
    }
}

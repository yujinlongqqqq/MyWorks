
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.LoginRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class LoginResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return LoginRespBean.class;
    }
}

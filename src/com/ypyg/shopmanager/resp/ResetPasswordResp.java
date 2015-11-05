
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.ResetPasswordRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class ResetPasswordResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return ResetPasswordRespBean.class;
    }
}

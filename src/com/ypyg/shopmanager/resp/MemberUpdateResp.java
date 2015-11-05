
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.MemberUpdateRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class MemberUpdateResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return MemberUpdateRespBean.class;
    }
}

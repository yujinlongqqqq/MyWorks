
package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.MemberDetailRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class MemberDetailResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return MemberDetailRespBean.class;
    }
}


package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.respbean.MemberListRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class MemberListResp extends BaseResp {
    @Override
    public Class<?> getRespClass() {
        return MemberListRespBean.class;
    }
}

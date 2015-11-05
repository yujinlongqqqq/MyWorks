package com.ypyg.shopmanager.req;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.MemberInfoBean;
import com.ypyg.shopmanager.bean.reqbean.MemberUpdateReqBean;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

public class MemberUpdateReq extends BaseQueryReq {
	private String action = "40005";
	private final MemberUpdateReqBean mReqBean = new MemberUpdateReqBean();
	private MemberInfoBean infoBean = new MemberInfoBean();

	public MemberUpdateReq(String smallhead, String nickname, String phone,
			String score, String level, String brand, String customtag) {
		super();
		infoBean.setSmallhead(smallhead);
		infoBean.setNickname(nickname);
		infoBean.setPhone(phone);
		infoBean.setScore(score);
//		infoBean.setLevel(level);
//		infoBean.setBrand(brand);
//		infoBean.setCustomtag(customtag);
		mReqBean.setInfobean(infoBean);

	}

	@Override
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {
			BaseReqBean.cpoyObjAttr(this.getBaseReqBean(), mReqBean,
					BaseReqBean.class);
			DataCener aCenter = DataCener.getInstance();
			if (null != aCenter) {
				mReqBean.setGroup(Constants.getMobileClientGroup());
				mReqBean.setUserName(aCenter.getUserName());
				mReqBean.setToken(aCenter.getToken());
				mReqBean.setOperation(action);
			}
			StringEntity aStrEntity = getStringEntity(mReqBean);
			aEntity = aStrEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aEntity;
	}
}

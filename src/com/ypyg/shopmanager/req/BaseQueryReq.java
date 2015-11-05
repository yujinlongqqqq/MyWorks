package com.ypyg.shopmanager.req;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.ypyg.shopmanager.bean.BaseClientInfoBean;
import com.ypyg.shopmanager.bean.BaseReqBean;
import com.ypyg.shopmanager.bean.PhoneInfo;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.util.SignUtil;
import com.ypyg.shopmanager.net.IReq;
import com.ypyg.shopmanager.net.IServer;

public class BaseQueryReq implements IReq, IServer {
	protected String token = "c06d7920cb09766e6a37259427d0d1";
	protected DataCener mdataCener = DataCener.getInstance();
	
	
	private final BaseReqBean mBaseReqBean = new BaseReqBean();

	protected void initBaseReqBean(BaseReqBean aBean) {
		DataCener aCenter = DataCener.getInstance();
		if (null != aCenter) {
			PhoneInfo aPhone = aCenter.getPhoneInfo();
			if (null != aPhone) {
				aBean.setBrand(aPhone.getBrand());
				aBean.setCellId(aPhone.getCellId());
				aBean.setDensity(aPhone.getDensity());
				aBean.setHeight(aPhone.getHeight());
				aBean.setIMEI(aPhone.getIMEI());
				aBean.setIMSI(aPhone.getIMSI());
				aBean.setLac(aPhone.getLac());
				aBean.setMac(aPhone.getMac());
				aBean.setMcc(aPhone.getMcc());
				aBean.setMnc(aPhone.getMnc());
				aBean.setModel(aPhone.getModel());
				aBean.setNetStandard(aPhone.getNetStandard());
				aBean.setNetType(aPhone.getNetType());
				aBean.setPhoneId(aPhone.getPhoneId());
				aBean.setRam(aPhone.getRam());
				aBean.setRom(aPhone.getRom());
				aBean.setSdkVer(aPhone.getSdkVer());
				aBean.setWidth(aPhone.getWidth());
			}
		}
	}

	protected BaseReqBean getBaseReqBean() {
		initBaseReqBean(mBaseReqBean);
		return mBaseReqBean;
	}

	public StringEntity getStringEntity(Object aObj)
			throws UnsupportedEncodingException {
		// 如果是常规请求 加上 验证参数
		if (aObj instanceof BaseClientInfoBean) {
			String time = String.valueOf(System.currentTimeMillis());
			String number = String.valueOf(new Random().nextInt());
			String randomStr = SignUtil
					.randomString(1 + (int) (Math.random() * 35));
			String sign = SignUtil.getSignature(time, number);
			((BaseClientInfoBean) aObj).setSignature(sign);
			((BaseClientInfoBean) aObj).setTimestamp(time);
			((BaseClientInfoBean) aObj).setNonce(number);
			((BaseClientInfoBean) aObj).setEchostr(randomStr);
			// if (null != DataCener.getInstance()) {
			// if (null != DataCener.getInstance().getLocation()) {
			// LocationInfoBean homebean = DataCener.getInstance()
			// .getLocation();
			// ((BaseClientInfoBean) aObj).setReqaddress(homebean
			// .getAddress());
			// ((BaseClientInfoBean) aObj).setReqaddresscode(homebean
			// .getAddressCode());
			// ((BaseClientInfoBean) aObj).setReqlatitude(homebean
			// .getLat());
			// ((BaseClientInfoBean) aObj).setReqlongitude(homebean
			// .getLon());
			// }
			// }
		}

		Gson gson = new Gson();
		//要发送的json数据字符串
		String sStr = gson.toJson(aObj);
		StringEntity aStrEntity = new StringEntity(sStr, "UTF-8");
		return aStrEntity;
	}

	@Override
	public String getUrl() {
		DataCener aCener = DataCener.getInstance();
		if (null != aCener) {
			String sDomain = aCener.getMainServerDomain();
			if (!sDomain.startsWith("http://")) {
				sDomain = new String("http://" + sDomain);
			}
			StringBuilder sBuilder = new StringBuilder(sDomain);
			final String sUri = sBuilder.toString();
			return sUri;
		}
		return null;
	}

	@Override
	public HttpEntity getEntry() {
		return null;
	}

	@Override
	public Object getFromCache() {
		return null;
	}

	@Override
	public String getCacheKey() {
		return null;
	}

	@Override
	public Object getFromMemory() {
		return null;
	}

}

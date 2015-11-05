package com.ypyg.shopmanager.bean;

import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;

//请求数据的Bean
public class BaseClientInfoBean extends BaseReqBean {

	private String username; // 用户名
	private String clienttype = Constants.getApptype();
	private Long devicetype = Constants.getDevicetype();
	private String pushuserid;
	private String uniqueuserid;// 用户唯一识别码

	private String signature;// 加密签名
	private String timestamp;// 时间戳
	private String nonce;// 随机数
	private String echostr;// 随机字符串

	private Long clientver;// 客户端版本号
	private String channelnumber;// 渠道编号

	private String operation;// 操作接口名

	private String group;// 用户的分组，个人还是机构

	private String token;// 请求的用户的Token

	public BaseClientInfoBean() {
		try {
			this.uniqueuserid = DataCener.getInstance().getMyPushUserId();
			setClientver(DataCener.getInstance().getVersionName());
			setChannelnumber(DataCener.getInstance().getChannelnumber());
			setGroup("person");
		} catch (Exception e) {
		}
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	public Long getClientver() {
		return clientver;
	}

	public void setClientver(Long clientver) {
		this.clientver = clientver;
	}

	public String getChannelnumber() {
		return channelnumber;
	}

	public void setChannelnumber(String channelnumber) {
		this.channelnumber = channelnumber;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUniqueuserid() {
		return uniqueuserid;
	}

	public void setUniqueuserid(String uniqueuserid) {
		this.uniqueuserid = uniqueuserid;
	}

	public String getUsername() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public String getPushuserid() {
		return pushuserid;
	}

	public void setPushuserid(String pushuserid) {
		this.pushuserid = pushuserid;
	}

	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

	public Long getDevicetype() {
		return devicetype;
	}

}

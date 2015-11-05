package com.ypyg.shopmanager.net;

public interface IRespCode {

	public final static int SUCCESS = 200;
	/** 通信令牌错误 token error **/
	public final static int TOKEN_ERROR = 800;
	/** 参数不全 params error **/
	public final static int PARAMS_ERROR = 801;
	/** 没有登录 no login **/
	public final static int NO_LOGIN = 802;
	/** 异地登录 ip error **/
	public final static int IP_ERROR = 803;
	/** 登录授权过期 time over **/
	public final static int TIME_OVER = 804;
	/** 信息获取失败 msg fail **/
	public final static int MSG_FAIL = 810;
	
	public final static int FAILED = -1;

}

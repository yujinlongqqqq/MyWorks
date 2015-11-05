package com.ypyg.shopmanager.net;

import com.ypyg.shopmanager.common.Constants;

public interface IServer {
	// 获取服务器配置信息
	public static final String ConfigQuery_URL = "/config";
	/**图片上传接口**/
	public static final String Image_URL = "/file/index";
	// public static final String action = "000";
	/**登录**/
	public static final String Logoin_URL = "/login/index";
	/**退出登录**/
	public static final String EXIT_LOGOIN_URL = "/login/login_out";
	/**线上商品列表**/
	public static final String OnlineGood_URL = "/good/goodsonline";
	/**线下商品列表**/
	public static final String OfflineGood_URL = "/good/goodsoffline";
	/**商品详情**/
	public static final String GOODDETAIL_URL = "/good/gooddetail";
	/**商品状态修改**/
	public static final String GOODDSTATE_URL = "/good/goodstatus";
	/**商品状态修改**/
	public static final String OrderList_URL = "/order/index";
	/**删除订单（单个，多个）**/
	public static final String DeleteOrder_URL = "/order/order_delete";
	/**销售数据**/
	public static final String SalesData_URL = "/statistics_list/index";
	/**访客数据**/
	public static final String VisitorsData_URL = "/order/order_delete";
	/**订单数据**/
	public static final String OrderData_URL = "/statistics_list/dataorder";
	/**会员列表**/
	public static final String VIPList_URL = "/member/index";
	
}
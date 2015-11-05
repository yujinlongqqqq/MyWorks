package com.ypyg.shopmanager.common;

//本文件定义SharedPreferences 中用到的所有的KEY
public class SharedPreferencesConstants {
	// 第一次启动的标记，用来处理引导页面
	public static final String FIRST_START_FLAG = "first_pref";
	// first_pref配置文件里的key 用来判断初始页面
	public static final String FIRST_IN_KEY = "isFirstIn";
	// 广告活动导航KEY
	public static final String ADVERT_KEY = "advertkey";
	// 是否是第一次注册
	public static final String FIRST_REGIST = "isFirstRegist";
	public static final String VERSION_KEY = "CurrentVersion";
	public static final String GRADE_KEY = "grade";
	public static final String GRADE2_KEY = "grade2";
	public static final String SEX = "sex";
	// 记录的城市
	public static final String SELECT_CITY = "selectCity";
	// 主服务器的域名
	public static final String MAINSERVER_DOMAIN = "mainserver_domain";
	// Comet服务器的域名
	public static final String COMETSERVER_DOMAIN = "cometserver_domain";
	// 图片服务器的域名
	public static final String IMAGESERVER_DOMAIN = "imageserver_domain";
	// 登陆的用户名
	public static final String LOGIN_USERNAME = "login_username";
	// 登陆的用户密码
	public static final String LOGIN_PASSWORD = "login_password";
	// 声音设置
	public static final String SOUND_SET = "sound_set";
	// 振动设置
	public static final String VIBRATION_SET = "vibration_set";
	// 用户昵称
	public static final String USER_NICKNAME = "user_nickname";
	// 相册Url
	public static final String ALBUM_URL = "album_url";

	public static final String FROM_PUSHUSERID_KEY = "frompushuserid";

	public static final String FIRST_LOGIN_KEY = "firstlogin";// 该用户第一次登录
	public static final String TOKEN = "token";// 登录token
	public static final String GGKF_PUSHUSERID = "ggkf_pushuserid";// 登录token
	public static final String TOKEN_CREATE_TIME = "token_time";// token
																// 生成的时间，token有效期为24小时
}

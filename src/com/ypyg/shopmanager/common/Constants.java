package com.ypyg.shopmanager.common;

import java.util.Locale;

import android.os.Build.VERSION;
import android.os.Environment;

public class Constants {

	public static final String secretKey = "ypyg2015";
	// SD卡存储路径
	public static final String FILE_SDCARD = Environment
			.getExternalStorageDirectory().getPath();
	// 升级app下载目录
	public static final String FILE_APK_DOWNLOAD = FILE_SDCARD
			+ "/ShopManager/download/";
	// 图片存储路径
	public static final String FILE_image = FILE_SDCARD + "/ShopManager/images/";
	// 当前版本号
	private static final String CurrentVersion = "1.3.1";
	// 百度KEY
	// private static final String BAIDU_KEY =
	// // "fg5ihfOo1K5hiLuWfiWZ9RPj";
	// // JS de
	// // "5uBtI0zYKGXcbxLdU4ULeCna";
	// // yjl desdf
	// // "thsNMizYEq19NyXVrKic7H0B";
	// // 打包的
	// "xlnBHnCrPdWzEbGFSOZkjGAe";

	// 打包需要修改的 是否是测试
	public static final boolean DEBUG = true;
	public static final String TAG = "";
	public static final String XIAOFANG_VIDEO_URL = "http://tutor.qiniudn.com/educationoffiresafety.mp4";
	public static final String VIDEO_URL =
	// "http://v.youku.com/v_show/id_XODAwNzA0NTg4.html";
	"http://tutor.qiniudn.com/bywsTeaching.mp4";
	// 服务器域名
	// private static final String mMainServerDomain = "10.42.0.103:8099";

	public static final String mMainServerDomain = // "10.42.0.80:8099/Main3";
	"http://wxmall.boluo361.com/app_interface";// 登录测试
	// "main.i31.com/main";
	// "mobi.i31.com/main";
	// "10.42.1.13/main";// 现在开发使用main
	// "10.42.1.13/main4";
	// "www.05kf.com/main";
	// private static final String mMainServerDomain = "10.42.0.200:9080";
	private static final Locale defaultLocale = Locale.US;
	
	/**商品编辑状态分类**/
	public static final int GOODEDIT_UPDATE= 0;
	public static final int GOODEDIT_ADD= 1;
	

	// 代理客户端所在分组
	private static final String PROXYCLIENTGROUP = "universityproxy";

	public static String prePath = "/ShopManager/";
	// 找老师分类 key
	public static final String tutor_category_key = "tutor_category";
	// 找机构分类 key
	public static final String organization_category_key = "organization_category";

	/**
	 * qiniu 图片缩略参数 按原图高宽比例等比缩放，缩放后的像素数量不超过指定值。取值范围0-100000000。 初始设置不超过8000像素
	 */
	public static final String thumbnailImage = Integer
			.valueOf(VERSION.SDK_INT) > 10 ? "?imageMogr2/thumbnail/20000@"
			: "?imageMogr2/thumbnail/8000@";

	public static final String thumb10000 = "?imageMogr2/thumbnail/10000@";
	public static final String thumb20000 = "?imageMogr2/thumbnail/20000@";
	public static final String thumb40000 = "?imageMogr2/thumbnail/40000@";
	public static final String thumb60000 = "?imageMogr2/thumbnail/60000@";
	public static final String thumb80000 = "?imageMogr2/thumbnail/80000@";
	public static final String thumb300000 = "?imageMogr2/thumbnail/300000@";

	// 聊天消息类型 消息分类 0-->文本 1-->图片 2-->音频 3-->订单
	public static final String TEXT = "0";
	public static final String IMAGE = "1";
	public static final String SOUND = "2";
	public static final String ORDERNUM = "3";

	// 聊天页面消息是左右边参数，1-居左 0-居右
	public static final int IM_IS_LEFT = 1;
	public static final int IM_IS_RIGHT = 0;

	/** IM end **/

	// 对象序列化 保存路径
	public static final String OBJECT_PATH = FILE_SDCARD
			+ "/ShopManager/cache/object/";

	// 企业用户的分组
	private static final String mCompanyGroup = "company";

	// 保姆用户的分组
	private static final String NurseGroup = "nurse";

	// 老师用户的分组
	private static String TutorGroup = "tutor";
	// 家长版验证码
	private static String TutorPerson = "person";

	// 订单分类标识
	public final static int TUTOR = 1, AGENCY = 2, ACTIVITY = 3, ALLORDER = 0; // 老师课程订单状态、活动订单状态、机构订单状态、全部订单状态

	private static final String mToParentGroup = "person";

	// 老师详情截图
	private static final String sTutorDetailCapture = "tutor_detail_capture";
	// 设备类型
	private static final Long deviceType = 3l;
	// App类型
	private static final String Apptype = "0";

	// 老师分类
	public static final int NEARBY_TUTOR = 0;
	public static final int SUBJECT_TUTOR = 1;
	public static final int SCHOOL_TUTOR = 2;
	public static final int SEARCH_TUTOR = 3;
	public static final int AUTHENTICATE_TUTOR = 4;

	// 未登录时 限制打电话的次数
	private static final int ContactLimit = 5;

	// 组别设置
	private static boolean isTutorDetailCapture = false;

	public static void setIsParent(boolean isparent) {
		if (!isparent) {
			TutorPerson = "tutortutor";
		}
	}

	// app 主色1 蓝色
	public static String blue = "#00A1D8";
	// app 主色2 黄色
	public static String yello = "#F99D41";
	// app 主色23 白色
	public static String white = "#FFFFFF";
	// app 文字颜色 灰色
	public static String gray = "#eeeeee";
	// 商品状态 0：下架 1：上架
	public static String online = "1";
	public static String offline = "0";

	public static boolean isTutorDetailCapture() {
		return isTutorDetailCapture;
	}

	public static void setTutorDetailCapture(boolean isTutorDetailCapture) {
		Constants.isTutorDetailCapture = isTutorDetailCapture;
	}

	public static String getStutordetailcapture() {
		return sTutorDetailCapture;
	}

	public static String getMtoparentgroup() {
		return mToParentGroup;
	}

	public static int getContactlimit() {
		return ContactLimit;
	}

	public static String getCurrentversion() {
		return CurrentVersion;
	}

	public static String getTutorperson() {
		return TutorPerson;
	}

	public static String getTutorgroup() {
		return TutorGroup;
	}

	public static String getNursegroup() {
		return NurseGroup;
	}

	static public String getMainServerDomain() {
		return mMainServerDomain;
	}

	public static String getProxyclientgroup() {
		return PROXYCLIENTGROUP;
	}

	static public String getCompanyGroup() {
		return mCompanyGroup;
	}

	static public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public static String getPrePath() {
		return prePath;
	}

	public static void setPrePath(String prePath) {
		Constants.prePath = prePath;
	}

	public static Long getDevicetype() {
		return deviceType;
	}

	public static String getApptype() {
		return Apptype;
	}

	public static String getMobileClientGroup() {
		return "";
	}

}

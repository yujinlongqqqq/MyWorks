package com.ypyg.shopmanager.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.bean.BasicUserInfoBean;
import com.ypyg.shopmanager.bean.GoodParentsSortBean;
import com.ypyg.shopmanager.bean.PhoneInfo;
import com.ypyg.shopmanager.cache.ByteCache;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.cache.ObjectCache;
import com.ypyg.shopmanager.net.IServer;
import com.ypyg.shopmanager.net.NetState;
import com.ypyg.shopmanager.view.LoadingView;

public class DataCener {
	private static DataCener mInstance = null;
	private String mUserName = null;
	private String mUserPassword = null;
	private String mUserNickName = null;
	/** 用户基本信息 **/
	public BasicUserInfoBean mBasicUserInfoBean;
	private String mToken = null;
	private final PhoneInfo mPhoneInfo = new PhoneInfo();
	private Context mContext = null;
	private NetState mNetState = null;
	private DataService mDataService = null;
	private String channelnumber;// 渠道编号

	/**商品分类**/
	public List<GoodParentsSortBean> mGoodSorts;
	// 当前权限码
	private int permission = 7;
	// 主服务器的域名
	private String mMainServerDomain = Constants.getMainServerDomain();
	// 主服务器的地址
	private String mServConfigQueryUrl = IServer.ConfigQuery_URL;
	// public key
	private static String mPublicKey = null;
	// keyvernum
	private static String mKeyvernum = null;

	private String mCometServerDomain = null;
	private String mImageServerDomain = null;

	// private LocationInfoBean mLocation = null;
	// private LocationInfoBean mLastLocation = null;
	// private LocationBean mHomeLocation = null;
	// private UptokenBean mUptoken = null;

	private boolean mbWaitForInit = true;

	private boolean mbInitSuccess = false;

	private boolean isRegist = false;

	private String userId = null;

	private String myPushUserId = null;

	// private BasicInfoBean mBasicInfoBean;

	public DataCener(Context aContext) {
		mContext = aContext;
		mInstance = this;

		// 读取存储的服务器配置信息与用户的个人信息
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		// 用户名
		mUserName = preferences.getString(SharedPreferencesConstants.LOGIN_USERNAME, null);
		mUserPassword = preferences.getString(SharedPreferencesConstants.LOGIN_PASSWORD, null);
		// 是否第一次登录
		isRegist = preferences.getBoolean(SharedPreferencesConstants.FIRST_REGIST, true);
		// 用户密码

		mMainServerDomain = preferences.getString(SharedPreferencesConstants.MAINSERVER_DOMAIN, Constants.getMainServerDomain());

		mUserNickName = preferences.getString(SharedPreferencesConstants.USER_NICKNAME, null);
		mCometServerDomain = preferences.getString(SharedPreferencesConstants.COMETSERVER_DOMAIN, null);
		mImageServerDomain = preferences.getString(SharedPreferencesConstants.IMAGESERVER_DOMAIN, null);
		// mImageServerDomain获取图片服务器Url
		mImageServerDomain = "http://wxmall.wuyuejin.net/data/attachment/";

		mbWaitForInit = !((null != mMainServerDomain) && (null != mCometServerDomain) && (null != mImageServerDomain));
		initDataCenter();
	}

	public void initDataCenter() {
		if (!mbInitSuccess) {
			// readDevConfig();
			PhoneInfo.initPhoneInfo(mContext, mPhoneInfo);
			mNetState = new NetState(mContext);
			mDataService = new DataService(mContext, mNetState);
			mbInitSuccess = true;
			// 清理缓存
			try {
				ImageCache.getInstance(mContext).sanitizeDiskCache();
				ByteCache.getInstance(mContext).sanitizeDiskCache();
				ObjectCache.getInstance(mContext).sanitizeDiskCache();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 开启自更新
			// mSelfUpdateService = new SelfUpdateService(mContext, mNetState);
			// mSelfUpdateService.start();
		} else {

			// InitAppResultEvent aEvent = new InitAppResultEvent();
			// aEvent.setCode(IRespCode.SUCCESS);
			// BusProvider.get().post(aEvent);
		}
	}

	// // 服务器配置信息返回事件
	// public void postEvent(ServerConfigRespEvent event) {
	// if (event.getCode() == IRespCode.SUCCESS) {
	// ServerConfigBean aBean = event.getEventEntity();
	// mCometServerDomain = aBean.getCometServUrl();
	// mMainServerDomain = aBean.getMainServUrl();
	// mImageServerDomain = aBean.getImageServUrl();
	// mPublicKey = aBean.getPublickey();
	// mKeyvernum = aBean.getKeyvernum();
	// SharedPreferences preferences = PreferenceManager
	// .getDefaultSharedPreferences(mContext);
	// Editor editor = preferences.edit();
	// // 存入数据
	// editor.putString(SharedPreferencesConstants.MAINSERVER_DOMAIN,
	// mMainServerDomain);
	// editor.putString(SharedPreferencesConstants.COMETSERVER_DOMAIN,
	// mCometServerDomain);
	// editor.putString(SharedPreferencesConstants.IMAGESERVER_DOMAIN,
	// mImageServerDomain);
	// // 提交修改
	// editor.commit();
	// // 是否升级
	// // UpdateManager manager = new UpdateManager(mContext);
	// // manager.checkUpdate();
	// if (mbWaitForInit) {
	// mbWaitForInit = false;
	// // InitAppResultEvent aEvent = new InitAppResultEvent();
	// // aEvent.setCode(IRespCode.SUCCESS);
	// // BusProvider.get().post(aEvent);
	// }
	// } else {
	// if (mbWaitForInit) {
	// // InitAppResultEvent aEvent = new InitAppResultEvent();
	// // aEvent.setCode(IRespCode.FAILED);
	// // BusProvider.get().post(aEvent);
	// }
	// }
	// }

	public String getmPublicKey() {
		return mPublicKey;
	}

	public String getmKeyvernum() {
		return mKeyvernum;
	}

	public DataService getDataService() {
		return mDataService;
	}

	private final void readDevConfig() {
		final String TAG = "Environment:readDevConfig";
		BufferedReader br = null;
		Log.v(TAG, "readDevConfig");
		try {
			File file = AppUtil.getExternalStorageDirectory();
			if (file == null) {
				return;
			}
			file = new File(file, "_hzgangangedu.ini");
			if (file == null || !file.exists()) {
				return;
			}
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String data = null;
			while ((data = br.readLine()) != null) {
				// 是否开发环境
				if (data.startsWith("main_domain:")) {
					if (data.length() > 18) {
						mMainServerDomain = data.substring("main_domain:".length()).trim();
					}
				} else if (data.startsWith("main_url:")) {
					if (data.length() > 0) {
						mServConfigQueryUrl = data.substring("main_url:".length()).trim();
					}
				}
			}

		} catch (Exception e) {
			Log.e(TAG, "读取开发配置文件出错:" + e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
				}
			}
			Log.v(TAG, "Main_ServerDomain=" + mMainServerDomain);
		}

	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	public void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	// 用户是否已经登陆
	public boolean IsUserLogin() {
		return null != mToken;
	}

	public String getCometServerUrl() {
		return mCometServerDomain;
	}

	public String getMainServerDomain() {
		return mMainServerDomain;
	}

	public String getServConfigQueryUrl() {
		return mServConfigQueryUrl;
	}

	public String getImageServUrl() {
		return mImageServerDomain;
	}

	public String getUserName() {
		return mUserName;
	}

	public String getUserPassword() {
		return mUserPassword;
	}

	public void setUserName(String aUser) {
		mUserName = aUser;
	}

	/**
	 * 改变用户保存的密码
	 * 
	 */
	public void setUserPassword(String aPwd) {
		mUserPassword = aPwd;
		if (aPwd == null) {
			// 读取存储的服务器配置信息与用户的个人信息
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
			Editor edit = preferences.edit();
			edit.remove(SharedPreferencesConstants.LOGIN_PASSWORD);
			edit.commit();
		}
	}

	public boolean isFirstRegist() {
		return isRegist;
	}

	public void setIsFirstRegist(boolean isRegist) {
		this.isRegist = isRegist;
	}

	public String getToken() {
		return mToken;
	}

	public void setToken(String aToken) {
		mToken = aToken;
	}

	public void onRecvieNetChangeEvent(Context aContext) {
		PhoneInfo.setNetType(aContext, mPhoneInfo);
		// mNetState.onNetChange(aContext);//开启后 百度定位无法接收
	}

	public NetState getNetState() {
		return mNetState;
	}

	public static DataCener getInstance() {
		return mInstance;
	}

	public PhoneInfo getPhoneInfo() {
		return mPhoneInfo;
	}

	public String getmUserNickName() {
		return mUserNickName;
	}

	public void setmUserNickName(String mUserNickName) {
		this.mUserNickName = mUserNickName;
	}

	// 向SharedPreferences里面写数据
	public <T> boolean writeToSharedPreferences(String key, T values) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor = preferences.edit();
		// 存入数据
		if (values instanceof String) {
			editor.putString(key, (String) values);
		}
		if (values instanceof Boolean) {
			editor.putBoolean(key, (Boolean) values);
		}
		// 提交修改
		return editor.commit();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public String getChannelnumber() {
		if (null == channelnumber || "".equals(channelnumber)) {
			ApplicationInfo appInfo;
			try {
				appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
			} catch (NameNotFoundException e) {
				return null;
			}
			if (!AppUtil.isNull(appInfo.metaData))
				channelnumber = String.valueOf(appInfo.metaData.get("BaiduMobAd_CHANNEL"));
		}
		return channelnumber;
	}

	public void showToast(Context mContext, String string) {
		Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
	}

	// // 创建加载中dialog
	// public Dialog createLoadingDialog(Context context) {
	// return createLoadingDialog(context, "加载中");
	// }

	// // 创建加载中dialog
	// public Dialog createLoadingDialog(final Context context, String msg) {
	// LoadingView mLoadingView = new LoadingView(mContext);
	// // LayoutInflater inflater = LayoutInflater.from(context);
	// // View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
	// // LinearLayout layout = (LinearLayout) v
	// // .findViewById(R.id.loading_dialog);// 加载布局
	// // // main.xml中的ImageView
	// // ImageView spaceshipImage = (ImageView) v
	// // .findViewById(R.id.loading_dialog_image);
	// // TextView tipTextView = (TextView) v
	// // .findViewById(R.id.loading_dialog_text);// 提示文字
	// // 加载动画
	// Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context,
	// R.anim.loading_anim);
	// // // 使用ImageView显示动画
	// // spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	// // tipTextView.setText(msg);// 设置加载信息
	// mLoadingView.start();
	// Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);//
	// 创建自定义样式dialog
	//
	// loadingDialog.setCancelable(false);// 不可以用“返回键”取消
	//
	// loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	//
	// @Override
	// public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
	// {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// dialog.dismiss();
	// ((Activity) context).finish();
	// return true;
	// }
	// return false;
	// }
	// });
	// loadingDialog.setContentView(mLoadingView, new
	// LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
	// LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
	// return loadingDialog;
	//
	// }

	// public UptokenBean getUptoken() {
	// return mUptoken;
	// }
	//
	// public void setUptoken(UptokenBean uptoken) {
	// this.mUptoken = uptoken;
	// }

	// 登陆结果事件
	// public void loginResult(LoginResultEvent event) {
	// if (event.getCode() == IRespCode.SUCCESS) {
	// AppUtil.setPreferences(mContext,
	// SharedPreferencesConstants.LOGIN_USERNAME, mUserName);
	// AppUtil.setPreferences(mContext,
	// SharedPreferencesConstants.LOGIN_PASSWORD, mUserPassword);
	// AppUtil.setPreferences(mContext, SharedPreferencesConstants.TOKEN,
	// event.getToken());
	// String tokenCreateTime = AppUtil.getTimeInMillis() + "";
	// AppUtil.setPreferences(mContext,
	// SharedPreferencesConstants.TOKEN_CREATE_TIME,
	// tokenCreateTime);
	// AppUtil.setPreferences(mContext,
	// SharedPreferencesConstants.FROM_PUSHUSERID_KEY,
	// event.getUniqueuserid());
	// setToken(event.getToken());
	// this.mBasicInfoBean = event.getInfobean();
	// this.myPushUserId = event.getUniqueuserid();
	// mDataService.BehaviorData();// 获取行为统计数据
	//
	// // if (null != event.get.getInfobean().getLastbean()) {
	// // mLocation = new LocationInfoBean();// 如果登录成功 保存上次的位置信息
	// // mLocation.setAddress(event.getBasicinfobean().getInfobean()
	// // .getLastbean().getAddress());
	// // mLocation.setLat(event.getBasicinfobean().getInfobean()
	// // .getLastbean().getLatitude());
	// // mLocation.setLon(event.getBasicinfobean().getInfobean()
	// // .getLastbean().getLongitude());
	// // mLocation.setCityarea(event.getBasicinfobean().getInfobean()
	// // .getLastbean().getAddresscode());
	// // }
	// // startMessageService();
	// }
	// }

	public Long getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
		return (long) packInfo.versionCode;
		// return version;
	}

	public String getMyPushUserId() {
		return myPushUserId;
	}

	public void setMyPushUserId(String myPushUserId) {
		this.myPushUserId = myPushUserId;
	}

}

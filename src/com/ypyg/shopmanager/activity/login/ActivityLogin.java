package com.ypyg.shopmanager.activity.login;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseActivity;
import com.ypyg.shopmanager.activity.MainActivity;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.event.LoginEvent;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.dialog.MyBigDialog;

public class ActivityLogin extends BaseActivity {
	private EditText mPhone = null;
	public EditText mPassword = null;
	private Button mLoginButton = null;

	private SharedPreferences preferences = null;
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static final String TAG = "SMSReceiver";
	private Context mContext = null;
	private LinearLayout forget_passwd = null;
	private TextView mPhonedelete, mPasswordDelete;
	private String tag = "ActivityLogin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		first();
		initView();
		setListener();
		setData();
		initPush();
	}

	@Override
	protected void setLoadingViewText(String text) {
		super.setLoadingViewText("登录中");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "注册成功");
		// 百度移动数据统计
	}

	@Override
	protected void onPause() {
		// overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_out_right);
		super.onPause();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		View currentFocus = ActivityLogin.this.getCurrentFocus();

		if (null != currentFocus) {
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		Log.i(TAG, "解除注册");
		// 百度移动数据统计
	}

	private void first() {
		mContext = ActivityLogin.this;
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	private void initView() {
		mPhone = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.login_passwd_edit);
		mLoginButton = (Button) findViewById(R.id.login_login_btn);
		forget_passwd = (LinearLayout) findViewById(R.id.forget_passwd);
		mPhonedelete = (TextView) findViewById(R.id.input_delete1);
		mPasswordDelete = (TextView) findViewById(R.id.input_delete2);

	}

	private void setListener() {
		forget_passwd.setOnClickListener(forgetPwd);
		mPhone.addTextChangedListener(TextChange);
		mPassword.addTextChangedListener(TextChange2);
		mPhonedelete.setOnClickListener(mPhoneDeleteListener);
		mPasswordDelete.setOnClickListener(mPasswordDeleteListener);
		mLoginButton.setOnClickListener(Login);
	}

	private void setData() {
		// String username = DataCener.getInstance().getUserName();
		String username = "15868458510";
		// String pwd = DataCener.getInstance().getUserPassword();
		String pwd = "123456";
		mPhone.setText(username);
		mPassword.setText(pwd);
		if (!AppUtil.isNull(username) && !AppUtil.isNull(pwd)) {
			mPassword.requestFocus();
			mPassword.setSelection(pwd.length());
		}
		if (!AppUtil.isNull(username) && AppUtil.isNull(pwd)) {
			mPassword.requestFocus();
		}
	}

	private void initPush() {
		// 以apikey的方式登录，一般放在主Activity的onCreate中
		// PushManager.startWork(getApplicationContext(),
		// PushConstants.LOGIN_TYPE_API_KEY,
		// Utils.getMetaValue(ActivityLogin.this, "api_key"));
	}

	private OnClickListener Login = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if ("".equals(mPhone.getText().toString()) || "".equals(mPassword.getText().toString())) {
				final MyBigDialog dialog = new MyBigDialog(ActivityLogin.this, R.style.dialog_contact);
				dialog.setTitle("登录错误");
				dialog.setText("帐号或者密码不能为空，\n请输入后再登录！");
				dialog.setSubmit(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.setCancelGone();
				dialog.show();
				return;
			}

			if (!AppUtil.isPhoneNumber(mPhone.getText().toString().trim())) {
				mDataCener.showToast(mContext, "手机号码不正确");
				return;
			}
			// mLoadingView.show();
			mPhone.setEnabled(false);
			mPassword.setEnabled(false);
			mLoginButton.setEnabled(false);
			myLogin();// 登录

		}
	};
	private OnClickListener forgetPwd = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// Intent intent = new Intent();
			// intent.setClass(ActivityLogin.this, ActivityForgetPwd.class);
			// startActivity(intent);
			// // ActivityLogin.this.finish();
		}
	};

	// 登录
	public void myLogin() {
		// // 如果当前登录帐号与前一次不同则删除本地当前的聊天用户列表
		// if (!AppUtil.isNull(mPhone.getText().toString().trim()))
		// if (!mPhone.getText().toString().trim()
		// .equals(mDataCener.getUserName())) {
		// ChatUserManager mChatUserManager = ChatUserManager
		// .getInstance(mContext);
		// mChatUserManager.clearAllUsers();
		// }
		if (null != mDataService) {
			mDataCener.setUserName(mPhone.getText().toString().trim());
			mDataCener.setUserPassword(mPassword.getText().toString().trim());
			// 执行登陆
			if (!AppUtil.isNull(mDataService)) {
				// String pushUserId = AppUtil.getPreferences(mContext,
				// "userid");
				// Intent intent = new Intent(mContext, MainActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// mContext.startActivity(intent);
				// finish();

				loadingView.show();
				mDataService.Login(mPhone.getText().toString().trim(), mPassword.getText().toString().trim(), "0");
			}
		}

		// Intent intent = new Intent(mContext, MainActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// mContext.startActivity(intent);
		// this.finish();
	}

	/**
	 * 是否为数字的字符串。
	 * 
	 * @param str
	 *            字符串
	 * @return true/false
	 */
	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) > '9' || str.charAt(i) < '0') {
				return false;
			}
		}
		return true;
	}

	private static boolean isEmpty(String str) {
		if (null == str || str.trim().length() == 0) {
			return true;
		}
		return false;

	}

	private TextWatcher TextChange = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			int number = s.length();
			if (number > 0)
				mPhonedelete.setVisibility(View.VISIBLE);
			else
				mPhonedelete.setVisibility(View.INVISIBLE);
		}
	};

	private TextWatcher TextChange2 = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			int number = s.length();
			if (number > 0)
				mPasswordDelete.setVisibility(View.VISIBLE);
			else
				mPasswordDelete.setVisibility(View.INVISIBLE);
		}
	};

	private OnClickListener mPhoneDeleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mPhone.setText("");
		}
	};
	private OnClickListener mPasswordDeleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mPassword.setText("");
		}
	};

	private void setTag(String aTag) {
		List<String> tags = getTagsList(aTag);
		// PushManager.setTags(getApplicationContext(), tags);
	}

	private List<String> getTagsList(String originalText) {

		List<String> tags = new ArrayList<String>();
		int indexOfComma = originalText.indexOf(',');
		String tag;
		while (indexOfComma != -1) {
			tag = originalText.substring(0, indexOfComma);
			tags.add(tag);

			originalText = originalText.substring(indexOfComma + 1);
			indexOfComma = originalText.indexOf(',');
		}

		tags.add(originalText);
		return tags;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			if (mDataCener.isShouldHideInput(v, ev)) {
				mDataCener.hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 登录结果返回
	 * 
	 * @param event
	 */
	protected void onEventMainThread(LoginEvent event) {
		loadingView.dismiss();
		mPhone.setEnabled(true);
		mPassword.setEnabled(true);
		mLoginButton.setEnabled(true);
		if (null == event) {
			mDataCener.showToast(mContext, "网络错误！");
			return;
		}
		if (event.getCode() != IRespCode.SUCCESS) {
			dialog("登录错误,帐号或者密码错误，\n请重试！");
			return;
		}

		mDataCener.mBasicUserInfoBean = event.getInfobean();

		Intent intent = new Intent(mContext, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		this.finish();

	}

	protected void onEvent(String string) {
		if ("registersuccess".equals(string)) {
			ActivityLogin.this.finish();
		}
	}

	// protected void onEventMainThread(BehaviorDataEvent event) {
	// if (event.getCode() == IRespCode.SUCCESS) {
	// String user_collect_seek = (String) event.getEventEntity();
	// String[] user_collect_seeks = null;
	// if (!AppUtil.isNull(user_collect_seek))
	// user_collect_seeks = user_collect_seek.split("\\|");
	// CollectsBeanDao mDao = CollectsBeanDao.getInstance(mContext);
	// if (!AppUtil.isNull(user_collect_seeks)) {
	// for (int j = 0; j < user_collect_seeks.length; j++) {
	// if (AppUtil.isNull(user_collect_seeks[j]))
	// continue;
	// try {
	// mDao.SaveOnOff(Constants.NEWS, Constants.COLLECT,
	// user_collect_seeks[j]);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	//
	// if (!AppUtil.isNull(childList)) {
	// if (childList.size() > 1) {
	// Intent intent = new Intent(mContext, ActivitySelectChild.class);
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// mContext.startActivity(intent);
	// this.finish();
	// } else {
	// Intent intent = new Intent(mContext, MainActivitySchool.class);
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// mContext.startActivity(intent);
	// this.finish();
	// }
	// }
	// }

}

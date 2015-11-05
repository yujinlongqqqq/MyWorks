package com.ypyg.shopmanager.activity.good;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseFragmentActivity;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.event.BaseEvent;
import com.ypyg.shopmanager.view.TextView.JellyTextView;

public class ActivityGoodManager extends BaseFragmentActivity implements OnClickListener {
	private String tag = "ActivityGoodManager";
	private FragmentGoodOnline mFragmentGoodOnline;

	private FragmentGoodOffline mFragmentGoodOffline;

//	private View good_online_layout;
//	private View good_offline_layout;

	private RadioButton good_online_tv;

	private RadioButton good_offline_tv;

	private FragmentManager fragmentManager;

	private int index = 0;

	private DataCener mDataCener = null;
	private Context mContext = null;
	private DataService mDataService = null;
	// 别处登陆
	private boolean isOut = false;

	/** 上下架按钮 **/
	private View more_menu = null;
	/** 筛选按钮 **/
	private View shaixuang = null;
	/** 添加商品按钮 **/
	private JellyTextView mJellyTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_manager);
		first();
		// 初始化布局元素
		initViews();
		fragmentManager = getSupportFragmentManager();
		// 第一次启动时选中第0个tab
		setTabSelection(0);

	}

	String newsAction = "com.schoolnews.pushservice.action.news";

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter NewsActionFilter = new IntentFilter(newsAction);

		if (!checkNetwork()) {
			// startActivity(new Intent(this, ActivitySplash.class));
			// finish();
			// return;
		}
		check();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void first() {
		mContext = this;

		// mDataCener = DataCener.getInstance();
		// mDataService = mDataCener.getDataService();
		// mContext = this;
		// NotificationReceiver.msgtype = getIntent().getStringExtra("msgtype");
		// if (mDataCener.IsUserLogin()) {
		//
		// }
		// initPush();
	}

	/**
	 * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
	 */

	private void initViews() {
//		good_online_layout = findViewById(R.id.good_online_layout);
//		good_offline_layout = findViewById(R.id.good_offline_layout);
		good_online_tv = (RadioButton) findViewById(R.id.good_online_tv);
		good_offline_tv = (RadioButton) findViewById(R.id.good_offline_tv);
		more_menu = (View) findViewById(R.id.more_menu);
		shaixuang = (View) findViewById(R.id.shaixuan);
		good_online_tv.setOnClickListener(this);
		good_offline_tv.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.good_online_tv:
			setTabSelection(0);
			break;
		case R.id.good_offline_tv:
			setTabSelection(1);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
	 */
	public void setTabSelection(int index) {
		this.index = index;
		// 每次选中之前先清楚掉上次的选中状态
//		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		// if (index == 2) {
		// if (!mDataCener.IsUserLogin()) {
		// startActivity(new Intent(MainActivitySchool.this,
		// ActivityLogin.class));
		// // return;
		// }
		// }
		switch (index) {
		case 0:
//			good_online_tv.setTextColor(Color.parseColor("#ffffff"));
			if (mFragmentGoodOnline == null) {
				// 如果fragmentIndex为空，则创建一个并添加到界面上
				mFragmentGoodOnline = new FragmentGoodOnline();
				transaction.add(R.id.good_content, mFragmentGoodOnline);
			} else {
				// 如果fragmentIndex不为空，则直接将它显示出来
				transaction.show(mFragmentGoodOnline);
			}
			if (!AppUtil.isNull(mFragmentGoodOnline)) {
				mFragmentGoodOnline.setMoreBtn(more_menu);
				mFragmentGoodOnline.setShaiXuanBtn(shaixuang);

				if (!AppUtil.isNull(mFragmentGoodOffline))
					mFragmentGoodOffline.setMoreBtn(null);
				mFragmentGoodOnline.setShaiXuanBtn(null);
			}
			break;
		case 1:
//			good_offline_tv.setTextColor(Color.parseColor("#ffffff"));
			if (mFragmentGoodOffline == null) {
				// 如果fragmentNews为空，则创建一个并添加到界面上
				mFragmentGoodOffline = new FragmentGoodOffline();
				transaction.add(R.id.good_content, mFragmentGoodOffline);

			} else {
				// 如果fragmentNews不为空，则直接将它显示出来
				transaction.show(mFragmentGoodOffline);
			}
			if (!AppUtil.isNull(mFragmentGoodOffline)) {
				mFragmentGoodOffline.setMoreBtn(more_menu);
				mFragmentGoodOffline.setShaiXuanBtn(shaixuang);
				if (!AppUtil.isNull(mFragmentGoodOnline))
					mFragmentGoodOnline.setMoreBtn(null);
				mFragmentGoodOffline.setShaiXuanBtn(null);
			}
			break;

		default:
			break;
		}
		transaction.commit();
	}


	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (mFragmentGoodOnline != null) {
			transaction.hide(mFragmentGoodOnline);
		}
		if (mFragmentGoodOffline != null) {
			transaction.hide(mFragmentGoodOffline);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if (index == 0)
		// fragmentIndex.onActivityResult(requestCode, resultCode, data);
	}

	private boolean checkNetwork() {

		ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		// 检查网络连接，如果无网络可用，就不需要进行连网操作等
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return true;
		}
		return false;
	}

	private void check() {
		if (isOut) {
			isOut = false;
			setTabSelection(0);
		}
	}

	public void back(View v) {
		this.finish();
	}

	public void shaixuan(View v) {
		mDataCener.showToast(mContext, "筛选");
	}

	protected void onEventMainThread(BaseEvent event) {

	}
}

package com.ypyg.shopmanager.activity.data;

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
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseFragmentActivity;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.TestDataEvent;

public class ActivityDataManager extends BaseFragmentActivity implements OnClickListener {
	private String tag = "ActivityDataManager";
	private FragmentDataRevenue mFragmentDataRevenue;

	private FragmentDataOrder mFragmentDataOrder;

	private FragmentDataVisitors mFragmentDataVisitors;

	// private View data_revenue_layout;
	// private View data_order_layout;
	// private View data_visitor_layout;

	private RadioButton data_revenue_tv;

	private RadioButton data_order_tv;
	private RadioButton data_visitor_tv;

	private FragmentManager fragmentManager;

	private int index = 0;

	private DataCener mDataCener = null;
	private Context mContext = null;
	private DataService mDataService = null;
	// 别处登陆
	private boolean isOut = false;
	// 设置是否显示动画，为了防止在创建时就开启动画，用以下三个参数做了判断，只有当看到视图后才会显示动画
	public static int flag1 = 2;
	public static int flag2 = 2;
	public static int flag3 = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_manager);
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
		BusProvider.get().unregister(this);
	}

	private void first() {
		BusProvider.get().register(this);
		mDataCener = DataCener.getInstance();
		mDataService = mDataCener.getDataService();
		mContext = this;
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
		// data_revenue_layout = findViewById(R.id.data_revenue_layout);
		// data_order_layout = findViewById(R.id.data_order_layout);
		// data_visitor_layout = findViewById(R.id.data_visitor_layout);
		data_revenue_tv = (RadioButton) findViewById(R.id.data_revenue_tv);
		data_order_tv = (RadioButton) findViewById(R.id.data_order_tv);
		data_visitor_tv = (RadioButton) findViewById(R.id.data_visitor_tv);
		data_revenue_tv.setOnClickListener(this);
		data_order_tv.setOnClickListener(this);
		data_visitor_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.data_revenue_tv:
			setTabSelection(0);
			break;
		case R.id.data_order_tv:
			setTabSelection(1);
			break;
		case R.id.data_visitor_tv:
			setTabSelection(2);
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
		// // 每次选中之前先清楚掉上次的选中状态
		// clearSelection();
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
			if (mFragmentDataRevenue == null) {
				// 如果fragmentIndex为空，则创建一个并添加到界面上
				mFragmentDataRevenue = new FragmentDataRevenue();
				transaction.add(R.id.good_content, mFragmentDataRevenue);
			} else {
				// 如果fragmentIndex不为空，则直接将它显示出来
				transaction.show(mFragmentDataRevenue);
			}
			break;
		case 1:
			if (mFragmentDataOrder == null) {
				// 如果fragmentNews为空，则创建一个并添加到界面上
				mFragmentDataOrder = new FragmentDataOrder();
				transaction.add(R.id.good_content, mFragmentDataOrder);
			} else {
				// 如果fragmentNews不为空，则直接将它显示出来
				transaction.show(mFragmentDataOrder);
			}
			break;
		case 2:
			if (mFragmentDataVisitors == null) {
				// 如果fragmentNews为空，则创建一个并添加到界面上
				mFragmentDataVisitors = new FragmentDataVisitors();
				transaction.add(R.id.good_content, mFragmentDataVisitors);
			} else {
				// 如果fragmentNews不为空，则直接将它显示出来
				transaction.show(mFragmentDataVisitors);
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
		if (mFragmentDataRevenue != null) {
			transaction.hide(mFragmentDataRevenue);
		}
		if (mFragmentDataOrder != null) {
			transaction.hide(mFragmentDataOrder);
		}
		if (mFragmentDataVisitors != null) {
			transaction.hide(mFragmentDataVisitors);
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

	protected void onEventMainThread(TestDataEvent event) {
		Toast.makeText(mContext, "接收到的数据：" + event.getData(), Toast.LENGTH_LONG).show();
	}
}

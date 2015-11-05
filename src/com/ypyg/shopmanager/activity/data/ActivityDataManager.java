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
	// �𴦵�½
	private boolean isOut = false;
	// �����Ƿ���ʾ������Ϊ�˷�ֹ�ڴ���ʱ�Ϳ����������������������������жϣ�ֻ�е�������ͼ��Ż���ʾ����
	public static int flag1 = 2;
	public static int flag2 = 2;
	public static int flag3 = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_manager);
		first();
		// ��ʼ������Ԫ��
		initViews();
		fragmentManager = getSupportFragmentManager();
		// ��һ������ʱѡ�е�0��tab
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
	 * �������ȡ��ÿ����Ҫ�õ��Ŀؼ���ʵ���������������úñ�Ҫ�ĵ���¼���
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
	 * ���ݴ����index����������ѡ�е�tabҳ��
	 * 
	 * @param index
	 *            ÿ��tabҳ��Ӧ���±ꡣ0��ʾ��Ϣ��1��ʾ��ϵ�ˣ�2��ʾ��̬��3��ʾ���á�
	 */
	public void setTabSelection(int index) {
		this.index = index;
		// // ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬
		// clearSelection();
		// ����һ��Fragment����
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
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
				// ���fragmentIndexΪ�գ��򴴽�һ������ӵ�������
				mFragmentDataRevenue = new FragmentDataRevenue();
				transaction.add(R.id.good_content, mFragmentDataRevenue);
			} else {
				// ���fragmentIndex��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(mFragmentDataRevenue);
			}
			break;
		case 1:
			if (mFragmentDataOrder == null) {
				// ���fragmentNewsΪ�գ��򴴽�һ������ӵ�������
				mFragmentDataOrder = new FragmentDataOrder();
				transaction.add(R.id.good_content, mFragmentDataOrder);
			} else {
				// ���fragmentNews��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(mFragmentDataOrder);
			}
			break;
		case 2:
			if (mFragmentDataVisitors == null) {
				// ���fragmentNewsΪ�գ��򴴽�һ������ӵ�������
				mFragmentDataVisitors = new FragmentDataVisitors();
				transaction.add(R.id.good_content, mFragmentDataVisitors);
			} else {
				// ���fragmentNews��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(mFragmentDataVisitors);
			}
			break;

		default:
			break;
		}
		transaction.commit();
	}

	/**
	 * �����е�Fragment����Ϊ����״̬��
	 * 
	 * @param transaction
	 *            ���ڶ�Fragmentִ�в���������
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
		// ����������ӣ������������ã��Ͳ���Ҫ��������������
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
		Toast.makeText(mContext, "���յ������ݣ�" + event.getData(), Toast.LENGTH_LONG).show();
	}
}

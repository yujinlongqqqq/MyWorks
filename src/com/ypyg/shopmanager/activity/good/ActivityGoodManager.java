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
	// �𴦵�½
	private boolean isOut = false;

	/** ���¼ܰ�ť **/
	private View more_menu = null;
	/** ɸѡ��ť **/
	private View shaixuang = null;
	/** �����Ʒ��ť **/
	private JellyTextView mJellyTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_manager);
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
	 * �������ȡ��ÿ����Ҫ�õ��Ŀؼ���ʵ���������������úñ�Ҫ�ĵ���¼���
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
	 * ���ݴ����index����������ѡ�е�tabҳ��
	 * 
	 * @param index
	 *            ÿ��tabҳ��Ӧ���±ꡣ0��ʾ��Ϣ��1��ʾ��ϵ�ˣ�2��ʾ��̬��3��ʾ���á�
	 */
	public void setTabSelection(int index) {
		this.index = index;
		// ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬
//		clearSelection();
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
//			good_online_tv.setTextColor(Color.parseColor("#ffffff"));
			if (mFragmentGoodOnline == null) {
				// ���fragmentIndexΪ�գ��򴴽�һ������ӵ�������
				mFragmentGoodOnline = new FragmentGoodOnline();
				transaction.add(R.id.good_content, mFragmentGoodOnline);
			} else {
				// ���fragmentIndex��Ϊ�գ���ֱ�ӽ�����ʾ����
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
				// ���fragmentNewsΪ�գ��򴴽�һ������ӵ�������
				mFragmentGoodOffline = new FragmentGoodOffline();
				transaction.add(R.id.good_content, mFragmentGoodOffline);

			} else {
				// ���fragmentNews��Ϊ�գ���ֱ�ӽ�����ʾ����
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
	 * �����е�Fragment����Ϊ����״̬��
	 * 
	 * @param transaction
	 *            ���ڶ�Fragmentִ�в���������
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

	public void shaixuan(View v) {
		mDataCener.showToast(mContext, "ɸѡ");
	}

	protected void onEventMainThread(BaseEvent event) {

	}
}

package com.ypyg.shopmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.good.ActivityGoodEdit;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.GoodSortsEvent;
import com.ypyg.shopmanager.event.TabSelectionEvent;
import com.ypyg.shopmanager.fragment.FragmentIndex2;
import com.ypyg.shopmanager.fragment.FragmentMarket;
import com.ypyg.shopmanager.fragment.FragmentMine;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.uploadphoto.CropImageActivity;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private String tag = "MainActivity";
	/**
	 * 用于展示首页的Fragment
	 */
	private FragmentIndex2 fragmentIndex;

	/**
	 * 用于展示动态的Fragment
	 */
	private FragmentMarket mFragmentMarket;
	// private FragmentSkillList fragmentNews;
	private FragmentIndex2 fragmentThree;
	// private FragmentSkillList fragmentNews;

	/**
	 * 用于展示设置的Fragment
	 */
	private FragmentMine fragmentMine;

	/**
	 * 消息界面布局
	 */
	private View indexLayout;

	/**
	 * 动态界面布局
	 */
	private View newsLayout;

	/**
	 * 第三个layout
	 */
	private View threeLayout;

	/**
	 * 设置界面布局
	 */
	private View settingLayout;
	/**
	 * 底部tablayout
	 */
	public static View bottom_bar;

	/**
	 * 在Tab布局上显示消息图标的控件
	 */
	private ImageView indexImage;

	/**
	 * 在Tab布局上显示联系人图标的控件
	 */
	private ImageView newsImage;

	/**
	 * 在Tab布局上显示动态图标的控件
	 */
	private ImageView threeImage;

	/**
	 * 在Tab布局上显示设置图标的控件
	 */
	private ImageView mineImage;

	/**
	 * 在Tab布局上显示消息标题的控件
	 */
	private TextView indexText;

	/**
	 * 在Tab布局上显示联系人标题的控件
	 */
	private TextView twoText;

	/**
	 * 在Tab布局上显示动态标题的控件
	 */
	private TextView newsText;

	/**
	 * 在Tab布局上显示设置标题的控件
	 */
	private TextView threeText;
	private TextView mineText;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	private int index = 0;

	private DataCener mDataCener = null;
	private Context mContext = null;
	private DataService mDataService = null;
	// 别处登陆
	private boolean isOut = false;
	// 蓝色
	private String blue = "#00A1D8";
	// 白色
	private String white = "#ffffff";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BusProvider.get().register(this);
		setContentView(R.layout.activity_main);
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
		// mDataCener = DataCener.getInstance();
		// mDataService = mDataCener.getDataService();
		// NotificationReceiver.msgtype = getIntent().getStringExtra("msgtype");
		// if (mDataCener.IsUserLogin()) {
		//
		// }
		// initPush();
		mContext = this;
	}

	/**
	 * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
	 */

	private void initViews() {
		bottom_bar = findViewById(R.id.bottom_bar);
		indexLayout = findViewById(R.id.index_layout);
		newsLayout = findViewById(R.id.news_layout);
		threeLayout = findViewById(R.id.three_layout);
		settingLayout = findViewById(R.id.mine_layout);
		indexImage = (ImageView) findViewById(R.id.index_image);
		newsImage = (ImageView) findViewById(R.id.news_image);
		threeImage = (ImageView) findViewById(R.id.three_image);
		mineImage = (ImageView) findViewById(R.id.mine_image);
		indexText = (TextView) findViewById(R.id.index_text);
		newsText = (TextView) findViewById(R.id.news_text);
		threeText = (TextView) findViewById(R.id.three_text);
		mineText = (TextView) findViewById(R.id.mine_text);
		indexLayout.setOnClickListener(this);
		newsLayout.setOnClickListener(this);
		threeLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.index_layout:
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(0);
			break;
		case R.id.news_layout:
			// 当点击了联系人tab时，选中第2个tab
			setTabSelection(1);
			break;
		case R.id.three_layout:
			// 当点击了联系人tab时，选中第2个tab
			setTabSelection(2);
			break;
		case R.id.mine_layout:
			// 当点击了设置tab时，选中第4个tab
			setTabSelection(3);
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
		clearSelection();
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
			// 当点击了消息tab时，改变控件的图片和文字颜色
			indexLayout.setBackgroundColor(Color.parseColor(white));
			indexImage.setImageResource(R.drawable.main_tab_one_checked);
			indexText.setTextColor(Color.parseColor(blue));
			if (fragmentIndex == null) {
				// 如果fragmentIndex为空，则创建一个并添加到界面上
				fragmentIndex = new FragmentIndex2();
				transaction.add(R.id.content, fragmentIndex);
			} else {
				// 如果fragmentIndex不为空，则直接将它显示出来
				transaction.show(fragmentIndex);
			}
			break;
		case 1:
			// 当点击了联系人tab时，改变控件的图片和文字颜色
			newsLayout.setBackgroundColor(Color.parseColor(white));
			newsImage.setImageResource(R.drawable.main_tab_two_checked);
			newsText.setTextColor(Color.parseColor(blue));
			if (mFragmentMarket == null) {
				// 如果fragmentNews为空，则创建一个并添加到界面上
				mFragmentMarket = new FragmentMarket();
				transaction.add(R.id.content, mFragmentMarket);
			} else {
				// 如果fragmentNews不为空，则直接将它显示出来
				transaction.show(mFragmentMarket);
			}
			break;
		case 2:
			threeLayout.setBackgroundColor(Color.parseColor(white));
			threeImage.setImageResource(R.drawable.main_tab_three_checked);
			threeText.setTextColor(Color.parseColor(blue));
			if (fragmentThree == null) {
				// 如果fragmentNews为空，则创建一个并添加到界面上
				fragmentThree = new FragmentIndex2();
				transaction.add(R.id.content, fragmentThree);
			} else {
				// 如果fragmentNews不为空，则直接将它显示出来
				transaction.show(fragmentThree);
			}
			break;
		default:
			// 当点击了设置tab时，改变控件的图片和文字颜色
			settingLayout.setBackgroundColor(Color.parseColor(white));
			mineImage.setImageResource(R.drawable.main_tab_three_checked);
			mineText.setTextColor(Color.parseColor(blue));
			if (fragmentMine == null) {
				// 如果fragmentMine为空，则创建一个并添加到界面上
				fragmentMine = new FragmentMine();
				transaction.add(R.id.content, fragmentMine);
			} else {
				// 如果fragmentMine不为空，则直接将它显示出来
				transaction.show(fragmentMine);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		indexLayout.setBackgroundColor(Color.parseColor(blue));
		indexImage.setImageResource(R.drawable.main_tab_one_nochecked);
		indexText.setTextColor(Color.parseColor(white));
		newsLayout.setBackgroundColor(Color.parseColor(blue));
		newsImage.setImageResource(R.drawable.main_tab_two_nochecked);
		newsText.setTextColor(Color.parseColor(white));
		threeLayout.setBackgroundColor(Color.parseColor(blue));
		threeImage.setImageResource(R.drawable.main_tab_three_nochecked);
		threeText.setTextColor(Color.parseColor(white));
		settingLayout.setBackgroundColor(Color.parseColor(blue));
		mineImage.setImageResource(R.drawable.main_tab_three_nochecked);
		mineText.setTextColor(Color.parseColor(white));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (fragmentIndex != null) {
			transaction.hide(fragmentIndex);
		}
		if (mFragmentMarket != null) {
			transaction.hide(mFragmentMarket);
		}
		if (fragmentThree != null) {
			transaction.hide(fragmentThree);
		}
		if (fragmentMine != null) {
			transaction.hide(fragmentMine);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case AppUtil.FLAG_CHOOSE_PHONE:// 拍照返回
			Uri uri1 = AppUtil.tempUri;
			if (uri1 != null) {
				startPhotoZoom(uri1);
			}

			break;
		case AppUtil.FLAG_CHOOSE_IMG:// 相册返回
			if (null != data) {// 相册返回
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}
			}
			break;

		case CUT_PHOTO_REQUEST_CODE:
			if (null != data) {// 裁剪返回
				String path = data.getStringExtra("path");// 得到裁剪后的图片路径
				if (!"".equals(path) && !"".equals(OriginalPath)) {
					Intent intent = new Intent(mContext, ActivityGoodEdit.class);
					intent.putExtra("state", Constants.GOODEDIT_ADD);
					intent.putExtra("imagepath", path);
					mContext.startActivity(intent);
					// Bitmap bitmap = Bimp.getLoacalBitmap(path);
					// Toast.makeText(mContext, "裁剪成功", 1000).show();

				}
			}

			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
		// if (index == 0)
		// fragmentIndex.onActivityResult(requestCode, resultCode, data);
	}

	private String OriginalPath = "";
	private static final int CUT_PHOTO_REQUEST_CODE = 2;

	private void startPhotoZoom(Uri uri) {
		// 去裁剪Activity
		if (!TextUtils.isEmpty(uri.getAuthority())) {
			Cursor cursor = mContext.getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
			if (null == cursor) {
				Toast.makeText(mContext, "图片没找到", 0).show();
				return;
			}
			cursor.moveToFirst();
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			cursor.close();
			OriginalPath = path;
			Intent intent = new Intent(mContext, CropImageActivity.class);
			intent.putExtra("path", path);
			startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
		} else {
			OriginalPath = uri.getPath();
			Intent intent = new Intent(mContext, CropImageActivity.class);
			intent.putExtra("path", uri.getPath());
			startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
		}
	}

	private long exitTime = 0;

	/**
	 * 按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				moveTaskToBack(false);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
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

	protected void onEventMainThread(TabSelectionEvent event) {
		if (event.getTabPosition() < 4)
			setTabSelection(event.getTabPosition());
	}
}

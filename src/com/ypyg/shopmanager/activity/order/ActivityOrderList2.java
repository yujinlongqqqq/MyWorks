package com.ypyg.shopmanager.activity.order;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.view.PagerSlidingTabStrip;
import com.ypyg.shopmanager.view.popupwindow.ThreeSelectPopupWindow;
import com.ypyg.shopmanager.view.popupwindow.ThreeSelectPopupWindow.ThreeSelectSubmit;

public class ActivityOrderList2 extends FragmentActivity {

	private String imageTag = "ActivityCustomerList";
	private View rootView = null;
	private TextView main_top_title = null;
	private Context mContext = null;
	private final Handler handler = new Handler();

	private PagerSlidingTabStrip tabs;
	private ViewPager mViewPager;
	private MyPagerAdapter adapter;
	private ImageCacheManager mImageCacheManager = null;
	private DataCener mDataCener = null;
	private DataService mDataService = null;
	private View more_menu = null;
	private FragmentOrderListSingle mFragmentCustomerSingle = null;
	private ThreeSelectPopupWindow mThreeSelectPopupWindow = null;
	private String[] firstMenu = { "全部", "珀莱雅", "韩束", "兰瑟", "自然堂" };
	private String[] secondMenu = { "全部", "A级会员", "B级会员", "C级会员", "D级会员" };
	private String[] thirdMenu = { "全部", "同事", "家人", "朋友" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list2);
		first();
		initView();
		setData();
		 setListener();//设置筛选按钮
		// req1();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void first() {
		mContext = this;
		mDataCener = DataCener.getInstance();
		// loading_dialog = mDataCener.createLoadingDialog(mContext);
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
		mImageCacheManager = ImageCacheManager.getInstance(mContext);

	}

	private void initView() {
		main_top_title = (TextView) findViewById(R.id.main_top_title);
		main_top_title.setText("订单管理");
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		more_menu = findViewById(R.id.more_menu);
		mThreeSelectPopupWindow = new ThreeSelectPopupWindow(mContext);
	}

	private void setListener() {
		more_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				mThreeSelectPopupWindow.setData(firstMenu, secondMenu, thirdMenu);
//				mThreeSelectPopupWindow.show(more_menu);
//				mThreeSelectPopupWindow.setListener(mThreeSelectSubmit);
			}
		});
	}
	
	private FragmentOrderListSingle [] fListSingles=new FragmentOrderListSingle[6];

	private void setData() {
		for (int i = 0; i < fListSingles.length; i++) {
//			fListSingles[i]=FragmentOrderListSingle.newInstance(mViewPager);
		}
		
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
		mViewPager.setPageMargin(pageMargin);

		tabs.setViewPager2(mViewPager,fListSingles);
		
		

	}

	// // 数据请求
	// private void req1() {
	//
	// // loading_dialog.show();
	// }
	//
	// // 数据请求
	// private void req2(Long course_id) {
	// // loading_dialog.show();
	// }

	private ThreeSelectSubmit mThreeSelectSubmit = new ThreeSelectSubmit() {

		@Override
		public void submit(String value1, String value2, String value3) {

		}
	};

	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		private final String[] TITLES = { "所有", "待付款", "待发货", "已发货", "已完成", "已关闭" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return fListSingles[position];
		}

	}

	public void back(View v) {
		this.finish();
	}

	protected void onEventMainThread(ImageLoadFinishEvent event) {
		if (null == event) {
			return;
		}
		if (null == event.getTag())
			return;
		if (!imageTag.equals(event.getTag()))
			return;
		String imageUrl = (String) event.getImageUrl();
		String key = event.getImageKey();
		ImageView imageView = (ImageView) rootView.findViewWithTag(imageUrl);
		DiskLruCache mDiskLruCache = ImageCacheManager.getInstance(mContext).getmDiskLruCache();
		// 缓存被写入后，再次查找key对应的缓存
		Snapshot snapShot;
		try {
			snapShot = mDiskLruCache.get(key);

			FileInputStream fileInputStream = null;
			FileDescriptor fileDescriptor = null;
			if (snapShot != null) {
				fileInputStream = (FileInputStream) snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
			}
			// 将缓存数据解析成Bitmap对象
			Bitmap bitmap = null;
			if (fileDescriptor != null) {
				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
			}

			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void onEventMainThread(ImageForSdkTenEvent event) {
		if (null == event) {
			return;
		}
		if (null == event.getmTag())
			return;
		if (!imageTag.equals(event.getmTag()))
			return;
		String imageUrl = (String) event.getImageUrl();
		ImageCache imageCache = ImageCache.getInstance(mContext);
		ImageView imageView = (ImageView) rootView.findViewWithTag(imageUrl);
		Bitmap bitmap = null;
		if (!AppUtil.isNull(imageUrl)) {
			String key = AppUtil.getStringMD5(imageUrl);
			bitmap = imageCache.get(key);
			if (null != imageView)
				imageView.setImageBitmap(bitmap);
		}
	}
}

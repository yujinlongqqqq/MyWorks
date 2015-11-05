package com.ypyg.shopmanager.activity.order;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.view.scrollstripview.PagerItem;
import com.ypyg.shopmanager.view.scrollstripview.SlidingTabLayout;

public class ActivityOrderManager extends FragmentActivity {
	private String[] TITLES = { "所有", "待付款", "待发货", "已发货", "已完成", "已关闭" };
	/** Tab栏 **/
	private List<PagerItem> mPagerItems = new ArrayList<PagerItem>();

	private ViewPager mViewPager;
	private SlidingTabLayout mLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_manager);

		first();
		initView();
	}

	private void first() {
		mViewPager = (ViewPager) findViewById(R.id.id_view_pager);
		int length = TITLES.length;
		for (int i = 0; i < length; i++) {
			mPagerItems.add(new PagerItem(TITLES[i], i, mViewPager));
		}

	}

	private void initView() {
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
		mLayout = (SlidingTabLayout) findViewById(R.id.id_sliding_view);
		mLayout.setViewPager(mViewPager, mFragmentMap);
	}

	public void back(View v) {
		this.finish();
	}

	// 管理FragmentOrderListSingle
	private ArrayMap<Integer, Fragment> mFragmentMap = new ArrayMap<Integer, Fragment>();

	public class MyFragmentPageAdapter extends FragmentStatePagerAdapter {

		private Fragment mContentFragment;

		// private Map<Integer, Fragment> mFragmentMap = new HashMap<>();

		public MyFragmentPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mPagerItems.size();
		}

		@Override
		public Fragment getItem(int position) {
			mContentFragment = mFragmentMap.get(position);
			if (mContentFragment == null) {
				mContentFragment = mPagerItems.get(position).createFragment(position);
				mFragmentMap.put(position, mContentFragment);
			}

			return mContentFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mPagerItems.get(position).getTitle();
		}

	}

}

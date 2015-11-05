package com.ypyg.shopmanager.view.scrollstripview;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ypyg.shopmanager.activity.order.FragmentOrderListSingle;

/**
 * Created by moon.zhong on 2015/5/25.
 */
public class PagerItem {

	private final String mContent;
	private final Integer mIndex;
	private ViewPager mViewPager;
	public PagerItem(String mTitle, Integer index,ViewPager viewPager) {
		mViewPager=viewPager;
		mContent = mTitle;
		mIndex = index;
	}

	public Fragment createFragment(int position) {
		return FragmentOrderListSingle.newInstance(mContent, mIndex,mViewPager);
	}

	public CharSequence getTitle() {
		return mContent;
	}
}

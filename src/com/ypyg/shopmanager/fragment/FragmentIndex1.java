package com.ypyg.shopmanager.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.adapter.ImagePagerAdapter;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.util.ListUtils;
import com.ypyg.shopmanager.view.scrollviewpager.AutoScrollViewPager;

public class FragmentIndex1 extends BaseFragment {

	private View rootView = null;
	private AutoScrollViewPager viewPager;
	private TextView indexText;

	private List<Integer> imageIdList;
	private Context mContext = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		rootView = inflater.inflate(R.layout.fragment_index, null);
		initAd();
		return rootView;
	}

	private void initAd() {
		viewPager = (AutoScrollViewPager) rootView
				.findViewById(R.id.view_pager);
		indexText = (TextView) rootView.findViewById(R.id.view_pager_index);

		imageIdList = new ArrayList<Integer>();
		imageIdList.add(R.drawable.widget_autoscroollview_demo_banner1);
		imageIdList.add(R.drawable.widget_autoscroollview_demo_banner2);
		imageIdList.add(R.drawable.widget_autoscroollview_demo_banner3);
		imageIdList.add(R.drawable.widget_autoscroollview_demo_banner4);
		viewPager.setAdapter(new ImagePagerAdapter(mContext, imageIdList)
				.setInfiniteLoop(true));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		viewPager.setInterval(2000);
		viewPager.startAutoScroll();
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% ListUtils.getSize(imageIdList));
	}

	@Override
	public void onPause() {
		super.onPause();
		// stop auto scroll when onPause
		viewPager.stopAutoScroll();
	}

	@Override
	public void onResume() {
		super.onResume();
		// start auto scroll when onResume
		viewPager.startAutoScroll();
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			indexText.setText(new StringBuilder()
					.append((position) % ListUtils.getSize(imageIdList) + 1)
					.append("/").append(ListUtils.getSize(imageIdList)));
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	

}

/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ypyg.shopmanager.activity.customer;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.adapter.AdapterCustomerList;
import com.ypyg.shopmanager.bean.MemberInfoBean;
import com.ypyg.shopmanager.bean.MemberListinfoBean;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.event.MemberListEvent;
import com.ypyg.shopmanager.fragment.BaseFragment;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.dialog.MyDialog;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshListView;

/**
 * 客户列表
 * 
 * @author Administrator
 * 
 */
public class FragmentCustomerSingle extends BaseFragment {

	private String imageTag = "FragmentCustomerSingle";
	private static final String ARG_POSITION = "position";

	private String mPosition;
	private View rootView = null;
	private static ViewPager myViewPager;
	private ImageCacheManager mImageCacheManager = null;
	private Context mContext = null;
	private AdapterCustomerList mAdapter;
	private List<MemberInfoBean> mDataList = new ArrayList<MemberInfoBean>();
	public Long offset = 0l;
	private Long count = 10l;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private DataCener mDataCener = null;
	private DataService mDataService = null;
	private int deviceWidth;
	private PullToRefreshListView mPullListView;
	private ListView mListView;
	public MyDialog dialog = null;
	/** 避免其它页面请求 **/
	public boolean isRequest = true;

	public static FragmentCustomerSingle newInstance(ViewPager viewPager) {
		FragmentCustomerSingle f = new FragmentCustomerSingle();
		myViewPager = viewPager;
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_customer_single, null);
		BusProvider.get().register(this);
		first();
		initView();
		setData();
		setLastUpdateTime();
		req();
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		BusProvider.get().unregister(this);
	}

	private void first() {
		mContext = getActivity();
		mDataCener = DataCener.getInstance();
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
		deviceWidth = getResources().getDisplayMetrics().widthPixels;
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
	}

	private void initView() {

		initPullToRefreshListView();
	}

	// 装载数据
	private void setData() {
	}

	// 数据请求
	private void req() {
		if (isRequest) {
			mPosition = String.valueOf(myViewPager.getCurrentItem());
			imageTag = imageTag + mPosition;
			mAdapter.setImageTag(imageTag);
			// TODO 根据订单状态，请求订单列表数据
			mDataService.MemberList(mPosition, offset, count);
		}
	}

	/**
	 * 刷新数据
	 */
	public void reFlash(int count, String value1, String value2, String value3) {
		mDataCener.showToast(mContext, "页面" + count + "条件刷新：" + value1 + value2 + value3);
	}

	// 初始化下拉列表
	private void initPullToRefreshListView() {
		mPullListView = (PullToRefreshListView) rootView.findViewById(R.id.pulltorefresh);
		// 下拉刷性不可用
		mPullListView.setPullRefreshEnabled(true);
		// 上拉加载不可用
		mPullListView.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		mPullListView.setScrollLoadEnabled(true);
		// mList = new ArrayList<PCollectShopListInfoBean>();
		mAdapter = new AdapterCustomerList(mContext, imageTag, mDataList);
		// 得到实际的ListView
		mListView = mPullListView.getRefreshableView();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			}
		});

		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				offset = 0l;
				lastPullUpOrDown = UP;
				req();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				lastPullUpOrDown = DOWN;
				req();
			}
		});
		setLastUpdateTime();
		// 自动刷新
		// mPullListView.doPullRefreshing(true, 500);
	}

	private void setLastUpdateTime() {
		String text = formatDateTime(System.currentTimeMillis());
		mPullListView.setLastUpdatedLabel(text);
	}

	private String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}

	/**
	 * 设置刷新
	 */
	public void setReflash() {
		mPullListView.doPullRefreshing(true, 500);// 主动刷新
	}

	/**
	 * 客户列表返回
	 * 
	 * @param event
	 */
	protected void onEventMainThread(MemberListEvent event) {
		mPullListView.onPullUpRefreshComplete();
		mPullListView.onPullDownRefreshComplete();

		if (mPosition.equals(event.getCatid())) {
			if (null == event || IRespCode.SUCCESS != event.getCode()) {

				dialog2("无数据！");

				return;
			}

			List<MemberInfoBean> list = ((MemberListinfoBean) event.getEventEntity()).getOnline();
			if (!AppUtil.isNull(list))
				if (10 > list.size()) {
					mPullListView.setHasMoreData(false);
				} else {
					mPullListView.setHasMoreData(true);
				}
			if (lastPullUpOrDown == UP)
				mDataList.clear();
			offset += list.size();
			mDataList.addAll(list);
			mAdapter.notifyDataSetChanged();
		}
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

	private MyDialog dialog2(String text) {
		if (AppUtil.isNull(mContext))
			return null;
		dialog.setTitle("温馨提示");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}
}
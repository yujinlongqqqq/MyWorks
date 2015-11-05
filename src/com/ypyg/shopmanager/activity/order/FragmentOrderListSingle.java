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

package com.ypyg.shopmanager.activity.order;

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
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.adapter.AdapterOrderList;
import com.ypyg.shopmanager.bean.OrderBean;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.DataOrderEvent;
import com.ypyg.shopmanager.event.DeleteOrderEvent;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.fragment.BaseFragment;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.dialog.MyDialog;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshListView;

/**
 * 单个订单列表
 * 
 * @author Administrator
 * 
 */
public class FragmentOrderListSingle extends BaseFragment {

	private String imageTag = "FragmentOrderListSingle";
	private static final String ARG_POSITION = "position";

	private String mPosition;
	private String mTitle;
	private int mIndex;
	private View rootView = null;

	private ImageCacheManager mImageCacheManager = null;
	private Context mContext = null;
	private AdapterOrderList mAdapter;
	private List<OrderBean> mDataList = new ArrayList<OrderBean>();
	private Long offset = 0l;
	private Long count = 10l;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private DataCener mDataCener = null;
	private DataService mDataService = null;
	private int deviceWidth;
	private PullToRefreshListView mPullListView;
	private ListView mListView;

	public MyDialog dialog = null;
	// private static FragmentOrderListSingle[] fListSingle;

	/** 避免其它页面请求 **/
	public static ViewPager mViewPager;

	public static FragmentOrderListSingle newInstance(String title, int index, ViewPager viewPager) {
		mViewPager = viewPager;
		FragmentOrderListSingle fragment = new FragmentOrderListSingle();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putInt("index", index);
		fragment.setArguments(bundle);
		return fragment;
	}

	// @Override
	// public void onDestroyView() {
	// super.onDestroyView();
	// BusProvider.get().unregister(this);
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_customer_single, null);
		BusProvider.get().register(this);
		Bundle bundle = getArguments();
		mIndex = bundle.getInt("index");
		mTitle = bundle.getString("title");

		first();
		initView();
		setData();
		if (0 == mIndex) {
			req();
		}
		// setLastUpdateTime();
		return rootView;
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
		dialog = new MyDialog(mContext, R.style.dialog_contact);
		initPullToRefreshListView();
	}

	// 装载数据
	private void setData() {
	}

	/**
	 * 请求数据
	 */
	private void req() {
		if (mIndex == mViewPager.getCurrentItem()) {
			mPosition = String.valueOf(mIndex);
			imageTag = imageTag + mPosition;
			mAdapter.setImageTag(imageTag);
			// TODO 根据订单状态，请求订单列表数据
			mDataService.DataOrder(mPosition, offset, count);
		}

	}

	// 初始化下拉列表
	private void initPullToRefreshListView() {
		mPullListView = (PullToRefreshListView) rootView.findViewById(R.id.pulltorefresh);
		// 下拉刷性可用
		mPullListView.setPullRefreshEnabled(true);
		// 上拉加载不可用
		mPullListView.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		mPullListView.setScrollLoadEnabled(true);
		// mList = new ArrayList<PCollectShopListInfoBean>();
		mAdapter = new AdapterOrderList(mContext, imageTag, mDataList);
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
				// count = 10l;
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
	 * 去重新刷新
	 */
	public void toReflash() {
		offset = 0L;
		mDataList.clear();
		req();
	}

	/**
	 * 订单列表返回
	 * 
	 * @param event
	 */
	protected void onEventMainThread(DataOrderEvent event) {
		mPullListView.onPullUpRefreshComplete();
		mPullListView.onPullDownRefreshComplete();

		if (mPosition.equals(event.getCatid())) {
			if (null == event || IRespCode.SUCCESS != event.getCode()) {
				// if(!fListSingle[myViewPager.getCurrentItem()].dialog.isShowing()){
				// dialog2("无数据！");
				// }

				dialog2("无数据！");

				return;
			}

			List<OrderBean> list = (List<OrderBean>) event.getEventEntity();
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

	/**
	 * 删除订单返回
	 * 
	 * @param event
	 */
	protected void onEventMainThread(DeleteOrderEvent event) {
		if (imageTag.equals(event.getCatid())) {
			if (null == event || IRespCode.SUCCESS != event.getCode()) {
				dialog2("操作异常！");
				return;
			}
			mDataList.remove((OrderBean) event.getItem());
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 图片1
	 * 
	 * @param event
	 */
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

	/**
	 * 图片2
	 * 
	 * @param event
	 */
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
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

package com.ypyg.shopmanager.activity.good;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.adapter.AdapterGoodList;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.event.RemoveCollectEvent;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshSwipeListView;
import com.ypyg.shopmanager.view.swipeview.BaseSwipeListViewListener;
import com.ypyg.shopmanager.view.swipeview.SwipeListView;

public class FragmentGoodOnlineSingle extends Fragment {

	private String imageTag = "FragmentGoodOnlineSingle";
	private static final String ARG_POSITION = "position";

	private int position;
	private View rootView = null;

	private ImageCacheManager mImageCacheManager = null;
	private Context mContext = null;
	private AdapterGoodList mAdapter;
	private List<GoodInfoBean> mList = new ArrayList<GoodInfoBean>();
	private PullToRefreshSwipeListView mPullListView;
	private SwipeListView mSwipeListView;
	private Long offset = 0l;
	private Long count = 10l;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private DataCener mDataCener = null;
	private DataService mDataService = null;
	private int deviceWidth;

	public static FragmentGoodOnlineSingle newInstance(int position) {
		FragmentGoodOnlineSingle f = new FragmentGoodOnlineSingle();
		// QuickContactFragment f = new QuickContactFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_goodonline_single, null);
		first();
		initView();
		setData();
		setLastUpdateTime();
		return rootView;
	}

	private void first() {
		mContext = getActivity();
		mDataCener = DataCener.getInstance();
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
		deviceWidth = getResources().getDisplayMetrics().widthPixels;
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
		mAdapter = new AdapterGoodList(mContext, imageTag, mList);
	}

	private void initView() {

		initPullToRefreshListView();
	}

	// 装载数据
	private void setData() {
	}

	// 数据请求
	private void req() {
	}

	// 初始化下拉列表
	private void initPullToRefreshListView() {
		mPullListView = (PullToRefreshSwipeListView) rootView
				.findViewById(R.id.swipe_listview);

		// 上拉加载不可用
		mPullListView.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		mPullListView.setScrollLoadEnabled(false);
		mSwipeListView = (SwipeListView) mPullListView.getRefreshableView();
		mSwipeListView.setCacheColorHint(Color.parseColor("#00000000"));
		mSwipeListView.setDivider(null);
		// 更新 加载数据事件
		mPullListView.setOnRefreshListener(refreshListener);
		// mGridView.setColumnWidth(columnWidth);
		mSwipeListView.setAdapter(mAdapter);
		// mSwipeListView
		// .setOnItemLongClickListener(new OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent,
		// View view, int position, long id) {
		// listPosition = position;
		// final Long publishdemandid = (Long) view
		// .getTag(R.id.action_settings);
		// LayoutInflater inflater = LayoutInflater.from(mContext);
		// View v = inflater.inflate(R.layout.dialog_item_view,
		// null);
		// TextView delete = (TextView) v
		// .findViewById(R.id.custom_dialog_text);// 提示文字
		// loadingDialog = new Dialog(mContext,
		// R.style.loading_dialog);// 创建自定义样式dialog
		// loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		// loadingDialog.setContentView(v);
		// delete.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mDataService.PReleaseDelete(publishdemandid);
		// }
		// });
		// loadingDialog.show();
		// return false;
		// }
		// });

		mSwipeListView
				.setSwipeListViewListener(new TestBaseSwipeListViewListener());
		reload();
		// 自动刷新
//		mPullListView.doPullRefreshing(true, 500);

	}

	private void reload() {
		// 设置往左滑动
		mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		// mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
		mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		mSwipeListView
				.setSwipeActionRight(mSwipeListView.getSwipeActionRight());
		mSwipeListView.setOffsetLeft(deviceWidth * 2 / 3);
		// settings.getSwipeOffsetRight()
		// mSwipeListView.setOffsetRight(deviceWidth * 2 / 3);
		mSwipeListView.setAnimationTime(0);
		mSwipeListView.setSwipeOpenOnLongPress(false);
	}

	// 刷新 事件
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			offset = 0l;
			count = 10l;
			req();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			req();
		}
	};

	class TestBaseSwipeListViewListener extends BaseSwipeListViewListener {

		@Override
		public void onClickParentView(View v, int position) {
			super.onClickParentView(v, position);

		}

		@Override
		public void onClickFrontView(View v, int position) {
			super.onClickFrontView(v, position);
			Long id = (Long) v.getTag(R.id.bottom_bar);
			Intent intent = new Intent(mContext, ActivityGoodDetail.class);
			intent.putExtra("id", id);
			mContext.startActivity(intent);
		}

		@Override
		public void onDismiss(ArrayList<Integer> reverseSortedPositions) {
			try {
				for (int position : reverseSortedPositions) {
					mList.remove(position);
				}
			} catch (Exception e) {
			}
			mAdapter.notifyDataSetChanged();
		}
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

	// protected void onEventMainThread(CollectNewsListEvent event) {
	// mPullListView.onPullUpRefreshComplete();
	// mPullListView.onPullDownRefreshComplete();
	// if (null == event || IRespCode.SUCCESS != event.getCode()) {
	// mDataCener.dialog("无数据！");
	// return;
	// }
	// if (!AppUtil.isNull(event.getCount()))
	// if (10 > event.getCount()) {
	// mPullListView.setHasMoreData(false);
	// } else {
	// mPullListView.setHasMoreData(true);
	// }
	// if (!AppUtil.isNull(event.getOffset()))
	// offset = event.getOffset();
	// if (offset < count) {
	// mList.clear();
	// mAdapter.notifyDataSetChanged();
	// }
	// if (!AppUtil.isNull(event.getCount()))
	// if (offset == count && event.getCount() == count) {
	// mList.clear();
	// mAdapter.notifyDataSetChanged();
	// }
	// mList.addAll((List<GoodInfoBean>) event.getEventEntity());
	// mAdapter.notifyDataSetChanged();
	// }

	protected void onEventMainThread(RemoveCollectEvent event) {
		if (null == event || IRespCode.SUCCESS != event.getCode()) {
			Toast.makeText(mContext, "取消失败", 500).show();
			return;
		}
		Integer listPosition = event.getPosition();
		try {
			mSwipeListView.closeAnimate(listPosition);
			mSwipeListView.dismiss(listPosition);
		} catch (Exception e) {
			e.printStackTrace();
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
		DiskLruCache mDiskLruCache = ImageCacheManager.getInstance(mContext)
				.getmDiskLruCache();
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
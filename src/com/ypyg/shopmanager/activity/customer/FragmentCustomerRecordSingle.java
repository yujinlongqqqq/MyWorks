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

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.BaseEvent;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.fragment.BaseFragment;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ypyg.shopmanager.view.tltlelistview.PullToRefreshTitledListView;
import com.ypyg.shopmanager.view.tltlelistview.TitledListAdapter;
import com.ypyg.shopmanager.view.tltlelistview.TitledListView;

public class FragmentCustomerRecordSingle extends BaseFragment {

	private String imageTag = "FragmentCustomerRecordSingle";
	private static final String ARG_POSITION = "position";
	private int position;
	private Context mContext = null;
	private View rootView = null;
	private ImageCacheManager mImageCacheManager = null;

	private PullToRefreshTitledListView mPullListView;
	private TitledListView mTitledListView;
	private TitledListAdapter adapter;
	private ArrayList<GoodInfoBean> dataList;

	public static FragmentCustomerRecordSingle newInstance(int position) {
		FragmentCustomerRecordSingle f = new FragmentCustomerRecordSingle();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		BusProvider.get().register(this);
		rootView = inflater.inflate(R.layout.fragmnt_consumption_record, container, false);
		initData();
		first();
		initView(rootView);

		return rootView;
	}

	private void initData() {

		dataList = new ArrayList<GoodInfoBean>();
		for (int i = 0; i < 30; i++) {
			if (i < 10) {
				dataList.add(new GoodInfoBean("本月"));
			} else if (i >= 10 && i < 15) {
				dataList.add(new GoodInfoBean("7月"));
			} else {
				dataList.add(new GoodInfoBean("6月"));
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroyView() {
		BusProvider.get().unregister(this);
		super.onDestroyView();
	}

	private void first() {
		mContext = getActivity();
		mDataCener = DataCener.getInstance();
		// loading_dialog = mDataCener.createLoadingDialog(mContext);
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
		mImageCacheManager = ImageCacheManager.getInstance(mContext);

	}

	private void initView(View v) {

		mPullListView = (PullToRefreshTitledListView) v.findViewById(R.id.expandablelist);
		mPullListView.setTipString("没有更多商品啦~");
		// 上拉加载不可用
		mPullListView.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		mPullListView.setScrollLoadEnabled(true);
		// 得到实际的ListView
		mTitledListView = (TitledListView) mPullListView.getRefreshableView();
		adapter = new TitledListAdapter(mContext, dataList);
		mTitledListView.setAdapter(adapter);
		mTitledListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// 第一项与第二项标题不同，说明标题需要移动
				if (!dataList.get(firstVisibleItem).getTitle().equals(dataList.get(firstVisibleItem + 1).getTitle())) {
					((TitledListView) view).moveTitle(dataList.get(firstVisibleItem).getTitle());
				} else {
					((TitledListView) view).updateTitle(dataList.get(firstVisibleItem).getTitle());
				}

			}
		});

		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			}
		});
		setLastUpdateTime();
		// 自动刷新
		mPullListView.doPullRefreshing(true, 500);
	}

	private void setLastUpdateTime() {
		String text = formatDateTime(System.currentTimeMillis());
		mPullListView.setLastUpdatedLabel(text);
	}

	private String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		return AppUtil.format(time);
	}

	// /***
	// * 数据源
	// *
	// * @author Administrator
	// *
	// */
	// class MyexpandableListAdapter extends BaseExpandableListAdapter {
	// private Context context;
	// private LayoutInflater inflater;
	//
	// public MyexpandableListAdapter(Context context) {
	// this.context = context;
	// inflater = LayoutInflater.from(context);
	// }
	//
	// // 返回父列表个数
	// @Override
	// public int getGroupCount() {
	// return groupList.size();
	// }
	//
	// // 返回子列表个数
	// @Override
	// public int getChildrenCount(int groupPosition) {
	//
	// if (groupPosition >= dataList.size()) {
	// return 0;
	// }
	// return dataList.get(groupPosition).size();
	// }
	//
	// @Override
	// public Object getGroup(int groupPosition) {
	//
	// return groupList.get(groupPosition);
	// }
	//
	// @Override
	// public Object getChild(int groupPosition, int childPosition) {
	// return dataList.get(groupPosition).get(childPosition);
	// }
	//
	// @Override
	// public long getGroupId(int groupPosition) {
	// return groupPosition;
	// }
	//
	// @Override
	// public long getChildId(int groupPosition, int childPosition) {
	// return childPosition;
	// }
	//
	// @Override
	// public boolean hasStableIds() {
	//
	// return true;
	// }
	//
	// @Override
	// public View getGroupView(int groupPosition, boolean isExpanded, View
	// convertView, ViewGroup parent) {
	// GroupHolder groupHolder = null;
	// if (convertView == null) {
	// groupHolder = new GroupHolder();
	// convertView = inflater.inflate(R.layout.group, null);
	// groupHolder.textView = (TextView) convertView.findViewById(R.id.group);
	// convertView.setTag(groupHolder);
	// } else {
	// groupHolder = (GroupHolder) convertView.getTag();
	// }
	//
	// groupHolder.textView.setText(((String) getGroup(groupPosition)));
	// // if (isExpanded)// ture is Expanded or false is not isExpanded
	// // groupHolder.imageView.setImageResource(R.drawable.expanded);
	// // else
	// // groupHolder.imageView.setImageResource(R.drawable.collapse);
	// return convertView;
	// }
	//
	// @Override
	// public View getChildView(int groupPosition, int childPosition, boolean
	// isLastChild, View convertView, ViewGroup parent) {
	// ChildHolder childHolder = null;
	// if (convertView == null) {
	// childHolder = new ChildHolder();
	// convertView = inflater.inflate(R.layout.child, null);
	// childHolder.imageView = (ImageView)
	// convertView.findViewById(R.id.good_icon_checked);
	// childHolder.textName = (TextView)
	// convertView.findViewById(R.id.good_name);
	// childHolder.textTime = (TextView)
	// convertView.findViewById(R.id.good_time);
	// childHolder.textNo = (TextView) convertView.findViewById(R.id.good_num);
	// childHolder.textPrice = (TextView)
	// convertView.findViewById(R.id.good_price);
	//
	// convertView.setTag(childHolder);
	// } else {
	// childHolder = (ChildHolder) convertView.getTag();
	// }
	//
	// // childHolder.textName.setText(((People) getChild(groupPosition,
	// // childPosition)).getName());
	// // childHolder.textAge.setText(String.valueOf(((People) getChild(
	// // groupPosition, childPosition)).getAge()));
	// // childHolder.textAddress.setText(((People) getChild(groupPosition,
	// // childPosition)).getAddress());
	//
	// return convertView;
	// }
	//
	// @Override
	// public boolean isChildSelectable(int groupPosition, int childPosition) {
	// return true;
	// }
	//
	// class GroupHolder {
	// TextView textView;
	// }
	//
	// class ChildHolder {
	// ImageView imageView;
	// TextView textName;
	// TextView textTime;
	// TextView textNo;
	// TextView textPrice;
	//
	// }
	// }

	// @Override
	// public View getPinnedHeader() {
	// View headerView = (ViewGroup) ((Activity)
	// mContext).getLayoutInflater().inflate(R.layout.group, null);
	// headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	//
	// return headerView;
	// }
	//
	// @Override
	// public void updatePinnedHeader(View headerView, int firstVisibleGroupPos)
	// {
	// String firstVisibleGroup = (String)
	// adapter.getGroup(firstVisibleGroupPos);
	// TextView textView = (TextView) headerView.findViewById(R.id.group);
	// textView.setText(firstVisibleGroup);
	// }

	protected void onEventMainThread(BaseEvent event) {

	}

}
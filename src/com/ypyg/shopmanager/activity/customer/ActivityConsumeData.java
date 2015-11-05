package com.ypyg.shopmanager.activity.customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseActivity;
import com.ypyg.shopmanager.adapter.AdapterDataVisitorList;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.bean.infobean.DataVisitorInfoBean;
import com.ypyg.shopmanager.event.DataVisitorEvent;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshListView;

public class ActivityConsumeData extends BaseActivity {

	private String imageTag = "ActivityConsumeData";

	private Context mContext = null;

	private View dataTopView = null;
	private int deviceWidth;
	private PullToRefreshListView mPullListView;
	private ListView mListView;
	private AdapterDataVisitorList mAdapter = null;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private List<GoodInfoBean> mList = new ArrayList<GoodInfoBean>();
	private LayoutInflater inflater = null;
	private TextView main_top_title = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		first();
		setContentView(R.layout.activity_consume_data);
		dataTopView = inflater
				.inflate(R.layout.fragment_data_top_visitor, null);
		initView();
		setData1();
		req1();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void first() {
		mContext = this;
		inflater = LayoutInflater.from(mContext);
		mAdapter = new AdapterDataVisitorList(mContext, imageTag, mList);
	}

	private void initView() {
		main_top_title = (TextView) findViewById(R.id.main_top_title);

		initPullToRefreshListView();
	}

	private void setData1() {
		main_top_title.setText("消费概况");
		mAdapter.setDataTopView(dataTopView);
	}

	private void setData2(DataVisitorInfoBean bean) {
		mAdapter.setData(bean);
	}

	private void req1() {
		mDataService.DataRevenue();
	}

	// 初始化下拉列表
	private void initPullToRefreshListView() {
		mPullListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh);
		// 下拉刷性不可用
		mPullListView.setPullRefreshEnabled(false);
		// 上拉加载不可用
		mPullListView.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		mPullListView.setScrollLoadEnabled(true);
		// mList = new ArrayList<PCollectShopListInfoBean>();
		// mAdapter = new AdapterPrettyArticle(mContext, imageTag, mList);
		// 得到实际的ListView
		mListView = mPullListView.getRefreshableView();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
			}
		});

		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// offset = 0l;
				lastPullUpOrDown = UP;
				// if (!AppUtil.isNull(courseId))
				// req2(courseId);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				lastPullUpOrDown = DOWN;
				// if (!AppUtil.isNull(courseId))
				// req2(courseId);
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

	protected void onEventMainThread(DataVisitorEvent event) {
		if (event.getCode() != IRespCode.SUCCESS)
			return;
		DataVisitorInfoBean bean = (DataVisitorInfoBean) event.getEventEntity();
		setData2(bean);
	}

}

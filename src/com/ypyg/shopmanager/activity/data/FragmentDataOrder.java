package com.ypyg.shopmanager.activity.data;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.order.ActivityOrderList2;
import com.ypyg.shopmanager.activity.order.ActivityOrderManager;
import com.ypyg.shopmanager.adapter.AdapterDataOrderList;
import com.ypyg.shopmanager.bean.DataOrderCountinfobean;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.DataOrderCountEvent;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.fragment.BaseFragment;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshListView;

/**
 * 订单统计
 * 
 * @author Administrator
 * 
 */
public class FragmentDataOrder extends BaseFragment {

	private String imageTag = "FragmentDataOrder";
	private View rootView = null;

	private Context mContext = null;

	private View dataTopView = null;
	private View toOrderView = null;
	private int deviceWidth;
	private PullToRefreshListView mPullListView;
	private ListView mListView;
	private AdapterDataOrderList mAdapter = null;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private List<GoodInfoBean> mList = new ArrayList<GoodInfoBean>();

	private BarChart mChart;// 柱状图
	private ArrayList<String> m = new ArrayList<String>();
	private ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_data_revenue, null);
		dataTopView = inflater.inflate(R.layout.fragment_data_top_order, null);
		first();
		initView();
		setData1();
		req1();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void first() {
		mContext = getActivity();
		mAdapter = new AdapterDataOrderList(mContext, imageTag, mList);
	}

	private void initView() {
		mChart = (BarChart) dataTopView.findViewById(R.id.bar_chart);
		Typeface iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
		TextView arrow1 = (TextView) dataTopView.findViewById(R.id.arrow1);
		arrow1.setTypeface(iconfont);

		toOrderView = dataTopView.findViewById(R.id.my_order_manager);
		toOrderView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, ActivityOrderManager.class));
			}
		});
		initPullToRefreshListView();

	}

	/**
	 * 初始化图表
	 */
	private void initBarChart() {
		mChart.setDescription("");
		mChart.setDrawGridBackground(false);
		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setSpaceBetweenLabels(0);
		xAxis.setDrawGridLines(false);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setValueFormatter(myFormatter);

		mChart.getAxisLeft().setDrawGridLines(true);
		mChart.getAxisRight().setEnabled(false);
		// set data
		mChart.setData(getBarChartData());
		mChart.animateY(1500, Easing.EasingOption.EaseInCubic);
		mChart.getLegend().setEnabled(false);

	}

	/**
	 * 获取图表数据
	 * 
	 * @return
	 */
	private BarData getBarChartData() {
		BarDataSet d = new BarDataSet(entries, "");
		d.setBarSpacePercent(30f);
		d.setColors(ColorTemplate.VORDIPLOM_COLORS);
		d.setBarShadowColor(Color.rgb(203, 203, 203));
		d.setValueTextSize(12f);
		d.setValueTextColor(Color.rgb(220, 20, 60));
		d.setValueFormatter(myFormatter);

		ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
		sets.add(d);

		BarData cd = new BarData(m, sets);
		return cd;
	}

	/**
	 * 格式化图表数字显示
	 */
	private MyValueFormatter myFormatter = new MyValueFormatter();

	class MyValueFormatter implements ValueFormatter {
		/** decimalformat for formatting */
		private DecimalFormat mFormat;

		public MyValueFormatter() {
			mFormat = new DecimalFormat("######0");
		}

		@Override
		public String getFormattedValue(float value) {

			return mFormat.format(value);
		}

	}

	private void setData1() {
		mAdapter.setDataTopView(dataTopView);
	}

	private void setData2(DataOrderCountinfobean bean) {
		mAdapter.setData(bean);
	}

	private void req1() {
		mDataService.DataOrderCount();
	}

	// 初始化下拉列表
	private void initPullToRefreshListView() {
		mPullListView = (PullToRefreshListView) rootView.findViewById(R.id.pulltorefresh);
		// 下拉刷性不可用
		mPullListView.setPullRefreshEnabled(false);
		// 上拉加载不可用
		mPullListView.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		mPullListView.setScrollLoadEnabled(false);
		// mList = new ArrayList<PCollectShopListInfoBean>();
		// mAdapter = new AdapterPrettyArticle(mContext, imageTag, mList);
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
				// offset = 0l;
				lastPullUpOrDown = UP;
				// if (!AppUtil.isNull(courseId))
				// req2(courseId);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
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

	protected void onEventMainThread(DataOrderCountEvent event) {
		if (event.getCode() != IRespCode.SUCCESS)
			return;
		DataOrderCountinfobean bean = (DataOrderCountinfobean) event.getEventEntity();
		List<String> week = bean.getWeekorder();
		m.clear();
		entries.clear();
		for (int i = week.size() - 1; i >= 0; i--) {
			String[] temp = week.get(i).split(",");
			m.add(temp[0]);
			entries.add(new BarEntry(Integer.parseInt(temp[1]), i));
		}

		// 初始化图表
		initBarChart();
		// 初始化订单数量
		mAdapter.setData(bean);
		mList.clear();
		mList.addAll(bean.getList());
		mAdapter.notifyDataSetChanged();

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

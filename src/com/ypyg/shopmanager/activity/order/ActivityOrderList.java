package com.ypyg.shopmanager.activity.order;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseActivity;
import com.ypyg.shopmanager.adapter.AdapterOrderList;
import com.ypyg.shopmanager.bean.GoodCategoryBean;
import com.ypyg.shopmanager.bean.OrderBean;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.view.SyncHorizontalScrollView;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshListView;

/**
 * 订单管理页面
 * 
 */
public class ActivityOrderList extends BaseActivity {
	private String imageTag = "ActivityOrderList";
	private Context mContext = null;
	private AdapterOrderList mAdapter;
	private List<OrderBean> mList;
	private PullToRefreshListView mPullListView;
	private ListView mListView;
	private Long offset = 0l;
	private Long count = 10l;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private boolean delete = false;
	// private RelativeLayout nodatalayout;// 空界面
	// private TextView nodatalayouttext;// 空界面文字
	private TextView main_top_title = null;
	private String[] TITLES = { "所有", "待付款", "待发货", "已发货", "已完成", "已关闭" };
	private int indicatorWidth;
	private LinearLayout rg_nav_content;
	private RelativeLayout rl_nav;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private SyncHorizontalScrollView mHsv = null;
	private List<GoodCategoryBean> categoryList = new ArrayList<GoodCategoryBean>();
	private LayoutInflater mInflater = null;
	private Long courseId = null;
	private ArrayList<View> cells = new ArrayList<View>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		first();
		initView();
		setData();
		// req();
		// new QuickReturnHeaderRefreshHelper(this, getWindow().getDecorView(),
		// R.id.top_title_layout, R.id.pulltorefresh);
	}

	// @Override
	// protected void onResume() {
	// // req();
	// super.onResume();
	// }

	private void first() {
		mContext = this;
		mDataCener = DataCener.getInstance();
		mInflater = LayoutInflater.from(mContext);
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
	}

	private void initView() {
		main_top_title = (TextView) findViewById(R.id.main_top_title);

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		indicatorWidth = dm.widthPixels / 5;
		mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);
		rg_nav_content = (LinearLayout) findViewById(R.id.rg_nav_content);
		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);

		mHsv.setSomeParam(rl_nav, (Activity) mContext);
		initPullToRefreshListView();
	}

	private void setData() {
		main_top_title.setText("订单管理");
		for (int i = 0; i < TITLES.length; i++) {
			GoodCategoryBean bean = new GoodCategoryBean();
			bean.setId(i + "");
			bean.setName(TITLES[i]);
			categoryList.add(bean);
		}
		initNavigationHSV(categoryList);
	}

	private void initNavigationHSV(List<GoodCategoryBean> infobean) {

		rg_nav_content.removeAllViews();
		if (AppUtil.isNull(infobean) || infobean.size() == 0)
			return;

		for (int i = 0; i < infobean.size(); i++) {

			View view = (View) mInflater.inflate(R.layout.nav_radiogroup_item,
					null);
			view.setTag(infobean.get(i).getId());
			TextView textview = (TextView) view.findViewById(R.id.text);
			textview.setText(infobean.get(i).getName());
			// view.setLayoutParams(new LayoutParams(indicatorWidth,
			// LayoutParams.MATCH_PARENT));
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// setSelectedDay(v);
					offset = 0l;
					lastPullUpOrDown = UP;
					// courseId = (Long) v.getTag();
					// req2(courseId);
				}
			});
			cells.add(view);
			rg_nav_content.addView(view);
		}

		// 初始化科目view后默认第一个科目为选择项
		// courseId = infobeans.get(0).getCourse_id();
		// if (!AppUtil.isNull(courseId))
		// req2(courseId);
		// setSelectedDay(cells.get(0));
		// if (!AppUtil.isNull(classid)) {
		// Long class_id = null;
		// if (!AppUtil.isNull(classId))
		// class_id = Long.parseLong(classid);
		// for (int i = 0; i < cells.size(); i++) {
		// long id = (Long) cells.get(i).getTag();
		// if (class_id == id)
		// setSelectedDay(cells.get(i));
		// }
		// classid = null;
		// }

	}

	// 数据请求
	private void req() {
	}

	// 删除
	public void delete(View v) {
		if (delete) {
			delete = false;
			mAdapter.setDelete(delete);
		} else {
			delete = true;
			mAdapter.setDelete(delete);
		}
	}

	public void delete() {
		if (delete) {
			delete = false;
			mAdapter.setDelete(delete);
		}
	}

	public boolean getDelete() {
		return delete;
	}

	// 删除收藏老师
	public void deleteShop(Long id) {
		// DataCener.getInstance().deletePCollectShop(id);
	}

	// 初始化下拉列表
	private void initPullToRefreshListView() {
		// nodatalayout = (RelativeLayout)
		// findViewById(R.id.no_data_background);
		// nodatalayouttext = (TextView)
		// findViewById(R.id.no_data_background_text);
		// nodatalayouttext.setText("您还没关注过老师呢~");
		mPullListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh);
		mPullListView.setTipString("无更多订单~");
		// 上拉加载不可用
		mPullListView.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		mPullListView.setScrollLoadEnabled(true);
		mList = new ArrayList<OrderBean>();
		mAdapter = new AdapterOrderList(mContext, imageTag, mList);
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
				offset = 0l;
				count = 10l;
				// loadingStart();
				req();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				req();
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
		return mDateFormat.format(new Date(time));
	}

	// protected void onEventMainThread(PViewCollectTeacherListEvent event) {
	// loadingStop();
	// mPullListView.onPullDownRefreshComplete();
	// mPullListView.onPullUpRefreshComplete();
	// if (null == event || IRespCode.SUCCESS != event.getCode()) {
	// dialog();
	// return;
	// }
	// if (event.getCount() < 10) {
	// mPullListView.setHasMoreData(false);
	// }
	// if (offset == event.getOffset()) {
	// checkListSize();
	// return;
	// }
	// offset = event.getOffset();
	// if (offset < 10)
	// mList.clear();
	// if (offset == 10 && event.getCount() == 10) {
	// mList.clear();
	// }
	// mAdapter.notifyDataSetChanged();
	// mList.addAll((ArrayList<OrderBean>) event.getInfobeans());
	// mAdapter.notifyDataSetChanged();
	// checkListSize();
	//
	// }

	// protected void onEventMainThread(PCancelCollectionInterfaceEvent event) {
	// if (null == event || IRespCode.SUCCESS != event.getCode()) {
	// dialog();
	// return;
	// }
	// mList.remove((ArrayList<OrderBean>) event.getEventEntity());
	// mAdapter.notifyDataSetChanged();
	// checkListSize();
	// }

	/**
	 * 关注操作后刷新
	 * 
	 * @param event
	 */
	// protected void onEventMainThread(MyCenterListNotificationEvent event) {
	// if ("关注的老师".equals(event.getFlag())) {
	// offset = 0l;
	// count = 10l;
	// req();
	// }
	// }

	/**
	 * 图片
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
		ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
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
		ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
		Bitmap bitmap = null;
		if (!AppUtil.isNull(imageUrl)) {
			String key = AppUtil.getStringMD5(imageUrl);
			bitmap = imageCache.get(key);
			if (null != imageView)
				imageView.setImageBitmap(bitmap);
		}
	}

	private void checkListSize() {
		// if (mList.size() == 0) {// 如果listView里面没有数据（内容为空）
		// nodatalayout.setVisibility(View.VISIBLE);// 设置空页面内提示页面可见
		// } else {
		// nodatalayout.setVisibility(View.GONE);// 设置空页面内提示页面不可见
		// }
	}
}

package com.ypyg.shopmanager.activity.good;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.adapter.AdapterGoodOfflineList;
import com.ypyg.shopmanager.bean.GoodCategoryBean;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.GoodOfflineListEvent;
import com.ypyg.shopmanager.event.GoodSStatusEvent;
import com.ypyg.shopmanager.event.GoodStatusEvent;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.fragment.BaseFragment;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.SyncHorizontalScrollView;
import com.ypyg.shopmanager.view.loadingview.DialogUtil;
import com.ypyg.shopmanager.view.popupwindow.DoubleSelectPopupWindow;
import com.ypyg.shopmanager.view.popupwindow.DoubleSelectPopupWindow.DoubleSelectSubmit;
import com.ypyg.shopmanager.view.popupwindow.MoreMenuPopupWindow;
import com.ypyg.shopmanager.view.popupwindow.MoreMenuPopupWindow.MoreMenuListener;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase;
import com.ypyg.shopmanager.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.ypyg.shopmanager.view.swipemenulistview.PullToRefreshSwipeMenuListView;
import com.ypyg.shopmanager.view.swipemenulistview.SwipeMenu;
import com.ypyg.shopmanager.view.swipemenulistview.SwipeMenuCreator;
import com.ypyg.shopmanager.view.swipemenulistview.SwipeMenuItem;
import com.ypyg.shopmanager.view.swipemenulistview.SwipeMenuListView;
import com.ypyg.shopmanager.view.topcondition.TopConditionList;

public class FragmentGoodOffline extends BaseFragment {
	private String imageTag = "FragmentGoodOffline";
	private View rootView;
	private ImageCacheManager mImageCacheManager = null;

	private ArrayList<GoodInfoBean> mDataList = new ArrayList<GoodInfoBean>();
	private List<GoodCategoryBean> categoryList = new ArrayList<GoodCategoryBean>();
	private String[] TITLES = { "个人护肤", "彩妆", "身体护理", "香氛", "其它" };
	private String[] firstMenu = { "全部", "珀莱雅", "韩束", "兰瑟", "自然堂" };
	private String[] secondMenu = { "全部", "爽肤水", "洁面奶", "粉底" };
	private int[] smallLogo = { R.drawable.scxh1, R.drawable.scxh2, R.drawable.scxh3, R.drawable.scxh4, R.drawable.scxh5 };

	private AdapterGoodOfflineList mAdapter;

	private SyncHorizontalScrollView mHsv = null;

	private LayoutInflater mInflater = null;
	private int indicatorWidth;
	private LinearLayout rg_nav_content;
	private RelativeLayout rl_nav;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private ArrayList<View> cells = new ArrayList<View>();
	private ListView mListView;
	private Long courseId = null;

	private PullToRefreshSwipeMenuListView mPullListView;
	private SwipeMenuListView mSwipeListView;
	private Long offset = 0l;
	private Long count = 10l;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private DataCener mDataCener = null;
	private DataService mDataService = null;
	private int deviceWidth;
	private View top_condition = null;
	private String catId = "-1";
	private View top_condition_list_layout = null;
	private View good_cat_bg = null;
	private TopConditionList good_cat_menu = null;
	private MoreMenuPopupWindow mMoreMenuPopupWindow = null;
	private View moreBtn = null;
	private View shaixuan = null;
	private View batch_good_btn_layout = null;
	private TextView batch_good_cancle_btn = null;
	private TextView batch_good_offline_btn = null;

	private DoubleSelectPopupWindow mDoubleSelectPopupWindow = null;
	private String[] firstMenu1 = { "个人护肤", "彩妆", "身体护理", "香氛", "其它" };
	private String[] secondMenu2 = { "全部", "珀莱雅", "韩束", "兰瑟", "自然堂" };

	// 加载中
	private Dialog loadingView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mInflater = inflater;
		BusProvider.get().register(this);
		rootView = inflater.inflate(R.layout.fragmnt_good_offline, container, false);
		first();
		initView();
		setData();
		setListener();
		req1();
		return rootView;
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
		mInflater = LayoutInflater.from(mContext);
		mDataCener = DataCener.getInstance();
		// loading_dialog = mDataCener.createLoadingDialog(mContext);
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
		mAdapter = new AdapterGoodOfflineList(mContext, imageTag, mDataList);
		deviceWidth = getResources().getDisplayMetrics().widthPixels;
		mMoreMenuPopupWindow = new MoreMenuPopupWindow(mContext);
		mMoreMenuPopupWindow.setmTextView("一键上架");
	}

	private void initView() {
		loadingView = DialogUtil.createLoadingDialog(mContext, "加载中");

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);

		indicatorWidth = dm.widthPixels / 5;
		mHsv = (SyncHorizontalScrollView) rootView.findViewById(R.id.mHsv);
		top_condition = (View) rootView.findViewById(R.id.top_condition);
		good_cat_bg = (View) rootView.findViewById(R.id.good_cat_bg);
		top_condition_list_layout = (View) rootView.findViewById(R.id.top_condition_list_layout);
		good_cat_menu = (TopConditionList) rootView.findViewById(R.id.good_cat_menu);
		rg_nav_content = (LinearLayout) rootView.findViewById(R.id.rg_nav_content);
		rl_nav = (RelativeLayout) rootView.findViewById(R.id.rl_nav);
		iv_nav_left = (ImageView) rootView.findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) rootView.findViewById(R.id.iv_nav_right);
		batch_good_btn_layout = (View) rootView.findViewById(R.id.batch_good_btn_layout);
		batch_good_cancle_btn = (TextView) rootView.findViewById(R.id.batch_good_cancle_btn);
		batch_good_offline_btn = (TextView) rootView.findViewById(R.id.batch_good_offline_btn);
		mHsv.setSomeParam(rl_nav, (Activity) mContext);
		initPullToRefreshListView();

		mDoubleSelectPopupWindow = new DoubleSelectPopupWindow(mContext);

	}

	private void setListener() {
		good_cat_bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				top_condition_list_layout.setVisibility(View.GONE);
				clearTopBg();
			}
		});
		mMoreMenuPopupWindow.setOnMoreMenuClick(new MoreMenuListener() {

			@Override
			public void onclick(View v) {
				batch_good_btn_layout.setVisibility(View.VISIBLE);
				mMoreMenuPopupWindow.dismiss();
				mAdapter.setBatch(true);
				mAdapter.notifyDataSetChanged();
			}
		});
		batch_good_cancle_btn.setOnClickListener(batchCancleL);
		batch_good_offline_btn.setOnClickListener(batchOfflineL);

	}

	private void setData() {
		for (int i = 0; i < TITLES.length; i++) {
			GoodCategoryBean bean = new GoodCategoryBean();
			bean.setId(i + "");
			bean.setName(TITLES[i]);
			bean.setSmallLogo(smallLogo[i % 5]);
			categoryList.add(bean);
		}
		initNavigationHSV(categoryList);
	}

	private void initNavigationHSV(List<GoodCategoryBean> infobean) {

		rg_nav_content.removeAllViews();
		if (AppUtil.isNull(infobean) || infobean.size() == 0)
			return;

		for (int i = 0; i < infobean.size(); i++) {

			View view = (View) mInflater.inflate(R.layout.nav_radiogroup_item, null);
			view.setLayoutParams(new LinearLayout.LayoutParams(indicatorWidth, LayoutParams.MATCH_PARENT));
			view.setTag(infobean.get(i).getId());
			TextView textview = (TextView) view.findViewById(R.id.text);
			ImageView smallLogo = (ImageView) view.findViewById(R.id.smalllogo);
			textview.setTextColor(Color.parseColor(Constants.blue));
			textview.setText(infobean.get(i).getName());
			smallLogo.setImageResource(infobean.get(i).getSmallLogo());
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setSelectedDay(v);
					// 如果窗口存在，则更新
					offset = 0l;
					lastPullUpOrDown = UP;
					// courseId = (Long) v.getTag();
					req2(courseId);
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
	private void req1() {
		mDataService.GoodOfflineList(catId, offset, count);
	}

	// 数据请求
	private void req2(Long course_id) {
		// loading_dialog.show();
	}

	public void setSelectedDay(View v) {
		for (View view : cells) {
			view.setBackgroundColor(Color.parseColor(Constants.white));
		}
		v.setBackgroundColor(Color.parseColor(Constants.gray));

		if (!AppUtil.isVisible(top_condition_list_layout)) {
			top_condition_list_layout.setVisibility(View.VISIBLE);
		}
	}

	private void clearTopBg() {
		for (View view : cells) {
			view.setBackgroundColor(Color.parseColor(Constants.white));
		}
	}

	// protected void onEventMainThread(GetClassListEvent event) {
	// if (null == event || IRespCode.SUCCESS != event.getCode()) {
	// mDataCener.dialog("请求失败！");
	// return;
	// }
	// infobeans = (List<GetClassListInfoBean>) event.getEventEntity();
	// initNavigationHSV(infobeans);
	//
	// }

	// protected void onEventMainThread(ScoreListEvent event) {
	// mPullListView.onPullDownRefreshComplete();
	// mPullListView.onPullUpRefreshComplete();
	// if (null == event || IRespCode.SUCCESS != event.getCode()) {
	// mDataCener.dialog("请求失败！");
	// return;
	// }
	// List<ScoreListInfoBean> list = (List<ScoreListInfoBean>) event
	// .getEventEntity();
	// if (!AppUtil.isNull(list))
	// if (list.size() < 10) {
	// mPullListView.setHasMoreData(false);
	// } else {
	// mPullListView.setHasMoreData(true);
	// }
	// if (lastPullUpOrDown == UP)
	// mList.clear();
	// offset += list.size();
	// mList.addAll(list);
	// mAdapter.notifyDataSetChanged();
	// }
	// 初始化下拉列表
	private void initPullToRefreshListView() {
		mPullListView = (PullToRefreshSwipeMenuListView) rootView.findViewById(R.id.swipe_listview);

		// 下拉刷性可用
		mPullListView.setPullRefreshEnabled(true);
		// 上拉加载不可用
		mPullListView.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		mPullListView.setScrollLoadEnabled(true);
		mSwipeListView = (SwipeMenuListView) mPullListView.getRefreshableView();
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

//		mSwipeListView.setSwipeListViewListener(new TestBaseSwipeListViewListener());
		reload();
		setLastUpdateTime();
		// 自动刷新
		// mPullListView.doPullRefreshing(true, 500);

	}

	private void reload() {
		// 设置往左滑动
//		mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
//		// mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
//		mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
//		mSwipeListView.setSwipeActionRight(mSwipeListView.getSwipeActionRight());
//		mSwipeListView.setOffsetLeft(deviceWidth * 1 / 3);
//		// settings.getSwipeOffsetRight()
//		// mSwipeListView.setOffsetRight(deviceWidth * 2 / 3);
//		mSwipeListView.setAnimationTime(0);
//		mSwipeListView.setSwipeOpenOnLongPress(false);
		
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(mContext);
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("编辑");
				// set item title fontsize
				openItem.setTitleSize(13);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// set a icon
				openItem.setIcon(R.drawable.ic_edit);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				deleteItem.setTitle("上架");
				// set item title fontsize
				deleteItem.setTitleSize(18);
				// set item title font color
				deleteItem.setTitleColor(Color.WHITE);
				// set a icon
//				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mSwipeListView.setMenuCreator(creator);

		// step 2. listener item click event
		mSwipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				GoodInfoBean item = mDataList.get(position);
				switch (index) {
				case 0:
					// 编辑商品
					Intent intent = new Intent(mContext, ActivityGoodEdit.class);
					intent.putExtra("id", item.getId());
					intent.putExtra("state", Constants.GOODEDIT_UPDATE);
					mContext.startActivity(intent);
					break;
				case 1:
					// 上架商品
					loadingView.show();
					mDataService.GoodStatus(item.getId(), Constants.online, position, imageTag);
					break;
				}
				return false;
			}
		});

		mSwipeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, ActivityGoodDetail.class);
				intent.putExtra("bean", mDataList.get(position));
				mContext.startActivity(intent);
			}
		});
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	// 刷新 事件
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			offset = 0l;
			// count = 10l;
			lastPullUpOrDown = UP;
			req1();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			lastPullUpOrDown = DOWN;
			req1();
		}
	};

//	class TestBaseSwipeListViewListener extends BaseSwipeListViewListener {
//
//		@Override
//		public void onClickParentView(View v, int position) {
//			super.onClickParentView(v, position);
//
//		}
//
//		@Override
//		public void onClickFrontView(View v, int position) {
//			super.onClickFrontView(v, position);
//			Intent intent = new Intent(mContext, ActivityGoodDetail.class);
//			intent.putExtra("bean", mDataList.get(position));
//			mContext.startActivity(intent);
//		}
//
//		@Override
//		public void onDismiss(ArrayList<Integer> reverseSortedPositions) {
//			try {
//				for (int position : reverseSortedPositions) {
//					mDataList.remove(position);
//				}
//			} catch (Exception e) {
//			}
//			mAdapter.notifyDataSetChanged();
//		}
//	}

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
	 * 设置上下架按钮
	 * 
	 * @param more
	 */
	public void setMoreBtn(final View more) {
		this.moreBtn = more;
		if (!AppUtil.isNull(more)) {
			more.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					top_condition_list_layout.setVisibility(View.GONE);
					clearTopBg();
					mMoreMenuPopupWindow.showAsDropDown(moreBtn, 0, 0);
				}
			});
		}
	}

	/**
	 * 设置筛选按钮事件
	 * 
	 * @param more
	 */
	public void setShaiXuanBtn(final View btn) {
		this.shaixuan = btn;
		if (!AppUtil.isNull(btn)) {
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					top_condition_list_layout.setVisibility(View.GONE);
					clearTopBg();
					// mDataCener.showToast(mContext, "筛选");

					mDoubleSelectPopupWindow.setData(firstMenu1, secondMenu2);
					mDoubleSelectPopupWindow.show(top_condition);
					mDoubleSelectPopupWindow.setListener(mDoubleSelectSubmit);
				}
			});
		}
	}

	private DoubleSelectSubmit mDoubleSelectSubmit = new DoubleSelectSubmit() {

		@Override
		public void submit(String value1, String value2) {
			mDataCener.showToast(mContext, "已下架页面" + "，条件刷新：" + value1 + ";" + value2);
		}
	};

	private OnClickListener batchCancleL = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mAdapter.setBatch(false);
			ArrayList<GoodInfoBean> mlist = (ArrayList<GoodInfoBean>) mAdapter.getmList();
			for (int i = 0; i < mlist.size(); i++) {
				mlist.get(i).setChecked(false);
			}
			mAdapter.notifyDataSetChanged();
			batch_good_btn_layout.setVisibility(View.GONE);
		}
	};
	private OnClickListener batchOfflineL = new OnClickListener() {

		@Override
		public void onClick(View v) {

			ArrayList<GoodInfoBean> mlist = (ArrayList<GoodInfoBean>) mAdapter.getmList();
			StringBuffer sb = new StringBuffer();
			ArrayList<Integer> positions = new ArrayList<Integer>();
			for (int i = 0; i < mlist.size(); i++) {
				if (mlist.get(i).isChecked()) {
					sb.append(mlist.get(i).getId());
					sb.append(",");
					positions.add(i);
				}
			}
			String ids = sb.toString();
			if (AppUtil.isNull(ids))
				return;
			mAdapter.setBatch(false);
			loadingView.show();
			mDataService.GoodSStatus(ids.substring(0, ids.length() - 1), Constants.online, positions, imageTag);

		}
	};

	// 下架商品列表返回
	protected void onEventMainThread(GoodOfflineListEvent event) {
		mPullListView.onPullUpRefreshComplete();
		mPullListView.onPullDownRefreshComplete();
		if (null == event || IRespCode.SUCCESS != event.getCode()) {
			dialog("无数据！");
			return;
		}

		List<GoodInfoBean> list = (List<GoodInfoBean>) event.getEventEntity();
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

	// 单个商品上架
	protected void onEventMainThread(GoodStatusEvent event) {
		loadingView.dismiss();
		if (null == event || IRespCode.SUCCESS != event.getCode()) {
			dialog("数据访问异常！");
			return;
		}
		if (imageTag.equals(event.getTag())) {
			mDataCener.showToast(mContext, "上架成功！");
			Integer listPosition = event.getPosition();
			try {
				mDataList.remove(mDataList.get(listPosition));
				mAdapter.notifyDataSetChanged();
//				mSwipeListView.closeAnimate(listPosition);
//				mSwipeListView.dismiss(listPosition);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mPullListView.doPullRefreshing(true, 200);// 主动刷新
		}
	}

	// 批量商品上架
	protected void onEventMainThread(GoodSStatusEvent event) {
		loadingView.dismiss();
		if (null == event || IRespCode.SUCCESS != event.getCode()) {
			dialog("数据访问异常！");
			return;
		}
		if (imageTag.equals(event.getTag())) {
			mDataCener.showToast(mContext, "上架成功！");
			batch_good_btn_layout.setVisibility(View.GONE);
			ArrayList<Integer> listPosition = event.getPosition();
			List<GoodInfoBean> goodsList = mAdapter.getmList();
			List<GoodInfoBean> goodsListRemoved = new ArrayList<GoodInfoBean>();
			try {
				for (int i = 0; i < listPosition.size(); i++) {
					int position = listPosition.get(i);
					goodsListRemoved.add(goodsList.get(position));
				}
				goodsList.removeAll(goodsListRemoved);
				mAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mPullListView.doPullRefreshing(true, 200);// 主动刷新
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
}

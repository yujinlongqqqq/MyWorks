package com.ypyg.shopmanager.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.customer.ActivityCustomerList;
import com.ypyg.shopmanager.activity.data.ActivityDataManager;
import com.ypyg.shopmanager.activity.good.ActivityGoodManager;
import com.ypyg.shopmanager.activity.order.ActivityOrderList2;
import com.ypyg.shopmanager.activity.order.ActivityOrderManager;
import com.ypyg.shopmanager.activity.promotion.ActivityPromotionManager;
import com.ypyg.shopmanager.activity.shop.ActivityShopManager;
import com.ypyg.shopmanager.bean.BaseStatusInfoBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.event.BaseStatusEvent;
import com.ypyg.shopmanager.event.GoodSortsEvent;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.popupwindow.GoodAddPopupWindow;
import com.ypyg.shopmanager.view.textcounter.CounterView;

public class FragmentIndex2 extends BaseFragment implements OnClickListener {
	private String tag = "FragmentIndex2";
	private View rootView = null;

	private Context mContext = null;

	private TextView mTitle = null;

	private CounterView index_daily_amount = null;
	private TextView index_daily_order = null;
	private TextView index_week_order = null;
	private TextView index_week_amount = null;
	private TextView tianjia_text = null;
	private View index_good_submit = null;
	private View index_good_manager = null;
	private View index_order_manager = null;
	private View index_data_manager = null;
	private View index_shop_manager = null;
	private View index_customer_manager = null;
	private View index_promotion_manager = null;
	private View index_menu_layout = null;// 下方菜单layout
	private int deviceHeight;// 屏幕高
	private GoodAddPopupWindow mGoodAddPopupWindow = null;

	private Typeface iconfont;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_index, null);
		first();
		initView();
		req();
		setData();
		setListener();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void first() {
		mContext = getActivity();
		deviceHeight = getResources().getDisplayMetrics().heightPixels;
		iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");

		mDataCener = DataCener.getInstance();
		// loading_dialog = mDataCener.createLoadingDialog(mContext);
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
	}

	/**
	 * 请求商品分类并存入mDataCener
	 */
	private void req() {
		// mDataService.BaseStatus();
		mDataService.getGoodSorts(mDataCener.mBasicUserInfoBean.getId());
	}

	protected void onEventMainThread(GoodSortsEvent event) {
		if (null == event || IRespCode.SUCCESS != event.getCode()) {
			return;
		}
		mDataCener.mGoodSorts = event.getInfobean();
	}

	private void initView() {
		tianjia_text = (TextView) rootView.findViewById(R.id.tianjia_text);
		tianjia_text.setTypeface(iconfont);
		mTitle = (TextView) rootView.findViewById(R.id.main_top_title);
		index_good_submit = (View) rootView.findViewById(R.id.index_good_submit);
		index_good_manager = rootView.findViewById(R.id.index_good);
		index_order_manager = rootView.findViewById(R.id.index_order);
		index_data_manager = rootView.findViewById(R.id.index_data);
		index_shop_manager = rootView.findViewById(R.id.index_shop);
		index_customer_manager = rootView.findViewById(R.id.index_customer);
		index_promotion_manager = rootView.findViewById(R.id.index_promotion);
		index_menu_layout = rootView.findViewById(R.id.index_menu_layout);
		index_daily_amount = (CounterView) rootView.findViewById(R.id.index_daily_amount);
		index_daily_order = (TextView) rootView.findViewById(R.id.index_daily_order);
		index_week_order = (TextView) rootView.findViewById(R.id.index_week_order);
		index_week_amount = (TextView) rootView.findViewById(R.id.index_week_amount);

		mGoodAddPopupWindow = new GoodAddPopupWindow(mContext);
		// float topTitle = mContext.getResources().getDimension(
		// R.dimen.tab_normal_height);
		// float bottomBar = mContext.getResources().getDimension(
		// R.dimen.tab_sixty_hight);
		// mContext.getResources().getDimension(R.dimen.tab_normal_height);
		//
		// index_menu_layout.setLayoutParams(new LayoutParams(
		// LayoutParams.WRAP_CONTENT, deviceHeight));
		// AppUtil.applyFont(mContext, index_daily_amount,
		// "fonts/scoreboard.ttf");
	}

	private void setData() {
		mTitle.setText("珀莱雅");
	}

	private void setData2(BaseStatusInfoBean bean) {
		if (AppUtil.isNull(bean))
			return;
		if (!AppUtil.isNull(bean.getDaliymoney()))
			index_daily_amount.setText(bean.getDaliymoney());
		if (!AppUtil.isNull(bean.getDaliyorder()))
			index_daily_order.setText(bean.getDaliyorder());
		if (!AppUtil.isNull(bean.getWeekorder()))
			index_week_order.setText(bean.getWeekorder());
		if (!AppUtil.isNull(bean.getWeekmoney()))
			index_week_amount.setText(bean.getWeekmoney());
	}

	private void setListener() {
		index_good_submit.setOnClickListener(this);
		index_good_manager.setOnClickListener(this);
		index_order_manager.setOnClickListener(this);
		index_data_manager.setOnClickListener(this);
		index_shop_manager.setOnClickListener(this);
		index_customer_manager.setOnClickListener(this);
		index_promotion_manager.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.index_good_submit:

			mGoodAddPopupWindow.show(rootView);
			break;
		case R.id.index_good:
			startActivity(new Intent(mContext, ActivityGoodManager.class));
			break;
		case R.id.index_order:
			startActivity(new Intent(mContext, ActivityOrderManager.class));
			break;
		case R.id.index_data:
			startActivity(new Intent(mContext, ActivityDataManager.class));
			break;
		case R.id.index_shop:
			startActivity(new Intent(mContext, ActivityShopManager.class));
			break;
		case R.id.index_customer:
			startActivity(new Intent(mContext, ActivityCustomerList.class));
			break;

		case R.id.index_promotion:
			startActivity(new Intent(mContext, ActivityPromotionManager.class));
			break;
		default:
			break;
		}
	}

	protected void onEventMainThread(BaseStatusEvent event) {
		if (event.getCode() != IRespCode.SUCCESS)
			return;
		BaseStatusInfoBean bean = (BaseStatusInfoBean) event.getEventEntity();
		setData2(bean);
	}

}

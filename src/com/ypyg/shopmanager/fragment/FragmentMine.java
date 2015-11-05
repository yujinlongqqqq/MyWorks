package com.ypyg.shopmanager.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.login.ActivityLogin;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.event.ExitLoginEvent;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.loadingview.DialogUtil;

public class FragmentMine extends BaseFragment implements OnClickListener {

	private View rootView = null;

	private Context mContext = null;

	private TextView mTitle = null;

	private TextView index_daily_amount = null;
	private View index_good_submit = null;
	private TextView index_good_manager = null;
	private TextView index_order_manager = null;
	private TextView index_data_manager = null;
	private TextView index_shop_manager = null;
	private TextView index_customer_manager = null;
	private TextView index_promotion_manager = null;

	private Button exit_btn;

	// 加载中
	private Dialog loadingView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_mine_info, null);
		mContext = getActivity();
		first();
		initView();
		initData();
		setListener();
		return rootView;
	}

	private void first() {
		mContext = getActivity();
		mDataCener = DataCener.getInstance();
		// loading_dialog = mDataCener.createLoadingDialog(mContext);
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initView() {
		loadingView = DialogUtil.createLoadingDialog(mContext, "退出中");
		mTitle = (TextView) rootView.findViewById(R.id.main_top_title);
		exit_btn = (Button) rootView.findViewById(R.id.exit_btn);

		exit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadingView.show();
				mDataService.mainExitLogin(mDataCener.mBasicUserInfoBean.getId());

			}
		});
	}

	private void initData() {
		mTitle.setText("我的");
	}

	private void setListener() {
		// index_good_submit.setOnClickListener(this);
		// index_good_manager.setOnClickListener(this);
		// index_order_manager.setOnClickListener(this);
		// index_data_manager.setOnClickListener(this);
		// index_shop_manager.setOnClickListener(this);
		// index_customer_manager.setOnClickListener(this);
		// index_promotion_manager.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.index_good_submit:
		// break;
		// case R.id.index_good_manager:
		// startActivity(new Intent(mContext, ActivityGoodManager.class));
		// break;
		// case R.id.index_order_manager:
		// startActivity(new Intent(mContext, ActivityOrderList.class));
		// break;
		// case R.id.index_data_manager:
		// startActivity(new Intent(mContext, ActivityDataManager.class));
		// break;
		// case R.id.index_shop_manager:
		// startActivity(new Intent(mContext, ActivityShopManager.class));
		// break;
		// case R.id.index_customer_manager:
		// startActivity(new Intent(mContext, ActivityCustomerList.class));
		// break;
		//
		// case R.id.index_promotion_manager:
		// startActivity(new Intent(mContext, ActivityPromotionManager.class));
		// break;
		//
		// default:
		// break;
		// }

	}

	protected void onEventMainThread(ExitLoginEvent event) {
		loadingView.dismiss();
		if (null == event) {
			mDataCener.showToast(mContext, "网络错误！");
			return;
		}
		if (event.getCode() != IRespCode.SUCCESS) {
			dialog("登录错误,帐号或者密码错误，\n请重试！");
			return;
		}
		// mLoadingView.dismiss();
		// 重置登录用户信息
		mDataCener.mBasicUserInfoBean = null;
		startActivity(new Intent(mContext, ActivityLogin.class));
		((Activity) mContext).finish();

	}

}

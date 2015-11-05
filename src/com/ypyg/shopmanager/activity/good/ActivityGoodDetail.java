package com.ypyg.shopmanager.activity.good;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseActivity;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.event.BaseEvent;

/**
 * 商品详情
 * 
 */
public class ActivityGoodDetail extends BaseActivity {
	private String imageTag = "ActivityGoodDetail";
	private GoodInfoBean mGoodInfoBean;

	private WebView mWeb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_detail);
		first();
		initView();
	}

	private void first() {
		mContext = this;
		mDataCener = DataCener.getInstance();
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
		// if (!AppUtil.isNull(mDataCener)
		// && !AppUtil.isNull(mDataCener.getmBasicInfoBean()))
		// infobean = mDataCener.getmBasicInfoBean();
		// loading_dialog = mDataCener.createLoadingDialog(mContext);
		mGoodInfoBean = (GoodInfoBean) getIntent().getSerializableExtra("bean");
	}

	private void initView() {
		mWeb = (WebView) findViewById(R.id.webview);
		mWeb.getSettings().setSupportZoom(true);// 放大缩小
		mWeb.getSettings().setBuiltInZoomControls(true);// 设置双指缩放
		mWeb.setInitialScale(50);// 为25%，最小缩放等级
		mWeb.clearCache(true);
		
		if (mGoodInfoBean != null) {
			String content = mGoodInfoBean.getGooddetail();
			mWeb.loadData(AppUtil.CS(content), "text/html; charset=UTF-8", null);
		}

	}

	public void more(View v) {

	}

	protected void onEventMainThread(BaseEvent event) {

	}
}

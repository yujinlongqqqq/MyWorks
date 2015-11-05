package com.ypyg.shopmanager.activity.promotion;

import android.os.Bundle;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseActivity;
import com.ypyg.shopmanager.event.BaseEvent;

public class ActivityPromotionManager extends BaseActivity {
	private TextView mTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotion);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.main_top_title);

	}

	private void initData() {
		mTitle.setText("活动促销");
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

	protected void onEventMainThread(BaseEvent event) {

	}
}

package com.ypyg.shopmanager.activity.shop;

import android.os.Bundle;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseActivity;
import com.ypyg.shopmanager.event.BaseEvent;

public class ActivityEditShopName extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_edit);
	}
	
	protected void onEventMainThread(BaseEvent event) {
		
	}
}

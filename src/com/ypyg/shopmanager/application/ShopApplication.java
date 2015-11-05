package com.ypyg.shopmanager.application;

import android.app.Application;
import android.util.Log;

import com.ypyg.shopmanager.common.CrashHandler;
import com.ypyg.shopmanager.common.DataCener;

public class ShopApplication extends Application {

	private static final int UPDATE_TIME = 0;

	private static ShopApplication mInstance = null;
	private DataCener mDataCener = null;

	// 用于外部获取Application
	public static ShopApplication getInstance() {
		return mInstance;
	}

	public ShopApplication() {
		super();
		mInstance = this;
	}

	private void initCrashHandle() {
		CrashHandler cHandler = CrashHandler.getInstance();
		cHandler.init(getApplicationContext());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("application", "oncreate");
		mDataCener = new DataCener(this);
		initCrashHandle();
		mInstance = this;
	}

	// 程序退出
	@Override
	public void onTerminate() {
	}

}

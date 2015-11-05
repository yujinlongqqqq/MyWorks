package com.ypyg.shopmanager.net;

import android.content.Context;

public interface INetState {

	public static final int NETWORK_ERROR = -1;

	public static final int NETWORK_WIFI = 0;

	public static final int NETWORK_GPRS = 1;

	public static final int NETWORK_GPRS_3G = 2;

	public void resetNetType(Context context);

	public int getNetType();

	public boolean isUnavailable();

	public boolean isWifi();

	public boolean isGprs();

	public boolean is3G();

	public boolean is2G();

	public boolean isUniWap();

}

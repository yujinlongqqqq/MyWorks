package com.ypyg.shopmanager.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetState implements INetState, INetChangeListener {

	private static final String TAG = NetState.class.getSimpleName();

	private String mNetType = ""; // 非wifi的网络类型

	// Invalid :-1 / wifi:0 / gprs: 1 / 3G:2
	private int connectState = NETWORK_ERROR; // 当前网络状态

	Context context;

	public NetState(Context context) {
		this.context = context;
		resetNetType(context);
	}

	@Override
	public int getNetType() {
		return connectState;
	}

	@Override
	public boolean isUnavailable() {
		final boolean state = (NETWORK_ERROR == connectState);
		// 网络不可用
		if (state) {
			synchronized (NetState.class) {
				onNetChange(context);
				return NETWORK_ERROR == connectState;
			}

		}
		return state;
	}

	@Override
	public boolean isWifi() {
		return NETWORK_WIFI == connectState;
	}

	@Override
	public boolean is3G() {
		return NETWORK_GPRS_3G == connectState;
	}

	@Override
	public boolean is2G() {
		return NETWORK_GPRS == connectState;
	}

	@Override
	public boolean isGprs() {
		return NETWORK_GPRS == connectState || NETWORK_GPRS_3G == connectState;
	}

	@Override
	public boolean isUniWap() {
		return "uniwap".equalsIgnoreCase(mNetType)
				|| "3gwap".equalsIgnoreCase(mNetType);
	}

	@Override
	public void resetNetType(Context context) {
		onNetChange(context);
	}

	@Override
	public void onNetChange(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null != connectivityManager) {
			connectState = NETWORK_ERROR;
			// 调用android原生api
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
				try {
					String nettypename = networkInfo.getTypeName();
					if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
							|| "wifi".equalsIgnoreCase(nettypename)) {
						connectState = NETWORK_WIFI;
						mNetType = null;
					} else {
						if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							if (isFastGprs(networkInfo.getSubtype())) {
								connectState = NETWORK_GPRS_3G;
							} else {
								connectState = NETWORK_GPRS;
							}
						} else {
							connectState = NETWORK_GPRS;
						}
						// 获取网络类型名称 cm
						mNetType = networkInfo.getExtraInfo();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			Log.d(TAG, "当前网络状态:" + connectState
					+ "(Invalid:-1 / WIFI:0 / GPRS:1 / GPRS_3G:2)");
		}
	}

	private boolean isFastGprs(int subType) {
		switch (subType) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
			// NOT AVAILABLE YET IN API LEVEL 7
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
			// Unknown
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}

}

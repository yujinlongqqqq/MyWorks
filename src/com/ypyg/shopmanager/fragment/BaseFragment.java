package com.ypyg.shopmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.TestDataEvent;
import com.ypyg.shopmanager.view.dialog.MyDialog;

public class BaseFragment extends Fragment {
	protected int lastPullUpOrDown = 0;
	protected int UP = 3211;
	protected int DOWN = 3223;
	protected Context mContext = null;

	protected DataCener mDataCener = null;
	protected DataService mDataService = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		BusProvider.get().register(this);
		mContext = getActivity();
		mDataCener = DataCener.getInstance();
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		BusProvider.get().unregister(this);
	}

	protected MyDialog dialog() {
		if (AppUtil.isNull(mContext))
			return null;
		MyDialog dialog = new MyDialog(mContext, R.style.dialog_contact);
		dialog.setTitle("温馨提示");
		dialog.setText("很抱歉，请重试，或检查网络看看。");
		dialog.setSubmit(null);
		dialog.setSubmitText("好的");
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text, String submittext) {
		if (AppUtil.isNull(mContext))
			return null;
		MyDialog dialog = new MyDialog(mContext, R.style.dialog_contact);
		dialog.setTitle("温馨提示");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setSubmitText(submittext);
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text, String submittext, String canceltext) {
		if (AppUtil.isNull(mContext))
			return null;
		MyDialog dialog = new MyDialog(mContext, R.style.dialog_contact);
		dialog.setTitle("温馨提示");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setSubmitText(submittext);
		dialog.setCancelText(canceltext);
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text) {
		if (AppUtil.isNull(mContext))
			return null;
		MyDialog dialog = new MyDialog(mContext, R.style.dialog_contact);
		dialog.setTitle("温馨提示");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}
	
//	/**
//	 * 原始数据响应
//	 * @param event
//	 */
//	protected void onEventMainThread(TestDataEvent event) {
//		Toast.makeText(mContext, event.getData(), 500).show();
//	}
}

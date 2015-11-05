package com.ypyg.shopmanager.view.popupwindow;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.view.wheel.WheelVerticalView;
import com.ypyg.shopmanager.view.wheel.adapter.ArrayWheelAdapter;

public class SingleSelectPopupWindow extends SelectPopupWindow {
	private TextView mCancel, mSubmit;
	private WheelVerticalView mSingle;

	private Context mContext;
	private View mView;

	private String[] singleValues;

	public SingleSelectPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public SingleSelectPopupWindow(Context context) {
		super(context);
		mContext = context;
		init();

	}

	private void init() {
		first();
		initView();
		// GradeUtils mUtil = new GradeUtils(mContext);
		// setData(mUtil.getNames());
	}

	private void first() {
		mView = LayoutInflater.from(mContext).inflate(
				R.layout.popup_window_select_single, null);
		setContentView(mView);

	}

	private void initView() {
		mSubmit = (TextView) mView.findViewById(R.id.select_submit);
		mCancel = (TextView) mView.findViewById(R.id.select_cancel);
		mSingle = (WheelVerticalView) mView
				.findViewById(R.id.select_single_wheel);
	}

	public void setData(String[] values, int lastMonth) {
		Calendar cal = Calendar.getInstance();
		this.singleValues = values;
		if (!AppUtil.isNull(values))
			mSingle.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
					values));
		mSingle.setCyclic(false);
		mSingle.setCurrentItem(lastMonth);
		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		mSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					mListener.submit(singleValues[mSingle.getCurrentItem()]);
				}
				dismiss();
			}
		});
	}

	public void set(String value) {

	}

	private SingleSubmit mListener = null;

	public void setListener(SingleSubmit submit) {
		mListener = submit;
	}

	public interface SingleSubmit {
		public void submit(String value);
	}
}

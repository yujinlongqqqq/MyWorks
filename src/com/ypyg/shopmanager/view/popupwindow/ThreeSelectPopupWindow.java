package com.ypyg.shopmanager.view.popupwindow;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.view.wheel.WheelVerticalView;
import com.ypyg.shopmanager.view.wheel.adapter.ArrayWheelAdapter;

public class ThreeSelectPopupWindow extends SelectPopupWindow {
	private TextView mCancel, mSubmit;
	private WheelVerticalView select_three_wheel_first;
	private WheelVerticalView select_three_wheel_second;
	private WheelVerticalView select_three_wheel_third;

	private Context mContext;
	private View mView;

	private String[] Wvalues1;
	private String[] Wvalues2;
	private String[] Wvalues3;

	public ThreeSelectPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public ThreeSelectPopupWindow(Context context) {
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
				R.layout.popup_window_select_three, null);
		setContentView(mView);
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				ThreeSelectPopupWindow.this.onDismiss();
			}
		});

	}

	private void initView() {
		mSubmit = (TextView) mView.findViewById(R.id.select_submit);
		mCancel = (TextView) mView.findViewById(R.id.select_cancel);
		select_three_wheel_first = (WheelVerticalView) mView
				.findViewById(R.id.select_three_wheel_first);
		select_three_wheel_second = (WheelVerticalView) mView
				.findViewById(R.id.select_three_wheel_second);
		select_three_wheel_third = (WheelVerticalView) mView
				.findViewById(R.id.select_three_wheel_third);
	}

	public void setData(String[] values1, String[] values2, String[] values3) {
		Calendar cal = Calendar.getInstance();
		this.Wvalues1 = values1;
		this.Wvalues2 = values2;
		this.Wvalues3 = values3;
		if (!AppUtil.isNull(values1))
			select_three_wheel_first
					.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
							values1));
		if (!AppUtil.isNull(values2))
			select_three_wheel_second
					.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
							values2));
		if (!AppUtil.isNull(values3))
			select_three_wheel_third
					.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
							values3));
		select_three_wheel_first.setCyclic(true);
		select_three_wheel_second.setCyclic(true);
		select_three_wheel_third.setCyclic(true);
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
					mListener.submit(
							Wvalues1[select_three_wheel_first.getCurrentItem()],
							Wvalues2[select_three_wheel_second.getCurrentItem()],
							Wvalues3[select_three_wheel_third.getCurrentItem()]);
				}
				dismiss();
			}
		});
	}

	public void set(String value) {

	}

	private ThreeSelectSubmit mListener = null;

	public void setListener(ThreeSelectSubmit submit) {
		mListener = submit;
	}

	public interface ThreeSelectSubmit {
		public void submit(String value1, String value2, String value3);
	}

	public void show(View rootView) {
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
				.getAttributes();
		lp.alpha = 0.4f;
		((Activity) mContext).getWindow().setAttributes(lp);
		this.showAtLocation(rootView, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	// 在dismiss中恢复透明度
	public void onDismiss() {
		WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
				.getAttributes();
		lp.alpha = 1f;
		((Activity) mContext).getWindow().setAttributes(lp);
	}
}

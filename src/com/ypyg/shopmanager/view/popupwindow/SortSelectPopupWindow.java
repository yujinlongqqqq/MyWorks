package com.ypyg.shopmanager.view.popupwindow;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.view.wheel.AbstractWheel;
import com.ypyg.shopmanager.view.wheel.OnWheelChangedListener;
import com.ypyg.shopmanager.view.wheel.WheelVerticalView;
import com.ypyg.shopmanager.view.wheel.adapter.ArrayWheelAdapter;

public class SortSelectPopupWindow extends SelectPopupWindow {
	private TextView mCancel, mSubmit;
	private WheelVerticalView select_three_wheel_first;
	private WheelVerticalView select_three_wheel_second;

	private Context mContext;
	private View mView;

	private String[] Wvalues1;
	private String[][] Wvalues2;

	public SortSelectPopupWindow(Context context, String[] values1, String[][] values2, AttributeSet attrs) {
		super(context, attrs);
		this.Wvalues1 = values1;
		this.Wvalues2 = values2;
		mContext = context;
		init();
	}

	public SortSelectPopupWindow(Context context, String[] values1, String[][] values2) {
		super(context);
		this.Wvalues1 = values1;
		this.Wvalues2 = values2;
		mContext = context;
		init();
	}

	private void init() {
		first();
		initView();
		setData();
	}

	private void first() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.popup_window_select_double, null);
		setContentView(mView);
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				SortSelectPopupWindow.this.onDismiss();
			}
		});

	}

	private void initView() {
		mSubmit = (TextView) mView.findViewById(R.id.select_submit);
		mCancel = (TextView) mView.findViewById(R.id.select_cancel);
		select_three_wheel_first = (WheelVerticalView) mView.findViewById(R.id.select_three_wheel_first);
		select_three_wheel_second = (WheelVerticalView) mView.findViewById(R.id.select_three_wheel_second);

	}

	// WheelVerticalView控件监听
	private OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
		public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
			select_three_wheel_second.setViewAdapter(new ArrayWheelAdapter<String>(mContext, Wvalues2[newValue]));
			select_three_wheel_second.setCurrentItem(0);
		}
	};

	private void setData() {
		if (!AppUtil.isNull(Wvalues1))
			select_three_wheel_first.setViewAdapter(new ArrayWheelAdapter<String>(mContext, Wvalues1));
		if (!AppUtil.isNull(Wvalues2))
			select_three_wheel_second.setViewAdapter(new ArrayWheelAdapter<String>(mContext, Wvalues2[0]));
		select_three_wheel_first.addChangingListener(wheelListener);
		select_three_wheel_first.setCyclic(false);// 设置不循环
		select_three_wheel_second.setCyclic(false);// 设置不循环

		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		mSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position1 = select_three_wheel_first.getCurrentItem();
				int position2 = select_three_wheel_second.getCurrentItem();
				if (null != mListener) {
					mListener.submit(Wvalues1[position1], Wvalues2[position1][position2 % Wvalues2[position1].length], position1, position2);
				}
				dismiss();
			}
		});
	}

	public void set(String value) {

	}

	private SortSelectSubmit mListener = null;

	public void setListener(SortSelectSubmit submit) {
		mListener = submit;
	}

	public interface SortSelectSubmit {
		public void submit(String value1, String value2, Integer position1, Integer position2);
	}

	public void show(View rootView) {
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
		lp.alpha = 0.4f;
		((Activity) mContext).getWindow().setAttributes(lp);
		this.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	// 在dismiss中恢复透明度
	public void onDismiss() {
		WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
		lp.alpha = 1f;
		((Activity) mContext).getWindow().setAttributes(lp);
	}
}

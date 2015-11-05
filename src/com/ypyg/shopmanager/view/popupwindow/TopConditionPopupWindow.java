package com.ypyg.shopmanager.view.popupwindow;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.TabSelectionEvent;
import com.ypyg.shopmanager.view.topcondition.TopConditionList;

public class TopConditionPopupWindow extends PopupWindow {
	int H;
	private Context mContext;
	private View mView;

	private int height = 0;

	public TopConditionPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public TopConditionPopupWindow(Context context) {
		super(context);
		mContext = context;
		init();

	}

	private void init() {

		height = AppUtil.dip2px(mContext, 140);
		// H = ((Activity) mContext).getWindowManager().getDefaultDisplay()
		// .getHeight() / 3;
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(height);
		this.setOutsideTouchable(true);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimTtoB);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(Color.parseColor("#ffffff"));
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		first();
		initView();

	}

	private void first() {
		mView = new TopConditionList(mContext);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, AppUtil.dip2px(mContext, 140));
		mView.setLayoutParams(layoutParams);
		setContentView(mView);

		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				TopConditionPopupWindow.this.onDismiss();
			}
		});
	}

	private void initView() {
	}

	private OnClickListener marketSelect = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TopConditionPopupWindow.this.dismiss();
			TabSelectionEvent event = new TabSelectionEvent();
			event.setTabPosition(1);
			BusProvider.get().post(event);
		}
	};
	private OnClickListener album = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TopConditionPopupWindow.this.dismiss();
			// AbDialogUtil.removeDialog(v.getContext());
			// 从相册中去获取
			try {
				AppUtil.doGoToImg(mContext);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(mContext, "没有找到照片", Toast.LENGTH_SHORT).show();
			}
		}

	};
	private OnClickListener photo = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TopConditionPopupWindow.this.dismiss();
			AppUtil.doGoToPhone(mContext);
		}

	};
	private OnClickListener cancle = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TopConditionPopupWindow.this.dismiss();
		}
	};

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

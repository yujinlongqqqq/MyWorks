package com.ypyg.shopmanager.view.popupwindow;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.TabSelectionEvent;

public class GoodAddPopupWindow extends PopupWindow {
	int H;
	private Context mContext;
	private View mView;

	private int height = 0;

	public GoodAddPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public GoodAddPopupWindow(Context context) {
		super(context);
		mContext = context;
		init();

	}

	private void init() {

		height = AppUtil.dip2px(mContext, 140);
		H = ((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getHeight() / 3;
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(height);
		this.setOutsideTouchable(true);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimRBtoLT);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		first();
		initView();

	}

	private void first() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.good_choose,
				null);
		setContentView(mView);

		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				GoodAddPopupWindow.this.onDismiss();
			}
		});

	}

	private void initView() {
		View market_select = (View) mView.findViewById(R.id.market_select);
		View go_to_image = (View) mView.findViewById(R.id.go_to_image);
		View go_to_photo = (View) mView.findViewById(R.id.go_to_photo);
		View cancel = (View) mView.findViewById(R.id.cancel);
		market_select.setOnClickListener(marketSelect);
		go_to_image.setOnClickListener(album);
		go_to_photo.setOnClickListener(photo);
		cancel.setOnClickListener(cancle);

	}

	private OnClickListener marketSelect = new OnClickListener() {

		@Override
		public void onClick(View v) {
			GoodAddPopupWindow.this.dismiss();
			TabSelectionEvent event = new TabSelectionEvent();
			event.setTabPosition(1);
			BusProvider.get().post(event);
		}
	};
	private OnClickListener album = new OnClickListener() {

		@Override
		public void onClick(View v) {
			GoodAddPopupWindow.this.dismiss();
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
			GoodAddPopupWindow.this.dismiss();
			AppUtil.doGoToPhone(mContext);
		}

	};
	private OnClickListener cancle = new OnClickListener() {

		@Override
		public void onClick(View v) {
			GoodAddPopupWindow.this.dismiss();
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

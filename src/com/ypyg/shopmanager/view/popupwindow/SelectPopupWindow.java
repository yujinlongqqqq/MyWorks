package com.ypyg.shopmanager.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.ypyg.shopmanager.R;

public class SelectPopupWindow extends PopupWindow {
	int H;
	public SelectPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		H = ((Activity) context).getWindowManager().getDefaultDisplay()
				.getHeight() / 3;
		init();
	}

	public SelectPopupWindow(Context context) {
		super(context);
		H = ((Activity) context).getWindowManager().getDefaultDisplay()
				.getHeight() / 3;
		init();
	}
	private void init() {
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(H);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimRBtoLT);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}
}

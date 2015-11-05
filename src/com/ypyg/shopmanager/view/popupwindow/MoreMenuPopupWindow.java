package com.ypyg.shopmanager.view.popupwindow;

import android.app.Activity;
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
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;

public class MoreMenuPopupWindow extends PopupWindow {
	int H;
	private Context mContext;
	private View mView;
	private View batch_online_layout = null;

	private int height = 0;
	private MoreMenuListener mml = null;
	
	private TextView mTextView;

	public MoreMenuPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public MoreMenuPopupWindow(Context context) {
		super(context);
		mContext = context;
		init();

	}

	private void init() {

		height = AppUtil.dip2px(mContext, 140);
		// H = ((Activity) mContext).getWindowManager().getDefaultDisplay()
		// .getHeight() / 3;
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setOutsideTouchable(true);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimTtoB);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		initView();

	}

	private void initView() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.more_menu_list,
				null);
		batch_online_layout = mView.findViewById(R.id.batch_online_layout);
		mTextView= (TextView) mView.findViewById(R.id.batch_online_text);
		batch_online_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mml.onclick(v);
			}
		});

		setContentView(mView);
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				MoreMenuPopupWindow.this.onDismiss();
			}
		});
	}

	public void setmTextView(String text) {
		mTextView.setText(text);
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

	public void setOnMoreMenuClick(MoreMenuListener mml) {
		this.mml = mml;
	}

	public interface MoreMenuListener {
		void onclick(View v);
	}

}

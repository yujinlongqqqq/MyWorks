package com.ypyg.shopmanager.view.popupwindow;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;

public class CameraPopupWindow extends PopupWindow {
	int H;
	private Context mContext;
	private View mView;

	private int height = 0;

	public CameraPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public CameraPopupWindow(Context context) {
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
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimRBtoLT);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		first();
		initView();

	}

	private void first() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.choose_avatar,
				null);
		setContentView(mView);

	}

	private void initView() {
		Button albumButton = (Button) mView.findViewById(R.id.choose_album);
		Button camButton = (Button) mView.findViewById(R.id.choose_cam);
		Button cancelButton = (Button) mView.findViewById(R.id.choose_cancel);
		albumButton.setOnClickListener(album);
		camButton.setOnClickListener(cam);
		cancelButton.setOnClickListener(cancle);

	}

	private OnClickListener album = new OnClickListener() {

		@Override
		public void onClick(View v) {
			CameraPopupWindow.this.dismiss();
//			AbDialogUtil.removeDialog(v.getContext());
			// 从相册中去获取
			try {
				AppUtil.doGoToImg(mContext);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(mContext, "没有找到照片", Toast.LENGTH_SHORT).show();
			}
		}

	};
	private OnClickListener cam = new OnClickListener() {

		@Override
		public void onClick(View v) {
			CameraPopupWindow.this.dismiss();
//			AbDialogUtil.removeDialog(v.getContext());
			AppUtil.doGoToPhone(mContext);
		}

	};
	private OnClickListener cancle = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			AbDialogUtil.removeDialog(v.getContext());
		}

	};
}

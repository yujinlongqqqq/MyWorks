package com.ypyg.shopmanager.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypyg.shopmanager.R;

public class MyDialog extends Dialog {
	private TextView mCancel, mSubmit, mText, mTitle;
	private LinearLayout mTitlelayout;

	public MyDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_my);
		init();
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_my);
		init();
	}

	public MyDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_my);
		init();
	}

	private void init() {
		mCancel = (TextView) findViewById(R.id.dialog_contact_cancel);
		mSubmit = (TextView) findViewById(R.id.dialog_contact_submit);
		mText = (TextView) findViewById(R.id.dialog_contact_text);
		mTitle = (TextView) findViewById(R.id.dialog_contact_title);
		mTitlelayout = (LinearLayout) findViewById(R.id.dialog_contact_titlelayout);

		if (null != mCancel) {
			mCancel.setOnClickListener(cancelClick);
		}
	}

	public void setTitle(String title) {
		if (null != mTitle) {
			mTitle.setText(title);
			mTitlelayout.setVisibility(View.VISIBLE);
		}
	}

	public void setText(String text) {
		if (null != mText) {
			mText.setText(text);
		}
	}

	public void setText(String text, int size) {
		if (null != mText) {
			mText.setText(text);
			mText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
		}
	}


	public void setSubmit(View.OnClickListener submit) {
		if (null == submit) {
			if (null != mSubmit) {
				mSubmit.setOnClickListener(cancelClick);
			}
		} else if (null != mSubmit) {
			mSubmit.setOnClickListener(submit);
		}
	}

	public void setSubmitText(String str) {
		mSubmit.setText(str);
	}

	public void setCancelText(String str) {
		mCancel.setText(str);
	}

	public void setCancel(View.OnClickListener cancel) {
		if (null == cancel) {
			if (null != mCancel) {
				mCancel.setOnClickListener(cancelClick);
			}
		} else if (null != mCancel) {
			mCancel.setOnClickListener(cancel);
		}
	}

	public void setCancelGone() {
		if (null != mCancel) {
			mCancel.setVisibility(View.GONE);
			mSubmit.setBackgroundResource(R.drawable.btn_pressed_orange_bg3);
		}
	}

	private View.OnClickListener cancelClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			MyDialog.this.cancel();
		}
	};
}

package com.ypyg.shopmanager.view.TextView;

import com.ypyg.shopmanager.activity.good.ActivityGoodEdit;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.OverScroller;
import android.widget.TextView;
import android.widget.Toast;

public class JellyTextView extends TextView {
	private String Tag = "JellyTextView";
	private OverScroller mScroller;

	private float lastX;
	private float lastY;

	private float startX;
	private float startY;
	private int width;
	private int height;

	private int sixtydp;
	private Context mContext;
	public JellyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		mScroller = new OverScroller(context, new BounceInterpolator());
		WindowManager wm = ((Activity) context).getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		sixtydp = AppUtil.dip2px(context, 60);
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(mContext,ActivityGoodEdit.class).putExtra("state", Constants.GOODEDIT_ADD));
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = event.getRawX();
			lastY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float disX = event.getRawX() - lastX;
			float disY = event.getRawY() - lastY;

			offsetLeftAndRight((int) disX);
			offsetTopAndBottom((int) disY);
			lastX = event.getRawX();
			lastY = event.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			// mScroller.startScroll((int) getX(), (int) getY(),
			// -(int) (getX() - startX), -(int) (getY() - startY));
			// invalidate();
			break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void computeScroll() {
		System.out.println(Tag + ": " + "computeScroll");
		int x = mScroller.getCurrX();
		int y = mScroller.getCurrY();
		if (mScroller.computeScrollOffset()) {
			setX(x);
			setY(y);
			invalidate();
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		System.out.println(Tag + ": " + "onSizeChanged");
		super.onSizeChanged(w, h, oldw, oldh);
		startX = getX();
		startY = getY();
	}

	public void spingBack() {
		System.out.println(Tag + ": " + "spingBack");
		if (mScroller.springBack((int) getX(), (int) getY(), 0, (int) getX(),
				0, (int) getY() - 100)) {
			Log.d("TAG", "getX()=" + getX() + "__getY()=" + getY());
			invalidate();
		}

	}

}
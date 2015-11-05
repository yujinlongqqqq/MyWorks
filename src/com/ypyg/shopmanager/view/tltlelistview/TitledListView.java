package com.ypyg.shopmanager.view.tltlelistview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;

public class TitledListView extends ListView {

	private View mTitle;

	public TitledListView(Context context) {
		super(context);
		init(context);
	}

	public TitledListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TitledListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mTitle != null) {
			measureChild(mTitle, widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mTitle != null) {
			mTitle.layout(0, 0, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight());
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		drawChild(canvas, mTitle, getDrawingTime());
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		mTitle = inflater.inflate(R.layout.title, this, false);
		title_text = (TextView) mTitle.findViewById(R.id.titled_text);
	}

	TextView title_text;

	public void moveTitle(String title) {
		View bottomChild = getChildAt(0);
		if (bottomChild != null) {
			int bottom = bottomChild.getBottom();
			int height = mTitle.getMeasuredHeight();
			int y = 0;
			if (bottom < height) {
				y = bottom - height;
			}
			mTitle.layout(0, y, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight() + y);
		}

		if (title != null) {
			title_text.setText(title);
		}
	}

	public void updateTitle(String title) {
		if (title != null) {
			title_text.setText(title);
		}
		mTitle.layout(0, 0, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight());
	}
}

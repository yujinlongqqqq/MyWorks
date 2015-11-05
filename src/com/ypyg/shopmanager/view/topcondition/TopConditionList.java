package com.ypyg.shopmanager.view.topcondition;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Constants;

public class TopConditionList extends LinearLayout {
	private Context mContext = null;

	private String[] firstMenu = { "全部", "珀莱雅", "兰瑟", "自然堂", "韩束", "一叶子",
			"珀莱雅", "兰瑟", "自然堂", "韩束", "一叶子" };
	private String[] secondMenu = { "全部宝贝", "爽肤水", "化妆乳", "洗面奶", "面膜", "爽肤水",
			"化妆乳", "洗面奶", "面膜" };
	private View lastView = null;

	public TopConditionList(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public TopConditionList(Context c, AttributeSet attrs) {
		super(c, attrs);
		this.mContext = c;
		init();
	}

	public void init() {
		initView();
	}

	/**
	 * 初始化基本控件
	 */
	private void initView() {
		setOrientation(LinearLayout.HORIZONTAL);

		// 外层scrollview
		final int width = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 100, getResources()
						.getDisplayMetrics());
		// 左边
		ScrollView scrollViewLeft = new ScrollView(mContext);
		LinearLayout.LayoutParams scrollViewParamsLeft = new LinearLayout.LayoutParams(
				width, LayoutParams.WRAP_CONTENT);
		scrollViewParamsLeft.weight = 0;
		scrollViewLeft.setLayoutParams(scrollViewParamsLeft);
		scrollViewLeft.setBackgroundColor(Color.parseColor(Constants.gray));
		// 右边
		ScrollView scrollViewRight = new ScrollView(mContext);
		LinearLayout.LayoutParams scrollViewParamsRight = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scrollViewParamsRight.weight = 1;
		scrollViewRight.setLayoutParams(scrollViewParamsRight);
		scrollViewRight.setBackgroundColor(Color.parseColor(Constants.white));

		//
		LinearLayout linearLayoutLeft = new LinearLayout(mContext);

		LinearLayout.LayoutParams layoutParamLeft = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// layoutParamLeft.weight = 0;
		linearLayoutLeft.setOrientation(LinearLayout.VERTICAL);
		linearLayoutLeft.setLayoutParams(layoutParamLeft);

		//
		LinearLayout linearLayoutRight = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParamRight = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// layoutParamRight.weight = 1;
		linearLayoutRight.setOrientation(LinearLayout.VERTICAL);
		linearLayoutRight.setLayoutParams(layoutParamRight);

		scrollViewLeft.addView(linearLayoutLeft);
		scrollViewRight.addView(linearLayoutRight);

		// 一级分类
		for (int i = 0; i < firstMenu.length; i++) {
			LinearLayout linearLayoutItem = new LinearLayout(mContext);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			// layoutParams.setMargins(0, 10, 0, 10);
			linearLayoutItem.setLayoutParams(layoutParams);
			linearLayoutItem.setOrientation(LinearLayout.HORIZONTAL);
			linearLayoutItem.setGravity(Gravity.CENTER_VERTICAL);

			TextView tv1 = new TextView(mContext);
			tv1.setLayoutParams(new LinearLayout.LayoutParams(AppUtil.dip2px(
					mContext, 3), LayoutParams.MATCH_PARENT));
			tv1.setBackgroundColor(Color.parseColor(Constants.blue));
			tv1.setVisibility(View.INVISIBLE);
			TextView tv = new TextView(mContext);
			tv.setPadding(0, 10, 0, 10);
			tv.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tv.setTextSize(AppUtil.sp2px(mContext, 5));
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setTextColor(Color.parseColor("#000000"));
			tv.setText(firstMenu[i]);
			linearLayoutItem.setTag(R.id.both, firstMenu[i]);
			linearLayoutItem.setOnClickListener(firstItemListener);
			linearLayoutItem.addView(tv1);
			linearLayoutItem.setTag(R.id.cancel, tv1);
			linearLayoutItem.addView(tv);
			linearLayoutLeft.addView(linearLayoutItem);
		}
		// 二级分类
		for (int i = 0; i < secondMenu.length; i++) {
			LinearLayout linearLayoutItem = new LinearLayout(mContext);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 10, 0, 10);
			linearLayoutItem.setLayoutParams(layoutParams);
			linearLayoutItem.setOrientation(LinearLayout.HORIZONTAL);
			linearLayoutItem.setGravity(Gravity.CENTER_VERTICAL);
			TextView tv = new TextView(mContext);
			tv.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tv.setTextSize(AppUtil.sp2px(mContext, 5));
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setTextColor(Color.parseColor("#000000"));
			tv.setText(secondMenu[i]);
			linearLayoutItem.setTag(secondMenu[i]);
			linearLayoutItem.setOnClickListener(secondItemListener);
			linearLayoutItem.addView(tv);
			linearLayoutRight.addView(linearLayoutItem);
		}
		//
		addView(scrollViewLeft);
		addView(scrollViewRight);
	}

	private OnClickListener firstItemListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setSelectedStyle(v);

		}
	};

	private OnClickListener secondItemListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	// 设置选中item的样式
	private void setSelectedStyle(View v) {
		if (!AppUtil.isNull(lastView)) {
			if (lastView.equals(v)) {
				return;
			}
			lastView.setBackgroundColor(Color.parseColor(Constants.gray));
			((View) lastView.getTag(R.id.cancel)).setVisibility(View.INVISIBLE);
		}
		v.setBackgroundColor(Color.parseColor(Constants.white));
		((View) v.getTag(R.id.cancel)).setVisibility(View.VISIBLE);
		lastView = v;
	}

	public void setFirstMenu(String[] firstMenu) {
		this.firstMenu = firstMenu;
	}

	public void setSecondMenu(String[] secondMenu) {
		this.secondMenu = secondMenu;
	}

}
package com.ypyg.shopmanager.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ypyg.shopmanager.R;

public class LoadingView extends RelativeLayout {
	private ImageView mImg;

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_loading, this);
		mImg = (ImageView) findViewById(R.id.view_loading_img);
		mImg.setBackgroundResource(R.drawable.loading_anim_view);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_loading, this);
		mImg = (ImageView) findViewById(R.id.view_loading_img);
		mImg.setBackgroundResource(R.drawable.loading_anim_view);
	}

	public LoadingView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.view_loading, this);
		mImg = (ImageView) findViewById(R.id.view_loading_img);
		mImg.setBackgroundResource(R.drawable.loading_anim_view);
	}
	public void start() {
		((AnimationDrawable) mImg.getBackground()).start();
		setVisibility(View.VISIBLE);
	}
	public void stop() {
		((AnimationDrawable) mImg.getBackground()).stop();
		setVisibility(View.GONE);
	}
}

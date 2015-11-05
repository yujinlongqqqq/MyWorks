package com.ypyg.shopmanager.activity.shop;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.ImageCacheManager;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShopManagerAdapter extends BaseAdapter {
	private Context mContext;
	private ImageCacheManager mImageCacheManager = null;
	private String imageTag = null;
	private Typeface iconfont;

	public ShopManagerAdapter(Context mContext, String imageTag) {
		this.mContext = mContext;
		this.imageTag = imageTag;
		iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_content_view, null);
			TextView arrow1 = (TextView) convertView.findViewById(R.id.arrow1);
			TextView arrow2 = (TextView) convertView.findViewById(R.id.arrow2);
			TextView arrow3 = (TextView) convertView.findViewById(R.id.arrow3);
			TextView arrow4 = (TextView) convertView.findViewById(R.id.arrow4);
			TextView arrow5 = (TextView) convertView.findViewById(R.id.arrow5);
			TextView arrow6 = (TextView) convertView.findViewById(R.id.arrow6);
			TextView arrow7 = (TextView) convertView.findViewById(R.id.arrow7);
			arrow1.setTypeface(iconfont);
			arrow2.setTypeface(iconfont);
			arrow3.setTypeface(iconfont);
			arrow4.setTypeface(iconfont);
			arrow5.setTypeface(iconfont);
			arrow6.setTypeface(iconfont);
			arrow7.setTypeface(iconfont);
			TextView ico1 = (TextView) convertView.findViewById(R.id.text_item1);
			TextView ico2 = (TextView) convertView.findViewById(R.id.text_item2);
			TextView ico3 = (TextView) convertView.findViewById(R.id.text_item3);
			TextView ico4 = (TextView) convertView.findViewById(R.id.text_item4);
			TextView ico5 = (TextView) convertView.findViewById(R.id.text_item5);
			TextView ico6 = (TextView) convertView.findViewById(R.id.text_item6);
			TextView ico7 = (TextView) convertView.findViewById(R.id.text_item7);
			ico1.setTypeface(iconfont);
			ico2.setTypeface(iconfont);
			ico3.setTypeface(iconfont);
			ico4.setTypeface(iconfont);
			ico5.setTypeface(iconfont);
			ico6.setTypeface(iconfont);
			ico7.setTypeface(iconfont);
		}
		return convertView;
	}

}

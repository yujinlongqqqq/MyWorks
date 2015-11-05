package com.ypyg.shopmanager.view.tltlelistview;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.bean.GoodInfoBean;

public class TitledListAdapter extends BaseAdapter {

	private List<GoodInfoBean> datas;
	private Context context;

	public TitledListAdapter(Context context, List<GoodInfoBean> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private ViewHoder vh = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			vh = new ViewHoder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.titled_listitem, null);

			vh.title = (TextView) convertView.findViewById(R.id.title);
			vh.imageView = (ImageView) convertView.findViewById(R.id.good_icon_checked);
			vh.textName = (TextView) convertView.findViewById(R.id.good_name);
			vh.textTime = (TextView) convertView.findViewById(R.id.good_time);
			vh.textNo = (TextView) convertView.findViewById(R.id.good_num);
			vh.textPrice = (TextView) convertView.findViewById(R.id.good_price);

			convertView.setTag(vh);
		} else {
			vh = (ViewHoder) convertView.getTag();
		}

		vh.title.setText(datas.get(position).getTitle());

		// 第一项和前后不同的项需要显示标题，否则隐藏
		if (position == 0) {
			vh.title.setVisibility(View.VISIBLE);
		} else if (position < getCount() && !datas.get(position).getTitle().equals(datas.get(position - 1).getTitle())) {
			vh.title.setVisibility(View.VISIBLE);
		} else {
			vh.title.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ViewHoder {
		TextView title;

		ImageView imageView;
		TextView textName;
		TextView textTime;
		TextView textNo;
		TextView textPrice;
	}

}

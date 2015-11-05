package com.ypyg.shopmanager.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.good.ActivityGoodDetail;
import com.ypyg.shopmanager.activity.good.ActivityGoodEdit;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.ImageCacheManager;

public class AdapterGoodOfflineList extends BaseAdapter {

	private Context mContext;
	private List<GoodInfoBean> mList;

	// private TutorTypeUtils mUtils;

	private ImageCacheManager mImageCacheManager = null;
	private String imageTag = null;
	private boolean isBatch = false;// 是否显示批量上下架

	public AdapterGoodOfflineList(Context context, String imageTag, List<GoodInfoBean> list) {
		mContext = context;
		mList = list;
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
		this.imageTag = imageTag;
		// mUtils = new TutorTypeUtils(mContext);

	}

	public List<GoodInfoBean> getmList() {
		return mList;
	}

	public void setmList(List<GoodInfoBean> mList) {
		this.mList = mList;
	}

	public void setBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public GoodInfoBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private Views vh = null;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			vh = new Views();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.goodonline_list_item2, null);

			vh = new Views();
			vh.smallhead = (ImageView) convertView.findViewById(R.id.good_icon);
			vh.title = (TextView) convertView.findViewById(R.id.good_name);
			vh.price = (TextView) convertView.findViewById(R.id.good_price);
			vh.goodNum = (TextView) convertView.findViewById(R.id.good_num);
			vh.totalVolume = (TextView) convertView.findViewById(R.id.good_total_volume);
			vh.good_checkbox = (CheckBox) convertView.findViewById(R.id.good_checkbox);
			vh.frontView = (View) convertView.findViewById(R.id.front);
			vh.backBtn2 = (Button) convertView.findViewById(R.id.example_row_b_action_2);
			vh.backBtn3 = (Button) convertView.findViewById(R.id.example_row_b_action_3);

			convertView.setTag(vh);
		} else {
			vh = (Views) convertView.getTag();
		}
		GoodInfoBean item = getItem(position);
		if (!AppUtil.isNull(item)) {
			// 图片
			vh.smallhead.setTag(item.getSmallhead());
			vh.smallhead.setImageResource(R.drawable.failed_to_load);
			mImageCacheManager.loadBitmaps(vh.smallhead, item.getSmallhead(), imageTag, "");

			vh.title.setText(AppUtil.CS(item.getName()));
			vh.goodNum.setText(AppUtil.CS(item.getCode()));
			vh.price.setText(AppUtil.CS(item.getPrice()) + "元");
			vh.totalVolume.setText(AppUtil.CS(item.getSalesvolume()));
			if (!isBatch)
				vh.good_checkbox.setVisibility(View.GONE);
			else {
				vh.good_checkbox.setVisibility(View.VISIBLE);
				if (item.isChecked())
					vh.good_checkbox.setChecked(true);
				else
					vh.good_checkbox.setChecked(false);
				vh.good_checkbox.setTag(R.id.cancel, position);
				vh.good_checkbox.setOnClickListener(onCheckedChange);
			}

			// vh.frontView.setTag(R.id.bottom_bar, item.getId());
			// vh.backBtn3.setTag(R.id.bottom_bar, item.getId());
			// vh.backBtn3.setTag(R.id.about_us, position);
			// vh.backBtn3.setOnClickListener(cancleCollect);
			//
			// vh.backBtn2.setTag(R.id.bottom_bar, item.getId());
			// vh.backBtn2.setOnClickListener(toEdit);
		}
		return convertView;
	}

	// private OnClickListener toEdit = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// Long id = (Long) v.getTag(R.id.bottom_bar);
	// Intent intent = new Intent(mContext, ActivityGoodEdit.class);
	// intent.putExtra("id", id);
	// mContext.startActivity(intent);
	//
	// }
	// };
	//
	// private OnClickListener cancleCollect = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Long id = (Long) v.getTag(R.id.bottom_bar);
	// Integer position = (Integer) v.getTag(R.id.about_us);
	// DataCener.getInstance().getDataService().GoodStatus(id, Constants.online,
	// position, imageTag);
	// }
	// };

	private OnClickListener onCheckedChange = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag(R.id.cancel);
			if (mList.get(position).isChecked())
				mList.get(position).setChecked(false);
			else
				mList.get(position).setChecked(true);
		}
	};

	private class Views {

		private ImageView smallhead;
		private TextView title;
		private TextView price;
		private TextView totalVolume;
		private TextView goodNum;
		private CheckBox good_checkbox;
		private Button backBtn2;
		private Button backBtn3;
		private View frontView;
	}

}

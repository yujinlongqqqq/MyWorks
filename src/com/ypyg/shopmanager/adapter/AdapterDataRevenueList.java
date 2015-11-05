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
import android.widget.ImageView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.data.ActivityDataManager;
import com.ypyg.shopmanager.activity.good.ActivityGoodDetail;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.bean.infobean.DataRevenueInfoBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.ImageCacheManager;

public class AdapterDataRevenueList extends BaseAdapter {

	private Context mContext;
	private List<GoodInfoBean> mList;

	// private TutorTypeUtils mUtils;
	private View dataTopView = null;

	private ImageCacheManager mImageCacheManager = null;
	private String imageTag = null;
	private boolean isBatch = false;// 是否显示批量上下架
	// private HistogramView mHistogramView = null;
	private TextView data_total_revenue = null;
	private TextView data_today_revenue = null;
	private ArrayList<String> weekData = null;

	public AdapterDataRevenueList(Context context, String imageTag, List<GoodInfoBean> list) {
		mContext = context;
		mList = list;
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
		this.imageTag = imageTag;
		// mUtils = new TutorTypeUtils(mContext);
		testData();
	}

	private void testData() {
		mList = new ArrayList<GoodInfoBean>();
		for (int i = 0; i < 10; i++) {
			GoodInfoBean bean = new GoodInfoBean();
			bean.setId(1002l);
			bean.setName("珀莱雅保湿水");
			bean.setCode("A010");
			bean.setPrice("307.00");
			bean.setSalesvolume("400");
			mList.add(bean);
		}
	}

	public void setDataTopView(View topView) {
		dataTopView = topView;
		// mHistogramView = (HistogramView)
		// dataTopView.findViewById(R.id.histogramView);
		data_total_revenue = (TextView) dataTopView.findViewById(R.id.data_total_revenue);
		data_today_revenue = (TextView) dataTopView.findViewById(R.id.data_today_revenue);
		// 树状图test 之后记得删掉该代码
		// mHistogramView.start(ActivityDataManager.flag1);
		ActivityDataManager.flag1 = 3;
	}

	public void setData(DataRevenueInfoBean bean) {
		if (AppUtil.isNull(bean))
			return;

		if (!AppUtil.isNull(bean.getTotalrevenue()))
			data_total_revenue.setText(bean.getTotalrevenue());
		if (!AppUtil.isNull(bean.getNow_all()))
			data_today_revenue.setText(bean.getNow_all());
		// if (!AppUtil.isNull(bean.getWeekrevenue())) {
		// weekData = (ArrayList<String>) AppUtil.getStringArray(
		// bean.getWeekrevenue(), ",");
		// try {
		// if (!AppUtil.isNull(weekData.get(6))) {
		// data_today_revenue.setText(weekData.get(6));
		// }
		// } catch (Exception e) {
		// }
		// }
		// mHistogramView.start(ActivityDataManager.flag1);
		ActivityDataManager.flag1 = 3;
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
		return mList.size() + 1;
	}

	@Override
	public GoodInfoBean getItem(int position) {
		return mList.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private Views vh = null;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (position == 0)
			return dataTopView;
		if (null == convertView || convertView.equals(dataTopView)) {
			vh = new Views();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.goodonline_list_item2, null);

			vh = new Views();
			vh.smallhead = (ImageView) convertView.findViewById(R.id.good_icon);
			vh.title = (TextView) convertView.findViewById(R.id.good_name);
			vh.price = (TextView) convertView.findViewById(R.id.good_price);
			vh.goodNum = (TextView) convertView.findViewById(R.id.good_num);
			vh.totalVolume = (TextView) convertView.findViewById(R.id.good_total_volume);

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

			convertView.setTag(R.id.bottom_bar, position);
		}
		convertView.setOnClickListener(toDetailL);
		return convertView;
	}

	private OnClickListener toDetailL = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (int) v.getTag(R.id.bottom_bar);
			Intent intent = new Intent(mContext, ActivityGoodDetail.class);
			intent.putExtra("bean", mList.get(position - 1));
			mContext.startActivity(intent);
		}
	};

	private OnClickListener cancleCollect = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Long id = (Long) v.getTag(R.id.bottom_bar);
			Integer position = (Integer) v.getTag(R.id.about_us);
			DataCener.getInstance().getDataService().GoodStatus(id, Constants.offline, position, imageTag);
		}
	};

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
	}

}

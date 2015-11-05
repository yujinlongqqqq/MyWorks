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
import com.ypyg.shopmanager.activity.data.ActivityDataManager;
import com.ypyg.shopmanager.activity.good.ActivityGoodDetail;
import com.ypyg.shopmanager.bean.GoodInfoBean;
import com.ypyg.shopmanager.bean.infobean.DataRevenueInfoBean;
import com.ypyg.shopmanager.bean.infobean.DataVisitorInfoBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.view.chart.HistogramView;

public class AdapterDataVisitorList extends BaseAdapter {

	private Context mContext;
	private List<GoodInfoBean> mList;

	// private TutorTypeUtils mUtils;
	private View dataTopView = null;

	private ImageCacheManager mImageCacheManager = null;
	private String imageTag = null;
	private boolean isBatch = false;// 是否显示批量上下架
	private HistogramView mHistogramView = null;
	private TextView data_pv = null;
	private TextView data_uv = null;
//	private View my_revenue_detail = null;
	private ArrayList<String> weekData = null;

	public AdapterDataVisitorList(Context context, String imageTag,
			List<GoodInfoBean> list) {
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
		mHistogramView = (HistogramView) dataTopView
				.findViewById(R.id.histogramView);
		data_pv = (TextView) dataTopView.findViewById(R.id.data_pv);
		data_uv = (TextView) dataTopView.findViewById(R.id.data_uv);
//		my_revenue_detail = (View) dataTopView
//				.findViewById(R.id.my_revenue_detail);
		// 树状图test 之后记得删掉该代码
		mHistogramView.start(ActivityDataManager.flag3);
		ActivityDataManager.flag3 = 3;
	}

	public void setData(DataVisitorInfoBean bean) {
		if (AppUtil.isNull(bean))
			return;

		if (!AppUtil.isNull(bean.getPv()))
			data_pv.setText(bean.getPv());
		if (!AppUtil.isNull(bean.getUv())) {
			data_uv.setText(bean.getUv());
		}
		mHistogramView.start(ActivityDataManager.flag3);
		ActivityDataManager.flag3 = 3;
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.good_list_item, null);

			vh = new Views();
			vh.smallhead = (ImageView) convertView.findViewById(R.id.good_icon);
			vh.title = (TextView) convertView.findViewById(R.id.good_name);
			vh.price = (TextView) convertView.findViewById(R.id.good_price);
			vh.goodNum = (TextView) convertView.findViewById(R.id.good_num);
			vh.totalVolume = (TextView) convertView
					.findViewById(R.id.good_total_volume);

			convertView.setTag(vh);
		} else {
			vh = (Views) convertView.getTag();
		}
		GoodInfoBean item = getItem(position);
		if (!AppUtil.isNull(item)) {
			// 图片
			// vh.smallhead.setTag(item.getSeek_img());
			// vh.smallhead.setImageResource(R.drawable.failed_to_load);
			// mImageCacheManager.loadBitmaps(vh.smallhead, item.getSeek_img(),
			// imageTag, Constants.thumb20000);

			vh.title.setText(AppUtil.CS(item.getName()) + "  " + position);
			vh.goodNum.setText(AppUtil.CS(item.getCode()));
			vh.price.setText(AppUtil.CS(item.getPrice()));
			vh.totalVolume.setText(AppUtil.CS(item.getSalesvolume()));

			convertView.setTag(R.id.bottom_bar, item.getId());
		}
		convertView.setOnClickListener(toDetailL);
		return convertView;
	}

	private OnClickListener toDetailL = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Long id = (Long) v.getTag(R.id.both);
			Intent intent = new Intent(mContext, ActivityGoodDetail.class);
			intent.putExtra("id", id);
			mContext.startActivity(intent);
		}
	};

	private OnClickListener cancleCollect = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Long id = (Long) v.getTag(R.id.bottom_bar);
			Integer position = (Integer) v.getTag(R.id.about_us);
			DataCener.getInstance().getDataService()
					.GoodStatus(id, Constants.offline, position, imageTag);
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

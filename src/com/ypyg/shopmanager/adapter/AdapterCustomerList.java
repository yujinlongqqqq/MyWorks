package com.ypyg.shopmanager.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.customer.ActivityConsumptionRecord;
import com.ypyg.shopmanager.bean.MemberInfoBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.view.roundedimageview.RoundAngleImageView;

public class AdapterCustomerList extends BaseAdapter {

	private Context mContext;
	private List<MemberInfoBean> mList;

	// private TutorTypeUtils mUtils;

	private ImageCacheManager mImageCacheManager = null;
	private String imageTag = null;

	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}

	public AdapterCustomerList(Context context, String imageTag, List<MemberInfoBean> list) {
		mContext = context;
		mList = list;
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
		this.imageTag = imageTag;
		// mUtils = new TutorTypeUtils(mContext);
		// testData();

	}

	// private void testData() {
	// mList = new ArrayList<MemberInfoBean>();
	// for (int i = 0; i < 10; i++) {
	// MemberInfoBean bean = new MemberInfoBean();
	// bean.setNickname("加肥猫");
	// bean.setPhone("18768104716");
	// bean.setScore("1200");
	// bean.setOrdernum("23");
	// mList.add(bean);
	// }
	// }

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public MemberInfoBean getItem(int position) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.customer_list_item, null);

			vh = new Views();
			vh.smallhead = (RoundAngleImageView) convertView.findViewById(R.id.small_head);
			vh.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
			vh.customer_phone = (TextView) convertView.findViewById(R.id.customer_phone);
			vh.customer_score = (TextView) convertView.findViewById(R.id.customer_score);
			vh.customer_order_num = (TextView) convertView.findViewById(R.id.customer_order_num);
			vh.frontView = (View) convertView.findViewById(R.id.front);
			vh.backBtn2 = (Button) convertView.findViewById(R.id.example_row_b_action_2);
			vh.backBtn3 = (Button) convertView.findViewById(R.id.example_row_b_action_3);

			convertView.setTag(vh);
		} else {
			vh = (Views) convertView.getTag();
		}
		MemberInfoBean item = getItem(position);
		if (!AppUtil.isNull(item)) {
			// 图片
			vh.smallhead.setTag(item.getSmallhead());
			vh.smallhead.setImageResource(R.drawable.failed_to_load);
			mImageCacheManager.loadBitmaps(vh.smallhead, item.getSmallhead(), imageTag, "");

			vh.customer_name.setText(AppUtil.CS(item.getNickname()));
			vh.customer_phone.setText(AppUtil.CS(item.getPhone()));
			vh.customer_score.setText(AppUtil.CS(item.getScore()));
			// vh.customer_order_num.setText(AppUtil.CS(item.getOrdernum()));

			// vh.frontView.setTag(R.id.bottom_bar, item.getSeek_id());
			// vh.backBtn3.setTag(R.id.bottom_bar, item.getSeek_id());
			// vh.backBtn3.setTag(R.id.child_class, position);
			// vh.backBtn3.setOnClickListener(cancleCollect);
			// convertView.setTag(R.id.both, item.getSeek_id());
		}
		vh.frontView.setOnClickListener(listener);
		return convertView;
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Long id = (Long) v.getTag(R.id.both);
			Intent intent = new Intent(mContext, ActivityConsumptionRecord.class);
			intent.putExtra("id", id);
			mContext.startActivity(intent);

		}
	};

	private OnClickListener cancleCollect = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Long id = (Long) v.getTag(R.id.bottom_bar);
			Integer position = (Integer) v.getTag(R.id.back);
			// DataCener.getInstance().getDataService()
			// .RemoveCollect(id, position);
		}
	};

	private class Views {
		private RoundAngleImageView smallhead;
		private TextView customer_name;
		private TextView customer_phone;
		private TextView customer_score;
		private TextView customer_order_num;
		private Button backBtn2;
		private Button backBtn3;
		private View frontView;
	}

}

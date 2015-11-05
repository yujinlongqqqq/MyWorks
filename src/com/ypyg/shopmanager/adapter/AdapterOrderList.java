package com.ypyg.shopmanager.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.order.ActivityOrderList2;
import com.ypyg.shopmanager.activity.order.ActivityOrderManager;
import com.ypyg.shopmanager.bean.OrderBean;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.ImageCacheManager;

public class AdapterOrderList extends BaseAdapter {
	private Context mContext;
	private List<OrderBean> mList;
	private DataCener mDataCener = null;
	private String imageTag = null;
	private boolean delete = false;

	private String UserName="";
	// 订单状态
	private Map<String, String> STATUS = new HashMap<String, String>();

	private SimpleDateFormat mDateFormat = null;
	/**
	 * 图片缓存管理类
	 */
	private ImageCacheManager mImageCacheManager = null;

	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}

	public AdapterOrderList(Context context, String imageTag, List<OrderBean> list) {
		mContext = context;
		mList = list;
		this.imageTag = imageTag;
		mDataCener = DataCener.getInstance();
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
		mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// testData();
		try {
			UserName = DataCener.getInstance().mBasicUserInfoBean.getRealname();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		STATUS.put("11", "买家待付款");
		STATUS.put("20", "买家已付款，待卖家发货");
		STATUS.put("30", "买家已发货");
		STATUS.put("40", "交易完成");
		STATUS.put("0", "交易取消");
		STATUS.put("1", "交易取消（过期取消）");
		STATUS.put("31", "退款中");
		STATUS.put("12", "待取货");
		STATUS.put("41", "退款完成");
	}

	// private void testData() {
	// mList = new ArrayList<OrderBean>();
	// for (int i = 0; i < 10; i++) {
	// OrderBean bean = new OrderBean();
	// // bean.setName("珀莱雅保湿水");
	// // bean.setGoodnum("A010");
	// // bean.setPrice("307.00");
	// // bean.setSalevolume("400");
	// mList.add(bean);
	// }
	// }

	public void setDelete(boolean delete) {
		this.delete = delete;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public OrderBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private ViewHoder vh = null;
	private long time = 0;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView || ((ViewHoder) convertView.getTag()).needInflate) {
			vh = new ViewHoder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_order_list_item, null);
			vh.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
			vh.order_state = (TextView) convertView.findViewById(R.id.order_state);
			vh.smallhead = (ImageView) convertView.findViewById(R.id.smallhead);
			vh.good_name = (TextView) convertView.findViewById(R.id.good_name);
			vh.good_num = (TextView) convertView.findViewById(R.id.good_num);
			vh.order_time = (TextView) convertView.findViewById(R.id.order_time);
			vh.order_price = (TextView) convertView.findViewById(R.id.order_price);
			vh.order_delete = (TextView) convertView.findViewById(R.id.order_delete);

			convertView.setTag(vh);
			// // 过场动画
			// if (position == 0) {
			// time = System.currentTimeMillis();
			// }
			// if (1000 > System.currentTimeMillis() - time) {
			// Animation anim = AnimationUtils.loadAnimation(mContext,
			// R.anim.slide_right_in);
			// anim.setStartOffset(200 * position);
			// convertView.startAnimation(anim);// 加入动画
			// }
		} else {
			vh = (ViewHoder) convertView.getTag();
		}
		OrderBean item = getItem(position);
		if (!AppUtil.isNull(item)) {
			vh.customer_name.setText(UserName);
			vh.order_state.setText(STATUS.get(item.getOrder_state()));// 交易状态

			// 图片
			vh.smallhead.setTag(item.getOrder_img());
			vh.smallhead.setImageResource(R.drawable.failed_to_load);
			mImageCacheManager.loadBitmaps(vh.smallhead, item.getOrder_img(), imageTag, "");

			vh.good_name.setText("订单号:" + item.getOrder_number());
			vh.good_num.setText("货号:" + item.getOrder_payment());
			vh.order_time.setText(formatDateTime(item.getOrder_time()));
			vh.order_price.setText("￥" + item.getOrder_price());
		}

		vh.order_delete.setTag(R.id.rl_tab, position);
		vh.order_delete.setTag(convertView);
		vh.order_delete.setOnClickListener(deleteListener);

		return convertView;
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Long id = (Long) v.getTag(R.id.action_settings);
			// Long tutorid = (Long) v.getTag(R.id.activity_partaddress);
			// Intent intent = new Intent(mContext,
			// ActivityTProductDetail3.class);
			// intent.putExtra("tutorid", tutorid);
			// mContext.startActivity(intent);
		}
	};
	private OnClickListener deleteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag(R.id.rl_tab);
			if (position >= mList.size()) {
				return;
			}
			deleteCell(v, position);// 执行删除动画
			if (mContext instanceof ActivityOrderManager) {
				DataCener.getInstance().getDataService().DeleteOrder(imageTag, mList.get(position).getOrder_id(), mList.get(position));
			}
		}
	};

	private void deleteCell(final View v, final int index) {
		final View convertview = (View) v.getTag();

		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				mList.remove(index);
				ViewHoder vh = (ViewHoder) convertview.getTag();
				vh.needInflate = true;
				AdapterOrderList.this.notifyDataSetChanged();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		};

		collapse(convertview, al);
	}

	private void collapse(final View v, AnimationListener al) {
		final int initialHeight = v.getMeasuredHeight();

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		if (al != null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(200);
		v.startAnimation(anim);
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 * @return
	 */
	private String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}

	private class ViewHoder {
		private TextView customer_name = null;
		private TextView order_state = null;
		private ImageView smallhead = null;
		private TextView good_name = null;
		private TextView good_num = null;
		private TextView order_time = null;
		private TextView order_price = null;
		private TextView order_delete = null;

		boolean needInflate = false;

	}
}

package com.ypyg.shopmanager.bean.infobean;

import java.io.Serializable;
import java.util.List;

import com.ypyg.shopmanager.bean.GoodInfoBean;

public class DataRevenueInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> weekrevenue;// 七日数据以“星期一，55”分隔
	private String totalrevenue;// 总营收
	private String now_all;// 今日营收
	private List<GoodInfoBean> list;// 七日销量排行榜

	public List<String> getWeekrevenue() {
		return weekrevenue;
	}

	public void setWeekrevenue(List<String> weekrevenue) {
		this.weekrevenue = weekrevenue;
	}

	public String getTotalrevenue() {
		return totalrevenue;
	}

	public void setTotalrevenue(String totalrevenue) {
		this.totalrevenue = totalrevenue;
	}

	public String getNow_all() {
		return now_all;
	}

	public void setNow_all(String now_all) {
		this.now_all = now_all;
	}

	public List<GoodInfoBean> getList() {
		return list;
	}

	public void setList(List<GoodInfoBean> list) {
		this.list = list;
	}

}

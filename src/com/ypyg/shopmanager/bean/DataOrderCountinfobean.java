package com.ypyg.shopmanager.bean;

import java.io.Serializable;
import java.util.List;

public class DataOrderCountinfobean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> weekorder;// 七日数据以“星期一，55”分隔
	private String paymentorder;// 付款订单
	private String shipmentorder;// 发货订单
	private List<GoodInfoBean> list;// 七日销量排行榜

	public List<String> getWeekorder() {
		return weekorder;
	}

	public void setWeekorder(List<String> weekorder) {
		this.weekorder = weekorder;
	}

	public String getPaymentorder() {
		return paymentorder;
	}

	public void setPaymentorder(String paymentorder) {
		this.paymentorder = paymentorder;
	}

	public String getShipmentorder() {
		return shipmentorder;
	}

	public void setShipmentorder(String shipmentorder) {
		this.shipmentorder = shipmentorder;
	}

	public List<GoodInfoBean> getList() {
		return list;
	}

	public void setList(List<GoodInfoBean> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "DataOrderCountinfobean [weekorder=" + weekorder + ", paymentorder=" + paymentorder + ", shipmentorder=" + shipmentorder + ", list=" + list + "]";
	}

	
}

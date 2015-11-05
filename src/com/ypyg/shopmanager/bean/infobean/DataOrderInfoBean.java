package com.ypyg.shopmanager.bean.infobean;

import java.io.Serializable;
import java.util.List;

import com.ypyg.shopmanager.bean.GoodInfoBean;

public class DataOrderInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String weekorder;
	private String paymentorder;
	private String shipmentorder;
	private List<GoodInfoBean> list;

	public String getWeekorder() {
		return weekorder;
	}

	public void setWeekorder(String weekorder) {
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

}

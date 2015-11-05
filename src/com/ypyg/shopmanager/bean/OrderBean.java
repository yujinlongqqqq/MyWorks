package com.ypyg.shopmanager.bean;

import java.io.Serializable;

public class OrderBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String order_state;// 订单状态
	private String order_id;// 订单id
	private String order_img;// 订单图片
	private String order_payment;// 支付单号
	private long order_number;// 订单号
	private long order_time;// 订单生成时间
	private double order_price;// 订单价格

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_state() {
		return order_state;
	}

	public void setOrder_state(String order_state) {
		this.order_state = order_state;
	}

	public String getOrder_img() {
		return order_img;
	}

	public void setOrder_img(String order_img) {
		this.order_img = order_img;
	}

	public String getOrder_payment() {
		return order_payment;
	}

	public void setOrder_payment(String order_payment) {
		this.order_payment = order_payment;
	}

	public long getOrder_number() {
		return order_number;
	}

	public void setOrder_number(long order_number) {
		this.order_number = order_number;
	}

	public long getOrder_time() {
		return order_time;
	}

	public void setOrder_time(long order_time) {
		this.order_time = order_time;
	}

	public double getOrder_price() {
		return order_price;
	}

	public void setOrder_price(double order_price) {
		this.order_price = order_price;
	}

	// private ArrayList<GoodInfoBean> goodlist;

}

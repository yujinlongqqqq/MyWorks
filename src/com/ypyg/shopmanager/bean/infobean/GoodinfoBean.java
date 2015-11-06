package com.ypyg.shopmanager.bean.infobean;

import java.io.Serializable;

public class GoodinfoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
	private String price;
	private String salesvolume;// 基础销量
	private String smallhead;// 商品图片
	private String inventory;// 库存
	private String goodcode;// 货号
	private String goodclass;// 分类，大类;小类
	private Integer inventory_count;// 库存计数

	public GoodinfoBean() {
		super();
	}

	public GoodinfoBean(String title, String description, String price, String salesvolume, String smallhead, String inventory, String goodcode, String goodclass, Integer inventory_count) {
		super();
		this.title = title;
		this.description = description;
		this.price = price;
		this.salesvolume = salesvolume;
		this.smallhead = smallhead;
		this.inventory = inventory;
		this.goodcode = goodcode;
		this.goodclass = goodclass;
		this.inventory_count = inventory_count;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSalesvolume() {
		return salesvolume;
	}

	public void setSalesvolume(String salesvolume) {
		this.salesvolume = salesvolume;
	}

	public String getSmallhead() {
		return smallhead;
	}

	public void setSmallhead(String smallhead) {
		this.smallhead = smallhead;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getGoodcode() {
		return goodcode;
	}

	public void setGoodcode(String goodcode) {
		this.goodcode = goodcode;
	}

	public String getGoodclass() {
		return goodclass;
	}

	public void setGoodclass(String goodclass) {
		this.goodclass = goodclass;
	}

	public Integer getInventory_count() {
		return inventory_count;
	}

	public void setInventory_count(Integer inventory_count) {
		this.inventory_count = inventory_count;
	}

}

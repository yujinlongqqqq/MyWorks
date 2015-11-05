package com.ypyg.shopmanager.bean;

import java.io.Serializable;

public class GoodInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;//日期分组
	private String smallhead;// 商品缩略图
	private String name;// 商品名字
	private String code;// 商品货号
	private long id;// 商品id
	private String price;// 价格
	private String salesvolume;// 销量
	private String gooddetail;// 商品明细
	private boolean checked = false;// 商品是否已选中

	public GoodInfoBean() {
		super();
	}

	public GoodInfoBean(String title) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSmallhead() {
		return smallhead;
	}

	public void setSmallhead(String smallhead) {
		this.smallhead = smallhead;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getGooddetail() {
		return gooddetail;
	}

	public void setGooddetail(String gooddetail) {
		this.gooddetail = gooddetail;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return "GoodInfoBean [smallhead=" + smallhead + ", name=" + name + ", code=" + code + ", id=" + id + ", price=" + price + ", salesvolume=" + salesvolume + ", gooddetail=" + gooddetail + ", checked=" + checked + "]";
	}

}

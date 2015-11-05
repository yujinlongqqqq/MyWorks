package com.ypyg.shopmanager.bean;

import java.io.Serializable;

/**
 * 商品分类Bean
 * 
 * @author 小俞 2015-11-6下午3:21:48
 */
public class GoodSortBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "GoodSortBean [id=" + id + ", name=" + name + "]";
	}

}

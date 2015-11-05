package com.ypyg.shopmanager.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类Bean
 * 
 * @author 小俞 2015-11-6下午3:21:48
 */
public class GoodParentsSortBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private List<GoodSortBean> data;

	public List<GoodSortBean> getData() {
		return data;
	}

	public void setData(List<GoodSortBean> data) {
		this.data = data;
	}

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
		return "GoodParentsSortBean [id=" + id + ", name=" + name + ", data=" + data + "]";
	}

}

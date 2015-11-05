package com.ypyg.shopmanager.bean.infobean;

import java.io.Serializable;
import java.util.ArrayList;

import com.ypyg.shopmanager.bean.GoodInfoBean;

public class DataVisitorInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pv;
	private String uv;
	private ArrayList<GoodInfoBean> list;

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public ArrayList<GoodInfoBean> getList() {
		return list;
	}

	public void setList(ArrayList<GoodInfoBean> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "DataVisitorInfoBean [pv=" + pv + ", uv=" + uv + ", list=" + list + "]";
	}

}

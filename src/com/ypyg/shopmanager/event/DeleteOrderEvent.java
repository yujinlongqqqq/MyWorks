package com.ypyg.shopmanager.event;

public class DeleteOrderEvent extends BaseEvent {
	private String catid;
	private Object item;
	
	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

}
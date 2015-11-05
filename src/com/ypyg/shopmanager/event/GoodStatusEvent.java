package com.ypyg.shopmanager.event;

import com.ypyg.shopmanager.event.BaseEvent;

public class GoodStatusEvent extends BaseEvent {
	private int position;
	private String tag;
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
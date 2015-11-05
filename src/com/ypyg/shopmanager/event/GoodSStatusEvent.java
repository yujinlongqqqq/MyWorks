package com.ypyg.shopmanager.event;

import java.util.ArrayList;

public class GoodSStatusEvent extends BaseEvent {
	private ArrayList<Integer> position;
	private String tag;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<Integer> getPosition() {
		return position;
	}

	public void setPosition(ArrayList<Integer> position) {
		this.position = position;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
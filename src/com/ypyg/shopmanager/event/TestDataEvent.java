package com.ypyg.shopmanager.event;

public class TestDataEvent extends BaseEvent {
	private String data;

	public TestDataEvent(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}

package com.ypyg.shopmanager.event;


public class RemoveCollectEvent extends BaseEvent {

	private Integer position;
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
package com.ypyg.shopmanager.event;

public class BaseEvent implements IEvent {
	private int code;
	private String msg;// 相关信息
	private String status;// 状态
	private Long offset;
	private Long count;

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public void setCode(int aCode) {
		code = aCode;
	}

	@Override
	public void setEventEntity(Object aObj) {

	}

	@Override
	public Object getEventEntity() {
		return null;
	}

	@Override
	public Long getOffset() {
		return offset;
	}
	@Override
	public void setOffset(Long offset) {
		this.offset = offset;
	}
	@Override
	public Long getCount() {
		return count;
	}
	@Override
	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public void setMsg(String msg) {
		this.msg = msg;
	}

}

package com.ypyg.shopmanager.event;

public interface IEvent {

	// 读取事件错误代码
	public int getCode();

	// 设置事件错误代码
	public void setCode(int aCode);

	public String getStatus();

	public void setStatus(String status);

	// 设置事件对象数据
	public void setEventEntity(Object aObj);

	// 获取事件对象数据
	public Object getEventEntity();

	// list count
	public void setCount(Long count);

	public Long getCount();

	// list offset
	public void setOffset(Long offset);

	public Long getOffset();

	public String getMsg();

	public void setMsg(String msg);
}

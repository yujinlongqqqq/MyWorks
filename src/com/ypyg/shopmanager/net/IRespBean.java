package com.ypyg.shopmanager.net;

public interface IRespBean {
	// ��ȡBean ��ʵ�����
	public Object getBeanEntity();

	// ��ȡ���ص�Code
	public int getCode();

	public String getErrorcontext();

	// ���list offset
	public Long getOffset();

	// ���list count
	public Long getCount();

	public String getMsg();

	public String getStatus();
}

package com.ypyg.shopmanager.bean;

import java.io.Serializable;

import com.ypyg.shopmanager.net.IRespBean;

public class BaseRespBean implements Serializable, IRespBean {
	private static final long serialVersionUID = 1L;
	private int code;// 操作错误代码，200 代码OK
	private String operation;// 客户端请求的功能号
	private String errorcontext;// 错误内容
	private String status;// 状态
	private String msg;// 相关信息

	@Override
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getErrorcontext() {
		return errorcontext;
	}

	public void setErrorcontext(String errorcontext) {
		this.errorcontext = errorcontext;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public int getCode() {
		return code;
	}

	public void setCode(int aCode) {
		code = aCode;
	}

	@Override
	public Object getBeanEntity() {
		return this;
	}

	@Override
	public Long getOffset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getCount() {
		// TODO Auto-generated method stub
		return null;
	}
}

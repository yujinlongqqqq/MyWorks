package com.ypyg.shopmanager.bean;

public class ImageUploadRespBean extends BaseRespBean {
	private static final long serialVersionUID = 1L;
	private String result;

	@Override
	public Object getBeanEntity() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}

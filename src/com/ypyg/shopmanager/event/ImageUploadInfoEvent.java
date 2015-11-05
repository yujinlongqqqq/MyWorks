package com.ypyg.shopmanager.event;

public class ImageUploadInfoEvent extends BaseEvent {
	private String mTag;
	private String url;

	@Override
	public void setEventEntity(Object aObj) {
		this.url = (String) aObj;
	}

	public String getmTag() {
		return mTag;
	}

	public void setmTag(String mTag) {
		this.mTag = mTag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

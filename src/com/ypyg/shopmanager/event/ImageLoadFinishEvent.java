package com.ypyg.shopmanager.event;

import java.util.List;

public class ImageLoadFinishEvent {
	private List<String> mUrls;
	private Object mTag;

	private String imageUrl = null;

	private String imageKey = null;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public List<String> getUrls() {
		return mUrls;
	}

	public void setUrls(List<String> aUrls) {
		mUrls = aUrls;
	}

	public Object getTag() {
		return mTag;
	}

	public void setTag(Object aTag) {
		mTag = aTag;
	}
}

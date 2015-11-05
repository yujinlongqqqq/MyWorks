package com.ypyg.shopmanager.resp;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.net.IReq;
import com.ypyg.shopmanager.net.IResp;

public class ImageDownloadResp implements IResp {
	private final String mKey;
	private final ImageCache mCache;
	private Bitmap mBitMap = null;

	public ImageDownloadResp(String imageUrl, ImageCache aCache) {
		mKey = AppUtil.getStringMD5(imageUrl);
		mCache = aCache;
	}

	@Override
	public void onResponse(IReq aReq, InputStream aInStream) throws IllegalStateException, IOException {
		mBitMap = BitmapFactory.decodeStream(aInStream);
		if (null != mBitMap)
			mCache.put(mKey, mBitMap);
	}

	@Override
	public Bitmap getRespObject() {
		return mBitMap;
	}

	@Override
	public Class<?> getRespClass() {
		return null;
	}

}

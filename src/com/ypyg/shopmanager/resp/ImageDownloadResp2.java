package com.ypyg.shopmanager.resp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;

import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.net.IReq;
import com.ypyg.shopmanager.net.IResp;

public class ImageDownloadResp2 implements IResp {
	private final String mKey;
	private DiskLruCache mDiskLruCache;
	private Bitmap mBitMap = null;

	public ImageDownloadResp2(String sUrl, DiskLruCache mDiskLruCache,
			Object tag) {
		this.mDiskLruCache = mDiskLruCache;
		mKey = AppUtil.getStringMD5(sUrl);
	}

	@Override
	public void onResponse(IReq aReq, InputStream aInStream)
			throws IllegalStateException, IOException {
		BufferedOutputStream out = null;
		BufferedInputStream in = null;

			// 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
			DiskLruCache.Editor editor = mDiskLruCache.edit(mKey);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				try {
					in = new BufferedInputStream(aInStream, 8 * 1024);
					out = new BufferedOutputStream(outputStream, 8 * 1024);
					int b;
					while ((b = in.read()) != -1) {
						out.write(b);
					}
					editor.commit();
				} catch (final IOException e) {
					e.printStackTrace();
					editor.abort();
				} finally {
					try {
						if (out != null) {
							out.close();
						}
						if (in != null) {
							in.close();
						}
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			
			}

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

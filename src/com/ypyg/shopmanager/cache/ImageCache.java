package com.ypyg.shopmanager.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.ypyg.shopmanager.common.AppUtilLIB;

/**
 * 图标缓存管理类
 */
public final class ImageCache extends AbsCache<String, Bitmap> {
	// 单例模式
	private static ImageCache imageCache;

	private static final String CACHE_DEST_NAME = "image";

	private ImageCache(int threadPoolSize) {
		super(threadPoolSize, ReferenceType.SOFT);
	}

	private ImageCache(int initialCapacity, long cacheSaveInMinutes,
			long cacheSaveInMemory, int threadPoolSize, ReferenceType refType) {
		super(initialCapacity, cacheSaveInMinutes, cacheSaveInMemory,
				threadPoolSize, refType);

	}

	/**
	 * 获取图片缓存实例，如果图片缓存未创建，则创建它
	 */
	public static synchronized ImageCache getInstance(Context aContext) {
		final int threadPoolSize = 1;
		if (imageCache == null) {
			imageCache = new ImageCache(threadPoolSize);
			// sdcard可用
			if (AppUtilLIB.getExternalStorageDirectory() != null) {
				imageCache.isDiskCacheEnable(aContext, DISK_CACHE_SDCARD,
						CACHE_DEST_NAME);
			} else {
				imageCache.isDiskCacheEnable(aContext, DISK_CACHE_UPHONE,
						CACHE_DEST_NAME);
			}
		}
		return imageCache;
	}

	public static void setDiskEnable(Context context, boolean isSdcard) {
		if (imageCache == null) {
			return;
		}
		if (isSdcard) {
			imageCache.isDiskCacheEnable(context, DISK_CACHE_SDCARD,
					CACHE_DEST_NAME, false);
		} else {
			imageCache.isDiskCacheEnable(context, DISK_CACHE_UPHONE,
					CACHE_DEST_NAME, false);
		}
	}

	public void setDiskEnable(ImageCache cache, Context context,
			boolean isSdcard) {
		if (cache == null) {
			return;
		}
		if (isSdcard) {
			cache.isDiskCacheEnable(context, DISK_CACHE_SDCARD,
					CACHE_DEST_NAME, false);
		} else {
			cache.isDiskCacheEnable(context, DISK_CACHE_UPHONE,
					CACHE_DEST_NAME, false);
		}
	}

	/**
	 * 将图片写入缓存文件
	 */
	@Override
	protected void writeValueToDisk(File file, Bitmap bitmap) {
		if (bitmap != null && file != null) {
			BufferedOutputStream ostream = null;
			try {
				FileOutputStream fos = new FileOutputStream(file);
				ostream = new BufferedOutputStream(fos);
				// 写一个压缩版本的位图到指定的输出流。如果返回true，可重构的位图，BitmapFactory.decodeStream。
				if (!bitmap.compress(CompressFormat.PNG, 100, ostream)) {
					bitmap.compress(CompressFormat.JPEG, 100, ostream);
				}
				ostream.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				AppUtilLIB.closeOutputStream(ostream);
			}
		}
	}

	/**
	 * @param key
	 * @return
	 */
	@Override
	protected String getFileNameForKey(String key) {
		return key;
	}

	/**
	 * 从缓存的文件读取图片
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@Override
	protected Bitmap readValueFromDisk(File file) throws IOException {
		FileInputStream ins = null;
		Bitmap temp = null;
		try {
			ins = new FileInputStream(file);
			temp = BitmapFactory.decodeStream(ins, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			temp = null;
		} catch (Throwable t) {
			t.printStackTrace();
			temp = null;
		} finally {
			AppUtilLIB.closeInputStream(ins);
		}
		return temp;
	}
}

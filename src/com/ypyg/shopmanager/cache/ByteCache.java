package com.ypyg.shopmanager.cache;

import java.io.File;
import java.io.IOException;

import android.content.Context;

import com.ypyg.shopmanager.common.AppUtilLIB;

/**
 * 二进制文件缓存 ByteCache
 */
public class ByteCache extends AbsCache<String, byte[]> {

	// 单例模式
	private static ByteCache byteCache;

	// 缓存的子文件夹
	private static final String CACHE_DEST_NAME = "byte";

	public boolean mCacheEffictive = false;

	private ByteCache(int threadPoolSize) {
		super(10, 7 * 24 * 60, 30, 1, ReferenceType.SOFT);
	}

	public static synchronized ByteCache getInstance(Context aContext) {
		if (byteCache == null) {
			// 无用的参数，写死是1个
			byteCache = new ByteCache(2);
			// 初始化工作，新建文件夹等
			if (AppUtilLIB.getExternalStorageDirectory() != null) {
				byteCache.isDiskCacheEnable(aContext, DISK_CACHE_SDCARD,
						CACHE_DEST_NAME);
			} else {
				byteCache.isDiskCacheEnable(aContext, DISK_CACHE_UPHONE,
						CACHE_DEST_NAME);
			}
		}
		return byteCache;
	}

	/**
	 * 设置缓存是否可用
	 * 
	 * @param isSdcard
	 *            是否缓存在sdcard
	 */
	public static void setDiskEnable(Context context, boolean isSdcard) {
		if (byteCache == null) {
			return;
		}
		if (isSdcard) {
			byteCache.isDiskCacheEnable(context, DISK_CACHE_SDCARD,
					CACHE_DEST_NAME, false);
		} else {
			byteCache.isDiskCacheEnable(context, DISK_CACHE_UPHONE,
					CACHE_DEST_NAME, false);
		}
	}

	/**
	 * @param threadPoolSize
	 * @param refType
	 */
	private ByteCache(int threadPoolSize, ReferenceType refType) {
		super(threadPoolSize, refType);
	}

	/**
	 * @param key
	 * @return
	 */
	@Override
	public String getFileNameForKey(String key) {
		return key;
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@Override
	protected byte[] readValueFromDisk(File file) throws IOException {
		if (file == null) {
			return null;
		}
		return readByteFromDisk(file);
	}

	/**
	 * @param file
	 * @param value
	 */
	@Override
	protected void writeValueToDisk(File file, byte[] value) {
		if (value == null || value.length == 0) {
			return;
		}
		writeByteToDisk(file, value, 0, value.length);
	}

	public synchronized void updateCacheTime(String key) {
		File file = getFileForKey(key);
		if (file != null) {
			file.setLastModified(System.currentTimeMillis());
		}
	}

	public synchronized void deleteStartWith(String prefix) {
		File[] cachedFiles = new File(destCacheDir).listFiles();
		for (File f : cachedFiles) {
			String name = getFileNameForKey(f.getName());
			if (name.startsWith(getFileNameForKey(prefix))) {
				f.delete();
			}
		}
	}

}

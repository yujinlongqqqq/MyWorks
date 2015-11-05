package com.ypyg.shopmanager.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

import com.ypyg.shopmanager.common.AppUtilLIB;

public class ObjectCache extends AbsCache<String, Object> {
	// 单例模式
	private static ObjectCache objectCache;
	// 磁盘保存的目录
	private static final String CACHE_DEST_NAME = "object";
	// 默认的初始化大小
	private static final int DEFAULT_INIT_CAPACLITY = 100;
	// 默认的生存在内存中的时间（分钟）
	private static final int DEFAULT_LIVE_IN_MEMORY_MIN = 3;
	// 默认的生存在内存中的时间（秒）
	private static final int DEFAULT_LIVE_IN_MEMORY_SEC = 30;
	// 默认的线程池的大小
	private static final int DEFAULT_THREAD_POOL_SIZE = 3;

	private ObjectCache(int initialCapacity, long cacheSaveInMinutes,
			long cacheSaveInMemory, int threadPoolSize, ReferenceType refType) {
		super(initialCapacity, cacheSaveInMinutes, cacheSaveInMemory,
				threadPoolSize, refType);
	}

	/**
	 * 获取图片缓存实例，如果图片缓存未创建，则创建它
	 */
	public static synchronized ObjectCache getInstance(Context aContext) {
		if (objectCache == null) {
			objectCache = new ObjectCache(DEFAULT_INIT_CAPACLITY,
					DEFAULT_LIVE_IN_MEMORY_MIN, DEFAULT_LIVE_IN_MEMORY_SEC,
					DEFAULT_THREAD_POOL_SIZE, ReferenceType.SOFT);
			// sdcard可用
			if (AppUtilLIB.getExternalStorageDirectory() != null) {
				objectCache.isDiskCacheEnable(aContext, DISK_CACHE_SDCARD,
						CACHE_DEST_NAME);
			} else {
				objectCache.isDiskCacheEnable(aContext, DISK_CACHE_UPHONE,
						CACHE_DEST_NAME);
			}
		}
		return objectCache;
	}

	@Override
	protected String getFileNameForKey(String key) {
		return key;
	}

	@Override
	protected Object readValueFromDisk(File file) throws IOException {
		FileInputStream ins = null;
		Object obj = null;
		ObjectInputStream objIns = null;
		try {
			ins = new FileInputStream(file);
			objIns = new ObjectInputStream(ins);
			obj = objIns.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
		} catch (Throwable t) {
			t.printStackTrace();
			obj = null;
		} finally {
			AppUtilLIB.closeInputStream(objIns);
			AppUtilLIB.closeInputStream(ins);
		}
		return obj;
	}

	@Override
	protected void writeValueToDisk(File file, Object value) throws IOException {
		if (value != null && file != null) {
			ObjectOutputStream objOutStream = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				objOutStream = new ObjectOutputStream(fos);
				objOutStream.writeObject(value);
				objOutStream.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				AppUtilLIB.closeOutputStream(objOutStream);
				AppUtilLIB.closeOutputStream(fos);
			}
		}
	}

	public static void setDiskEnable(Context context, boolean isSdcard) {
		if (objectCache == null) {
			return;
		}
		if (isSdcard) {
			objectCache.isDiskCacheEnable(context, DISK_CACHE_SDCARD,
					CACHE_DEST_NAME, false);
		} else {
			objectCache.isDiskCacheEnable(context, DISK_CACHE_UPHONE,
					CACHE_DEST_NAME, false);
		}
	}

}

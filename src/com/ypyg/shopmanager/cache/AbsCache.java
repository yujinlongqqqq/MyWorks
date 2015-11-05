package com.ypyg.shopmanager.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.ypyg.shopmanager.common.AppUtilLIB;
import com.ypyg.shopmanager.common.Constants;

/**
 * 缓存管理基类 区分Map缓存与磁盘缓存，磁盘缓存是持久化在sdcard或phone上的
 */
public abstract class AbsCache<Key, Val> {

	private static final String TAG = "[AbstCache]";

	// 缓存的路径,持久化到文件目录的路径
	protected String destCacheDir;

	// 缓存文件在磁盘上存在的时间
	private long cacheSaveInMinutes;

	// 缓存保存在内存中的生存时间
	private long cacheSaveInMemory;

	// 磁盘缓存是否可用
	private boolean isDiskCacheEnabled;

	// map缓存，可调整并发的哈希表，所有操作都是线程安全的
	private ConcurrentMap<Key, Val> cache;

	/**
	 * 标识缓存至手机
	 */
	public static final int DISK_CACHE_UPHONE = 0;

	/**
	 * 标识缓存至SDCARD
	 */
	public static final int DISK_CACHE_SDCARD = 1;

	// 默认缓存至sdcard
	protected int whichDiskSave = DISK_CACHE_SDCARD;

	/**
	 * 获取当前缓存的位置，“手机”或“sdcard”
	 */
	public int getWhichDiskSave() {
		return whichDiskSave;
	}

	/**
	 * 构造器，默认缓存时间为7天
	 */
	public AbsCache(int threadPoolSize, ReferenceType refType) {
		this(25, 7 * 24 * 60, 60, threadPoolSize, refType);
	}

	/**
	 * STRONG 是 Java 的默认引用实现, 它会尽可能长时间的存活于 JVM 内， 当没有任何对象指向它时 GC 执行后将会被回收
	 * WEAK，是一个弱引用, 当所引用的对象在 JVM 内不再有强引用时, GC 后 weak reference 将会被自动回收 SOFT
	 * 与WEAK的特性基本一致， 最大的区别在于 SOFT 会尽可能长的保留引用直到 JVM 内存不足时才会被回收(虚拟机保证), 这一特性使得
	 * SOFT 非常适合缓存应用
	 */
	public static enum ReferenceType {
		STRONG, WEAK, SOFT
	}

	/**
	 * @param initialCapacity
	 *            初始容量
	 * @param cacheSaveInMinutes
	 *            缓存有效时间，以分为单位
	 * @param cacheSaveInMemory
	 *            在缓存中的生存时间，以秒为单位
	 * @param threadPoolSize
	 * @param ReferenceType
	 */
	public AbsCache(int initialCapacity, long cacheSaveInMinutes,
			long cacheSaveInMemory, int threadPoolSize, ReferenceType refType) {
		this.cacheSaveInMinutes = cacheSaveInMinutes;
		this.cacheSaveInMemory = cacheSaveInMemory;
		CacheBuilder<Object, Object> aBuilder = CacheBuilder.newBuilder();
		aBuilder.initialCapacity(initialCapacity);
		// 并发级别，该属性估计应用程序线程的最大数目，同时访问在同一时间的区域条目
		aBuilder.concurrencyLevel(threadPoolSize > 0 ? threadPoolSize : 3);
		aBuilder.expireAfterWrite(this.cacheSaveInMemory, TimeUnit.SECONDS);
		if (ReferenceType.SOFT.equals(refType))
			aBuilder.softValues();
		else if (refType.equals(ReferenceType.WEAK))
			aBuilder.weakValues();
		this.cache = aBuilder.build(new CacheLoader<Key, Val>() {
			@Override
			public Val load(Key key) throws Exception {
				return null;
			}
		}).asMap();
	}

	public long getCacheSaveInMinutes() {
		return cacheSaveInMinutes;
	}

	/**
	 * Enable caching to the phone's internal storage or SD card.
	 * 
	 * @param context
	 * @param storageDevice
	 * @param destDir
	 *            both of the start and the end won't '/'
	 * @return
	 */
	public boolean isDiskCacheEnable(Context context, int storageDevice,
			String destDir) {
		return isDiskCacheEnable(context, storageDevice, destDir, false);
	}

	/**
	 * 判断是否可以缓存至磁盘（sdcard、phone），顺便清理删除已过期的缓存 Enable caching to the phone's
	 * internal storage or SD card.
	 * 
	 * @param context
	 * @param storageDevice
	 * @param destDir
	 *            both of the start and the end won't '/'
	 * @param isSanitizeDisk
	 * @return
	 */
	public boolean isDiskCacheEnable(Context context, int storageDevice,
			String destDir, boolean isSanitizeDisk) {
		whichDiskSave = storageDevice;
		StringBuffer destBuf = null;
		if (storageDevice == DISK_CACHE_SDCARD
				&& AppUtilLIB.getExternalStorageDirectory() != null) {
			// sdcard available
			destBuf = new StringBuffer(Environment
					.getExternalStorageDirectory().getAbsolutePath());
			destBuf.append(Constants.getPrePath());
			destBuf.append("cache");
		} else {
			File internalCacheDir = context.getCacheDir();
			if (internalCacheDir == null) {
				return isDiskCacheEnabled = false;
			}
			destBuf = new StringBuffer(internalCacheDir.getAbsolutePath());
			destBuf.append("/cache");
		}
		destBuf.append(File.separator);
		if (destDir != null && destDir.trim().length() > 0) {
			destBuf.append(destDir);
			destBuf.append(File.separator);
		}

		this.destCacheDir = destBuf.toString();
		File outFile = new File(destCacheDir);
		if (!(isDiskCacheEnabled = outFile.exists())) {
			isDiskCacheEnabled = outFile.mkdirs();
		}

		if (!isDiskCacheEnabled) {
			Log.v(TAG, "Failed creating disk cache directory " + destCacheDir);
		} else {
			// sanitize disk cache
			if (isSanitizeDisk) {
				sanitizeDiskCache();
			}
		}

		return isDiskCacheEnabled;
	}

	/**
	 * 清理已过期的二进制文件缓存 sanitize disk cache that is overdue
	 */
	public void sanitizeDiskCache() {
		File[] cachedFiles = new File(destCacheDir).listFiles();
		if (cachedFiles == null) {
			return;
		}
		for (File f : cachedFiles) {
			long lastModified = f.lastModified();
			Date now = new Date();
			long ageInMinutes = (now.getTime() - lastModified) / (1000 * 60);

			if (ageInMinutes >= cacheSaveInMinutes) {
				f.delete();
			}
		}
	}

	/**
	 * 获取缓存路径
	 */
	public String getDiskCacheDirectory() {
		return destCacheDir;
	}

	/**
	 * 子类实现 ，根据key获取缓存持久化的文件名
	 */
	protected abstract String getFileNameForKey(Key key);

	/**
	 * 子类实现 ，根据文件对象获取缓存值
	 */
	protected abstract Val readValueFromDisk(File file) throws IOException;

	/**
	 * 读取文件对象
	 */
	protected byte[] readByteFromDisk(File file) throws IOException {
		if (file == null || file.length() == 0) {
			return null;
		}
		BufferedInputStream istream = new BufferedInputStream(
				new FileInputStream(file));
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			istream.close();
			throw new IOException("Cannot read files larger than "
					+ Integer.MAX_VALUE + " bytes");
		}

		int imageDataLength = (int) fileSize;

		byte[] imageData = new byte[imageDataLength];
		istream.read(imageData, 0, imageDataLength);
		istream.close();
		return imageData;
	}

	/**
	 * 子类实现 ，将缓存持久化至文件
	 */
	protected abstract void writeValueToDisk(File file, Val value)
			throws IOException;

	/**
	 * 依当前的缓存目标(sdcard\phone)，将二进制数据写入文件
	 */
	protected void writeByteToDisk(File file, byte[] data, int offset,
			int length) {
		if (data == null || data.length == 0) {
			return;
		}
		/*
		 * check cache Disk is have enough storage
		 */
		switch (whichDiskSave) {
			case DISK_CACHE_SDCARD :
				if (!AppUtilLIB.checkSdcardHavEnghStorage(data.length)) {
					return;
				}
				break;
			case DISK_CACHE_UPHONE :
				if (!AppUtilLIB.checkPhoneHavEnghStorage(data.length)) {
					return;
				}
				break;
			default :
				return;
		}
		BufferedOutputStream ostream = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ostream = new BufferedOutputStream(fos);
			ostream.write(data, offset, length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			AppUtilLIB.closeOutputStream(ostream);
		}
	}

	/**
	 * 缓存至磁盘，如果文件不存在，创建文件后写入缓存
	 */
	private void cacheToDisk(Key key, Val value, byte[] data, int offset,
			int length) {
		// 文件已经存在，直接返回
		File file = getFileForKey(key);
		if (file.exists()) {
			return;
		}
		try {
			// 创建新文件
			if (file.createNewFile()) {
				if (data != null && data.length > 0) {
					writeByteToDisk(file, data, offset, length);
				} else {
					writeValueToDisk(file, value);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据key获取持久化缓存文件
	 */
	public File getFileForKey(Key key) {
		File outFile = new File(destCacheDir);
		if (!(isDiskCacheEnabled = outFile.exists())) {
			isDiskCacheEnabled = outFile.mkdirs();
		}
		return new File(outFile, getFileNameForKey(key));
	}

	/**
	 * 根据key获取缓存值，先从map缓存中取，如果不存在再从磁盘缓存中取 read from cache or disk
	 * 
	 * @param elementKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized Val get(Object elementKey) {
		Key key = (Key) elementKey;
		Val value = cache.get(key);
		if (value != null) {
			return value;
		}

		File file = getFileForKey(key);
		if (file.exists()) {
			try {
				value = readValueFromDisk(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			if (value == null) {
				return null;
			}
			cache.put(key, value);
			return value;
		}
		return null;
	}

	/**
	 * 判断某个key是否在有效期内
	 */
	@SuppressWarnings("unchecked")
	public synchronized boolean isCacheEffictive(Object elementKey,
			long aTimeMillis) {
		Key key = (Key) elementKey;
		File file = getFileForKey(key);
		if (file.exists()) {
			long modifyTime = file.lastModified();
			long nowTime = System.currentTimeMillis();
			if (Math.abs(nowTime - modifyTime) < aTimeMillis) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断某个key是否在有效期内
	 */
	public Val getFromInMemery(Object elementKey) {
		@SuppressWarnings("unchecked")
		Key key = (Key) elementKey;
		Val value = cache.get(key);
		return value;
	}

	/**
	 * 从磁盘缓存中获取值
	 */
	public synchronized Val getFromDisk(Object elementKey) {
		@SuppressWarnings("unchecked")
		Key key = (Key) elementKey;
		File file = getFileForKey(key);
		Val value = null;
		if (file.exists()) {
			try {
				value = readValueFromDisk(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			if (value != null) {
				cache.put(key, value);
			}
		}
		return value;
	}

	/**
	 * 将值置入缓存（磁盘[如果磁盘可用的话]与Map）， put value into ram and into disk,in sub must
	 * Override writeValueToDisk method
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public synchronized Val put(Key key, Val value) {
		return put(key, value, null);
	}

	/**
	 * 将值置入Map缓存
	 */
	public Val putInMemery(Key key, Val value) {
		return cache.put(key, value);
	}

	/**
	 * 将值置入缓存（磁盘[如果磁盘可用的话]与Map） put value into ram and into disk,in sub class
	 * not need Override writeValueToDisk method
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public synchronized Val put(Key key, Val value, byte[] data) {
		if (isDiskCacheEnabled) {
			cacheToDisk(key, value, data, 0, data == null ? 0 : data.length);
		}
		return cache.put(key, value);
	}

	/**
	 * 将值置入缓存（磁盘[如果磁盘可用的话]与Map）， Method: put
	 */
	public synchronized Val put(Key key, Val value, byte[] data, int offset,
			int length) {
		if (isDiskCacheEnabled) {
			cacheToDisk(key, value, data, offset, length);
		}
		Val v = null;
		if (null != value) {
			v = cache.put(key, value);
		}
		return v;
	}

	/**
	 * 判断key是否存在，不分磁盘与map Checks if a key is present in the in-memory cache or
	 * in-disk
	 * 
	 * @param key
	 *            the cache key
	 * @return true if the value is currently hold in memory or in-disk false
	 *         otherwise
	 */
	@SuppressWarnings("unchecked")
	public synchronized boolean containsKey(Object key) {
		return cache.containsKey(key) || isDiskCacheEnabled
				&& getFileForKey((Key) key).exists();
	}

	/**
	 * 判断key是否存在map缓存中 Checks if a value is present in the in-memory cache.
	 * ignores the disk cache
	 * 
	 * @param key
	 *            the cache key
	 * @return true if the value is currently hold in memory false otherwise
	 */
	public synchronized boolean containsKeyInMemory(Object key) {
		return cache.containsKey(key);
	}

	/**
	 * 判断在map缓存中是否存在这个值 Checks if the given value is currently hold in memory.
	 */
	public synchronized boolean containsValue(Object value) {
		return cache.containsValue(value);
	}

	/**
	 * 从map缓存中移除key
	 */
	public synchronized Val remove(Object key) {
		return cache.remove(key);
	}

	/**
	 * 从map缓存中移除（key与value都要匹配)
	 */
	public synchronized boolean remove(Object key, Object value) {
		return cache.remove(key, value);
	}

	/**
	 * 从磁盘缓存与map缓存中删除key
	 */
	@SuppressWarnings("unchecked")
	public synchronized Val delete(Object key) {
		if (isDiskCacheEnabled) {
			File file = getFileForKey((Key) key);
			if (file.exists()) {
				file.delete();
			}
		}
		return cache.remove(key);
	}

	/**
	 * 从磁盘缓存与map缓存中更新key
	 */
	public synchronized Val update(Key key, Val value, byte[] data) {
		if (isDiskCacheEnabled) {
			delete(key);
			cacheToDisk(key, value, data, 0, data == null ? 0 : data.length);
		}
		return cache.put(key, value);
	}

	/**
	 * 从磁盘缓存与map缓存中更新key
	 */
	public synchronized Val update(Key key, Val value, byte[] data, int start,
			int length) {
		if (isDiskCacheEnabled) {
			delete(key);
			cacheToDisk(key, value, data, start, length);
		}
		return cache.put(key, value);
	}

	/**
	 * 获取map缓存的所有key
	 */
	public Set<Key> keySet() {
		return cache.keySet();
	}

	public Set<Map.Entry<Key, Val>> entrySet() {
		return cache.entrySet();
	}

	/**
	 * 获取map缓存的大小
	 */
	public synchronized int size() {
		return cache.size();
	}

	/**
	 * 判断map缓存是否为空
	 */
	public synchronized boolean isEmpty() {
		return cache.isEmpty();
	}

	/**
	 * 返回磁盘缓存是否可用
	 */
	public boolean isDiskCacheEnabled() {
		return isDiskCacheEnabled;
	}

	/**
	 * 清空map缓存
	 */
	public synchronized void clear() {
		for (Entry<Key, Val> entry : cache.entrySet()) {
			WeakReference<Key> keyReference = new WeakReference<Key>(
					entry.getKey());
			WeakReference<Val> valueReference = new WeakReference<Val>(
					entry.getValue());
			Key key = keyReference.get();
			if (key != null) {
				cache.remove(key, valueReference.get());
			}
		}
	}

	/**
	 * 获取map缓存的所有值
	 */
	public Collection<Val> values() {
		return cache.values();
	}

}

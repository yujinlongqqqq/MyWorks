package com.ypyg.shopmanager.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;

public class ImageCacheManager {

	private Context context;

	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private LruCache<String, Bitmap> mMemoryCache;

	/**
	 * 图片硬盘缓存核心类。
	 */
	private DiskLruCache mDiskLruCache;

	private static ImageCacheManager uniqueInstance = null;

	public ImageCacheManager(Context context) {

		try {
			if (DataCener.getInstance().getPhoneInfo().getSdkVer() < 12) {
				return;
			}
		} catch (Exception e) {
		}
		this.context = context;
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;

		// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};

		try {
			// 获取图片缓存路径
			File cacheDir = getDiskCacheDir(context, "thumb");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			// 创建DiskLruCache实例，初始化缓存数据
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ImageCacheManager getInstance(Context context) {
		if (uniqueInstance == null)
			uniqueInstance = new ImageCacheManager(context);
		return uniqueInstance;
	}

	// public void onDestory() {
	// try {
	// if (DataCener.getInstance().getPhoneInfo().getSdkVer() < 12)
	// return;
	// } catch (Exception e) {
	// }
	// }
	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param bitmap
	 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	/**
	 * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
	 * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
	 */
	public void loadBitmaps(ImageView imageView, String imageUrl, String tag, String px) {
		try {
			if (DataCener.getInstance().getPhoneInfo().getSdkVer() < 12) {
				if (px.endsWith(Constants.thumb20000))
					px = Constants.thumb10000;
				if (px.endsWith(Constants.thumb40000))
					px = Constants.thumb20000;
				Bitmap bitmap = null;
				// 图片
				if (null == bitmap) {
					if (null != imageUrl) {
						// if(imageUrl.endsWith("small"))
						// imageUrl = imageUrl.substring(0,imageUrl.length()-5);
						bitmap = ImageLoad.getImageForSdk10(context, imageUrl, tag, px);
						imageView.setTag(imageUrl);
					}
					if (null == bitmap) {
						bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.failed_to_load);
					}
				}
				imageView.setImageBitmap(bitmap);
				return;
			}
		} catch (Exception e) {
			return;
		}
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		Snapshot snapShot = null;
		if (null == imageUrl)
			return;
		try {
			// 生成图片URL对应的key
			final String key = AppUtil.getStringMD5(imageUrl);
			Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
			if (bitmap == null) {
				// BitmapWorkerTask task = new BitmapWorkerTask();
				// taskCollection.add(task);
				// task.execute(imageUrl);
				// 查找key对应的缓存
				snapShot = mDiskLruCache.get(key);
				if (snapShot == null) {
					// 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
					DataCener.getInstance().getDataService().getLruCacheImage(mMemoryCache, imageUrl, mDiskLruCache, tag, px);
					return;
				}
				if (snapShot != null) {
					fileInputStream = (FileInputStream) snapShot.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				// 将缓存数据解析成Bitmap对象

				if (fileDescriptor != null) {
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				if (bitmap != null) {
					// 将Bitmap对象添加到内存缓存当中
					addBitmapToMemoryCache(imageUrl, bitmap);
					if (imageView != null && bitmap != null) {
						imageView.setImageBitmap(bitmap);
					}
				}
			} else {
				if (imageView != null && bitmap != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 本地图片加载
	 * 
	 * @param imageView
	 * @param imageUrl
	 */
	public void loadLocalImage(ImageView imageView, String imageUrl) {
		String key = AppUtil.getStringMD5(imageUrl);
		try {
			if (DataCener.getInstance().getPhoneInfo().getSdkVer() < 12) {
				ImageCache imageCache = ImageCache.getInstance(context);
				Bitmap bitmap = imageCache.get(key);
				if (bitmap != null)
					imageView.setImageBitmap(bitmap);
				return;
			}
		} catch (Exception e) {
		}
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		Snapshot snapShot = null;
		if (null == imageUrl)
			return;
		try {
			// 生成图片URL对应的key
			Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
			if (bitmap == null) {
				// BitmapWorkerTask task = new BitmapWorkerTask();
				// taskCollection.add(task);
				// task.execute(imageUrl);
				// 查找key对应的缓存
				snapShot = mDiskLruCache.get(key);
				if (snapShot != null) {
					fileInputStream = (FileInputStream) snapShot.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				// 将缓存数据解析成Bitmap对象

				if (fileDescriptor != null) {
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				if (bitmap != null) {
					// 将Bitmap对象添加到内存缓存当中
					addBitmapToMemoryCache(imageUrl, bitmap);
					if (imageView != null && bitmap != null) {
						imageView.setImageBitmap(bitmap);
					}
				}
			} else {
				if (imageView != null && bitmap != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将线上的图片保存到缓存
	 * 
	 * @throws IOException
	 */
	public void setNetImage(String imageUrl, InputStream aInStream) throws IOException {
		String mKey = AppUtil.getStringMD5(imageUrl);
		try {
			if (DataCener.getInstance().getPhoneInfo().getSdkVer() < 12) {
				ImageCache imageCache = ImageCache.getInstance(context);
				Bitmap bitmap = BitmapFactory.decodeStream(aInStream);
				imageCache.put(mKey, bitmap);
				return;
			}
		} catch (Exception e) {
		}
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		Snapshot snapShot = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		DiskLruCache.Editor editor = null;
		try {
			// 生成图片URL对应的key
			if (snapShot == null) {
				// 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
				editor = mDiskLruCache.edit(mKey);
				if (editor != null) {
					OutputStream outputStream = editor.newOutputStream(0);

					in = new BufferedInputStream(aInStream, 8 * 1024);
					out = new BufferedOutputStream(outputStream, 8 * 1024);
					int b;
					while ((b = in.read()) != -1) {
						out.write(b);
					}
					editor.commit();
				}
				// 缓存被写入后，再次查找key对应的缓存
				snapShot = mDiskLruCache.get(mKey);
			}
			if (snapShot != null) {
				fileInputStream = (FileInputStream) snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
			}
			// 将缓存数据解析成Bitmap对象
			Bitmap bitmap = null;
			if (fileDescriptor != null) {
				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
			}
			if (bitmap != null) {
				// 将Bitmap对象添加到内存缓存当中
				if (mMemoryCache.get(imageUrl) == null) {
					mMemoryCache.put(imageUrl, bitmap);
				}
			}
		} catch (Exception e) {
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
			if (fileDescriptor == null && fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
				}
			}
		}

	}

	/**
	 * 将bitmap 存到缓存中
	 * 
	 * @param imageName
	 *            图片的名字
	 * @param bitmap
	 *            要缓存的图片
	 */
	public void saveToCache(String imageName, Bitmap bitmap) {
		DiskLruCache.Editor editor = null;
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		Snapshot snapShot = null;
		String mKey = AppUtil.getStringMD5(imageName);
		if (AppUtil.isNull(mKey))
			return;
		if (DataCener.getInstance().getPhoneInfo().getSdkVer() < 12) {
			ImageCache imageCache = ImageCache.getInstance(context);
			imageCache.put(mKey, bitmap);
			return;
		}
		try {

			// 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
			editor = mDiskLruCache.edit(mKey);
			OutputStream outputStream = editor.newOutputStream(0);
			BufferedOutputStream out = new BufferedOutputStream(outputStream, 8 * 1024);
			bitmap.compress(CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			editor.commit();

			// 缓存被写入后，再次查找key对应的缓存
			snapShot = mDiskLruCache.get(mKey);
			if (snapShot != null) {
				fileInputStream = (FileInputStream) snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
			}
			// 将缓存数据解析成Bitmap对象
			Bitmap bitmap2 = null;
			if (fileDescriptor != null) {
				bitmap2 = BitmapFactory.decodeFileDescriptor(fileDescriptor);
			}
			if (bitmap2 != null) {
				// 将Bitmap对象添加到内存缓存当中
				if (mMemoryCache.get(imageName) == null) {
					mMemoryCache.put(imageName, bitmap);
				}
			}
		} catch (Exception e) {
			try {
				editor.abort();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 根据传入的uniqueName获取硬盘缓存的路径地址。
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 获取当前应用程序的版本号。
	 */
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	/**
	 * 将缓存记录同步到journal文件中。
	 */
	public void fluchCache() {
		if (mDiskLruCache != null) {
			try {
				mDiskLruCache.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 建立HTTP请求，并获取Bitmap对象。
	 * 
	 * @param imageUrl
	 *            图片的URL地址
	 * @return 解析后的Bitmap对象
	 */
	private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
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
		return false;
	}

	public DiskLruCache getmDiskLruCache() {
		return mDiskLruCache;
	}

	public LruCache<String, Bitmap> getmMemoryCache() {
		return mMemoryCache;
	}

}

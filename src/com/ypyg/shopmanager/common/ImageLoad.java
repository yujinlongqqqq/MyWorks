package com.ypyg.shopmanager.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.cache.ImageCache;

public class ImageLoad {
	/**
	 * 缓存中没有 需重写onEventMainThread(ImageLoadFinishEvent event)方法
	 * 
	 * @param context
	 * @param imgUrl
	 * @return
	 */
	public Bitmap getSourceImage(Context context, String imgUrl, String mTag) {

		ImageCache imageCache = ImageCache.getInstance(context);
		Bitmap bitmap = null;
		if (null == imgUrl || "".equals(imgUrl))
			return null;// 返回加载失败图片
		// 查看缓存
		String key = AppUtil.getStringMD5(imgUrl);
		bitmap = imageCache.get(key);
		if (null != bitmap)
			return bitmap;
		// 网上下载\
		DataCener dataCener = DataCener.getInstance();
		if (null == dataCener)
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.failed_to_load);// 返回加载失败图片
		DataService dataService = dataCener.getDataService();
		if (null == dataService)
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.failed_to_load);// 返回加载失败图片
		// dataService.getImageForSdk10(imgUrl, imageCache, mTag, null);
		return null;
		// return BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.image_loading);// 返回加载中图片
	}

	/**
	 * 缓存中没有 需重写onEventMainThread(ImageLoadFinishEvent event)方法
	 * 
	 * @param context
	 * @param imgUrl
	 * @return
	 */
	public Bitmap getImage(Context context, String imgUrl) {
		if (null == imgUrl || "".equals(imgUrl))
			return null;// 返回加载失败图片
		ImageCache imageCache = ImageCache.getInstance(context);
		Bitmap bitmap = null;
		// 查看缓存
		String key = AppUtil.getStringMD5(imgUrl);
		bitmap = imageCache.get(key);
		if (null != bitmap)
			return bitmap;
		// 网上下载\
		DataCener dataCener = DataCener.getInstance();
		if (!imgUrl.endsWith(Constants.thumbnailImage)) {
			imgUrl += Constants.thumbnailImage;
		}
		if (null == dataCener)
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.failed_to_load);// 返回加载失败图片
		DataService dataService = dataCener.getDataService();
		if (null == dataService)
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.failed_to_load);// 返回加载失败图片
		List<String> list = new ArrayList<String>();
		list.add(imgUrl);
		// dataService.getImage(list, imageCache, imgUrl);
		return null;
		// return BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.image_loading);// 返回加载中图片
	}

	/**
	 * 缓存中没有 需重写onEventMainThread(ImageLoadFinishEvent event)方法
	 * 
	 * @param context
	 * @param imgUrl
	 * @return
	 */
	public Bitmap getImage(Context context, String imgUrl, int atag) {

		ImageCache imageCache = ImageCache.getInstance(context);
		Bitmap bitmap = null;
		if (null == imgUrl || "".equals(imgUrl))
			return null;// 返回加载失败图片
		// 查看缓存
		String key = AppUtil.getStringMD5(imgUrl);
		bitmap = imageCache.get(key);
		if (null != bitmap)
			return bitmap;
		// 网上下载\
		DataCener dataCener = DataCener.getInstance();
		if (null == dataCener)
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.failed_to_load);// 返回加载失败图片
		DataService dataService = dataCener.getDataService();
		if (null == dataService)
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.failed_to_load);// 返回加载失败图片
		List<String> list = new ArrayList<String>();
		list.add(imgUrl);
		// dataService.getImage(list, imageCache, atag);
		return null;
		// return BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.image_loading);// 返回加载中图片
	}

	public static Bitmap getImageForSdk10(Context context, String imgUrl,
			String aTag, String px) {

		ImageCache imageCache = ImageCache.getInstance(context);
		Bitmap bitmap = null;
		if (null == imgUrl || "".equals(imgUrl))
			return null;// 返回加载失败图片
		// 查看缓存
		String key = AppUtil.getStringMD5(imgUrl);
		bitmap = imageCache.get(key);
		if (null != bitmap)
			return bitmap;
		// 网上下载\
		DataCener dataCener = DataCener.getInstance();
		if (null == dataCener)
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.failed_to_load);// 返回加载失败图片
		DataService dataService = dataCener.getDataService();
		if (null == dataService)
			return BitmapFactory.decodeResource(context.getResources(),
					R.drawable.failed_to_load);// 返回加载失败图片
		// dataService.getImageForSdk10(imgUrl, imageCache, aTag, px);
		return null;
		// return BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.image_loading);// 返回加载中图片
	}
}

package com.ypyg.shopmanager.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ypyg.shopmanager.R;
/**
 * 图片裁剪压缩
 *
 */
public class CropSaveImage {

	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD
			+ Constants.prePath + "images/", "");
	private final Context mContext;
	public int screenWidth = 0;
	public int screenHeight = 0;

	public CropSaveImage(Context context) {
		this.mContext = context;
	}

	/**
	 * 裁剪并保存
	 * 
	 * @return
	 */
	public Bitmap cropAndSave(Bitmap bm) {
		final Bitmap bmp = onSaveClicked(bm);
		return bmp;
	}

	private Bitmap onSaveClicked(Bitmap bm) {
		// 剪裁后照片像素大小的调整
		int cropimagew = (int) mContext.getResources().getDimension(
				R.dimen.cropimagew);
		int cropimageh = (int) mContext.getResources().getDimension(
				R.dimen.cropimageh);
		int width = cropimagew;// dr.width(); // modify by yc
		int height = cropimageh;// dr.height();
		Bitmap croppedImage = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		return croppedImage;
	}

	public String saveToLocal(Bitmap bm, String imageName) {
		if (!FILE_LOCAL.exists())
			FILE_LOCAL.mkdirs();
		String path = FILE_LOCAL + "/" + imageName + ".jpg";
		try {
			FileOutputStream fos = new FileOutputStream(path);
			bm.compress(CompressFormat.JPEG, 75, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return path;
	}

	/**
	 * 获取屏幕的高和宽
	 */
	private void getWindowWH() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	// 缩放_原图
	public String createThumbSourceImage(String sourcePath, String imageName) {

		if (!FILE_LOCAL.exists())
			FILE_LOCAL.mkdirs();
		String path = FILE_LOCAL + "/" + imageName + "1.kk";
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(sourcePath, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度

			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			int padding_in_dp = 500; // 400 dps
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
			int w = padding_in_px;
			int h = padding_in_px;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inSampleSize = (int) ratio + 1;
			newOpts.inJustDecodeBounds = false;
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			Bitmap bitmap = BitmapFactory.decodeFile(sourcePath, newOpts);
			FileOutputStream fos = new FileOutputStream(path);
			if (null != bitmap) {
				bitmap.compress(CompressFormat.JPEG, 75, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
//		File picture = new File(path);
//		double m = (double) picture.length() / 1024;
//		Log.v("ThumbSourceI_size", m + "k");
		return path;
	}
	// 缩放_原图
	public String createAuthImage(String sourcePath, String imageName) {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int freeMemory =(int) Runtime.getRuntime().freeMemory();
		int totalMemory =(int) Runtime.getRuntime().totalMemory();
		if (!FILE_LOCAL.exists())
			FILE_LOCAL.mkdirs();
		String path = FILE_LOCAL + "/" + imageName + "3.kk";
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(sourcePath, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			int padding_in_dp = 200; // 200 dps
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
			int w = padding_in_px;
			int h = padding_in_px;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inSampleSize = (int) ratio + 1;
			newOpts.inJustDecodeBounds = false;
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			Bitmap bitmap = BitmapFactory.decodeFile(sourcePath, newOpts);
			FileOutputStream fos = new FileOutputStream(path);
			if (null != bitmap) {
				bitmap.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
//		File picture = new File(path);
//		double m = (double) picture.length() / 1024;
//		Log.v("ThumbSourceI_size", m + "k");
		return path;
	}
  
	// 缩放_小图
	public String createSmallBitmap(String path, String imageName, int w, int h) {

		String smallImgPath = FILE_LOCAL + "/" + imageName + "2.kk";

		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();

			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			int padding_in_dp = 50; // 50 dps
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
			w = padding_in_px;
			h = padding_in_px;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
			// 获取缩放后图片
			FileOutputStream fileOutputStream = new FileOutputStream(
					smallImgPath);
			if (null != bitmap)
				bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
//			File smallImageFile = new File(smallImgPath);
//			double m = (double) smallImageFile.length() / 1024;
//			Log.v("small_image_size", m + "k");
			return smallImgPath;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 缩放_小图2
	public String createSmallBitmap2(String path, String imageName, int w, int h) {
		
		String smallImgPath = FILE_LOCAL + "/" + imageName + "2.kk";
		
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			int padding_in_dp = 100; // 100 dps
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
			w = padding_in_px;
			h = padding_in_px;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
			// 获取缩放后图片
			FileOutputStream fileOutputStream = new FileOutputStream(
					smallImgPath);
			if (null != bitmap)
				bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
//			File smallImageFile = new File(smallImgPath);
//			double m = (double) smallImageFile.length() / 1024;
//			Log.v("small_image_size", m + "k");
			return smallImgPath;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 接收source图片后，对其进行缩放
	public String createRecieveSourceSmallBitmap(String path, String imageName,
			int w, int h) {

		String smallImgPath = FILE_LOCAL + "/" + imageName;

		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();

			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			int padding_in_dp = 100; // 100 dps
			final float scale = mContext.getResources().getDisplayMetrics().density;
			int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
			w = padding_in_px;
			h = padding_in_px;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
			// 获取缩放后图片
			FileOutputStream fileOutputStream = new FileOutputStream(
					smallImgPath);
			if (null != bitmap)
				bitmap.compress(CompressFormat.JPEG, 5, fileOutputStream);
			fileOutputStream.flush();	
			fileOutputStream.close();
//			File smallImageFile = new File(smallImgPath);
//			double m = (double) smallImageFile.length() / 1024;
//			Log.v("small_image_size", m + "k");
			return smallImgPath;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public Bitmap createBitmap(String path, String imageName, int w, int h) {

		File picture = new File(path);
		double m = (double) picture.length() / 1024;
		Log.v("image_size", m + "k");
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			w = 200;
			h = 200;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}

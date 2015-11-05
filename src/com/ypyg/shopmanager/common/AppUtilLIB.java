package com.ypyg.shopmanager.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ypyg.shopmanager.R;

public class AppUtilLIB {

	/**
	 * 移动手机号码的正则表达式。
	 */
	private static final String REGEX_CHINA_MOBILE = "1(3[4-9]|4[7]|5[012789]|8[278])\\d{8}";

	/**
	 * 联通手机号码的正则表达式。
	 */
	private static final String REGEX_CHINA_UNICOM = "1(3[0-2]|5[56]|8[56])\\d{8}";

	/**
	 * 电信手机号码的正则表达式。
	 */
	private static final String REGEX_CHINA_TELECOM = "(?!00|015|013)(0\\d{9,11})|(1(33|53|80|89)\\d{8})";

	/**
	 * 手机号码的正则表达式。
	 */
	private static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

	/**
	 * 邮箱的正则表达式
	 */

	private static final String REGEX_MAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	/**
	 * 密码的正则表达式
	 */

	private static final String REGEX_password = "^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,16}$";

	/**
	 * 判断是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNumber(String str) {
		// Regex for checking phone number
		return isRegexMatch(str, REGEX_CHINA_MOBILE)
				|| isRegexMatch(str, REGEX_CHINA_UNICOM)
				|| isRegexMatch(str, REGEX_CHINA_TELECOM)
				|| isRegexMatch(str, REGEX_MOBILE) || checkNewAddNum(str);
	}

	/**
	 * 判断是否是邮箱
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		// Regex for checking phone number
		return isRegexMatch(str, REGEX_MAIL);
	}

	/**
	 * 判断是否是邮箱
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPasswordInput(String str) {
		// Regex for checking phone number
		return isRegexMatch(str, REGEX_password);
	}

	/**
	 * 判断字符串是否匹配了正则表达式。
	 * 
	 * @param str
	 *            字符串
	 * @param regex
	 *            正则表达式
	 * @return true/false
	 */
	public static boolean isRegexMatch(String str, String regex) {
		return str != null && str.matches(regex);
	}

	/**
	 * 新增的号段
	 */
	public static boolean checkNewAddNum(String str) {
		return str != null
				&& ((str.startsWith("183") || str.startsWith("170")
						|| str.startsWith("177") || str.startsWith("176") || str
							.startsWith("178")) && (str.length() == 11));
	}

	/**
	 * 获取SDCARD目录
	 */
	public static File getExternalStorageDirectory() {
		String sFile = Environment.getExternalStorageState();
		if (null != sFile) {
			if (sFile.equals(Environment.MEDIA_MOUNTED)) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				return sdCardDir;
			}
		}
		return null;
	}

	/**
	 * 获取屏幕的宽
	 */
	public static int getWindowWidth(Context mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕的高
	 */
	public static int getWindowHeight(Context mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 判断相应大小的文件是否可以保存在手机上
	 */
	public static boolean checkPhoneHavEnghStorage(long size) {
		return checkStorageRom(Environment.getDataDirectory(), size);
	}

	/**
	 * 判断相应大小的文件是否可以保存在sdcard上
	 */
	public static boolean checkSdcardHavEnghStorage(long size) {
		return checkStorageRom(getExternalStorageDirectory(), size);
	}

	/**
	 * 关闭输入流 close InputStream
	 * 
	 * @param ins
	 */
	public static void closeInputStream(InputStream ins) {
		if (ins != null) {
			try {
				ins.close();
				ins = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭输出流 close OutputStream
	 * 
	 * @param out
	 */
	public static void closeOutputStream(OutputStream out) {
		if (out != null) {
			try {
				out.close();
				out = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static synchronized boolean checkStorageRom(File file, long size) {
		if (file == null) {
			return false;
		}

		StatFs mStat = new StatFs(file.getAbsolutePath());
		@SuppressWarnings("deprecation")
		long blockSize = mStat.getBlockSize();
		@SuppressWarnings("deprecation")
		long avaleCout = mStat.getAvailableBlocks();
		long val = avaleCout * blockSize;
		if (size + 10 * 1024 * 1024 <= val) {
			return true;
		} else {
			return false;
		}
	}

	static String getExceptionStackTrace(String head, Exception exception) {
		StringBuilder err = new StringBuilder();
		if (!TextUtils.isEmpty(head)) {
			err.append(head + " <br/>");
		}
		err.append(exception.toString());
		StackTraceElement[] stack = exception.getStackTrace();
		if (stack != null) {
			for (StackTraceElement element : stack) {
				err.append("\tat " + element + "<br/>");
			}
		}

		StackTraceElement[] parentStack = stack;
		Throwable throwable = exception.getCause();
		while (throwable != null) {
			err.append("Caused by: ");
			err.append(throwable + "<br/>");
			StackTraceElement[] currentStack = throwable.getStackTrace();
			int duplicates = 0;
			int parentIndex = parentStack.length;
			for (int i = currentStack.length; --i >= 0 && --parentIndex >= 0;) {
				StackTraceElement parentFrame = parentStack[parentIndex];
				if (parentFrame.equals(currentStack[i])) {
					duplicates++;
				} else {
					break;
				}
			}
			for (int i = 0; i < currentStack.length - duplicates; i++) {
				err.append("\tat " + currentStack[i] + "<br/>");
			}
			if (duplicates > 0) {
				err.append("\t... " + duplicates + " more" + "<br/>");
			}
			parentStack = currentStack;
			throwable = throwable.getCause();
		}
		return err.toString();
	}

	public static String getStringMD5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();// 32位
	}

	public static String getFileMD5(File aFile) {
		byte[] hash;
		FileInputStream in = null;
		try {
			in = new FileInputStream(aFile);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY,
					0, aFile.length());
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(byteBuffer);
			hash = messagedigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			closeInputStream(in);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();// 32位
	}

	// 递归删除目录
	public static void removeDirs(File root) {
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isDirectory()) { // 判断是否为文件夹
					removeDirs(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否存在
						removeDirs(f);
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
	}

	/*
	 * bBackGround 是否允许后台安装
	 */
	public static void installApk(final String aApkPath, Context aContext) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + aApkPath),
				"application/vnd.android.package-archive");
		aContext.startActivity(intent);
	}

	/**
	 * 判断一个程序是否显示在前端,根据测试此方法执行效率在11毫秒,无需担心此方法的执行效率
	 * 
	 * @param packageName程序包名
	 * @param context上下文环境
	 * @return true--->在前端,false--->不在前端
	 */
	public static boolean isApplicationShowing(String packageName,
			Context context) {
		boolean result = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
		if (appProcesses != null) {
			for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
				if (runningAppProcessInfo.processName.equals(packageName)) {
					int status = runningAppProcessInfo.importance;
					if (status == RunningAppProcessInfo.IMPORTANCE_VISIBLE
							|| status == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	public static boolean isTopActivity(String packageName, Context context) {

		System.out.println("**********************top packageName:"

		+ packageName);

		ActivityManager activityManager = (ActivityManager) context

		.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);

		if (tasksInfo.size() > 0) {
			System.out.println("tasksInfo.size:" + tasksInfo.size());
			System.out
					.println("*********************tasksInfo.get(0).numActivities"

							+ tasksInfo.get(0).numActivities);

			// 应用程序位于堆栈的顶层

			if (tasksInfo.get(0).numActivities > 1)

			{

				return true;

			}

		}

		return false;

	}

	public static void outSoftInput(Activity context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(context.INPUT_METHOD_SERVICE);
		View currentFocus = context.getCurrentFocus();

		if (null != currentFocus) {
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

	public static String getDate(Long createTime) {
		String time = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			time = df.format(createTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String getDateForDay(Long createTime) {
		String time = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			time = df.format(createTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	

	public static Long getTimeInMillis() {
		Date date = new Date();
		return date.getTime();
	}

	// 优化后的时间
	public static String getWrappedDate(Long createTime) {
		Calendar c = Calendar.getInstance();
		long oneDay = 24 * 60 * 60 * 1000;
		long dateOffset = c.getTimeInMillis() - createTime;
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		// 星期几
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));
		StringBuffer sbBuffer = new StringBuffer();
		if (dateOffset < oneDay) {
			sbBuffer.append(hour + ":" + mins);
			return sbBuffer.toString();
		}
		if (dateOffset < oneDay * 2) {
			sbBuffer.append("昨天");
			return sbBuffer.toString();
		}
		if (dateOffset < oneDay * 7) {
			sbBuffer.append("星期：" + weekDay);
			return sbBuffer.toString();
		} else {
			sbBuffer.append(month + "-" + day);
		}

		return sbBuffer.toString();
	}

	public static boolean isTopActivy(String cmdName, Context mContext) {

		ActivityManager manager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		String cmpNameTemp = null;

		if (null != runningTaskInfos) {
			ComponentName aTop = runningTaskInfos.get(0).topActivity;
			if (null != aTop)
				cmpNameTemp = aTop.getClassName();
		}

		if (null == cmpNameTemp)
			return false;

		return cmpNameTemp.equals(cmdName);
	}

	// tutor项目 华丽丽的分割线--------------------------------------
	public static final String IMAGE_PATH = "My_weixin";
	/**
	 * 拍照后的照片
	 */
	public static String localTempImageFileName = "";
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory();
	public static final File FILE_LOCAL = FILE_SDCARD;
	private String TAG = "InformationActivity";

	public static boolean isServiceRunning(Context mContext, String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);

		if (!(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	public static boolean isActivityRunning(List<Activity> activity,
			String activityName) {
		boolean isRunning = false;

		if (!(activity.size() > 0)) {
			return false;
		}
		for (int i = 0; i < activity.size(); i++) {
			if (activity.get(i).getClass().getName().equals(activityName)) {
				isRunning = true;
				break;
			}
		}

		return isRunning;
	}

	// 数字判断
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String chatContentfilter(String content) {
		if (null != content) {
			if (content.endsWith(".amr"))
				return "[语音]";
			if (content.endsWith(".kk"))
				return "[图片]";
		}
		return content;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			// pre-condition
			return;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0);

			totalHeight += listItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);
	}

	public static void setListViewHeightBasedOnChildren2(Context mContext,
			ListView listView) {

		int tab_eighty_hight = (int) (mContext.getResources()
				.getDimension(R.dimen.two_hundred_twenty_five_hight));

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			// pre-condition
			return;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0);

			totalHeight += listItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		if (params.height >= tab_eighty_hight)
			params.height = tab_eighty_hight;
		listView.setLayoutParams(params);
	}

	// 单选dialog
	//
	// private static String singleDialog(Context mContext,
	// final ArrayList<String> list) {
	// final String value = null;
	//
	// }

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * 
	 * @param context
	 *            上下文，一般为Activity
	 * @param dpValue
	 *            dp数据值
	 * @return px像素值
	 */
	public static int dip2px(Context context, int dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static ArrayList<String> toArraylist(String[] stringArray) {
		ArrayList<String> list = new ArrayList<String>();
		if (null != stringArray)
			for (int i = 0; i < stringArray.length; i++) {
				list.add(stringArray[i]);
			}
		return list;
	}

	public static ArrayList<String> setArrayList(Context mContext, int array) {
		String[] arrayString = null;
		if (null == mContext)
			return null;
		if (null != mContext.getResources().getStringArray(array))
			arrayString = mContext.getResources().getStringArray(array);
		return toArraylist(arrayString);
	}

	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * 查看文件是否存在
	 */
	public static boolean isExistFile(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 判断对象是否为空
	 */
	public static boolean isNull(Object object) {
		if (object == null)
			return true;
		if (object instanceof String) {
			if ("".equals((String) object))
				return true;
		}
		return false;
	}
}

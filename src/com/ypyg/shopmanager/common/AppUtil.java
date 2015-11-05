package com.ypyg.shopmanager.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.application.ShopApplication;
import com.ypyg.shopmanager.view.uploadphoto.ModifyAvatarDialog;

public class AppUtil {

	/**
	 * �ƶ��ֻ������������ʽ��
	 */
	private static final String REGEX_CHINA_MOBILE = "1(3[4-9]|4[7]|5[012789]|8[278])\\d{8}";

	/**
	 * ��ͨ�ֻ������������ʽ��
	 */
	private static final String REGEX_CHINA_UNICOM = "1(3[0-2]|5[56]|8[56])\\d{8}";

	/**
	 * �����ֻ������������ʽ��
	 */
	private static final String REGEX_CHINA_TELECOM = "(?!00|015|013)(0\\d{9,11})|(1(33|53|80|89)\\d{8})";

	/**
	 * �ֻ������������ʽ��
	 */
	private static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

	/**
	 * �����������ʽ
	 */

	private static final String REGEX_MAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	/**
	 * �����������ʽ
	 */

	private static final String REGEX_password = "^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,16}$";

	/**
	 * �ж��Ƿ����ֻ�����
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNumber(String str) {
		// Regex for checking phone number
		return isRegexMatch(str, REGEX_CHINA_MOBILE) || isRegexMatch(str, REGEX_CHINA_UNICOM) || isRegexMatch(str, REGEX_CHINA_TELECOM) || isRegexMatch(str, REGEX_MOBILE) || checkNewAddNum(str);
	}

	/**
	 * �ж��Ƿ�������
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		// Regex for checking phone number
		return isRegexMatch(str, REGEX_MAIL);
	}

	/**
	 * �ж�����������Ƿ��Ǳ�������ַ�
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPasswordInput(String str) {
		// Regex for checking phone number
		return isRegexMatch(str, REGEX_password);
	}

	/**
	 * �ж��ַ����Ƿ�ƥ����������ʽ��
	 * 
	 * @param str
	 *            �ַ���
	 * @param regex
	 *            ������ʽ
	 * @return true/false
	 */
	public static boolean isRegexMatch(String str, String regex) {
		return str != null && str.matches(regex);
	}

	/**
	 * �����ĺŶ�
	 */
	public static boolean checkNewAddNum(String str) {
		return str != null && ((str.startsWith("183") || str.startsWith("170") || str.startsWith("177") || str.startsWith("176") || str.startsWith("178")) && (str.length() == 11));
	}

	/**
	 * ��ȡSDCARDĿ¼
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
	 * ��ȡ��Ļ�Ŀ�
	 */
	public static int getWindowWidth(Context mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * ��ȡ��Ļ�ĸ�
	 */
	public static int getWindowHeight(Context mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * �ж���Ӧ��С���ļ��Ƿ���Ա������ֻ���
	 */
	public static boolean checkPhoneHavEnghStorage(long size) {
		return AppUtil.checkStorageRom(Environment.getDataDirectory(), size);
	}

	/**
	 * �ж���Ӧ��С���ļ��Ƿ���Ա�����sdcard��
	 */
	public static boolean checkSdcardHavEnghStorage(long size) {
		return AppUtil.checkStorageRom(getExternalStorageDirectory(), size);
	}

	/**
	 * �ر������� close InputStream
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
	 * �ر������ close OutputStream
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
		byte[] hash = null;
		try {
			if (!isNull(string))
				hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		StringBuilder hex = null;
		if (!isNull(hash)) {
			hex = new StringBuilder(hash.length * 2);
			for (byte b : hash) {
				if ((b & 0xFF) < 0x10)
					hex.append("0");
				hex.append(Integer.toHexString(b & 0xFF));
			}
		} else {
			return null;
		}
		return hex.toString();// 32λ
	}

	public static String getFileMD5(File aFile) {
		byte[] hash;
		FileInputStream in = null;
		try {
			in = new FileInputStream(aFile);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, aFile.length());
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
			AppUtil.closeInputStream(in);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();// 32λ
	}

	public static Long getVersionCode() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = ShopApplication.getInstance().getApplicationContext().getPackageManager();
		String sPackageName = ShopApplication.getInstance().getApplicationContext().getPackageName();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = packageManager.getPackageInfo(sPackageName, 0);
		return Long.valueOf(packInfo.versionCode);
	}

	public static String getPackageName() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = ShopApplication.getInstance().getApplicationContext().getPackageManager();
		String sPackageName = ShopApplication.getInstance().getApplicationContext().getPackageName();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = packageManager.getPackageInfo(sPackageName, 0);
		return packInfo.packageName;
	}

	// ��ȡ�汾��
	public static int getVersionName() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = ShopApplication.getInstance().getApplicationContext().getPackageManager();
		String sPackageName = ShopApplication.getInstance().getApplicationContext().getPackageName();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = packageManager.getPackageInfo(sPackageName, 0);
		return packInfo.versionCode;
	}

	// �ݹ�ɾ��Ŀ¼
	public static void removeDirs(File root) {
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isDirectory()) { // �ж��Ƿ�Ϊ�ļ���
					removeDirs(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // �ж��Ƿ����
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
	 * bBackGround �Ƿ������̨��װ
	 */
	public static void installApk(final String aApkPath, Context aContext) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + aApkPath), "application/vnd.android.package-archive");
		aContext.startActivity(intent);
	}

	/**
	 * �ж�һ�������Ƿ���ʾ��ǰ��,���ݲ��Դ˷���ִ��Ч����11����,���赣�Ĵ˷�����ִ��Ч��
	 * 
	 * @param packageName�������
	 * @param context�����Ļ���
	 * @return true--->��ǰ��,false--->����ǰ��
	 */
	public static boolean isApplicationShowing(String packageName, Context context) {
		boolean result = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
		if (appProcesses != null) {
			for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
				if (runningAppProcessInfo.processName.equals(packageName)) {
					int status = runningAppProcessInfo.importance;
					if (status == RunningAppProcessInfo.IMPORTANCE_VISIBLE || status == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
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
			System.out.println("*********************tasksInfo.get(0).numActivities"

			+ tasksInfo.get(0).numActivities);

			// Ӧ�ó���λ�ڶ�ջ�Ķ���

			if (tasksInfo.get(0).numActivities > 1)

			{

				return true;

			}

		}

		return false;

	}

	public static void outSoftInput(Activity context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		View currentFocus = context.getCurrentFocus();

		if (null != currentFocus) {
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	// 4.1��������
	// 4.1.1���죺ʱ���֣��루24Сʱ�ƣ�
	// 4.1.2���죺����ʱ���֣��루24Сʱ�ƣ�
	// 4.1.3��������֮ǰ����-�� ʱ���֣��루���ӣ�11-13 15:25:22��
	// 4.1.4����֮ǰ����-��-�գ����ӣ�2012-11-12��
	// 4.2����
	// 4.2.1���죺ʱ���֣�24Сʱ�ƣ�
	// 4.2.2���죺����ʱ���֣�24Сʱ�ƣ�
	// 4.2.3��������֮ǰ����-�� ʱ���֣����ӣ�11-13 15:25��
	// 4.2.4����֮ǰ����-��-�գ����ӣ�2012-11-12��
	public static String format(Date date) {
		return format(date.getTime());
	}

	public static String format(Long time) {
		SimpleDateFormat df;
		try {
			SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = new Date();
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			// System.out.println("����:" + d.format(date));
			if (time - date.getTime() <= 24 * 60 * 60 * 1000 && time - date.getTime() >= 0) {
				df = new SimpleDateFormat("���� HH:mm");
				return df.format(new Date(time));
			}
			date = new Date(date.getTime() - 24 * 60 * 60 * 1000);
			// System.out.println("����:" + d.format(date));
			if (time - date.getTime() <= 24 * 60 * 60 * 1000 && time - date.getTime() >= 0) {
				df = new SimpleDateFormat("���� HH:mm");
				return df.format(new Date(time));
			}
			date = new Date();
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			date.setMonth(0);
			date.setDate(1);
			// System.out.println("����:" + d.format(date));
			if (time - date.getTime() <= (long) 365 * 24 * 60 * 60 * 1000 && time - date.getTime() >= 0) {
				df = new SimpleDateFormat("MM-dd HH:mm");
				return df.format(new Date(time));
			}
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return df.format(new Date(time));
		} catch (Exception e) {
			return "";
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
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);

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

	public static String getDateNoYear(Long createTime) {
		String time = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
			time = df.format(createTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static Long getTimeInMillis() {
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}

	public static boolean isTopActivy(String cmdName, Context mContext) {

		ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

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

	public static final String IMAGE_PATH = "My_weixin";
	/**
	 * ���պ����Ƭ
	 */
	public static String localTempImageFileName = "";
	public static final int FLAG_CHOOSE_IMG = 5;
	public static final int FLAG_CHOOSE_PHONE = 6;
	public static final int FLAG_MODIFY_FINISH = 7;
	private String TAG = "InformationActivity";

	public static boolean isServiceRunning(Context mContext, String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);

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

	public static boolean isActivityRunning(List<Activity> activity, String activityName) {
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

	public static boolean isAppRunning(Context context) {
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			try {
				if (info.topActivity.getPackageName().equals(getPackageName()) && info.baseActivity.getPackageName().equals(getPackageName())) {
					isAppRunning = true;
					// find it, break
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return isAppRunning;

	}

	/**
	 * ͼƬ�ϴ�
	 */
	public static void getphoto(final Context mContext) {

		final File filePath = new File(Constants.FILE_image);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		// ����ѡ�����ַ�ʽ��dialog
		ModifyAvatarDialog modifyAvatarDialog = new ModifyAvatarDialog(mContext, R.style.Transparent) {
			// ѡ�񱾵����
			@Override
			public void doGoToImg() {
				this.dismiss();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				((Activity) mContext).startActivityForResult(intent, FLAG_CHOOSE_IMG);
			}

			// ѡ���������
			@Override
			public void doGoToPhone() {
				this.dismiss();
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {
					try {
						localTempImageFileName = "";
						localTempImageFileName = String.valueOf((new Date()).getTime()) + ".jpg";

						Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File f = new File(filePath, localTempImageFileName);
						// localTempImgDir��localTempImageFileName���Լ����������
						Uri u = Uri.fromFile(f);
						intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
						((Activity) mContext).startActivityForResult(intent, FLAG_CHOOSE_PHONE);
					} catch (ActivityNotFoundException e) {
						//
					}
				}
			}
		};
		AlignmentSpan span = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
		AbsoluteSizeSpan span_size = new AbsoluteSizeSpan(25, true);
		SpannableStringBuilder spannable = new SpannableStringBuilder();
		String dTitle = ((Activity) mContext).getString(R.string.chat_copy);
		spannable.append(dTitle);
		spannable.setSpan(span, 0, dTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(span_size, 0, dTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// modifyAvatarDialog.setTitle(spannable);//�ϴ���Ƭdialog title
		modifyAvatarDialog.setCanceledOnTouchOutside(true);
		modifyAvatarDialog.show();

	}

	// public static void getphoto2(final Context mContext) {
	//
	// // ����ѡ�����ַ�ʽ��dialog
	// ModifyAvatarDialog modifyAvatarDialog = new ModifyAvatarDialog(mContext)
	// {
	// // ѡ�񱾵����
	// @Override
	// public void doGoToImg() {
	// this.dismiss();
	// Intent intent = new Intent();
	// intent.setAction(Intent.ACTION_PICK);
	// intent.setType("image/*");
	// ((Activity) mContext).startActivityForResult(intent,
	// FLAG_CHOOSE_IMG);
	// }
	//
	// // ѡ���������
	// @Override
	// public void doGoToPhone() {
	// this.dismiss();
	// String status = Environment.getExternalStorageState();
	// if (status.equals(Environment.MEDIA_MOUNTED)) {
	// try {
	// localTempImageFileName = "";
	// localTempImageFileName = String.valueOf((new Date())
	// .getTime()) + ".png";
	// File filePath = new File(Constants.FILE_image);
	// if (!filePath.exists()) {
	// filePath.mkdirs();
	// }
	// Intent intent = new Intent(
	// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	// File f = new File(filePath, localTempImageFileName);
	// // localTempImgDir��localTempImageFileName���Լ����������
	// Uri u = Uri.fromFile(f);
	// intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
	// ((Activity) mContext).startActivityForResult(intent,
	// FLAG_CHOOSE_PHONE);
	// } catch (ActivityNotFoundException e) {
	// //
	// }
	// }
	// }
	// };
	// AlignmentSpan span = new AlignmentSpan.Standard(
	// Layout.Alignment.ALIGN_CENTER);
	// AbsoluteSizeSpan span_size = new AbsoluteSizeSpan(25, true);
	// SpannableStringBuilder spannable = new SpannableStringBuilder();
	// String dTitle = ((Activity) mContext)
	// .getString(R.string.action_settings);
	// spannable.append(dTitle);
	// spannable.setSpan(span, 0, dTitle.length(),
	// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	// spannable.setSpan(span_size, 0, dTitle.length(),
	// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	// modifyAvatarDialog.setTitle(spannable);
	// modifyAvatarDialog.show();
	// }

	public static void doGoToImg(Context mContext) {
		final File filePath = new File(Constants.FILE_image);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		((Activity) mContext).startActivityForResult(intent, FLAG_CHOOSE_IMG);
	}

	public static Uri tempUri;

	public static void doGoToPhone(Context mContext) {
		try {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = Constants.FILE_image;
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// ��sd�����Ƿ���myImage�ļ���
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// �Ƿ���headImg�ļ�
				file = new File(sdcardPathDir + System.currentTimeMillis() + ".JPEG");
			}
			if (file != null) {
				tempUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
				((Activity) mContext).startActivityForResult(openCameraIntent, FLAG_CHOOSE_PHONE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����ж�
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static void setSpinner(Spinner spinner, String str) {
		if (null == spinner)
			return;
		int length = spinner.getAdapter().getCount();
		for (int i = 0; i < length; i++) {
			String item = (String) spinner.getAdapter().getItem(i);
			if (null != item) {
				if (item.equals(str))
					spinner.setSelection(i);
			}
		}
	}

	public static Long getVersionName(Context mContext) throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		return (long) packInfo.versionCode;
		// return version;
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

		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);
	}

	public static void setListViewHeightBasedOnChildren2(Context mContext, ListView listView) {
		Resources resource = mContext.getResources();
		int tab_eighty_hight = (int) (mContext.getResources().getDisplayMetrics().density * 225);

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

		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		if (params.height >= tab_eighty_hight)
			params.height = tab_eighty_hight;
		listView.setLayoutParams(params);
	}

	// ��ѡdialog
	//
	// private static String singleDialog(Context mContext,
	// final ArrayList<String> list) {
	// final String value = null;
	//
	// }

	public static int getStatusBarHeight(Context mContext) {
		Rect frame = new Rect();
		((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	/**
	 * ��pxֵת��Ϊdip��dpֵ����֤�ߴ��С����
	 * 
	 * @param pxValue
	 * @param scale
	 *            ��DisplayMetrics��������density��
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 * 
	 * @param context
	 *            �����ģ�һ��ΪActivity
	 * @param dpValue
	 *            dp����ֵ
	 * @return px����ֵ
	 */
	public static int dip2px(Context context, int dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * ��pxֵת��Ϊspֵ����֤���ִ�С����
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * ��spֵת��Ϊpxֵ����֤���ִ�С����
	 * 
	 * @param spValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
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

	public static List<String> getStringArray(String str) {
		try {
			List<String> list = new ArrayList<String>();
			while (str.indexOf("|") != -1) {
				String temp = str.substring(0, str.indexOf("|"));
				list.add(temp);
				str = str.substring(str.indexOf("|") + 1);
			}
			list.add(str);
			return list;
		} catch (Exception e) {
		}
		return null;
	}

	public static List<String> getStringArray(String str, String splitParam) {
		try {
			List<String> list = new ArrayList<String>();
			while (str.indexOf(splitParam) != -1) {
				String temp = str.substring(0, str.indexOf(splitParam));
				list.add(temp);
				str = str.substring(str.indexOf(splitParam) + 1);
			}
			list.add(str);
			return list;
		} catch (Exception e) {
		}
		return null;
	}

	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * �鿴�ļ��Ƿ����
	 */
	public static boolean isExistFile(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * �ж϶����Ƿ�Ϊ��
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

	/**
	 * ����byte���飬�����ļ�
	 */
	public static void createFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// �ж��ļ�Ŀ¼�Ƿ����
				dir.mkdirs();
			}
			file = new File(filePath + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
	 */
	public static String readFileByLines(File file) {
		StringBuffer sb = new StringBuffer("");
		BufferedReader reader = null;
		try {
			// System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				if (!tempString.contains("BEGIN PUBLIC KEY") && !tempString.contains("END PUBLIC KEY")) {
					sb.append(tempString);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

	public static void setPreferences(Context mContext, String key, String value) {
		// ��ȡ�洢�ķ�����������Ϣ���û��ĸ�����Ϣ
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getPreferences(Context mContext, String key) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(key, null);
	}

	/**
	 * �ַ����ж��Ƿ�Ϊ��
	 * 
	 * @param s
	 * @param bak
	 * @return ����""
	 */
	public static String CS(String s, String bak) {
		if (s == null || "".equals(s))
			return bak;
		return s;
	}

	public static String CS(String s) {
		return CS(s, "");
	}

	// public static String appedHttp(String mUrl) {
	// if (null == mUrl)
	// return null;
	// if (mUrl.length() <= 0)
	// return null;
	// if (mUrl.startsWith("http://")) {
	// return mUrl;
	// }
	// DataCener aCener = DataCener.getInstance();
	// String sUri = null;
	// if (null != aCener) {
	// String sDomain = aCener.getImageServUrl();
	// if (AppUtil.isNull(sDomain))
	// return null;
	// if (!sDomain.startsWith("http://")) {
	// sDomain = new String("http://" + sDomain);
	// }
	//
	// StringBuilder sBuilder = new StringBuilder(sDomain);
	// if (!sDomain.endsWith("/") && (!mUrl.startsWith("/"))) {
	// sBuilder.append("/");
	// }
	// sBuilder.append(mUrl.trim());
	// sUri = sBuilder.toString();
	// }
	// return sUri;
	// }

	// ȥ��
	public static String removeHead(String date) {
		if (AppUtil.isNull(date))
			return null;
		if (date.startsWith("0")) {
			return date.substring(1);
		}
		return date;
	}

	// �ؼ��Ƿ���ʾ
	public static boolean isVisible(View v) {
		if (v.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}

	public static final int WEEKDAYS = 7;

	public static String[] WEEK = { "������", "����һ", "���ڶ�", "������", "������", "������", "������" };

	/**
	 * ���ڱ���ת�ɶ�Ӧ�������ַ���
	 * 
	 * @param date
	 * @return
	 */
	public static String DateToWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) {
			return null;
		}
		return WEEK[dayIndex - 1];
	}

	/**
	 * ��������TextView������
	 * 
	 * @param context
	 * @param root
	 * @param fontName
	 */
	public static void applyFont(final Context context, final View root, final String fontName) {
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					applyFont(context, viewGroup.getChildAt(i), fontName);
			} else if (root instanceof TextView)
				((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

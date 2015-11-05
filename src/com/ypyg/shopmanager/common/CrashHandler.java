package com.ypyg.shopmanager.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * �쳣��־��
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private static Object classLock = CrashHandler.class;

	// ϵͳĬ�ϵ�uncaughtException������
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	// crash handleʵ��
	private static CrashHandler sInstance = null;

	// context
	private Context mContext = null;

	// �����洢�豸��Ϣ���쳣��Ϣ
	private final Map<String, String> infos = new HashMap<String, String>();

	// ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
	private final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss", Constants.getDefaultLocale());

	private final static String CRASH_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ Constants.prePath + "crash/";

	private final boolean isSaveFileLog = true;

	public static CrashHandler getInstance() {
		synchronized (classLock) {
			if (sInstance == null) {
				sInstance = new CrashHandler();
			}
			return sInstance;
		}
	}

	private CrashHandler() {
	}

	public void init(Context ctx) {
		mContext = ctx;
		// ��ȡϵͳĬ�ϵ�uncaughtException������
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// ���ø�CrashHandleΪ�����Ĭ�ϴ�����
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	// ��uncaughtExcepton����ʱ��ת��ú���������
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handleException(ex);
		if (mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}

	}

	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
	 * 
	 * @param ex
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null || !isSaveFileLog) {
			return false;
		}
		// �ռ��豸������Ϣ
		collectDeviceInfo(mContext);
		// ������־�ļ�
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * �ռ��豸������Ϣ
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
				infos.put("pruduct", android.os.Build.MANUFACTURER);
				infos.put("modle", android.os.Build.MODEL);
				infos.put("SDK",
						String.valueOf(android.os.Build.VERSION.SDK_INT));
			}
		} catch (NameNotFoundException e) {
		}
	}

	/**
	 * ���������Ϣ���ļ���
	 * 
	 * @param ex
	 * @return �����ļ�����,��exд�뵽sdcard�ļ���
	 */
	private void saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();

		// �����������
		long timestamp = System.currentTimeMillis();
		String time = formatter.format(new Date());
		String title = "\n\n\n\n crash-" + time + "-" + timestamp + "\n";
		sb.append(title);
		// ���������Ϣ
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		// ��¼�����ĵ���ջ
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File path = new File(CRASH_PATH);
				if (!path.exists()) {
					path.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(CRASH_PATH
						+ "crash.log", true);
				fos.write(sb.toString().getBytes());
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
		}
	}
}

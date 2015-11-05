package com.ypyg.shopmanager.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

import com.ypyg.shopmanager.common.Constants;

public class Log_ {
	private static final String FileName = "/hzganggangedu/log.txt";
	// ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH-mm-ss  ", Constants.getDefaultLocale());

	public static void v(String tag, String msg) {
		if (Constants.DEBUG) {
			android.util.Log.v(tag, msg);
			try {
				saveToSDCard(msg);
			} catch (Exception e) {
			}
		}
	}
	public static void v(String tag, String msg, Throwable tr) {
		if (Constants.DEBUG) {
			android.util.Log.v(tag, msg, tr);
			try {
				saveToSDCard(msg, tr);
			} catch (Exception e) {
			}
		}
	}
	public static void d(String tag, String msg) {
		if (Constants.DEBUG) {
			android.util.Log.d(tag, msg);
			try {
				saveToSDCard(msg);
			} catch (Exception e) {
			}
		}
	}
	public static void d(String tag, String msg, Throwable tr) {
		if (Constants.DEBUG) {
			android.util.Log.d(tag, msg, tr);
			try {
				saveToSDCard(msg, tr);
			} catch (Exception e) {
			}
		}
	}
	public static void i(String tag, String msg) {
		if (Constants.DEBUG) {
			android.util.Log.i(tag, msg);
			try {
				saveToSDCard(msg);
			} catch (Exception e) {
			}
		}
	}
	public static void i(String tag, String msg, Throwable tr) {
		if (Constants.DEBUG) {
			android.util.Log.i(tag, msg, tr);
			try {
				saveToSDCard(msg, tr);
			} catch (Exception e) {
			}
		}
	}
	public static void w(String tag, String msg) {
		if (Constants.DEBUG) {
			android.util.Log.w(tag, msg);
			try {
				saveToSDCard(msg);
			} catch (Exception e) {
			}
		}
	}
	public static void w(String tag, String msg, Throwable tr) {
		if (Constants.DEBUG) {
			android.util.Log.w(tag, msg, tr);
			try {
				saveToSDCard(msg, tr);
			} catch (Exception e) {
			}
		}
	}
	public static void w(String tag, Throwable tr) {
		if (Constants.DEBUG) {
			android.util.Log.w(tag, tr);
			try {
				saveToSDCard(tag, tr);
			} catch (Exception e) {
			}
		}
	}
	public static void e(String tag, String msg) {
		if (Constants.DEBUG) {
			android.util.Log.e(tag, msg);
			try {
				saveToSDCard(msg);
			} catch (Exception e) {
			}
		}
	}
	public static void e(String tag, String msg, Throwable tr) {
		if (Constants.DEBUG) {
			android.util.Log.e(tag, msg, tr);
			try {
				saveToSDCard(msg, tr);
			} catch (Exception e) {
			}
		}
	}
	/**
	 * ��sdcard��д���ļ�
	 * 
	 * @param filename
	 *            �ļ���
	 * @param content
	 *            �ļ�����
	 */
	public static void saveToSDCard(String content) throws Exception {
		File file = new File(Environment.getExternalStorageDirectory(),
				FileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		// RandomAccessFile file2=new RandomAccessFile(file,"rw" );
		// file2.seek();
		// OutputStream out = new FileOutputStream(file);
		// out.write(content.getBytes());
		// out.close();

		StringBuffer sb = new StringBuffer();

		// �����������
		long timestamp = System.currentTimeMillis();
		String time = formatter.format(new Date());
		String title = "\n\n data-" + time + "-" + timestamp + "\n";
		sb.append(title);
		sb.append("\t" + content + "\t");
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				FileOutputStream fos = new FileOutputStream(file, true);
				fos.write(sb.toString().getBytes());
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
		}
	}
	/**
	 * ��sdcard��д���ļ�
	 * 
	 * @param filename
	 *            �ļ���
	 * @param content
	 *            �ļ�����
	 */
	public static void saveToSDCard(String content, Throwable ex)
			throws Exception {
		File file = new File(Environment.getExternalStorageDirectory(),
				FileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		// RandomAccessFile file2=new RandomAccessFile(file,"rw" );
		// file2.seek();
		// OutputStream out = new FileOutputStream(file);
		// out.write(content.getBytes());
		// out.close();

		StringBuffer sb = new StringBuffer();

		// �����������
		long timestamp = System.currentTimeMillis();
		String time = formatter.format(new Date());
		String title = "\n\n log-" + time + "-" + timestamp + "\n";
		sb.append(title);
		sb.append("\t" + content + "\t");
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
				FileOutputStream fos = new FileOutputStream(file, true);
				fos.write(sb.toString().getBytes());
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
		}
	}
}

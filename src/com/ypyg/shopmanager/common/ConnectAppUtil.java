package com.ypyg.shopmanager.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectAppUtil {

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
	
	public static String format(Long date) {
		String time = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			time = df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
}

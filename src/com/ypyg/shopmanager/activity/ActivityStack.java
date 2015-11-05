package com.ypyg.shopmanager.activity;

import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;

/**
 * activity��ջ���?������������activityʱѹ�룬���activityʱ�Ƴ� Class: ActivityStack
 */
public class ActivityStack {
	// activity��ջ
	private static Stack<Activity> activites = new Stack<Activity>();

	private ActivityStack() {

	}

	/**
	 * �ر�activity�����Ӷ�ջ���Ƴ�
	 */
	public static void finish(Activity activity) {
		if (activity != null) {
			activity.finish();
			// �Ӷ�ջ���Ƴ�
			activites.remove(activity);
			activity = null;
		}
	}

	/**
	 * ��ȡ��ջ����activity
	 */
	public static Activity getTopActivity() {
		if (!activites.isEmpty()) {
			return activites.pop();
		}
		return null;
	}

	/**
	 * ��ȡջ����activity���Ƴ� Method: peekActivity
	 * <p>
	 * Author: yc
	 * <p>
	 * Description:
	 * <p>
	 * Modified: 2013-6-14
	 * 
	 * @return
	 */
	public static Activity peekActivity() {
		if (null != activites) {
			if (!activites.isEmpty()) {
				return activites.peek();
			}
		}
		return null;
	}

	/**
	 * ��ȡ��ջ�׵�activity
	 */
	public static Activity getLastActivity() {
		if (!activites.isEmpty()) {
			return activites.lastElement();
		}
		return null;
	}

	/**
	 * �Ƴ�ջ����activity
	 */
	public static void pop(Activity activity) {
		if (activity != null) {
			activites.remove(activity);
		}
	}

	/**
	 * ��ջ��ѹ��activity
	 */
	public static void push(Activity activity) {
		if (activity != null && null != activites) {
			activites.push(activity);
		}
	}

	/**
	 * ���ջ��activity,�����ջ
	 */
	public static void finishAll() {
		while (!activites.isEmpty()) {
			finish(getTopActivity());
		}
		activites.clear();
	}

	/**
	 * ��ȡջ�Ĵ�С
	 */
	public static int size() {
		if (null != activites) {
			return activites.size();
		}
		return 0;
	}

	public static ArrayList<Activity> getActivityList() {
		return new ArrayList<Activity>(activites);
	}
}

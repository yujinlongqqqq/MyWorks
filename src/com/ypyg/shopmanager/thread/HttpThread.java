package com.ypyg.shopmanager.thread;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

public class HttpThread extends Thread implements UncaughtExceptionHandler {

	public HttpClient defaultClient;

	public HttpRequestBase mDefaultPost;

	public HttpThread(String name, int priority) {
		this(name, priority, null);
	}

	public HttpThread(String name, int priority, Runnable ruannable) {
		super(ruannable);
		setName(name);
		setPriority(priority);
		setUncaughtExceptionHandler(this);
	}

	public HttpThread(ThreadGroup group, Runnable runnable, String threadName,
			long stackSize) {
		super(group, runnable, threadName, stackSize);
		setUncaughtExceptionHandler(this);
	}

	public void abort() {
		interrupt();
		try {
			if (mDefaultPost != null && (!mDefaultPost.isAborted())) {
				mDefaultPost.abort();
			}
		} catch (Exception e) {

		}
		defaultClient = null;
	}

	public boolean isAbort() {
		return mDefaultPost == null || mDefaultPost.isAborted();
	}

	public HttpRequestBase getDefaultPost() {
		return mDefaultPost;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			if (defaultClient != null) {
				if (mDefaultPost != null && (!mDefaultPost.isAborted())) {
					mDefaultPost.abort();
				}
				defaultClient = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ex != null) {
			ex.printStackTrace();
		}

	}

	@Override
	protected void finalize() throws Throwable {
		uncaughtException(this, null);

		super.finalize();

	}

}

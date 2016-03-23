package com.wipe.zc.journey.util;

import android.util.Log;

public class LogUtil {

	private static boolean isDebug = true;// log开关，发布应用前值为false

	/**
	 * 打印i级别的log
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	/**
	 * 打印i级别的log
	 * 
	 * @param object
	 * @param msg
	 */
	public static void i(Object object, String msg) {
		if (isDebug) {
			Log.i(object.getClass().getSimpleName(), msg);
		}
	}

	/**
	 * 打印e级别的log
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}

	/**
	 * 打印e级别的log
	 * 
	 * @param object
	 * @param msg
	 */
	public static void e(Object object, String msg) {
		if (isDebug) {
			Log.e(object.getClass().getSimpleName(), msg);
		}
	}
}

package com.wipe.zc.journey.util;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * SharedPreferences工具类
 * @author hp
 *
 */
public class SharedPreUtil {

	private static final String CONFIG = "config";
	
	/**
	 * 保存String
	 * @param context：上下文
	 * @param key：保存名
	 * @param value：保存String值
	 */
	public static void putString(Context context,String key,String value){
		SharedPreferences shared = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		shared.edit().putString(key, value).apply();
	}
	
	/**
	 * 获取String
	 * @param context：上下文
	 * @param key：获取名
	 * @param defValue：未找到时的默认String值
	 * @return	获取到的value
	 */
	public static String getString(Context context,String key,String defValue){
		SharedPreferences shared = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		return shared.getString(key, defValue);
	}

	/**
	 * 保存String
	 * @param context：上下文
	 * @param key：保存名
	 * @param value：保存boolean值
	 */
	public static void putBoolean(Context context,String key,boolean value){
		SharedPreferences shared = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		shared.edit().putBoolean(key, value).apply();
	}
	
	/**
	 * 获取String
	 * @param context：上下文
	 * @param key：获取名
	 * @param defValue：未找到时的默认boolean值
	 * @return	获取到的value
	 */
	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences shared = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		return shared.getBoolean(key, defValue);
	}

	/**
	 * 保存String
	 * @param context：上下文
	 * @param key：保存名
	 * @param value：保存int值
	 */
	public static void putInt(Context context,String key,int value){
		SharedPreferences shared = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		shared.edit().putInt(key, value).apply();
	}
	
	/**
	 * 获取String
	 * @param context：上下文
	 * @param key：获取名
	 * @param defValue：未找到时的默认int值
	 * @return	获取到的value
	 */
	public static int getInt(Context context,String key,int defValue){
		SharedPreferences shared = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		return shared.getInt(key, defValue);
	}
}

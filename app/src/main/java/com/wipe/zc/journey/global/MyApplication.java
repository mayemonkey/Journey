package com.wipe.zc.journey.global;

import java.util.Calendar;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	
	private static int total_year = Calendar.getInstance().get(Calendar.YEAR);
	
	private static Context context;
	
	//登陆完成，全局使用昵称
	private static String nickname;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		SDKInitializer.initialize(context);
	}
	
	public static int getTotal_year() {
		return total_year;
	}

	public static void setTotal_year(int total_year) {
		MyApplication.total_year = total_year;
	}



	/**
	 * 获取应用上下文
	 * @return
	 */
	public static Context getContext(){
		return context;
	}

	public static String getNickname() {
		return nickname;
	}

	public static void setNickname(String nickname) {
		MyApplication.nickname = nickname;
	}

}

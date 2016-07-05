package com.wipe.zc.journey.util;

import android.widget.Toast;

import com.wipe.zc.journey.global.MyApplication;

public class ToastUtil {

	private static Toast toast;
	
	/**
	 * 短暂时间Toast
	 * @param context
	 * @param text
	 */
	public static void shortToast(String text){
		if(toast == null){
			toast = Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/**
	 * 长时间Toast
	 * @param text
	 */
	public static void longToast(String text){
		if(toast == null){
			toast = Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}
	
}

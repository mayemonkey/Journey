package com.wipe.zc.journey.util;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

public class ViewUtil {

	public static boolean checkEmptyData(EditText editText, View view) {
		String name = editText.getText().toString();
		if (TextUtils.isEmpty(name)) {
			// 空输入提示
			TranslateAnimation anim = new TranslateAnimation(0, 20, 0, 0);
			anim.setDuration(300);
			anim.setInterpolator(new CycleInterpolator(2));
			view.startAnimation(anim);
			return false;
		}
		return true;
	}
}

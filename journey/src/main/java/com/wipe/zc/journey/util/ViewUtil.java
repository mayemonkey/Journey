package com.wipe.zc.journey.util;

import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;

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

	/**
	 * 验证时动画效果
	 * @param imageView    ImageView
	 * @param textView	   TextView
	 */
	public static void executeAnimation(ImageView imageView,TextView textView){
		final ImageView iv = imageView;
		ViewPropertyAnimator.animate(textView).scaleX(0.0f).alpha(0.0f).setDuration(500).start();
		iv.setVisibility(View.VISIBLE);
		ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(500).addUpdateListener(new ValueAnimator
				.AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				float value = (float) valueAnimator.getAnimatedValue();
				iv.setAlpha(value);
			}
		});
		RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(20);
		ra.setInterpolator(new AccelerateDecelerateInterpolator());
		ra.setRepeatMode(RotateAnimation.RESTART);
		iv.startAnimation(ra);
	}

	public static void recoverAnimatin(ImageView imageView,TextView textView){
		ViewPropertyAnimator.animate(textView).scaleX(1.0f).alpha(1.0f).setDuration(500).start();
		imageView.clearAnimation();
		imageView.setVisibility(View.INVISIBLE);
	}


}



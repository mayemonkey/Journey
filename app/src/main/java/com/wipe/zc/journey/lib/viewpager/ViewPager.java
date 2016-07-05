package com.wipe.zc.journey.lib.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ViewPager extends android.support.v4.view.ViewPager {
	public ViewPager(Context paramContext) {
		super(paramContext);
	}

	public ViewPager(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(paramMotionEvent);
	}

	protected void onMeasure(int paramInt1, int paramInt2) {
		int i = 0;
		for (int j = 0;; j++) {
			if (j >= getChildCount()) {
				super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(i, 1073741824));
				return;
			}
			View localView = getChildAt(j);
			localView.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(0, 0));
			int k = localView.getMeasuredHeight();
			if (k > i)
				i = k;
		}
	}
}
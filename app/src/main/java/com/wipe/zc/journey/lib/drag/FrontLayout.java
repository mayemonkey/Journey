package com.wipe.zc.journey.lib.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.wipe.zc.journey.lib.drag.SwipeLayout.Status;


public class FrontLayout extends RelativeLayout {

	private SwipeLayoutInterface mISwipeLayout;

	public FrontLayout(Context context) {
		super(context);
	}

	public FrontLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FrontLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setSwipeLayout(SwipeLayoutInterface mSwipeLayout){
		this.mISwipeLayout = mSwipeLayout;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(mISwipeLayout.getCurrentStatus() == Status.Close){
			return super.onInterceptTouchEvent(ev);
		}else {
			return true;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mISwipeLayout.getCurrentStatus() == Status.Close){
			return super.onTouchEvent(event);
		}else {
			if(event.getActionMasked() == MotionEvent.ACTION_UP){
				mISwipeLayout.close();
			}
			return true;
		}
	}

}
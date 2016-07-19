package com.wipe.zc.journey.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wipe.zc.journey.R;

public class RefreshListView extends ListView {

	private int headerHeight;
	private int downY = -1;
	private int headerState = PULLDOWN_STATE;
	private static final int PULLDOWN_STATE = 0;
	private static final int RELESE_STATE = 1;
	private static final int REFRESH_STATE = 2;

	private ProgressBar pb_cuslv_header;
	private View header_view;

	private OnRefreshListener listener;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeader(context);
		initAnimation();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeader(context);
		initAnimation();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeader(context);
		initAnimation();
	}

	/**
	 * 初始化头布局
	 * 
	 * @param context		上下文
	 */
	public void initHeader(Context context) {
		header_view = View.inflate(context, R.layout.item_lv_header, null);
		// 隐藏头布局
		// 1.测量头布局高度
		header_view.measure(0, 0);
		headerHeight = header_view.getMeasuredHeight();
		header_view.setPadding(0, -headerHeight, 0, 0);


		// 隐藏ProgressBar
		pb_cuslv_header = (ProgressBar) header_view.findViewById(R.id.pb_lv_header);
		pb_cuslv_header.setVisibility(View.INVISIBLE);

		this.addHeaderView(header_view);
	}

	/**
	 * 初始化动画
	 */
	public void initAnimation() {
		RotateAnimation ra_up = new RotateAnimation(
				0, -180, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);

		ra_up.setFillAfter(true);
		ra_up.setDuration(500);

		RotateAnimation ra_down = new RotateAnimation(
				-180, -360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);

		ra_down.setFillAfter(true);
		ra_down.setDuration(500);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();

			break;

		case MotionEvent.ACTION_MOVE:
			// 判断：当处在ViewPager处在第一行，并且是从上往下滑，处理事件,改变状态
			if (getFirstVisiblePosition() != 0) {
				break;
			}
			if(headerState == REFRESH_STATE){
				break;
			}

			if (downY == -1) {
				downY = (int) ev.getY();
			}
			// 获取移动中的Y坐标
			int moveY = (int) ev.getY();
			int offsetY = moveY - downY;

			if (offsetY > 0) { // 从上往下移动
				int headerPadding = offsetY - headerHeight;

				// 判断头布局状态
				if (headerPadding < 0 && headerState != PULLDOWN_STATE) {
					headerState = PULLDOWN_STATE;
					switchState(headerState);

				} else if (headerPadding > 0 && headerState != RELESE_STATE) {
					headerState = RELESE_STATE;
					switchState(headerState);
				}

				header_view.setPadding(0, headerPadding, 0, 0);
				return true;
			}

			break;

		case MotionEvent.ACTION_UP: // 抬起
			if (headerState == PULLDOWN_STATE) {
				header_view.setPadding(0, -headerHeight, 0, 0);

			} else if (headerState == RELESE_STATE) {
				headerState = REFRESH_STATE;
				header_view.setPadding(0, 0, 0, 0);
				switchState(headerState);

				if (listener != null) {
					listener.refreshing();
				}
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 根据状态处理
	 * 
	 * @param headerState	HeaderView状态
	 */
	private void switchState(int headerState) {
		if (headerState == RELESE_STATE) {

		}

		if (headerState == PULLDOWN_STATE) {

		}

		if (headerState == REFRESH_STATE) {
			pb_cuslv_header.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 向外提供恢复头布局方法
	 */
	public void reviewHeader() {
		// 隐藏标题栏
		header_view.setPadding(0, -headerHeight, 0, 0);
		pb_cuslv_header.setVisibility(View.INVISIBLE);
		headerState = PULLDOWN_STATE;

	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.listener = listener;
	}

	public interface OnRefreshListener {
		void refreshing();
	}

}

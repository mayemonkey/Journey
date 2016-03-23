package com.wipe.zc.journey.lib.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.wipe.zc.journey.R;

public class MonkeyCalendarHorizontal extends LinearLayout {

	private LinearLayout ll_monkeycalendar_horizontal;
	private MonkeyDateHorizontal mPreviousSelectedDate;
	private Calendar date_now = Calendar.getInstance();
	private Calendar date_selected = Calendar.getInstance();
	private Context context;

	private ArrayList<Calendar> list_record;
	private int windowWidth;
	private int startX;
	private int offsetLeft = 0;
	private int dateWidth;
	private boolean firstFill = true;

	public interface OnMonthChangeListener {
		public void OnMonthChange(Calendar date);
	}

	private OnMonthChangeListener mMonthChangeListener;

	public void setOnMonthChangeListener(OnMonthChangeListener mMonthChangeListener) {
		this.mMonthChangeListener = mMonthChangeListener;
	}

	public interface OnDateSelectedListener {
		public void onDateSelected(Calendar date);
	}

	private OnDateSelectedListener mDateSelectedListener;

	public void setOnDateSelectedListener(OnDateSelectedListener mDateSelectedListener) {
		this.mDateSelectedListener = mDateSelectedListener;
	}

	public MonkeyCalendarHorizontal(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MonkeyCalendarHorizontal(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setWindowWidth();
		init(context);
	}

	public MonkeyCalendarHorizontal(Context context) {
		super(context);
		this.context = context;
		setWindowWidth();
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.layout_monkeycalendar_horizontal, this);
		ll_monkeycalendar_horizontal = (LinearLayout) findViewById(R.id.ll_monkeycalendar_horizontal);
		fillDateView();

	}

	/**
	 * 添加
	 */
	private void fillDateView() {
		// 清除所有子控件
		ll_monkeycalendar_horizontal.removeAllViews();
		// TODO 填充日历内容
		dateWidth = getWindowWidth() / 7;

		MonkeyDateHorizontal date;
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// lp.weight = 1;
		lp.width = dateWidth;

		int mostdate = date_now.getActualMaximum(Calendar.DAY_OF_MONTH);
		int day = 1;
		for (int i = 0; i < mostdate; i++) {
			date = new MonkeyDateHorizontal(context);
			date.setLayoutParams(lp);
			date.setBackgroundColor(Color.parseColor("#F9F9F9"));
			date.setGravity(Gravity.CENTER);
			// 星期字体
			date.setWeekTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			date.setWeekTextColor(Color.parseColor("#BDBDBD"));
			// 日期字体
			date.setDateTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			date.setDateTextColor(Color.parseColor("#11111C"));
			date.setPointVisiable(true);

			// 设置年月日
			date.setYear(date_now.get(Calendar.YEAR));
			date.setMonth(date_now.get(Calendar.MONTH));
			date.setDay(day);

			// 设置触摸监听
			date.setOnTouchListener(dateClickedListener);
			date_now.set(Calendar.DAY_OF_MONTH, day);

			// 当前日期是否存在记录
			if (isRecord(date_now)) {
				date.setPointVisiable(true);
			} else {
				date.setPointVisiable(false);
			}

			// 今天
			if (isToday(date_now)) {
				mPreviousSelectedDate = date;
				date.setWeekTextColor(Color.parseColor("#BDBDBD"));
				date.setDateTextColor(Color.parseColor("#D73C10"));
				if (date_selected.get(Calendar.MONTH) == date_now.get(Calendar.MONTH)
						&& date_selected.get(Calendar.DAY_OF_MONTH) == day) { // 本日且被选中
					date.setWeekTextColor(Color.WHITE);
					date.setBackgroundColor(Color.parseColor("#52d2c4"));
					date.setPointBackground(R.drawable.icon_point_white);

				}
				if (firstFill) {
					// 在中间移动范围内
					if (date_now.get(Calendar.DAY_OF_MONTH) > 4 && date_now.get(Calendar.DAY_OF_MONTH) <= mostdate - 3) {
						int day_of_month = date_now.get(Calendar.DAY_OF_MONTH);
						ll_monkeycalendar_horizontal.scrollTo(dateWidth * (day_of_month - 4), 0);
						offsetLeft += dateWidth * (day_of_month - 4);
					}
					// 在后段范围内
					if (date_now.get(Calendar.DAY_OF_MONTH) > (mostdate - 3)
							&& date_now.get(Calendar.DAY_OF_MONTH) <= mostdate) {
						ll_monkeycalendar_horizontal.scrollTo(dateWidth * (mostdate - 7), 0);
						offsetLeft += dateWidth * (mostdate - 7);
					}
				}
			}
			// 选中
			else if (date_selected.get(Calendar.MONTH) == date_now.get(Calendar.MONTH)
					&& date_selected.get(Calendar.DAY_OF_MONTH) == day) {
				mPreviousSelectedDate = date;
				date.setWeekTextColor(Color.WHITE);
				date.setDateTextColor(Color.WHITE);
				date.setPointBackground(R.drawable.icon_point_blue);
				date.setBackgroundColor(Color.parseColor("#52d2c4"));
			}
			date.setDateText(String.valueOf(day++));
			date.setWeekText(date_now.get(Calendar.DAY_OF_WEEK));
			ll_monkeycalendar_horizontal.addView(date);
		}
	}

	// 获取选中的日期
	public Calendar getSelectedDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, date_selected.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, date_selected.get(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, date_selected.get(Calendar.DAY_OF_MONTH));
		return calendar;
	}

	/**
	 * 是否存在记录
	 * 
	 * @param date
	 * @return
	 */
	private boolean isRecord(Calendar date) {
		if (list_record != null) {
			for (Calendar record_date : list_record) {
				// 当前日期存在记录
				if (date.get(Calendar.YEAR) == record_date.get(Calendar.YEAR)
						&& date.get(Calendar.MONTH) == record_date.get(Calendar.MONTH)
						&& date.get(Calendar.DAY_OF_MONTH) == record_date.get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是今天
	 * 
	 * @param date
	 * @return
	 */
	private boolean isToday(Calendar date) {
		Calendar today = Calendar.getInstance();

		return date.get(Calendar.YEAR) == today.get(Calendar.YEAR)
				&& date.get(Calendar.MONTH) == today.get(Calendar.MONTH)
				&& date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 方法重载
	 * 
	 * @param yaer
	 * @param month
	 * @param day
	 * @return
	 */
	private boolean isToday(int yaer, int month, int day) {
		Calendar today = Calendar.getInstance();

		return yaer == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH)
				&& day == today.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 设置设备的宽度
	 */
	public void setWindowWidth() {
		Activity activity = (Activity) context;
		Display d = activity.getWindowManager().getDefaultDisplay();
		windowWidth = d.getWidth();
	}

	private int getWindowWidth() {
		return windowWidth;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 获取日期控件宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);
		dateWidth = width / 7;
	}

	/**
	 * 本月日期点击事件
	 */
	private OnTouchListener dateClickedListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				MonkeyDateHorizontal tv = (MonkeyDateHorizontal) v;
				Calendar selected = Calendar.getInstance();
				// TODO DEBUG日期
				selected.set(Calendar.YEAR, tv.getYear());
				selected.set(Calendar.MONTH, tv.getMonth());
				selected.set(Calendar.DAY_OF_MONTH, tv.getDay());
				if (!isToday(selected)) { // 选中的不是当天
					tv.setDateTextColor(Color.WHITE);
				} else {
					tv.setDateTextColor(Color.parseColor("#D73C10"));
				}
				tv.setWeekTextColor(Color.WHITE);
				tv.setPointBackground(R.drawable.icon_point_white);
				// tv.setPadding(0, 8, 0, 8);
				tv.setBackgroundColor(Color.parseColor("#52d2c4"));
				if (mPreviousSelectedDate != null) {
					if (mPreviousSelectedDate != tv) {
						try {
							if (isToday(mPreviousSelectedDate.getYear(), mPreviousSelectedDate.getMonth(),
									mPreviousSelectedDate.getDay())) {
								mPreviousSelectedDate.setWeekTextColor(Color.parseColor("#BDBDBD"));
								mPreviousSelectedDate.setDateTextColor(Color.parseColor("#D73C10"));
								mPreviousSelectedDate.setPointBackground(R.drawable.icon_point_blue);
								mPreviousSelectedDate.setBackgroundColor(Color.parseColor("#F9F9F9"));
							} else {
								mPreviousSelectedDate.setWeekTextColor(Color.parseColor("#BDBDBD"));
								mPreviousSelectedDate.setDateTextColor(Color.parseColor("#11111C"));
								mPreviousSelectedDate.setPointBackground(R.drawable.icon_point_blue);
								mPreviousSelectedDate.setBackgroundColor(Color.parseColor("#F9F9F9"));
							}
						} catch (Exception ex) {
							mPreviousSelectedDate.setWeekTextColor(Color.parseColor("#BDBDBD"));
							mPreviousSelectedDate.setDateTextColor(Color.parseColor("#11111C"));
							mPreviousSelectedDate.setPointBackground(R.drawable.icon_point_blue);
							mPreviousSelectedDate.setBackgroundColor(Color.parseColor("#F9F9F9"));
						}
					}
				}
				// 设置选中的日期
				int selectedDay = Integer.parseInt(((MonkeyDateHorizontal) v).getDateText().toString());
				date_selected.set(Calendar.YEAR, date_now.get(Calendar.YEAR));
				date_selected.set(Calendar.MONTH, date_now.get(Calendar.MONTH));
				date_selected.set(Calendar.DAY_OF_MONTH, selectedDay);
				mPreviousSelectedDate = (MonkeyDateHorizontal) v;
				if (mDateSelectedListener != null)
					mDateSelectedListener.onDateSelected(date_selected);
				break;
			default:
				break;
			}
			return true;
		}
	};
	private int start;
	private int overLeft;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN: // 按下记录
			start = (int) ev.getX();
			startX = (int) ev.getX();
			// System.out.println(startX);
			break;

		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int offset = moveX - startX;
			// System.out.println(offset);
			if (offset > 0) {// 认定为右移 上月
				// 月内移动
				ll_monkeycalendar_horizontal.scrollBy(-offset, 0);
				if (offsetLeft > 0) { // 超出左端
					offsetLeft += -offset;
					if (overLeft > 0) { // 回转操作
						overLeft += -offset;
					}
				} else { // 月初部分
					overLeft += -offset;
				}

				// System.out.println("右移" + offsetLeft);
			} else { // 认定为左移 下月
				int width = dateWidth * date_now.getActualMaximum(Calendar.DAY_OF_MONTH);
				ll_monkeycalendar_horizontal.scrollBy(-offset, 0);
				if (offsetLeft < width - windowWidth) { // 超出右端
					offsetLeft += -offset;
					if (overLeft < 0) { // 回转操作
						overLeft += -offset;
					}
				} else { // 月末部分
					overLeft += -offset; // 超出
				}
			}
			System.out.println("左移" + offsetLeft);
			System.out.println(overLeft);
			startX = (int) ev.getX();
			break;

		case MotionEvent.ACTION_UP:
			int endX = (int) ev.getX();
			if (endX > start) { // 右滑动处理
				if (overLeft <= -windowWidth / 2) { // 距离大,换至上月
					date_now.add(Calendar.MONTH, -1);
					date_now.set(Calendar.DAY_OF_MONTH, 1);
					int mostday = date_now.getActualMaximum(Calendar.DAY_OF_MONTH);
					fillDateView();
					// 动画效果
					ll_monkeycalendar_horizontal.scrollTo(0, 0);
					TranslateAnimation ta = new TranslateAnimation(-windowWidth, 0, 0, 0);
					ta.setDuration(500);
					ta.setRepeatMode(Animation.RESTART);
					ll_monkeycalendar_horizontal.startAnimation(ta);
					// 归零数据
					offsetLeft = 0;
					overLeft = 0;
					// 移动位置
					ll_monkeycalendar_horizontal.scrollTo(dateWidth * (mostday - 7), 0);
					offsetLeft += dateWidth * (mostday - 7);
					if (mMonthChangeListener != null) {
						mMonthChangeListener.OnMonthChange(date_now);
					}
				} else if (overLeft != 0) { // 距离小，还原本月
					ll_monkeycalendar_horizontal.scrollBy(-overLeft, 0);
					offsetLeft = 0;
					overLeft = 0;
				}
				return true;
			} else if (endX < start) { // 左滑动处理
				if (overLeft >= windowWidth / 2) { // 距离大,换至下月
					date_now.add(Calendar.MONTH, 1);
					date_now.set(Calendar.DAY_OF_MONTH, 1);
					fillDateView();
					// 动画效果
					ll_monkeycalendar_horizontal.scrollTo(0, 0);
					TranslateAnimation ta = new TranslateAnimation(0, -windowWidth, 0, 0);
					ta.setDuration(500);
					ta.setRepeatMode(Animation.RESTART);
					ll_monkeycalendar_horizontal.startAnimation(ta);
					// 归零数据
					offsetLeft = 0;
					overLeft = 0;
					if (mMonthChangeListener != null) {
						mMonthChangeListener.OnMonthChange(date_now);
					}
				} else if (overLeft != 0) { // 距离小，还原本月
					ll_monkeycalendar_horizontal.scrollBy(-overLeft, 0);
					int width = date_now.getActualMaximum(Calendar.DAY_OF_MONTH) * dateWidth;
					offsetLeft = width - windowWidth;
					overLeft = 0;
				}
				return true;
			} else {
				return false;
			}
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 设置记录集合
	 * 
	 * @param list
	 */
	public void setRecordList(List<Calendar> list) {
		list_record = (ArrayList<Calendar>) list;
		fillDateView();
	}

	/**
	 * 返回当天
	 */
	public void backToToday() {
		Calendar calendar = Calendar.getInstance();
		date_now = calendar;
		fillDateView();
		if (mMonthChangeListener != null) {
			mMonthChangeListener.OnMonthChange(date_now);
		}
	}

	/**
	 * 改变日期为
	 * 
	 * @param calendar
	 */
	public void changeDateTo(Calendar calendar) {
		// 同年变化
		if (calendar.get(Calendar.YEAR) == date_now.get(Calendar.YEAR)) {
			if (calendar.get(Calendar.MONTH) != date_now.get(Calendar.MONTH)) { // 不同月变化
				date_now = calendar;
				fillDateView();
				ll_monkeycalendar_horizontal.scrollTo(0, 0);
				if (calendar.get(Calendar.MONTH) > date_now.get(Calendar.MONTH)) { // 下月
					TranslateAnimation ta = new TranslateAnimation(0.0F, -windowWidth, 0.0F, 0.0F);
					ta.setDuration(500);
					ta.setRepeatMode(TranslateAnimation.RESTART);
					ll_monkeycalendar_horizontal.startAnimation(ta);
				}
				if (calendar.get(Calendar.MONTH) < date_now.get(Calendar.MONTH)) { // 上月
					TranslateAnimation ta = new TranslateAnimation(0.0F, -windowWidth, 0.0F, 0.0F);
					ta.setDuration(500);
					ta.setRepeatMode(TranslateAnimation.RESTART);
					ll_monkeycalendar_horizontal.startAnimation(ta);
				}
				offsetLeft = 0;
				overLeft = 0;
				if (this.mMonthChangeListener != null) {
					this.mMonthChangeListener.OnMonthChange(date_now);
				}
			}
		}else{	//不同年
			date_now = calendar;
			fillDateView();
			ll_monkeycalendar_horizontal.scrollTo(0, 0);
			if(calendar.get(Calendar.YEAR) > date_now.get(Calendar.YEAR)){
				TranslateAnimation ta = new TranslateAnimation(0.0F, -windowWidth, 0.0F, 0.0F);
				ta.setDuration(500);
				ta.setRepeatMode(TranslateAnimation.RESTART);
				ll_monkeycalendar_horizontal.startAnimation(ta);
			}
			if(calendar.get(Calendar.YEAR) < date_now.get(Calendar.YEAR)){
				TranslateAnimation ta = new TranslateAnimation(0.0F, -windowWidth, 0.0F, 0.0F);
				ta.setDuration(500);
				ta.setRepeatMode(TranslateAnimation.RESTART);
				ll_monkeycalendar_horizontal.startAnimation(ta);
			}
			offsetLeft = 0;
			overLeft = 0;
			if (this.mMonthChangeListener != null) {
				this.mMonthChangeListener.OnMonthChange(date_now);
			}
		}
	}
}

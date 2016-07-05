package com.wipe.zc.journey.lib.monthtab;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wipe.zc.journey.R;

public class MonthTab extends LinearLayout implements OnClickListener {

	private TextView preTextView;
	private int month = -1;
	private HorizontalScrollView hsv_monthtab;
	private ArrayList<TextView> list = new ArrayList<TextView>();
	private int year = Calendar.getInstance().get(1);

	public interface OnYearChangeListener {
		public void onYearChange(int i);
	};

	private OnYearChangeListener onYearChangeListener;

	public void setOnYearChangeListener(OnYearChangeListener onYearChangeListener) {
		this.onYearChangeListener = onYearChangeListener;
	}

	public interface OnMonthChangeListener {
		public void onMonthChange(int i);
	};

	private OnMonthChangeListener onMonthChangeListener;

	public void setOnMonthChangeListener(OnMonthChangeListener onMonthChangeListener) {
		this.onMonthChangeListener = onMonthChangeListener;
	}

	public MonthTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MonthTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MonthTab(Context context) {
		super(context);
	}

	/**
	 * 初始化控件
	 */
	private void init(Context context) {
		View.inflate(context, R.layout.view_monthtab, this);
		hsv_monthtab = (HorizontalScrollView) findViewById(R.id.hsv_monthtab);

		TextView tv_jan = (TextView) findViewById(R.id.tv_jan);
		TextView tv_feb = (TextView) findViewById(R.id.tv_feb);
		TextView tv_mar = (TextView) findViewById(R.id.tv_mar);
		TextView tv_apr = (TextView) findViewById(R.id.tv_apr);
		TextView tv_may = (TextView) findViewById(R.id.tv_may);
		TextView tv_jun = (TextView) findViewById(R.id.tv_jun);
		TextView tv_jul = (TextView) findViewById(R.id.tv_jul);
		TextView tv_aug = (TextView) findViewById(R.id.tv_aug);
		TextView tv_sep = (TextView) findViewById(R.id.tv_sep);
		TextView tv_oct = (TextView) findViewById(R.id.tv_oct);
		TextView tv_nov = (TextView) findViewById(R.id.tv_nov);
		TextView tv_dec = (TextView) findViewById(R.id.tv_dec);

		list.add(tv_jan);
		list.add(tv_feb);
		list.add(tv_mar);
		list.add(tv_apr);
		list.add(tv_may);
		list.add(tv_jun);
		list.add(tv_jul);
		list.add(tv_aug);
		list.add(tv_sep);
		list.add(tv_oct);
		list.add(tv_nov);
		list.add(tv_dec);

		for (int i = 0; i < list.size(); i++) {
			TextView tv = (TextView) this.list.get(i);
			tv.setTag(Integer.valueOf(i + 1));
			tv.setOnClickListener(this);
		}

	}

	/**
	 * 设置当前月份
	 * 
	 * @param i
	 */
	public void setMonth(int i) {
		if (preTextView != null) {
			preTextView.setEnabled(true);
			preTextView.setTextColor(Color.BLACK);
		}
		month = i;
		TextView tv = list.get(month - 1);
		tv.setEnabled(false);
		tv.setTextColor(Color.WHITE);
		preTextView = tv;
	}

	/**
	 * 获取当前月份
	 * 
	 * @return
	 */
	public int getMonth() {
		if (month != -1) {
			return month;
		} else {
			return 0;
		}
	}

	@Override
	public void onClick(View v) {
		if (preTextView != null) {
			// 前目标
			preTextView.setEnabled(true);
			preTextView.setTextColor(Color.BLACK);
		}
		TextView tv = (TextView) v;
		tv.setEnabled(false);
		tv.setTextColor(Color.WHITE);
		// 设置当前选中月
		int i = ((Integer) tv.getTag()).intValue();
		month = i;
		if (onMonthChangeListener != null) {
			onMonthChangeListener.onMonthChange(month);
		}
		// 更新前目标指向
		preTextView = (TextView) v;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		if (onYearChangeListener != null)
			onYearChangeListener.onYearChange(year);
	}

	public int getWholeWidth() {
		int i = 0;
		for (int j = 0; j < hsv_monthtab.getChildCount(); j++) {
			i += hsv_monthtab.getChildAt(j).getWidth();
		}
		return i;
	}

	public void scrollTo(int i) {
		hsv_monthtab.smoothScrollTo(i, 0);
	}
}

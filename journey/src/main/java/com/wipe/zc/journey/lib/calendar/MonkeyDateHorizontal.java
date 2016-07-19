package com.wipe.zc.journey.lib.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wipe.zc.journey.R;

public class MonkeyDateHorizontal extends LinearLayout {
	
	private LinearLayout ll_monkeydate_horizontal;
	private TextView tv_week_num;
	private TextView tv_date_num;
	private ImageView iv_date_point;
	
	
	private String[] weeks = {"Sun","Mon","Tue","Wed","Tuh","Fri","Sat"};
	private int year;
	private int day;
	private int month;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public MonkeyDateHorizontal(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MonkeyDateHorizontal(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MonkeyDateHorizontal(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.layout_monkeydate_horizontal, this);
		ll_monkeydate_horizontal = (LinearLayout) findViewById(R.id.ll_monkeydate_horizontal);
		tv_week_num = (TextView) findViewById(R.id.tv_week_num);
		tv_date_num = (TextView) findViewById(R.id.tv_date_num);
		iv_date_point = (ImageView) findViewById(R.id.iv_date_point);
	}
	
	/**
	 * 日期文本
	 * @param text	文本内容
	 */
	public void setDateText(CharSequence text) {
		tv_date_num.setText(text);
	}

	public void setDateTextSize(int unit, float size) {
		tv_date_num.setTextSize(unit, size);
	}

	public void setDateTextColor(int color) {
		tv_date_num.setTextColor(color);
	}
	
	public CharSequence getDateText(){
		return tv_date_num.getText();
	}
	/**
	 * 星期文本
	 * @param i		星期序号
	 */
	public void setWeekText(int i) {
		
		tv_week_num.setText(weeks[i-1]);
	}
	
	public void setWeekTextSize(int unit, float size) {
		tv_week_num.setTextSize(unit, size);
	}
	
	public void setWeekTextColor(int color) {
		tv_week_num.setTextColor(color);
	}
	/**
	 * 整体背景颜色
	 */
	@Override
	public void setBackgroundColor(int color) {
		ll_monkeydate_horizontal.setBackgroundColor(color);
	}
	
	/**
	 * 设置小点是否可见
	 * @param flag	判断boolean值
	 */
	public void setPointVisiable(boolean flag){
		iv_date_point.setVisibility(flag ? View.VISIBLE:View.INVISIBLE);
	}
	
	/**
	 * 设置小点背景
	 * @param resid		资源ID
	 */
	public void setPointBackground(int resid){
		iv_date_point.setBackgroundResource(resid);
	}
	
}

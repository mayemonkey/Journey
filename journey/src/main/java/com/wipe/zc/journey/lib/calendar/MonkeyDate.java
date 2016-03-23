package com.wipe.zc.journey.lib.calendar;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wipe.zc.journey.R;

public class MonkeyDate extends LinearLayout {

	private TextView tv_date_number;
	private ImageView iv_date_point;
	private LinearLayout ll_monkeydate;
	private Calendar date;
	private int year;
	private int day;
	private int month;

	public MonkeyDate(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MonkeyDate(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MonkeyDate(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 初始化控件
	 * 
	 * @param context
	 */
	private void init(Context context) {
		View.inflate(context, R.layout.layout_monkeydate, this);
		ll_monkeydate = (LinearLayout) findViewById(R.id.ll_monkeydate);
		tv_date_number = (TextView) findViewById(R.id.tv_date_number);
		iv_date_point = (ImageView) findViewById(R.id.iv_date_point);
	}

	/**
	 * 设置文本
	 * 
	 * @param text
	 */
	public void setText(CharSequence text) {
		tv_date_number.setText(text);
	}

	public void setTextSize(int unit, float size) {
		tv_date_number.setTextSize(unit, size);
	}

	public void setTextColor(int color) {
		tv_date_number.setTextColor(color);
	}

	public void setTypeface(Typeface tf, int style) {
		tv_date_number.setTypeface(tf, style);
	}

	public void setTextAppearance(Context context, int resid) {
		tv_date_number.setTextAppearance(context, resid);
	}

	@Override
	public void setBackgroundColor(int color) {
		ll_monkeydate.setBackgroundColor(color);
	}
	
	public CharSequence getText(){
		return tv_date_number.getText();
	}
	
	/**
	 * 设置小点是否可见
	 * @param flag
	 */
	public void setPointVisiable(boolean flag){
		iv_date_point.setVisibility(flag ? View.VISIBLE:View.INVISIBLE);
	}
	
	/**
	 * 设置小点背景
	 * @param resid
	 */
	public void setPointBackground(int resid){
		iv_date_point.setBackgroundResource(resid);
	}
	
	public void setDate(Calendar date){
		this.date = date;
	}
	
	public Calendar getDate(){
		return date;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	public void setDay(int day){
		this.day = day;
	}
	
	public int getDay(){
		return day;
	}
}

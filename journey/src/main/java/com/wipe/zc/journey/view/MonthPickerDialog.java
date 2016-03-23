package com.wipe.zc.journey.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class MonthPickerDialog extends DatePickerDialog {

	public MonthPickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.setTitle(year + "-" + (monthOfYear + 1));
		((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(
				View.GONE);
	}

	public MonthPickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		updateTitle(year, month);
	}

	private void updateTitle(int year, int month) {
		setTitle(year + "-" + (month + 1));
	}

}

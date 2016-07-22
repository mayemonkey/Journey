package com.wipe.zc.journey.ui.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.ToastUtil;
import com.wipe.zc.journey.util.ViewUtil;

public class AddJourneyActivity extends Activity implements OnClickListener {

	private EditText et_journey_name;
	private EditText et_journey_describe;
	private TextView tv_journey_date;
	private TextView tv_journey_stime;
	private TextView tv_journey_etime;
	private EditText et_journey_location;
	private View ve_journey_name;
	private View ve_journey_describe;
	private View ve_journey_location;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				finish();
				break;

			case 1:
				ToastUtil.shortToast("添加失败");
				break;

			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_journey);
		initWidget();
	}

	/**
	 * 初始化控件
	 */
	private void initWidget() {
		ImageView iv_add_cancle = (ImageView) findViewById(R.id.iv_add_cancle);
		iv_add_cancle.setOnClickListener(this);
		ImageView iv_add_ensure = (ImageView) findViewById(R.id.iv_add_ensure);
		iv_add_ensure.setOnClickListener(this);

		et_journey_name = (EditText) findViewById(R.id.et_journey_name);
		ve_journey_name = findViewById(R.id.ve_journey_name);

		et_journey_describe = (EditText) findViewById(R.id.et_journey_describe);
		ve_journey_describe = findViewById(R.id.ve_journey_describe);

		tv_journey_date = (TextView) findViewById(R.id.tv_journey_date);

		tv_journey_date.setText(Calendar.getInstance().get(Calendar.YEAR) + "-"
				+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
				+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		tv_journey_date.setOnClickListener(this);
		tv_journey_stime = (TextView) findViewById(R.id.tv_journey_stime);
		tv_journey_stime.setOnClickListener(this);
		tv_journey_etime = (TextView) findViewById(R.id.tv_journey_etime);
		tv_journey_etime.setOnClickListener(this);
		et_journey_location = (EditText) findViewById(R.id.et_journey_location);
		ve_journey_location = findViewById(R.id.ve_journey_location);

		ImageView iv_location = (ImageView) findViewById(R.id.iv_location);
		iv_location.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_cancle:
			finish();
			break;

		case R.id.iv_add_ensure:
			boolean flag_name = ViewUtil.checkEmptyData(et_journey_name, ve_journey_name);
			boolean flag_describe = ViewUtil.checkEmptyData(et_journey_describe, ve_journey_describe);
			boolean flag_location = ViewUtil.checkEmptyData(et_journey_location, ve_journey_location);
			if (flag_name && flag_describe && flag_location) {
				//比较开始时间值与结束时间值
				String stime = tv_journey_stime.getText().toString();
				String etime = tv_journey_etime.getText().toString();
				int start = Integer.parseInt(stime.split(":")[0]) * 60 + Integer.parseInt(stime.split(":")[1]);
				int end = Integer.parseInt(etime.split(":")[0]) * 60 + Integer.parseInt(etime.split(":")[1]);
				if (start < end) {
					// TODO 上传行程
					new Thread(new Runnable() {
						public void run() {
							Journey journey = new Journey();
							journey.setName(et_journey_name.getText().toString());
							journey.setDescribe(et_journey_describe.getText().toString());
							journey.setDate(tv_journey_date.getText().toString());
							journey.setStime(tv_journey_stime.getText().toString());
							journey.setEtime(tv_journey_etime.getText().toString());
							journey.setLocation(et_journey_location.getText().toString());
							journey.setNickname(MyApplication.getNickname());
							String result = HttpUtil.requestOkHttp(AppURL.addjourney, journey);
							if(result != null){
								if (result.equals("添加成功")) {
									handler.sendEmptyMessage(0);
								} else {
									handler.sendEmptyMessage(1);
								}
							}else {
								handler.sendEmptyMessage(1);
							}
						}
					}).start();

				} else {
					ToastUtil.shortToast("开始时间大于或等于结束时间");
				}
			} else {
				ToastUtil.shortToast("请输入完整的数据");
			}
			break;

		case R.id.tv_journey_date:
			DatePickerDialog dateDialog = new DatePickerDialog(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					tv_journey_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
				}
			}, Integer.parseInt(tv_journey_date.getText().toString().split("-")[0]), Integer.parseInt(tv_journey_date
					.getText().toString().split("-")[1]) - 1, Integer.parseInt(tv_journey_date.getText().toString()
					.split("-")[2]));
			dateDialog.show();

			break;

		case R.id.tv_journey_stime:
			TimePickerDialog stimeDialog = new TimePickerDialog(this, new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					String hour = hourOfDay + "";
					String min = minute + "";
					if (hourOfDay < 10) {
						hour = "0" + hour;
					}
					if (minute < 10) {
						min = "0" + minute;
					}
					tv_journey_stime.setText(hour + ":" + min);
				}
			}, 0, 0, true);
			stimeDialog.show();
			break;

		case R.id.tv_journey_etime:
			TimePickerDialog etimeDialog = new TimePickerDialog(this, new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					String stime = tv_journey_stime.getText().toString();
					int shour = Integer.parseInt(stime.split(":")[0]);
					int smin = Integer.parseInt(stime.split(":")[1]);

					if (shour > hourOfDay) {
						hourOfDay = shour + 1;
					} else if (shour == hourOfDay) {
						if(smin >= minute){
							hourOfDay = shour + 1;
						}
					}

					String hour = hourOfDay + "";
					String min = minute + "";
					if (hourOfDay < 10) {
						hour = "0" + hour;
					}
					if (minute < 10) {
						min = "0" + minute;
					}
					tv_journey_etime.setText(hour + ":" + min);
				}
			}, 0, 0, true);
			etimeDialog.show();
			break;

		case R.id.iv_location:
			// startActivity(new Intent(this,LocationActivity.class));
			startActivityForResult(new Intent(this, LocationActivity.class), 0);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			String location = data.getStringExtra("location");
			if (!TextUtils.isEmpty(location)) {
				et_journey_location.setText(location);
			}

		}
	}

}

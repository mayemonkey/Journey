package com.wipe.zc.journey.ui.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.calendar.MonkeyCalendarHorizontal;
import com.wipe.zc.journey.lib.calendar.MonkeyCalendarHorizontal.OnDateSelectedListener;
import com.wipe.zc.journey.lib.calendar.MonkeyCalendarHorizontal.OnMonthChangeListener;
import com.wipe.zc.journey.ui.activity.HomeActivity;
import com.wipe.zc.journey.ui.adapter.JourneyAdapter;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.JsonUtil;

public class HomeFragment extends Fragment implements OnClickListener {

	private View view;
	private MonkeyCalendarHorizontal mch_home;
	private ListView lv_home;
	private JourneyAdapter adapter;
	private List<Journey> list;
	private ArrayList<Calendar> list_record = new ArrayList<>();

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;

			case 1:
				if (mch_home != null) {
					mch_home.setRecordList(list_record);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		// 子线程中请求包含记录日期集合
		new Thread(new Runnable() {
			public void run() {
				requestRecordList();
			}
		}).start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initView();

		return view;
	}

	private void initView() {
		view = View.inflate(getActivity(), R.layout.fragment_home, null);
		mch_home = (MonkeyCalendarHorizontal) view.findViewById(R.id.mch_home);
		ImageView iv_home_backtoday = (ImageView) view.findViewById(R.id.iv_home_backtoday);
		iv_home_backtoday.setOnClickListener(this);
		// // 子线程中请求包含记录日期集合
		// new Thread(new Runnable() {
		// public void run() {
		// requestRecordList();
		// }
		// }).start();
		lv_home = (ListView) view.findViewById(R.id.lv_home);
		mch_home.setOnDateSelectedListener(mDateSelectedListener);

		// 设置月份变化监听
		mch_home.setOnMonthChangeListener(mMonthChangeListener);

		showDateTime();

		showJourney();
	}

	/**
	 * 显示行程数据
	 */
	private void showJourney() {
		list = new ArrayList<>();
		adapter = new JourneyAdapter(list, 0, ((HomeActivity)getActivity()));
		lv_home.setAdapter(adapter);
		new Thread(new Runnable() {
			@Override
			public void run() {
				requestJourney();
			}
		}).start();

	}

	/**
	 * 向服务端请求行程数据
	 */
	private void requestJourney() {
		Journey journey = new Journey();
		String nickname = MyApplication.getNickname();
		if (nickname != null) {
			journey.setNickname(nickname);
			Calendar calendar = mch_home.getSelectedDate();
			journey.setDate(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH));
			String result = HttpUtil.requestByPost(AppURL.getjourney, journey);
			if (result != null) {
				@SuppressWarnings("unchecked")
				List<Journey> list_journey = (List<Journey>) JsonUtil.parseJsonToList(result,
						new TypeToken<List<Journey>>() {
						}.getType());

				if (list_journey != null) {
					list.addAll(list_journey);
					handler.sendEmptyMessage(0);
				}
			}
		}
	}

	/**
	 * 请求包含记录的日期
	 */
	private void requestRecordList() {
		String nickname = MyApplication.getNickname();
		if (nickname != null) {
			String result = HttpUtil.requestByPost(AppURL.getjourneydate, nickname);
			if (result != null) {
				@SuppressWarnings("unchecked")
				List<Journey> list_calendar = (List<Journey>) JsonUtil.parseJsonToList(result,
						new TypeToken<List<Journey>>() {
						}.getType());

				if (list_calendar != null) {
					Calendar calendar ;
					for (Journey journey : list_calendar) {
						calendar = Calendar.getInstance();
						String year = journey.getDate().split("-")[0];
						String month = journey.getDate().split("-")[1];
						String day = journey.getDate().split("-")[2];
						calendar.set(Integer.parseInt(year), (Integer.parseInt(month) - 1), Integer.parseInt(day));
						list_record.add(calendar);
					}
					handler.sendEmptyMessage(1);
				}

			}
		}
	}

	/**
	 * 设置Calendar日期
	 * 
	 * @param calendar	被设置的Calendar对象
	 */
	public void changeDateTo(Calendar calendar) {
		mch_home.changeDateTo(calendar);
	}

	/**
	 * 初始显示时间数据（当年当月）
	 */
	private void showDateTime() {
		Calendar calendar = mch_home.getSelectedDate();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		((HomeActivity) getActivity()).setTitleText(year + "年" + (month + 1) + "月");
	}

	/**
	 * 设置日期选中更变监听
	 */
	OnDateSelectedListener mDateSelectedListener = new OnDateSelectedListener() {
		public void onDateSelected(Calendar date) {
			list.clear();
			new Thread(new Runnable() {
				@Override
				public void run() {
					requestJourney();
				}
			}).start();
		}
	};

	/**
	 * 设置月份更变监听
	 */
	OnMonthChangeListener mMonthChangeListener = new OnMonthChangeListener() {
		@Override
		public void OnMonthChange(Calendar date) {
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH);
			((HomeActivity) getActivity()).setTitleText(year + "年" + (month + 1) + "月");
		}
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_home_backtoday:
			Calendar calendar = Calendar.getInstance();
			backToToday();
			((HomeActivity) getActivity()).setTitleText(calendar.get(Calendar.YEAR) + "年"
					+ (calendar.get(Calendar.MONTH) + 1) + "月");
			((HomeActivity) getActivity()).setTitleTag(calendar);
			break;

		default:
			break;
		}
	}

	private void backToToday() {
		mch_home.backToToday();
	}

}

package com.wipe.zc.journey.ui.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.monthtab.MonthTab;
import com.wipe.zc.journey.lib.monthtab.MonthTab.OnMonthChangeListener;
import com.wipe.zc.journey.lib.monthtab.MonthTab.OnYearChangeListener;
import com.wipe.zc.journey.ui.activity.HomeActivity;
import com.wipe.zc.journey.ui.adapter.JourneyAdapter;
import com.wipe.zc.journey.ui.adapter.TotalPagerAdapter;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.JsonUtil;

public class TotalFragment extends Fragment {

	private View view;
	private ViewPager vp_total;
	private TotalPagerAdapter adapter;
	private MonthTab mt_total;
	private JourneyAdapter journeyAdapter;
	private List<Journey> list = new ArrayList<Journey>();
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (journeyAdapter != null) {
					journeyAdapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_total, null);
		initView();

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (adapter != null) {
			adapter = new TotalPagerAdapter(getActivity().getSupportFragmentManager(), vp_total);
			vp_total.setAdapter(adapter);
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		mt_total = (MonthTab) view.findViewById(R.id.mt_total);
		lv_total = (ListView) view.findViewById(R.id.lv_total);
		journeyAdapter = new JourneyAdapter(list, 1);

		// ViewPager初始化
		vp_total = (ViewPager) view.findViewById(R.id.vp_total);
		adapter = new TotalPagerAdapter(getChildFragmentManager(), vp_total);

		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		// 设置默认选中月
		mt_total.setMonth(month + 1);
		// 添加年份改变监听
		mt_total.setOnYearChangeListener(onYearChangeListener);
		// 添加月份选中监听
		mt_total.setOnMonthChangeListener(onMonthChangeListener);

		if (adapter != null) {
			vp_total.setAdapter(adapter);
		}
		if (journeyAdapter != null) {
			lv_total.setAdapter(journeyAdapter);
		}
		vp_total.setCurrentItem(month);
		vp_total.setOnPageChangeListener(onPageChangeListener);

		// 初始化标题
		((HomeActivity) getActivity()).setTitleText("全部");

		// 请求数据显示行程
		new Thread(new Runnable() {
			public void run() {
				requestJourney();
			}
		}).start();
	}

	/**
	 * 切换当前选中日期
	 * 
	 * @param calendar
	 */
	public void changeDateTo(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH) + 1;
		mt_total.setYear(year);
		// mt_total.setMonth(month);
	}

	/**
	 * 获取Total页的ViewPager对象
	 * 
	 * @return
	 */
	public ViewPager getViewPager() {
		if (vp_total != null) {
			return vp_total;
		} else {
			return null;
		}
	}

	/**
	 * 请求ListView中行程数据
	 */
	private void requestJourney() {
		list.clear();
		String result = HttpUtil.requestByPost(AppURL.getjourney, MyApplication.getNickname());
		@SuppressWarnings("unchecked")
		List<Journey> list_journey = (List<Journey>) JsonUtil.parseJsonToList(result, new TypeToken<List<Journey>>() {
		}.getType());
		if (list_journey != null) {
			int year = mt_total.getYear();
			int month = mt_total.getMonth();
			for (Journey journey : list_journey) {
				String date = journey.getDate();
				if (Integer.parseInt(date.split("-")[0]) == year && Integer.parseInt(date.split("-")[1]) == month) {
					list.add(journey);
				}
			}
			handler.sendEmptyMessage(0);
		}
	}

	/**
	 * MonthTab月份选择监听
	 */
	OnMonthChangeListener onMonthChangeListener = new OnMonthChangeListener() {
		public void onMonthChange(int i) {
			vp_total.setCurrentItem(i - 1);
			new Thread(new Runnable() {
				public void run() {
					requestJourney();
				}
			}).start();
		}
	};

	OnYearChangeListener onYearChangeListener = new OnYearChangeListener() {
		public void onYearChange(int i) {
			MyApplication.setTotal_year(i);
			adapter = new TotalPagerAdapter(getActivity().getSupportFragmentManager(), vp_total);
			vp_total.setAdapter(adapter);
			vp_total.setCurrentItem(0);
			mt_total.setMonth(1);
			new Thread(new Runnable() {
				public void run() {
					requestJourney();
				}
			}).start();
		}
	};

	/**
	 * ViewPager 页面切换监听
	 */
	OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		public void onPageSelected(int arg0) {
			int mtWidth = mt_total.getWholeWidth();
			int monWidth = mtWidth / 12;
			int winWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
			int i = winWidth / monWidth;
			if (arg0 > i - 1) {
				mt_total.scrollTo((arg0 - i + 2) * monWidth);
				if (arg0 > 12 - i) {
					mt_total.scrollTo((12 - i) * monWidth);
				}
			} else {
				mt_total.scrollTo(0);
			}
			mt_total.setMonth(arg0 + 1);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	private ListView lv_total;
}

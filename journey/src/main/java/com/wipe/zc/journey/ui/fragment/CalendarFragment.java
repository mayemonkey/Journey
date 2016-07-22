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
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.calendar.MonkeyCalendar;
import com.wipe.zc.journey.lib.calendar.MonkeyCalendar.OnDateSelectedListener;
import com.wipe.zc.journey.lib.calendar.MonkeyCalendar.OnMonthChangeListener;
import com.wipe.zc.journey.ui.activity.HomeActivity;
import com.wipe.zc.journey.ui.adapter.JourneyAdapter;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.JsonUtil;

public class CalendarFragment extends Fragment {

    private View view;
    private MonkeyCalendar mc_calendar;

    private JourneyAdapter adapter;
    private List<Journey> list;
    private ArrayList<Calendar> list_record = new ArrayList<>();
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    break;

                case 1:
                    if (mc_calendar != null) {
                        mc_calendar.setRecordList(list_record);
                    }
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        return view;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        view = View.inflate(getActivity(), R.layout.fragment_calendar, null);
        mc_calendar = (MonkeyCalendar) view.findViewById(R.id.mc_calendar);
        // 子线程中请求包含记录日期集合
        new Thread(new Runnable() {
            public void run() {
                requestRecordList();
            }
        }).start();

        lv_calendar = (ListView) view.findViewById(R.id.lv_calendar);
        showDateTime();
        showJourney();
        mc_calendar.setOnDateSelectedListener(mDateSelectedListener);
        mc_calendar.setOnMonthChangeListener(mMonthChangeListener);
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
                List<Journey> list_calendar = (List<Journey>) JsonUtil.parseJsonToList(result, new TypeToken<List<Journey>>() {
                }.getType());

                if (list_calendar != null) {
                    Calendar calendar;
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
     * 显示行程数据
     */
    private void showJourney() {
        list = new ArrayList<>();
        adapter = new JourneyAdapter(list, 0, ((HomeActivity) getActivity()));
        lv_calendar.setAdapter(adapter);
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
            Calendar calendar = mc_calendar.getSelectedDate();
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
     * 初始显示时间数据（当年当月）
     */
    private void showDateTime() {
        Calendar calendar = mc_calendar.getSelectedDate();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        ((HomeActivity) getActivity()).setTitleText(year + "年" + (month + 1) + "月");
        ((HomeActivity) getActivity()).setTitleTag(calendar);
    }

    /**
     * 改变日期至指定
     */
    public void changeDateTo(Calendar calendar) {
        mc_calendar.changeDateTo(calendar);
    }

    /**
     * 月份更变监听
     */
    OnMonthChangeListener mMonthChangeListener = new OnMonthChangeListener() {
        public void onMonthChange(Calendar calendar) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            ((HomeActivity) getActivity()).setTitleText(year + "年" + (month + 1) + "月");
            ((HomeActivity) getActivity()).setTitleTag(calendar);
        }
    };

    OnDateSelectedListener mDateSelectedListener = new OnDateSelectedListener() {

        public void onDateSelected(Calendar date) {
            list.clear();
            new Thread(new Runnable() {
                public void run() {
                    requestJourney();
                }
            }).start();
        }
    };
    private ListView lv_calendar;

}

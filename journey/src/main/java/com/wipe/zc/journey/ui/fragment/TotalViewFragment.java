package com.wipe.zc.journey.ui.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.JsonUtil;

public class TotalViewFragment extends Fragment {

    private int position;
    private View view;
    private ViewPager viewPager;
    private int year = 0;
    private TextView tv_total_done;
    private TextView tv_total_wait;
    private TextView tv_total_free;
    private ProgressBar pb_total_done;
    private ProgressBar pb_total_wait;
    private ProgressBar pb_total_free;
    private List<Journey> list = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doAnimation(msg);
        }

    };


    /**
     * 执行动画操作
     *
     * @param msg    Message(Handler)
     */
    private void doAnimation(Message msg) {
        switch (msg.what) {
            case 0:

                // 进度条动画
                int month_now = Calendar.getInstance().get(Calendar.MONTH);
                int day_now = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                String date ;
                int done = 0;
                int wait = 0;
                int free ;
                TreeSet<String> ts = new TreeSet<>();
                for (Journey journey : list) {
                    date = journey.getDate();
                    // 当前月日
                    if (Integer.parseInt(date.split("-")[0]) == year && Integer.parseInt(date.split("-")[1]) == (position + 1)) {
                        if (Integer.parseInt(date.split("-")[1]) - 1 < month_now) {  //记录月份小于当前月份
                            done++;
                        } else if (Integer.parseInt(date.split("-")[1]) - 1 > month_now) {
                            wait++;
                        } else if (Integer.parseInt(date.split("-")[1]) - 1 == month_now) {
                            if (Integer.parseInt(date.split("-")[2]) < day_now) {
                                done++;
                            }
                            if (Integer.parseInt(date.split("-")[2]) > day_now) {
                                wait++;
                            }
                        }
                        // 去除重复日期统计空闲日期
                        ts.add(date);

                    }
                }
                free = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - ts.size();

                tv_total_done.setText(done + "");
                tv_total_wait.setText(wait + "");
                tv_total_free.setText(free + "");

                // 进度条动画
                ValueAnimator va1 = ValueAnimator.ofInt(0, done);
                va1.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator arg0) {
                        int i = (Integer) arg0.getAnimatedValue();
                        if (pb_total_done != null) {
                            pb_total_done.setProgress(i);
                        }
                    }
                });
                va1.setDuration(1000);
                va1.start();
                // 进度条动画
                ValueAnimator va2 = ValueAnimator.ofInt(0, wait);
                va2.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator arg0) {
                        int i = (Integer) arg0.getAnimatedValue();
                        if (pb_total_wait != null) {
                            pb_total_wait.setProgress(i);
                        }
                    }
                });
                va2.setDuration(1000);
                va2.start();
                // 进度条动画
                ValueAnimator va3 = ValueAnimator.ofInt(0, free);
                va3.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator arg0) {
                        int i = (Integer) arg0.getAnimatedValue();
                        if (pb_total_free != null) {
                            pb_total_free.setProgress(i);
                        }
                    }
                });
                va3.setDuration(1000);
                va3.start();
                break;

            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("当前运行的Fragment的onStart()" + getArguments().getInt("position"));
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        System.out.println("当前运行的Fragment的onResume()" + getArguments().getInt("position"));
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Thread(new Runnable() {
                public void run() {
                    requestData();
                }
            }).start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = MyApplication.getContext();
        view = View.inflate(context, R.layout.layout_total_viewpager, null);

        initView();
        return view;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        // 数字
        tv_total_done = (TextView) view.findViewById(R.id.tv_total_done);
        tv_total_wait = (TextView) view.findViewById(R.id.tv_total_wait);
        tv_total_free = (TextView) view.findViewById(R.id.tv_total_free);
        // 进度条
        pb_total_done = (ProgressBar) view.findViewById(R.id.pb_total_done);
        pb_total_wait = (ProgressBar) view.findViewById(R.id.pb_total_wait);
        pb_total_free = (ProgressBar) view.findViewById(R.id.pb_total_free);

        position = getArguments().getInt("position");
        year = MyApplication.getTotal_year();

        final int index = position;

        // 左移
        ImageView iv_total_left = (ImageView) view.findViewById(R.id.iv_total_left);
        iv_total_left.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewPager.setCurrentItem(index - 1);
            }
        });
        // 右移
        ImageView iv_total_right = (ImageView) view.findViewById(R.id.iv_total_right);
        iv_total_right.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                viewPager.setCurrentItem(index + 1);
            }
        });
        // 一月不存在左移项
        if (position == 0) {
            iv_total_left.setVisibility(View.INVISIBLE);
            iv_total_left.setEnabled(false);
        }
        // 十二月不存在右移项
        if (position == 11) {
            iv_total_right.setVisibility(View.INVISIBLE);
            iv_total_right.setEnabled(false);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    /**
     * 请求网络数据
     */
    private void requestData() {
        list.clear();
        String nickname = MyApplication.getNickname();
        if (nickname != null) {
            String result = HttpUtil.requestByPost(AppURL.getjourney, nickname);
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

    /**
     * 设置当前年份
     *
     * @param year    年份数
     */
    public void setYear(int year) {
        this.year = year;
    }
}

package com.wipe.zc.journey.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.ui.activity.EditJourneyActivity;
import com.wipe.zc.journey.ui.activity.HomeActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 2016/6/14.
 */
public class TotalAdapter extends BaseAdapter {

    private HomeActivity activity;
    private List<Journey> list;

    public TotalAdapter(HomeActivity activity, List<Journey>list){
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Journey journey = list.get(position);
        String date = journey.getDate();

        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);

        int month_now = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day_now = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if( month < month_now ){        //早于当前月份
            return 0;
        }else if(month > month_now){    //晚于当前月份
            return 1;
        }else {                         //当前月份
            if(day < day_now){
                return 0;
            }else{
                return 1;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Journey journey = list.get(position);

        int itemViewType = getItemViewType(position);

        if(itemViewType == 0){      //done
            if(convertView == null){
                convertView = View.inflate(MyApplication.getContext(), R.layout.layout_list_total_done, null);
            }

            DoneViewHolder holder = DoneViewHolder.getInstance(convertView);

            holder.tv_total_done_time.setText(journey.getDate());
            holder.tv_total_done_title.setText(journey.getName());

            // item点击事件
           holder.rl_total_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startActivity(new Intent(activity, EditJourneyActivity.class));
                }
            });

        }else{
            if(convertView == null){    //wait
                convertView = View.inflate(MyApplication.getContext(), R.layout.layout_list_total_wait, null);
            }

            WaitViewHolder holder = WaitViewHolder.getInstance(convertView);

            holder.tv_total_wait_time.setText(journey.getDate());
            holder.tv_total_wait_title.setText(journey.getName());

        }

        return convertView;
    }

    static class DoneViewHolder{
        TextView tv_total_done_time;
        TextView tv_total_done_title;
        RelativeLayout rl_total_item;

        public DoneViewHolder(View convertView){
            rl_total_item = (RelativeLayout) convertView.findViewById(R.id.rl_total_item);
            tv_total_done_time = (TextView) convertView.findViewById(R.id.tv_total_done_time);
            tv_total_done_title = (TextView) convertView.findViewById(R.id.tv_total_done_title);
        }

        public static DoneViewHolder getInstance(View convertView){
            DoneViewHolder holder = (DoneViewHolder) convertView.getTag();
            if(holder == null){
                holder = new DoneViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    static class WaitViewHolder{
        TextView tv_total_wait_time;
        TextView tv_total_wait_title;
        RelativeLayout rl_total_item;

        public WaitViewHolder(View convertView){
            rl_total_item = (RelativeLayout) convertView.findViewById(R.id.rl_total_item);
            tv_total_wait_time = (TextView) convertView.findViewById(R.id.tv_total_wait_time);
            tv_total_wait_title = (TextView) convertView.findViewById(R.id.tv_total_wait_title);
        }

        public static WaitViewHolder getInstance(View convertView){
            WaitViewHolder holder = (WaitViewHolder) convertView.getTag();
            if(holder == null){
                holder = new WaitViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }









}

package com.wipe.zc.journey.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.domain.JourneyDetail;
import com.wipe.zc.journey.global.MyApplication;

import java.util.List;

/**
 * Created by admin on 2016/7/20.
 */
public class NewTotalAdapter extends BaseAdapter{

    private List<Journey> list;
    private List<JourneyDetail> list_detail;

    public NewTotalAdapter(List<Journey> list,List<JourneyDetail> list_detail){
        this.list = list;
        this.list_detail = list_detail;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = View.inflate(MyApplication.getContext(), R.layout.layout_lv_total, null);
        }

        TotalViewHolder holder = TotalViewHolder.getInstance(convertView);
        Journey journey = list.get(position);

        String date = journey.getDate();
        String name = journey.getName();

//        holder.tv_total_tv_time.setText();
//        holder.tv_total_lv_title.setText();

        JourneyDetail journeyDetail = journey.getJourneyDetail();
        if(journeyDetail == null){
            holder.tv_total_lv_text.setVisibility(View.GONE);
            holder.gv_total_lv.setVisibility(View.GONE);
        }else{
            holder.tv_total_lv_text.setVisibility(View.VISIBLE);
            holder.gv_total_lv.setVisibility(View.VISIBLE);

            journeyDetail.getText();

//            holder.tv_total_lv_text.setText();

        }

        return convertView;
    }


    static class TotalViewHolder{

        TextView tv_total_tv_time;
        TextView tv_total_lv_title;

        TextView tv_total_lv_text;
        GridView gv_total_lv;

        public TotalViewHolder(View convertView){
            tv_total_tv_time = (TextView) convertView.findViewById(R.id.tv_total_lv_time);
            tv_total_lv_title = (TextView) convertView.findViewById(R.id.tv_total_lv_title);

            tv_total_lv_text = (TextView) convertView.findViewById(R.id.tv_total_lv_text);
            gv_total_lv = (GridView) convertView.findViewById(R.id.gv_total_lv);
        }

        public static TotalViewHolder getInstance(View convertView){
            TotalViewHolder holder = (TotalViewHolder) convertView.getTag();
            if(holder == null){
                holder = new TotalViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }

    }

}

package com.wipe.zc.journey.ui.adapter;

import android.text.TextUtils;
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
 * 预留Total页中ListView适配Adapter
 */
public class NewTotalAdapter extends BaseAdapter {

    private List<Journey> list;

    public NewTotalAdapter(List<Journey> list) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.layout_lv_total, null);
        }

        TotalViewHolder holder = TotalViewHolder.getInstance(convertView);
        Journey journey = list.get(position);

        //设置时间、标题
        String date = journey.getDate();
        String name = journey.getName();

        holder.tv_total_tv_time.setText(TextUtils.isEmpty(date) ? "日期" : date);
        holder.tv_total_lv_title.setText(TextUtils.isEmpty(name) ? "标题" : name);

        //设置详细内容、Image
        JourneyDetail journeyDetail = journey.getJourneyDetail();
        if (journeyDetail == null) {
            holder.tv_total_lv_content.setVisibility(View.GONE);
            holder.gv_total_lv.setVisibility(View.GONE);
        } else {
            holder.tv_total_lv_content.setVisibility(View.VISIBLE);
            holder.gv_total_lv.setVisibility(View.VISIBLE);

            String content = journeyDetail.getContnent();
            holder.tv_total_lv_content.setText(content);

            List<String> list_url = journeyDetail.getList_url();
            if (list_url != null && list_url.size() > 0) {
                TotalGridAdapter adapter = new TotalGridAdapter(list_url);
                holder.gv_total_lv.setAdapter(adapter);
            }
        }

        return convertView;
    }


    static class TotalViewHolder {
        TextView tv_total_tv_time;
        TextView tv_total_lv_title;

        TextView tv_total_lv_content;
        GridView gv_total_lv;

        public TotalViewHolder(View convertView) {
            tv_total_tv_time = (TextView) convertView.findViewById(R.id.tv_total_lv_time);
            tv_total_lv_title = (TextView) convertView.findViewById(R.id.tv_total_lv_title);

            tv_total_lv_content = (TextView) convertView.findViewById(R.id.tv_total_lv_content);
            gv_total_lv = (GridView) convertView.findViewById(R.id.gv_total_lv);
        }

        public static TotalViewHolder getInstance(View convertView) {
            TotalViewHolder holder = (TotalViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new TotalViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

}

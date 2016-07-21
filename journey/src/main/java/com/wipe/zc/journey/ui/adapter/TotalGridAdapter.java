package com.wipe.zc.journey.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.util.ImageLoaderOption;

import java.util.List;

/**
 * Total页中ListView内GridView适配Adapter
 */
public class TotalGridAdapter extends BaseAdapter {

    private List<String> list;

    public TotalGridAdapter(List<String> list){
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
        if(convertView == null){
            convertView = View.inflate(MyApplication.getContext(), R.layout.layout_gv_total, null);
        }

        //显示Total页中Item内GridView中ImageView
        TotalViewHolder holder = TotalViewHolder.getInstance(convertView);

        String url = list.get(position);

        ImageLoader.getInstance().displayImage(url, holder.iv_gv_total, ImageLoaderOption.list_options);

        return convertView;
    }

    static class TotalViewHolder{
        ImageView iv_gv_total;

        public TotalViewHolder(View convertView){
            iv_gv_total = (ImageView) convertView.findViewById(R.id.iv_gv_total);
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

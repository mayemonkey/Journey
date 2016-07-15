package com.wipe.zc.journey.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.ui.activity.AlbumActivity;
import com.wipe.zc.journey.ui.activity.EditJourneyDetailActivity;
import com.wipe.zc.journey.util.ImageLoaderOption;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑Journey中详细内容
 */

public class EditJourneyDetailAdapter extends BaseAdapter {

    private EditJourneyDetailActivity activity;
    private List<String> list;

    public EditJourneyDetailAdapter(EditJourneyDetailActivity activity, List<String> list) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.layout_gv_edit_journey, null);
        }

        String path = list.get(position);

        final int index = position;

        EditJourneyViewHolder holder = EditJourneyViewHolder.getInstance(convertView);
        if (path.equals("add")) {
            if (position != 0) {
                holder.iv_gv_edit_journey.setImageResource(R.drawable.edit_journey_add_selected);
                //TODO 未测试内容
                holder.iv_gv_edit_journey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(activity, AlbumActivity.class);
                        intent.putStringArrayListExtra("selected", (ArrayList<String>) list);
                        activity.startActivityForResult(intent, 0);
                    }
                });
            }
            if(position == 10){
                holder.iv_gv_edit_journey.setVisibility(View.GONE);
            }
            holder.iv_gv_edit_journey_remove.setVisibility(View.GONE);
        } else {
            ImageLoader.getInstance().displayImage("file://" + path, holder.iv_gv_edit_journey, ImageLoaderOption.list_options);
            holder.iv_gv_edit_journey_remove.setVisibility(View.VISIBLE);
            holder.iv_gv_edit_journey_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //remove 选中
                    list.remove(index);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;

    }

    static class EditJourneyViewHolder {
        ImageView iv_gv_edit_journey;
        ImageView iv_gv_edit_journey_remove;

        public EditJourneyViewHolder(View convertView) {
            iv_gv_edit_journey = (ImageView) convertView.findViewById(R.id.iv_gv_edit_journey);
            iv_gv_edit_journey_remove = (ImageView) convertView.findViewById(R.id.iv_gv_edit_journey_remove);
        }

        public static EditJourneyViewHolder getInstance(View convertView) {
            EditJourneyViewHolder holder = (EditJourneyViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new EditJourneyViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}

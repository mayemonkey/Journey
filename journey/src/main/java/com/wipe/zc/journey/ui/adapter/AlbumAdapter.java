package com.wipe.zc.journey.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.ImageItem;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.ui.activity.AlbumActivity;
import com.wipe.zc.journey.util.ImageLoaderOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Album中GridView适配器
 */
public class AlbumAdapter extends BaseAdapter {

    private AlbumActivity activity;
    private List<ImageItem> list = new ArrayList<>();
    private List<String> list_selected_curr;
    private List<String> list_selected_other;

    public AlbumAdapter(AlbumActivity activity, List<ImageItem> list, List<String> list_selected_curr, List<String> list_selected_other) {
        this.activity = activity;
        this.list = list;
        this.list_selected_curr = list_selected_curr;
        this.list_selected_other = list_selected_other;
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
            convertView = View.inflate(MyApplication.getContext(), R.layout.layout_gv_album, null);
        }

        final ImageItem imageItem = list.get(position);

        final AlbumViewHolder holder = new AlbumViewHolder(convertView);

        //显示图片
        ImageLoader.getInstance().displayImage("file://" + imageItem.getImagePaht(), holder.iv_gv_album_bitmap, ImageLoaderOption.list_options);

        //显示选中状态
        holder.iv_gv_album_sign.setBackgroundResource(imageItem.isSelected() ? R.drawable.check_selected : R.drawable.check);

        //默认选中状态
//        holder.iv_gv_album_sign.setBackgroundResource(R.drawable.check);

        holder.iv_gv_album_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageItem.setSelected(!imageItem.isSelected());

                if (imageItem.isSelected()) {
                    list_selected_curr.add(imageItem.getImagePaht());
                } else {
                    list_selected_curr.remove(imageItem.getImagePaht());
                }

                //显示选中状态
                holder.iv_gv_album_sign.setBackgroundResource(imageItem.isSelected() ? R.drawable.check_selected : R.drawable.check);

                if (list_selected_curr.size() + list_selected_other.size() > 0) {
                    activity.setEnsureEnable(true);
                    activity.setEnsureText("确认(" + (list_selected_other.size() + list_selected_curr.size()) + ")");
                } else {
                    activity.setEnsureEnable(false);
                    activity.setEnsureText("确认");
                }
            }
        });
        return convertView;
    }

    static class AlbumViewHolder {

        ImageView iv_gv_album_bitmap;
        ImageView iv_gv_album_sign;

        public AlbumViewHolder(View convertView) {
            iv_gv_album_bitmap = (ImageView) convertView.findViewById(R.id.iv_gv_album_bitmap);
            iv_gv_album_sign = (ImageView) convertView.findViewById(R.id.iv_gv_album_sign);
        }

        public AlbumViewHolder getInstance(View convertView) {
            AlbumViewHolder holder = (AlbumViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new AlbumViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

}

package com.wipe.zc.journey.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.ImageItem;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.util.ImageLoaderOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Album中GridView适配器
 */
public class AlbumAdapter extends BaseAdapter {

    private List<ImageItem> list = new ArrayList<>();

    public AlbumAdapter(List<ImageItem> list) {
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
            convertView = View.inflate(MyApplication.getContext(), R.layout.layout_gv_album, null);
        }

        final int index = position;

        ImageItem imageItem = list.get(position);

        final AlbumViewHolder holder = new AlbumViewHolder(convertView);
        final boolean selected = imageItem.isSelected();

        //显示图片
        ImageLoader.getInstance().displayImage("file://" + imageItem.getImagePaht(), holder.iv_gv_album_bitmap, ImageLoaderOption.list_options);

        //显示选中状态
        holder.iv_gv_album_sign.setBackgroundResource(selected ? R.drawable.check_selected : R.drawable.check);



        //TODO 依然存在问题   单次点击
        holder.iv_gv_album_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(index).setSelected(!selected);
                //显示选中状态
                holder.iv_gv_album_sign.setBackgroundResource(!selected ? R.drawable.check_selected : R.drawable.check);
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

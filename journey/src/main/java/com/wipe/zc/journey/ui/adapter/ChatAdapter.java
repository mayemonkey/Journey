package com.wipe.zc.journey.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.ChatMessage;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.CircleImageView;
import com.wipe.zc.journey.util.ImageLoaderOption;
import com.wipe.zc.journey.util.LogUtil;
import com.wipe.zc.journey.util.TimeUtil;

import java.util.List;

/**
 * Chat页面Adapter
 */
public class ChatAdapter extends BaseAdapter {


    private List<ChatMessage> list;

    public ChatAdapter(List<ChatMessage> list) {
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
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        String receiver = list.get(position).getReceiveAvatar();
        if (receiver.equals(MyApplication.getNickname())) {     //本人接收
            return 1;
        } else {          //本人发送
            return 2;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        int type = getItemViewType(position);
        ChatMessage chatMessage = list.get(position);

        switch (type) {
            case 1:     //接收的消息（左）
                if (convertView == null) {
                    convertView = View.inflate(MyApplication.getContext(), R.layout
                            .layout_list_chat_left, null);
                }
                setLeftHolder(convertView, chatMessage, position);
                return convertView;

            case 2:     //发送的消息（右）
                if (convertView == null) {
                    convertView = View.inflate(MyApplication.getContext(), R.layout
                            .layout_list_chat_right, null);
                }
                setRightHolder(convertView, chatMessage, position);
                return convertView;

            default:
                LogUtil.i("ChatListView", position + "");
                return null;
        }

    }


    /**
     * 设置左侧Holder
     *
     * @param convertView    convertView
     * @param chatMessage    chat信息
     * @param position       位置
     */
    public void setLeftHolder(View convertView, ChatMessage chatMessage, int position) {
        LeftViewHolder holder_left = LeftViewHolder.getHolder(convertView);
        //头像加载
        ImageLoader.getInstance().displayImage(AppURL.getimage + "?nickname=" +
                        chatMessage.getSendAvatar(),
                holder_left
                        .civ_chat_left_icon, ImageLoaderOption.list_options);
        //消息内容
        if (chatMessage.getType() == EMMessage.Type.TXT) {
            //隐藏其他内容
            holder_left.iv_chat_left_image.setVisibility(View.INVISIBLE);

            holder_left.tv_chat_left_content.setVisibility(View.VISIBLE);
            holder_left.tv_chat_left_content.setText(chatMessage.getContent());
        }
        //图片内容
        else if (chatMessage.getType() == EMMessage.Type.IMAGE) {
            //隐藏其他内容
            holder_left.tv_chat_left_content.setVisibility(View.INVISIBLE);

            holder_left.iv_chat_left_image.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(chatMessage.getContent(), holder_left
                    .iv_chat_left_image, ImageLoaderOption.list_options);
        }

        //时间显示
        if (position != 0) {
            showLeftTime(chatMessage, holder_left, position);
        } else {
            holder_left.tv_chat_left_time.setVisibility(View.VISIBLE);
            if (position == list.size()) {
                holder_left.tv_chat_left_time.setText(TimeUtil.getFromatTime(chatMessage.getChatTime
                        (), false));
            } else {
                holder_left.tv_chat_left_time.setText(TimeUtil.getFromatDate(chatMessage
                        .getChatTime(), false) + "   " + TimeUtil.getFromatTime(chatMessage
                        .getChatTime(), false));
            }
        }
    }

    /**
     * 左侧时间显示
     *
     * @param chatMessage    chat信息
     * @param holder_left    左侧ViewHolder
     * @param position       位置
     */
    public void showLeftTime(ChatMessage chatMessage, LeftViewHolder holder_left, int position) {
        String time;
        switch (TimeUtil.compareCalendar10(chatMessage.getChatTime(),
                list.get(position - 1).getChatTime())) {
            case -1:
                time = TimeUtil.getFromatDate(chatMessage.getChatTime(), true);
                holder_left.tv_chat_left_time.setText(time);
                break;

            case 0:
                time = TimeUtil.getFromatDate(chatMessage.getChatTime(), false);
                holder_left.tv_chat_left_time.setText(time);
                break;

            case 1:
                time = TimeUtil.getFromatTime(chatMessage.getChatTime(), false);
                holder_left.tv_chat_left_time.setText(time);
                break;

            case 2:
                holder_left.tv_chat_left_time.setVisibility(View.GONE);
                break;
        }
    }


    /**
     * 设置右侧Hodler
     *
     * @param convertView    convertView
     * @param chatMessage    chat信息
     * @param position       位置
     */
    public void setRightHolder(View convertView, ChatMessage chatMessage, int position) {
        RightViewHolder holder_right = RightViewHolder.getHolder(convertView);
        //头像加载
        ImageLoader.getInstance().displayImage(AppURL.getimage + "?nickname=" +
                        chatMessage.getSendAvatar(),
                holder_right
                        .civ_chat_right_icon, ImageLoaderOption.list_options);
        //消息内容
        if (chatMessage.getType() == EMMessage.Type.TXT) {
            //隐藏其他内容
            holder_right.iv_chat_right_image.setVisibility(View.INVISIBLE);

            holder_right.tv_chat_right_content.setVisibility(View.VISIBLE);
            holder_right.tv_chat_right_content.setText(chatMessage.getContent());
        }
        //图片内容
        else if (chatMessage.getType() == EMMessage.Type.IMAGE) {
            //隐藏其他内容
            holder_right.tv_chat_right_content.setVisibility(View.INVISIBLE);

            holder_right.iv_chat_right_image.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(chatMessage.getContent(),
                    holder_right.iv_chat_right_image, ImageLoaderOption.list_options);
        }

        //时间显示
        if (position != 0) {
            showRightTime(chatMessage, holder_right, position);
        } else {
            holder_right.tv_chat_right_time.setVisibility(View.VISIBLE);
            holder_right.tv_chat_right_time.setText(TimeUtil.getFromatDate(chatMessage
                    .getChatTime(), false) + "   " + TimeUtil.getFromatTime(chatMessage
                    .getChatTime(), false));
        }
    }

    /**
     * 显示右侧时间
     *
     * @param chatMessage       chat信息
     * @param holder_right      右侧ViewHolder
     * @param position          位置
     */
    public void showRightTime(ChatMessage chatMessage, RightViewHolder holder_right, int position) {
        String time ;
        switch (TimeUtil.compareCalendar10(chatMessage.getChatTime(),
                list.get(position - 1).getChatTime())) {
            case -1:    //年月日
                time = TimeUtil.getFromatDate(chatMessage.getChatTime(), true);
                holder_right.tv_chat_right_time.setText(time);
                break;

            case 0:     //月日
                time = TimeUtil.getFromatDate(chatMessage.getChatTime(), false);
                holder_right.tv_chat_right_time.setText(time);
                break;

            case 1:
                time = TimeUtil.getFromatTime(chatMessage.getChatTime(), false);
                holder_right.tv_chat_right_time.setText(time);
                break;

            case 2:
                holder_right.tv_chat_right_time.setVisibility(View.GONE);
                break;
        }
    }

    static class LeftViewHolder {

        public TextView tv_chat_left_time;
        public CircleImageView civ_chat_left_icon;
        public TextView tv_chat_left_content;
        public ImageView iv_chat_left_image;

        public LeftViewHolder(View convertView) {
            tv_chat_left_time = (TextView) convertView.findViewById(R.id.tv_chat_left_time);
            civ_chat_left_icon = (CircleImageView) convertView.findViewById(R.id
                    .civ_chat_left_icon);
            tv_chat_left_content = (TextView) convertView.findViewById(R.id.tv_chat_left_content);
            iv_chat_left_image = (ImageView) convertView.findViewById(R.id.iv_chat_left_image);
        }

        public static LeftViewHolder getHolder(View convertView) {
            LeftViewHolder holder = (LeftViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new LeftViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }


    static class RightViewHolder {

        public TextView tv_chat_right_time;
        public TextView tv_chat_right_content;
        public CircleImageView civ_chat_right_icon;
        public ImageView iv_chat_right_image;

        public RightViewHolder(View convertView) {
            tv_chat_right_time = (TextView) convertView.findViewById(R.id.tv_chat_right_time);
            tv_chat_right_content = (TextView) convertView.findViewById(R.id.tv_chat_right_content);
            civ_chat_right_icon = (CircleImageView) convertView.findViewById(R.id
                    .civ_chat_right_icon);

            iv_chat_right_image = (ImageView) convertView.findViewById(R.id.iv_chat_right_image);
        }

        public static RightViewHolder getHolder(View convertView) {
            RightViewHolder holder = (RightViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new RightViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

}

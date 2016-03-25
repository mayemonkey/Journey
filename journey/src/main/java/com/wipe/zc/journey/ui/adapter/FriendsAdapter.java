package com.wipe.zc.journey.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.CircleImageView;
import com.wipe.zc.journey.ui.activity.ChatActivity;
import com.wipe.zc.journey.ui.activity.HomeActivity;
import com.wipe.zc.journey.util.ImageLoaderOption;
import com.wipe.zc.journey.util.TimeUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by hp on 2016/3/20.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List list;
    private HomeActivity activity;

    public FriendsAdapter(List list, HomeActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(MyApplication.getContext(), R.layout.layout_list_friends, null);
        FriendsViewHolder holder = FriendsViewHolder.getHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        final String name = (String) list.get(position);
        //处理View的显示

        ImageLoader.getInstance().displayImage(AppURL.getimage + "?nickname=" + name, holder
                .civ_friends_icon, ImageLoaderOption.list_options);

        holder.tv_friends_name.setText(name);

        //加载所有聊天记录
        EMChatManager.getInstance().loadAllConversations();
        //获取聊天记录
        EMConversation conversation = EMChatManager.getInstance().getConversation(name);
        //获取最新聊天记录

        EMMessage lastMessage = conversation.getLastMessage();
        if (lastMessage != null) {      //存在聊天记录
            if (lastMessage.getType() == EMMessage.Type.TXT) {
                holder.tv_friends_message.setText(((TextMessageBody)lastMessage.getBody()).getMessage());
            }
            holder.tv_friends_time.setVisibility(View.VISIBLE);
            Calendar calendar = TimeUtil.longToTime(lastMessage.getMsgTime());
            if (TimeUtil.isToday(calendar)) {       //如果是当天
                holder.tv_friends_time.setText(TimeUtil.getFromatTime(calendar,false));
            }else{  //不是当天
                holder.tv_friends_time.setText(TimeUtil.getFromatDate(calendar,false));
            }
        }else{      //不存在聊天记录
            holder.tv_friends_message.setText("[快去和好友聊天吧]");
            holder.tv_friends_time.setVisibility(View.INVISIBLE);
        }

        //整体条目点击事件
        holder.rl_list_friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChatActivity.class);
                intent.putExtra("receiver",name);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView civ_friends_icon;
        public TextView tv_friends_name;
        public TextView tv_friends_message;
        public TextView tv_friends_time;
        public RelativeLayout rl_list_friends;

        public FriendsViewHolder(View convertView) {
            super(convertView);
            civ_friends_icon = (CircleImageView) convertView.findViewById(R.id.civ_friends_icon);
            tv_friends_name = (TextView) convertView.findViewById(R.id.tv_friends_name);
            tv_friends_message = (TextView) convertView.findViewById(R.id.tv_friends_message);
            tv_friends_time = (TextView) convertView.findViewById(R.id.tv_friends_time);
            rl_list_friends = (RelativeLayout) convertView.findViewById(R.id.rl_list_friends);
        }

        public static FriendsViewHolder getHolder(View convertView) {
            FriendsViewHolder holder = (FriendsViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new FriendsViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

}

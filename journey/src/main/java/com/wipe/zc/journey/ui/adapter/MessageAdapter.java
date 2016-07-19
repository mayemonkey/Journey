package com.wipe.zc.journey.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.dao.InviteDao;
import com.wipe.zc.journey.domain.Invite;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.CircleImageView;
import com.wipe.zc.journey.util.ImageLoaderOption;

import java.util.List;

/**
 * 好友邀请信息Adapter
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Invite> list;

    public MessageAdapter(List<Invite> list) {
        this.list = list;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(MyApplication.getContext(), R.layout.layout_list_message, null);
        return MessageViewHolder.getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Invite invite = list.get(holder.getAdapterPosition());
        final String inviter = invite.getInviter();
        final int index = holder.getAdapterPosition();

        ImageLoader.getInstance().displayImage(AppURL.getimage + "?nickname=" + invite.getInviter
                (), holder.civ_message_icon, ImageLoaderOption.list_options);

        holder.tv_message_content.setText(inviter + "请求添加你为好友");
        holder.tv_message_reason.setText(invite.getReason());
        holder.tv_message_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteDecide(true,index,inviter);
            }
        });

        holder.tv_message_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteDecide(false,index,inviter);
            }
        });

    }

    /**
     * 决定好友请求的处理
     * true:接受
     * false:拒绝
     *
     * @param flag       是否接受boolean
     * @param index      序号
     * @param inviter    邀请人
     */
    public void inviteDecide(boolean flag, int index, String inviter) {
        try {
            if (flag) {
                EMChatManager.getInstance().acceptInvitation(inviter);
            } else {
                EMChatManager.getInstance().refuseInvitation(inviter);
            }
            InviteDao.deleteInvite(inviter);
            list.remove(index);
            notifyItemRemoved(index);
            notifyDataSetChanged();
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView civ_message_icon;
        public TextView tv_message_content;
        public TextView tv_message_reason;
        public TextView tv_message_agree;
        public TextView tv_message_refuse;

        public MessageViewHolder(View convertView) {
            super(convertView);
            civ_message_icon = (CircleImageView) convertView.findViewById(R.id.civ_message_icon);
            tv_message_content = (TextView) convertView.findViewById(R.id.tv_message_content);
            tv_message_reason = (TextView) convertView.findViewById(R.id.tv_message_reason);
            tv_message_agree = (TextView) convertView.findViewById(R.id.tv_message_agree);
            tv_message_refuse = (TextView) convertView.findViewById(R.id.tv_message_refuse);
        }

        public static MessageViewHolder getViewHolder(View convertView) {
            MessageViewHolder holder = (MessageViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new MessageViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}

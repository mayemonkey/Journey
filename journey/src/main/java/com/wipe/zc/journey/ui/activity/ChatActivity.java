package com.wipe.zc.journey.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.ChatMessage;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.ui.adapter.ChatAdapter;
import com.wipe.zc.journey.util.TimeUtil;
import com.wipe.zc.journey.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity implements View.OnClickListener {

    private ImageView iv_chat_cancel;
    private RefreshListView rlv_chat;
    private ChatAdapter adapter;
    private List<ChatMessage> list = new ArrayList<>();
    private TextView tv_chat_name;
    private String friendName;

    private EditText et_chat_content;
    private TextView tv_chat_send;
    private boolean isRefreshing = false;       //标记ListView刷新状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        friendName = getIntent().getStringExtra("receiver");

        initWidget();
        setIM();
        requestRecord(null, 20);
        setListViewPosition(list.size() - 1);
    }


    /**
     * 设置即时通讯
     */
    private void setIM() {
        initMessageBroadCastReceiver();
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        iv_chat_cancel = (ImageView) findViewById(R.id.iv_chat_cancel);
        iv_chat_cancel.setOnClickListener(this);
        tv_chat_name = (TextView) findViewById(R.id.tv_chat_name);
        tv_chat_name.setText(friendName);

        et_chat_content = (EditText) findViewById(R.id.et_chat_content);
        tv_chat_send = (TextView) findViewById(R.id.tv_chat_send);
        tv_chat_send.setOnClickListener(this);

        rlv_chat = (RefreshListView) findViewById(R.id.rlv_chat);
        rlv_chat.setOnRefreshListener(new MyRefreshListener());
        rlv_chat.setDividerHeight(0);
        rlv_chat.setVerticalScrollBarEnabled(true);
        rlv_chat.setSelector(android.R.color.transparent);

        adapter = new ChatAdapter(list);
        rlv_chat.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_chat_cancel:   //离开本页面
                finish();
                break;

            case R.id.tv_chat_send:     //发送消息
                String content = et_chat_content.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    sendMessage(friendName, content);
                }
                break;
        }
    }

    /**
     * 发送消息
     *
     * @param receiver
     * @param content
     */
    private void sendMessage(String receiver, final String content) {
        // 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance()
                .getConversation(receiver);
        // 创建一条文本消息
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        // // 如果是群聊，设置chattype,默认是单聊
        // message.setChatType(ChatType.GroupChat);
        // 设置消息body
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);

        // 设置接收人
        message.setReceipt(receiver);
        // 把消息加入到此会话对象中
        conversation.addMessage(message);
        // 发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("TAG", "消息发送失败");
            }

            @Override
            public void onProgress(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("TAG", "正在发送消息");
            }

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.i("TAG", "消息发送成功");

                //提取EMMessage数据至ChatMessage中
                ChatMessage data = setChatMessageData(message);

                //data.setType(EMMessage.Type.TXT);
                list.add(data);

                //TODO ListView刷新

                //mHandler.sendEmptyMessage(0x00001);
            }
        });
    }

    /**
     * 初始化接受消息监听
     */
    public void initMessageBroadCastReceiver() {
        EMChat.getInstance().setAppInited();
        MessageReceiver msgReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance()
                .getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        MyApplication.getContext().registerReceiver(msgReceiver, intentFilter);
    }

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //消息id
            String msgId = intent.getStringExtra("msgid");
            //发消息的人的username(userid)
            String msgFrom = intent.getStringExtra("from");
            //消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
            //所以消息type实际为是enum类型
            int msgType = intent.getIntExtra("type", 0);
            Log.d("main", "new message id:" + msgId + " from:" + msgFrom + " type:" + msgType);
            //更方便的方法是通过msgId直接获取整个message
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);

            //TODO 添加至集合，更新ListView



        }
    }

    /**
     * 自定义刷新监听
     */
    private class MyRefreshListener implements RefreshListView.OnRefreshListener {
        @Override
        public void refreshing() {
            //改变刷新状态
            isRefreshing = true;
            //加载更多消息记录
            requestRecord(list.get(0).getMessageId(), 10);
        }
    }

    private void requestRecord(String start, int size) {
        //获取此会话的所有消息
        EMConversation conversation = EMChatManager.getInstance().getConversation(friendName);
        int count = conversation.getAllMsgCount();
        if (start == null) {
            //sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
            List<EMMessage> messages = conversation.getAllMessages();
            addMessageToList(messages, count);
        } else {
            //获取startMsgId之前的pagesize条消息，此方法获取的messages sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
            List<EMMessage> messages = conversation.loadMoreMsgFromDB(start, size);
            addMessageToList(messages, count);
        }

    }

    /**
     * 将消息添加给显示List
     *
     * @param messages
     */
    private void addMessageToList(final List<EMMessage> messages, int count) {
        if (messages != null) {
            int index = 0;
            for (EMMessage message : messages) {
                if (list.size() < count) {
                    ChatMessage data = setChatMessageData(message);
                    list.add(0, data);
                    index++;
                    adapter.notifyDataSetChanged();
                } else {
                    break;
                }
            }
            setListViewPosition(index);
        }
        cancelHeadView();
    }

    /**
     * 从EMMessage中提取数据封装至ChatMessage对象中
     *
     * @param message
     * @return
     */
    private ChatMessage setChatMessageData(EMMessage message) {
        ChatMessage data = new ChatMessage();
        data.setSendAvatar(MyApplication.getNickname());
        data.setReceiveAvatar(friendName);
        if (message.getType() == EMMessage.Type.TXT) {
            data.setSendContent(((TextMessageBody) message.getBody()).getMessage());
        }
        data.setMessageId(message.getMsgId());
        data.setChatTime(TimeUtil.longToTime(message.getMsgTime()));
        return data;
    }

    /**
     * 隐藏ListView头视图
     */
    private void cancelHeadView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing) {
                    rlv_chat.reviewHeader();
                    isRefreshing = false;
                }
            }
        });
    }

    /**
     * 设置ListView视图位置
     *
     * @param selection
     */
    public void setListViewPosition(int selection) {
        rlv_chat.setSelection(selection);
    }

}


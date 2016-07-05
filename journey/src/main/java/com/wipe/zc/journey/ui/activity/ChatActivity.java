package com.wipe.zc.journey.ui.activity;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.VoiceRecorder;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.ChatMessage;

import com.wipe.zc.journey.ui.adapter.ChatAdapter;
import com.wipe.zc.journey.util.CommonUtil;

import com.wipe.zc.journey.util.TimeUtil;
import com.wipe.zc.journey.view.RefreshListView;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private MessageReceiver msgReceiver;
    private InputMethodManager imm;
    private ImageView iv_choose;
    private View view_popup;
    private ImageView iv_chatpopup_takephoto;
    private ImageView iv_chatpopup_choosephoto;
    private PopupWindow window;
    private LinearLayout ll_chat_send;

    private static final int GALLERY = 0;
    private static final int CAMERA = 1;
    private TextView tv_chat_record_voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        friendName = getIntent().getStringExtra("receiver");
        boolean flag_message_exit = getIntent().getBooleanExtra("flag_message_exit", false);
        //
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        initWidget();
        initPopupWindow();
        setIM();
//      EMChatManager.getInstance().loadAllConversations();

        //不存在消息记录
        if (flag_message_exit) {
            requestRecord(EMChatManager.getInstance().getConversation(friendName).getLastMessage()
                    .getMsgId(), 20);
            setListViewPosition(list.size() - 1);
        }
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
        //返回
        iv_chat_cancel = (ImageView) findViewById(R.id.iv_chat_cancel);
        iv_chat_cancel.setOnClickListener(this);
        //好友名
        tv_chat_name = (TextView) findViewById(R.id.tv_chat_name);
        tv_chat_name.setText(friendName);
        //输入内容、发送按钮
        et_chat_content = (EditText) findViewById(R.id.et_chat_content);
        tv_chat_send = (TextView) findViewById(R.id.tv_chat_send);
        tv_chat_send.setOnClickListener(this);
        //聊天内容
        rlv_chat = (RefreshListView) findViewById(R.id.rlv_chat);
        rlv_chat.setOnRefreshListener(new MyRefreshListener());
        rlv_chat.setDividerHeight(0);
        rlv_chat.setVerticalScrollBarEnabled(true);
        rlv_chat.setSelector(android.R.color.transparent);
        //选择多媒体内容
        iv_choose = (ImageView) findViewById(R.id.iv_choose);
        iv_choose.setOnClickListener(this);
        //输入框整体
        ll_chat_send = (LinearLayout) findViewById(R.id.ll_chat_send);

        //语音按钮
        tv_chat_record_voice = (TextView) findViewById(R.id.tv_chat_record_voice);
        tv_chat_record_voice.setOnClickListener(null);
        tv_chat_record_voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tv_chat_record_voice.setTextColor(Color.WHITE);
                        break;

                    case MotionEvent.ACTION_UP:
                        tv_chat_record_voice.setTextColor(Color.BLACK);
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        tv_chat_record_voice.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
        //测试使用
        et_chat_content.setEnabled(false);
        et_chat_content.setVisibility(View.INVISIBLE);

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

            case R.id.iv_choose:        //弹出PopupWindow，选择多媒体内容
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.7f;
                getWindow().setAttributes(params);
                int height = view_popup.getHeight();
                window.showAtLocation(view_popup, Gravity.LEFT | Gravity.TOP, 0, getWindowManager
                        ().getDefaultDisplay().getHeight() - 2 * ll_chat_send.getMeasuredHeight());
                break;

            case R.id.iv_chatpopup_takephoto:       //拍摄照片
                startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), CAMERA);
                window.dismiss();   //隐藏PopupWindow
                break;

            case R.id.iv_chatpopup_choosephoto:     //从图库选择图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"), GALLERY);
                window.dismiss();   //隐藏PopupWindow

                break;

        }
    }

    /**
     * 发送文本消息
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
                Log.i("TAG", "消息发送失败");
            }

            @Override
            public void onProgress(int arg0, String arg1) {
                Log.i("TAG", "正在发送消息");
            }

            @Override
            public void onSuccess() {
                Log.i("TAG", "消息发送成功");
                sendMessageSuccess(message);
                //mHandler.sendEmptyMessage(0x00001);
            }
        });
    }

    /**
     * 发送图片消息
     */
    private void sendImageMessage(String receiver, String filePath) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(receiver);
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);

        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        body.setLocalUrl(filePath);
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        message.setReceipt(receiver);
        conversation.addMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                //提取EMMessage数据至ChatMessage中
                sendMessageSuccess(message);
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 当信息发送成功
     *
     * @param message
     */
    private void sendMessageSuccess(EMMessage message) {
        //提取EMMessage数据至ChatMessage中
        ChatMessage data = setChatMessageData(message);

        //data.setType(EMMessage.Type.TXT);
        list.add(data);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                rlv_chat.setSelection(list.size());

                //修改EditText
                et_chat_content.setText("");    //清空输入框
                //隐藏软键盘
                imm.hideSoftInputFromWindow(et_chat_content.getWindowToken(), 0);
            }
        });
    }

    /**
     * 初始化接受消息监听
     */
    public void initMessageBroadCastReceiver() {
        EMChat.getInstance().setAppInited();
        msgReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance()
                .getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);
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
            ChatMessage chatMessage = setChatMessageData(message);
            list.add(chatMessage);
            adapter.notifyDataSetChanged();
            rlv_chat.setSelection(list.size());
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
        conversation.markAllMessagesAsRead();
        int count = conversation.getAllMsgCount();
        if (list.size() == 0) {
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
            List<ChatMessage> list_chat = new ArrayList<>();
            for (EMMessage message : messages) {
                if (list.size() + index < count) {
                    ChatMessage data = setChatMessageData(message);
                    list_chat.add(data);
                    index++;
                } else {
                    break;
                }
            }
            list.addAll(0, list_chat);
            adapter.notifyDataSetChanged();
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
        data.setSendAvatar(message.getFrom());  //发送者
        data.setReceiveAvatar(message.getTo()); //接收者
        //消息内容
        if (message.getType() == EMMessage.Type.TXT) {
            data.setContent(((TextMessageBody) message.getBody()).getMessage());
            data.setType(EMMessage.Type.TXT);
        }//图片内容
        else if (message.getType() == EMMessage.Type.IMAGE) {
            ImageMessageBody body = (ImageMessageBody) message.getBody();
            if (body.getThumbnailUrl().equals("null")) {
                data.setContent("file://" + body.getLocalUrl());
            } else {
                data.setContent(body.getThumbnailUrl());
            }
            data.setType(EMMessage.Type.IMAGE);
        }
        data.setMessageId(message.getMsgId());      //信息ID
        data.setChatTime(TimeUtil.longToTime(message.getMsgTime()));    //信息时间
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(msgReceiver);
    }


    /**
     * 初始化PopupWindow
     */
    private void initPopupWindow() {
        view_popup = View.inflate(this, R.layout.layout_chat_popup, null);
        iv_chatpopup_takephoto = (ImageView) view_popup.findViewById(R.id
                .iv_chatpopup_takephoto);
        iv_chatpopup_takephoto.setOnClickListener(this);

        iv_chatpopup_choosephoto = (ImageView) view_popup.findViewById(R.id
                .iv_chatpopup_choosephoto);
        iv_chatpopup_choosephoto.setOnClickListener(this);

        window = new PopupWindow(view_popup, LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // 设置返回键能够隐藏弹窗
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R
                .color.transparent)));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY:
                    // 选择图片
                    Uri uri = data.getData();
                    String path = CommonUtil.fronUriToPath(uri);
                    if (path != null) {
                        sendImageMessage(friendName, path);
                    }
                    break;

                case CAMERA:
                    String path_camera;
                    if (data.getData() == null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bm = (Bitmap) bundle.get("data");
                        if (bm != null) {
                            try {
                                //保存照片
                                bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream
                                        (Environment.getExternalStorageDirectory()
                                                .getAbsolutePath() +
                                                getApplication().getPackageName() + "/" + UUID
                                                .randomUUID()
                                                .toString() + ".jpg"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            //设置路径
                            path_camera = Environment.getExternalStorageDirectory()
                                    .getAbsolutePath() + getApplication().getPackageName() + "/"
                                    + UUID.randomUUID().toString() + ".jpg";
                        } else {  //内容为空保存为空
                            path_camera = null;
                        }
                    } else {    //存在URI直接转换
                        path_camera = CommonUtil.fronUriToPath(data.getData());
                    }
                    if (path_camera != null) {  //发送图片消息
                        sendImageMessage(friendName, path_camera);
                    }
                    break;
            }
        }
    }
}


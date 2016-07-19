package com.wipe.zc.journey.ui.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.exceptions.EaseMobException;
import com.wipe.zc.journey.R;


import com.wipe.zc.journey.domain.ChatMessage;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.ui.activity.ChatActivity;
import com.wipe.zc.journey.ui.activity.HomeActivity;
import com.wipe.zc.journey.ui.adapter.FriendsAdapter;
import com.wipe.zc.journey.util.LogUtil;
import com.wipe.zc.journey.util.ToastUtil;
import com.wipe.zc.journey.util.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {

    private View view;
    private SwipeRefreshLayout srl_friends;
    private RecyclerView rv_friends;

    private List list = new ArrayList();

    private FriendsAdapter adapter;
    private MessageReceiver msgReceiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = View.inflate(MyApplication.getContext(), R.layout.fragment_friends, null);
        //初始化标题
        ((HomeActivity) getActivity()).setTitleText("好友");

        initWidget();
        setReceiver();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册监听
        initMessageBroadCastReceiver();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(msgReceiver);
    }

    /**
     * 设置消息接收
     */
    private void setReceiver() {
        IntentFilter filter = new IntentFilter("im.add.broadcast.action");
        getActivity().registerReceiver(receiver, filter);
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        srl_friends = (SwipeRefreshLayout) view.findViewById(R.id.srl_friends);
        rv_friends = (RecyclerView) view.findViewById(R.id.rv_friends);
        TextView tv_friends_add = (TextView) view.findViewById(R.id.tv_friends_add);

        //设置SwipeRefreshLayout的刷新颜色
        srl_friends.setColorSchemeColors(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN);

        srl_friends.setOnRefreshListener(this);

//        //环信初始化
//        EMContactManager.getInstance().setContactListener(
//                new MyContactListener());
//        EMChat.getInstance().setAppInited();

        //初始化RecyclerView
        initRecyclerView();

        //初始化好友列表
        initFriendsList();

        //设置点击事件
        tv_friends_add.setOnClickListener(this);
    }


    /**
     * 初始化RecyclerView的基本设置
     */
    private void initRecyclerView() {

        //控制布局方式
        rv_friends.setLayoutManager(new LinearLayoutManager(getActivity()));

        //创建适配器
        adapter = new FriendsAdapter(list, (HomeActivity) getActivity());

        //设置适配器
        rv_friends.setAdapter(adapter);

        //控制间隔
        //rv_friends.addItemDecoration();

        //控制条目添加移除动画
    }

    /**
     * 初始化好友列表
     */
    private void initFriendsList() {
        try {
            List<String> list_friends = EMContactManager.getInstance().getContactUserNames();

            if (list_friends != null) {
                list.clear();
                list.addAll(list_friends);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 刷新监听
     */
    public void onRefresh() {
        initFriendsList();
        srl_friends.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_friends_add:
                View addView = LayoutInflater.from(getActivity()).inflate(R.layout
                        .layout_friends_add, null);
                final EditText et_friends_add_name = (EditText) addView.findViewById(R.id
                        .et_friends_add_name);
                final EditText et_friends_add_verify = (EditText) addView.findViewById(R.id
                        .et_friends_add_verify);

                final View ve_friends_add_name = addView.findViewById(R.id.ve_friends_add_name);
                final View ve_friends_add_verify = addView.findViewById(R.id.ve_friends_add_verify);

                new AlertDialog.Builder(getActivity()).setTitle("添加好友").setView(addView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ViewUtil.checkEmptyData(et_friends_add_name,
                                        ve_friends_add_name) && ViewUtil.checkEmptyData
                                        (et_friends_add_verify, ve_friends_add_verify)) {
                                    dialog.dismiss();
                                    String idStr = et_friends_add_name.getText().toString().trim();
                                    String reasonStr = et_friends_add_verify.getText().toString()
                                            .trim();
                                    try {
                                        EMContactManager.getInstance().addContact(idStr, reasonStr);
                                        ToastUtil.shortToast("成功发送请求，等待对方相应");
                                    } catch (EaseMobException e) {
                                        e.printStackTrace();
                                        Log.i("TAG", "addContacterrcode==>" + e.getErrorCode());
                                    }// 需异步处理
                                } else {      //信息输入不完整
                                    ToastUtil.shortToast("请输入完整请求信息");
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
    }


//    private class MyContactListener implements EMContactListener {
//        @Override
//        public void onContactAdded(List<String> list) {     //添加好友
//            FriendsFragment.this.list.clear();
//            FriendsFragment.this.list.addAll(list);
//            adapter.notifyItemInserted(0);
//        }
//
//        @Override
//        public void onContactDeleted(List<String> list) {
//
//        }
//
//        @Override
//        public void onContactInvited(String s, String s1) {     //收到好友请求
//            //显示好友请求
//            //收到好友邀请
//            LogUtil.i("环信好友", "好友:" + s + "申请说明" + s1);
//            System.out.println("环信好友------好友:" + s + "申请说明" + s1);
//        }
//
//        @Override
//        public void onContactAgreed(String s) {     //请求同意
//            EMNotifier.getInstance(MyApplication.getContext()).notifyOnNewMsg();
//        }
//
//        @Override
//        public void onContactRefused(String s) {
//
//        }
//    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getExtras().getString("data");
            list.clear();
            if(result == null){
                return;
            }

            if (result.contains("-")) {
                String[] results = result.split("-");
                list.addAll(Arrays.asList(results));
            } else {
                list.add(result);
            }
        }
    };

    /**
     * 初始化接受消息监听
     */
    public void initMessageBroadCastReceiver() {
        EMChat.getInstance().setAppInited();
        msgReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance()
                .getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        getActivity().registerReceiver(msgReceiver, intentFilter);
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
            ((HomeActivity) getActivity()).setUnreadSign();
        }
    }


}

package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.wipe.zc.journey.domain.User;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.SharedPreUtil;
import com.wipe.zc.journey.util.ToastUtil;
import com.wipe.zc.journey.R;

public class LaunchActivity extends Activity {

     private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: // 登陆成功
                    startActivity(new Intent(LaunchActivity.this, HomeActivity
                            .class));
                    MyApplication.setNickname((String) msg.obj);
                    finish();
                    break;

                case 2: // 登陆失败
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                    finish();
                    break;

                case 3: // 无用户信息缓存
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                    finish();
                    break;

                case 4:
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                    finish();
                    ToastUtil.shortToast("请检查网络连接");
                    break;

                case 5:
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                    finish();
                    ToastUtil.shortToast("IM连接错误");
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_launch);


        new Thread(new Runnable() {
            String password = SharedPreUtil.getString(LaunchActivity.this, "password", null);
            String nickname = SharedPreUtil.getString(LaunchActivity.this, "nickname", null);

            public void run() {
                if (nickname != null && password != null) {

                    //IM登陆
                    imLogin(nickname, password);

                } else {
                    handler.sendEmptyMessageDelayed(3, 3000);
                }
            }
        }).start();
    }

    /**
     * IM用户登陆
     *
     * @param nickname    昵称
     * @param password    密码
     */
    private void imLogin(final String nickname,final String password) {
        //环信登陆
        if (EMChat.getInstance().isLoggedIn()) {
            Log.d("TAG", "已经登陆过");
            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
            serverLogin(nickname,password);
        } else {
            EMChatManager.getInstance().login(nickname, password, new EMCallBack() {
                @Override
                public void onSuccess() {
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();

                    serverLogin(nickname,password);
                }

                @Override
                public void onError(int code, String message) {
                    if (code == -1005) {
                        message = "用户名或密码错误";
                        ToastUtil.shortToast(message);
                    }
                    handler.sendEmptyMessageDelayed(5, 3000);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }

    }

    private void serverLogin(String nickname,String password){
        //服务端登陆
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(password);
        String result = HttpUtil.requestByPost(AppURL.login, user);
        if (result != null) {
            if (result.equals("登陆成功")) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = nickname;
                handler.sendMessageDelayed(msg, 3000);
            } else if (result.equals("登陆失败")) {
                handler.sendEmptyMessageDelayed(2, 3000);
            }
        } else {
            handler.sendEmptyMessageDelayed(4, 3000);
        }
    }
}

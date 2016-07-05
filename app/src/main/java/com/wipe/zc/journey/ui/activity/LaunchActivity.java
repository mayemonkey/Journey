package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

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
				startActivity(new Intent(LaunchActivity.this, HomeActivity.class));
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
					}else{
						handler.sendEmptyMessageDelayed(4, 3000);
					}
				} else {
					handler.sendEmptyMessageDelayed(3, 3000);
				}
			}
		}).start();
	}
}

package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.wipe.zc.journey.domain.User;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.SharedPreUtil;
import com.wipe.zc.journey.util.ToastUtil;
import com.wipe.zc.journey.util.ViewUtil;
import com.wipe.zc.journey.R;

public class LoginActivity extends Activity implements OnClickListener {

	private TextView tv_lgoin_register;
	private TextView tv_login;
	private EditText et_login_account;
	private EditText et_login_password;
	private View ve_login_account;
	private View ve_login_password;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				if(result != null){		//网络请求不为空
					if (result.equals("登陆成功")) {
						// 保存用户名和密码
						SharedPreUtil.putString(LoginActivity.this, "nickname", et_login_account.getText().toString());
						SharedPreUtil.putString(LoginActivity.this, "password", et_login_password.getText().toString());
						// 跳转页面
						MyApplication.setNickname(et_login_account.getText().toString());
						startActivity(new Intent(LoginActivity.this, HomeActivity.class));
						LoginActivity.this.finish();
					}
					if (result.equals("登陆失败")) {
						ToastUtil.shortToast("用户名或密码错误");
					}
				}else {	//网络请求返回result为null
					ToastUtil.shortToast("请检查网络连接");
				}

				break;

			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 注册
		tv_lgoin_register = (TextView) findViewById(R.id.tv_lgoin_register);
		tv_lgoin_register.setOnClickListener(this);
		// 登陆
		tv_login = (TextView) findViewById(R.id.tv_login);
		tv_login.setOnClickListener(this);
		// 输入
		et_login_account = (EditText) findViewById(R.id.et_login_account);
		et_login_password = (EditText) findViewById(R.id.et_login_password);

		ve_login_account = findViewById(R.id.ve_login_account);
		ve_login_password = findViewById(R.id.ve_login_password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 注册跳转
		case R.id.tv_lgoin_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;

		case R.id.tv_login:
			boolean flag_account = ViewUtil.checkEmptyData(et_login_account, ve_login_account);
			boolean flag_password = ViewUtil.checkEmptyData(et_login_password, ve_login_password);
			if (flag_account && flag_password) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						User user = new User();
						user.setNickname(et_login_account.getText().toString());
						user.setPassword(et_login_password.getText().toString());
						String result = HttpUtil.requestByPost(AppURL.login, user);

						Message msg = new Message();
						msg.what = 1;
						msg.obj = result;
						handler.sendMessage(msg);
					}
				}).start();
			} else {
				ToastUtil.shortToast("请输入完整登陆信息");
			}
			break;

		default:
			break;
		}

	}

}
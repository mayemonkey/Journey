package com.wipe.zc.journey.ui.activity;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wipe.zc.journey.domain.User;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.CircleImageView;
import com.wipe.zc.journey.lib.drag.DragLayout;
import com.wipe.zc.journey.lib.drag.DragLayout.Status;
import com.wipe.zc.journey.lib.drag.DragRelativeLayout;
import com.wipe.zc.journey.ui.fragment.CalendarFragment;
import com.wipe.zc.journey.ui.fragment.FragmentFactory;
import com.wipe.zc.journey.ui.fragment.HomeFragment;
import com.wipe.zc.journey.ui.fragment.TotalFragment;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.ToastUtil;
import com.wipe.zc.journey.view.MonthPickerDialog;
import com.wipe.zc.journey.view.YearPickerDialog;
import com.wipe.zc.journey.R;

public class HomeActivity extends FragmentActivity implements OnClickListener {

	private static int Page_Home = 1, Page_Calendar = 2, Page_Total = 3;
	private int currentPage = Page_Home;
	private DragLayout dl_home;
	private DragRelativeLayout drl_home;
	private FragmentTransaction ft;
	private TextView tv_title_text;
	private ImageView iv_title_left;
	private ImageView iv_title_right;
	private TextView tv_menu_home;
	private TextView tv_menu_calendar;
	private TextView tv_menu_total;
	private TextView tv_total_year;

	private long exitTime = 0l;
	private CircleImageView civ_home_favicon;
	private TextView tv_home_nickname;
	private TextView tv_home_email;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				tv_home_nickname.setText("");
				tv_home_email.setText("");
				break;

			case 2:
				User user = (User) msg.obj;
				tv_home_nickname.setText(user.getNickname());
				tv_home_email.setText(user.getEmail());
				break;

			case 3:
				Bitmap bm = (Bitmap) msg.obj;
				civ_home_favicon.setImageBitmap(bm);
				// iv_home_favicon.setBackground(new BitmapDrawable(bm));
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		// 获取当前登陆的用户名
		new Thread(new Runnable() {
			public void run() {
				String nickname = MyApplication.getNickname();
				if (nickname != null) {
					User user = new User();
					user.setNickname(nickname);
					requestImage(nickname, user);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		}).start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		initLeftView();
		initContentView();
	}

	/**
	 * 初始化左侧视图
	 */
	private void initLeftView() {
		LinearLayout ll_menu = (LinearLayout) findViewById(R.id.ly_menu);
		tv_menu_home = (TextView) ll_menu.findViewById(R.id.tv_menu_home);
		tv_menu_calendar = (TextView) ll_menu.findViewById(R.id.tv_menu_calendar);
		tv_menu_total = (TextView) ll_menu.findViewById(R.id.tv_menu_total);

		tv_menu_home.setOnClickListener(this);
		tv_menu_calendar.setOnClickListener(this);
		tv_menu_total.setOnClickListener(this);
	}

	/**
	 * 初始化内容视图
	 */
	private void initContentView() {
		dl_home = (DragLayout) findViewById(R.id.dl_home);
		drl_home = (DragRelativeLayout) findViewById(R.id.drl_home);
		drl_home.setDragLayout(dl_home);

		civ_home_favicon = (CircleImageView) findViewById(R.id.iv_home_favicon);
		tv_home_nickname = (TextView) findViewById(R.id.tv_home_nickname);
		tv_home_email = (TextView) findViewById(R.id.tv_home_email);

//		// 获取当前登陆的用户名
//		new Thread(new Runnable() {
//			public void run() {
//				String nickname = MyApplication.getNickname();
//				if (nickname != null) {
//					User user = new User();
//					user.setNickname(nickname);
//					requestImage(nickname, user);
//				} else {
//					handler.sendEmptyMessage(1);
//				}
//			}
//		}).start();

		// 初始化标题栏
		tv_title_text = (TextView) findViewById(R.id.tv_title_text);
		tv_title_text.setTag(Calendar.getInstance());
		tv_title_text.setOnClickListener(this);
		// 左侧菜单栏
		iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
		iv_title_left.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 打开菜单栏
				dl_home.open(true);
			}
		});
		// 右侧按钮
		iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
		iv_title_right.setOnClickListener(this);
		// 右侧隐藏TextView
		tv_total_year = (TextView) findViewById(R.id.tv_total_year);
		tv_total_year.setTag(Calendar.getInstance());
		tv_total_year.setOnClickListener(this);

		// 初始化替换FrameLayout
		// 获取Fragment管理器，开启事务，替换View
		ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fl_content, FragmentFactory.createFactory(0), "home").commit();
	}

	/**
	 * 网络请求头像
	 * 
	 * @param user
	 */
	public void requestImage(String nickname, User user) {
		String result = HttpUtil.requestByPost(AppURL.getuser, user);
		if (result != null) {
			String[] userData = result.split(":");
			String email = userData[1];

			// 设置用户名及邮箱
			Message msg = new Message();
			msg.what = 2;
			msg.obj = new User(nickname, email);
			handler.sendMessage(msg);

			// 查询缓存文件
			File file = new File(getCacheDir().getAbsolutePath() + "//" + nickname + ".jpg");// 将要保存图片的路径
			if (file.exists()) { // 存在缓存文件
				Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
				Message msg3 = new Message();
				msg3.what = 3;
				msg3.obj = bm;
				handler.sendMessage(msg3);
			} else { // 不存在缓存文件
				try {
					if (!new File(getCacheDir().getAbsolutePath()).exists()) {
						new File(getCacheDir().getAbsolutePath()).mkdirs();
					}
					file.createNewFile();
					Bitmap bm = HttpUtil.getImage(AppURL.getimage, file, nickname);
					Message msg3 = new Message();
					msg.what = 3;
					msg.obj = bm;
					handler.sendMessage(msg3);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 提供公开设置标题文本方法
	 * 
	 * @param text
	 */
	public void setTitleText(CharSequence text) {
		tv_title_text.setText(text);
	}

	/**
	 * 设置标题文本tag
	 * 
	 * @param tag
	 */
	public void setTitleTag(Object tag) {
		tv_title_text.setTag(tag);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (dl_home.getStatus() == Status.Open) {
				dl_home.close(true);
				return true;
			} else if (dl_home.getStatus() == Status.Close) {
				// 两次返回退出
				if (System.currentTimeMillis() - exitTime > 2000) {
					ToastUtil.shortToast("再按一次返回键退出");
					exitTime = System.currentTimeMillis();
				} else {
					finish();
				}
				return true;
			}
			break;

		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 点击监听
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_menu_home:
			iv_title_right.setVisibility(View.VISIBLE);
			iv_title_right.setEnabled(true);
			tv_total_year.setVisibility(View.INVISIBLE);
			tv_total_year.setEnabled(false);
			tv_title_text.setEnabled(true);
			currentPage = Page_Home;
			// 替换Fragment
			ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.fl_content, FragmentFactory.createFactory(0), "home").commit();
			// 关闭LeftMenu
			dl_home.close(true);
			break;

		case R.id.tv_menu_calendar:
			iv_title_right.setVisibility(View.VISIBLE);
			iv_title_right.setEnabled(true);
			tv_total_year.setVisibility(View.INVISIBLE);
			tv_total_year.setEnabled(false);
			tv_title_text.setEnabled(true);
			currentPage = Page_Calendar;
			// 替换Fragment
			ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.fl_content, FragmentFactory.createFactory(1), "calendar").commit();
			// 关闭LeftMenu
			dl_home.close(true);
			dl_home.close(true);
			break;

		case R.id.tv_menu_total:
			// 显示TextView，隐藏ImageView
			tv_total_year.setVisibility(View.VISIBLE);
			tv_total_year.setEnabled(true);
			iv_title_right.setVisibility(View.INVISIBLE);
			iv_title_right.setEnabled(false);
			tv_title_text.setEnabled(false);
			currentPage = Page_Total;
			// 替换Fragment
			ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.fl_content, FragmentFactory.createFactory(2), "total").commit();
			// 关闭LeftMenu
			dl_home.close(true);
			break;

		case R.id.tv_title_text:
			MonthPickerDialog monthdialog = new MonthPickerDialog(this, new OnDateSetListener() {
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					switch (currentPage) {
					case 1: // Home页
						HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager()
								.findFragmentByTag("home");
						Calendar calendar_home = Calendar.getInstance();
						calendar_home.set(year, monthOfYear, 1);
						homeFragment.changeDateTo(calendar_home);
						tv_title_text.setTag(calendar_home);

						break;
					case 2: // Calendar页
						CalendarFragment calendarFargment = (CalendarFragment) getSupportFragmentManager()
								.findFragmentByTag("calendar");
						Calendar calendar_calendar = Calendar.getInstance();
						calendar_calendar.set(year, monthOfYear, dayOfMonth);
						calendarFargment.changeDateTo(calendar_calendar);
						tv_title_text.setTag(calendar_calendar);

						break;
					default:
						break;
					}
				}
			}, ((Calendar) tv_title_text.getTag()).get(Calendar.YEAR),
					((Calendar) tv_title_text.getTag()).get(Calendar.MONTH), 1);
			monthdialog.show();
			break;

		case R.id.iv_title_right:
			startActivity(new Intent(this, AddJourneyActivity.class));
			break;

		case R.id.tv_total_year:
			YearPickerDialog yeardialog = new YearPickerDialog(this, new OnDateSetListener() {
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					TotalFragment totalFargment = (TotalFragment) getSupportFragmentManager()
							.findFragmentByTag("total");
					Calendar total_calendar = Calendar.getInstance();
					total_calendar.set(year, monthOfYear, dayOfMonth);
					totalFargment.changeDateTo(total_calendar);
					tv_total_year.setText(String.valueOf(year));
					tv_total_year.setTag(total_calendar);
				}
			}, ((Calendar) tv_total_year.getTag()).get(Calendar.YEAR),
					((Calendar) tv_total_year.getTag()).get(Calendar.MONTH), 1);
			yeardialog.show();
			break;

		default:
			break;
		}
	}
}
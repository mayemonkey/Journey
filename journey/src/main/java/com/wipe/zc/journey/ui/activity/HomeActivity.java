package com.wipe.zc.journey.ui.activity;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.dao.InviteDao;
import com.wipe.zc.journey.domain.User;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.CircleImageView;
import com.wipe.zc.journey.ui.fragment.CalendarFragment;
import com.wipe.zc.journey.ui.fragment.FragmentFactory;
import com.wipe.zc.journey.ui.fragment.HomeFragment;
import com.wipe.zc.journey.ui.fragment.TotalFragment;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.IMUtil;
import com.wipe.zc.journey.util.ImageLoaderOption;
import com.wipe.zc.journey.view.MonthPickerDialog;
import com.wipe.zc.journey.view.YearPickerDialog;

import java.util.Calendar;

//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class HomeActivity extends FragmentActivity implements OnClickListener {

    private static final int Page_Home = 1, Page_Calendar = 2, Page_Total = 3;
    private int currentPage = Page_Home;
    private long exitTime = 0L;

    //    private DragLayout dl_home;
    private CircleImageView civ_menu_message_sign;
    private CircleImageView civ_menu_unread_sign;
    private CircleImageView civ_home_favicon;
    private TextView tv_title_text;
    private ImageView iv_title_right;
    private TextView tv_total_year;
    private TextView tv_home_nickname;
    private TextView tv_home_email;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    tv_home_nickname.setText("");
//                    tv_home_email.setText("");
                    break;

                case 2:
                    User user = (User) msg.obj;
//                    tv_home_nickname.setText(user.getNickname());
//                    tv_home_email.setText(user.getEmail());
                    break;

                case 3:
                    Bitmap bm = (Bitmap) msg.obj;
//                    civ_home_favicon.setImageBitmap(bm);
                    // iv_home_favicon.setBackground(new BitmapDrawable(bm));
                    break;

                default:
                    break;
            }
        }
    };
//    private SlidingMenu slidingMenu;

    @Override
    protected void onResume() {
        super.onResume();
        // 获取当前登陆的用户头像
        new Thread(new Runnable() {
            public void run() {
                String nickname = MyApplication.getNickname();
                if (!TextUtils.isEmpty(nickname)) {
                    User user = new User(nickname);
                    requestImage(nickname, user);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        initSlidingMenu();

        initLeftView();
        initContentView();

        //设置邀请监听
        IMUtil.setHXInviteListener(this);
    }

    /**
     * 初始化SlidingMenu
     */
    private void initSlidingMenu() {

        // 配置 SlidingMenu
//        slidingMenu = new SlidingMenu(this);
//        slidingMenu.setMode(SlidingMenu.LEFT);
//        // 设置触摸屏幕的模式
//        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//
//        // 设置滑动菜单视图的宽度
//        slidingMenu.setBehindOffsetRes(R.dimen.content_width);
//        // 设置渐入渐出效果的值
//        slidingMenu.setFadeDegree(0.35f);
//        /**
//         * SLIDING_WINDOW will include the Title/ActionBar in the content
//         * section of the SlidingMenu, while SLIDING_CONTENT does not.
//         */
//        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        //为侧滑菜单设置布局
//        slidingMenu.setMenu(R.layout.activity_menu);

    }


    /**
     * 初始化左侧视图
     */
    private void initLeftView() {
        //头像、昵称、邮箱
        civ_home_favicon = (CircleImageView) findViewById(R.id.iv_home_favicon);
        tv_home_nickname = (TextView) findViewById(R.id.tv_home_nickname);
        tv_home_email = (TextView) findViewById(R.id.tv_home_email);

        //选项菜单栏
        LinearLayout ll_menu = (LinearLayout) findViewById(R.id.ly_menu);
        TextView tv_menu_home = (TextView) findViewById(R.id.tv_menu_home);
        TextView tv_menu_calendar = (TextView) findViewById(R.id.tv_menu_calendar);
        TextView tv_menu_total = (TextView) findViewById(R.id.tv_menu_total);
        TextView tv_menu_setting = (TextView) findViewById(R.id.tv_menu_setting);

        TextView tv_menu_friends = (TextView) findViewById(R.id.tv_menu_friends);
        civ_menu_unread_sign = (CircleImageView) findViewById(R.id.civ_menu_unread_sign);

        TextView tv_menu_message = (TextView) findViewById(R.id.tv_menu_message);
        civ_menu_message_sign = (CircleImageView) findViewById(R.id.civ_menu_message_sign);

//        tv_menu_home.setOnClickListener(this);
//        tv_menu_calendar.setOnClickListener(this);
//        tv_menu_total.setOnClickListener(this);
//        tv_menu_setting.setOnClickListener(this);
//        tv_menu_friends.setOnClickListener(this);
//        tv_menu_message.setOnClickListener(this);
    }

    /**
     * 初始化内容视图
     */
    private void initContentView() {
//        dl_home = (DragLayout) findViewById(R.id.dl_home);
//        DragRelativeLayout drl_home = (DragRelativeLayout) findViewById(R.id.drl_home);
//        drl_home.setDragLayout(dl_home);

        // 初始化标题栏
        tv_title_text = (TextView) findViewById(R.id.tv_title_text);
        tv_title_text.setTag(Calendar.getInstance());
        tv_title_text.setOnClickListener(this);

        // 左侧菜单栏
        ImageView iv_title_left =  (ImageView) findViewById(R.id.iv_title_left);
        iv_title_left.setOnClickListener(this);
        // 右侧按钮
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_right.setOnClickListener(this);
        // 右侧隐藏TextView
        tv_total_year = (TextView) findViewById(R.id.tv_total_year);
        tv_total_year.setTag(Calendar.getInstance());
        tv_total_year.setOnClickListener(this);

        // 初始化替换FrameLayout
        // 获取Fragment管理器，开启事务，替换View
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, FragmentFactory.createFactory(0), "home").commit();
    }

    /**
     * 网络请求头像
     *
     * @param user 用户对象
     */
    public void requestImage(String nickname, User user) {
        String result = HttpUtil.requestByPost(AppURL.getuser, user);
        if (!TextUtils.isEmpty(result)) {
            String[] userData = result.split(":");
            String email = userData[1];

            // 设置用户名及邮箱
            Message msg = new Message();
            msg.what = 2;
            msg.obj = new User(nickname, email);
            handler.sendMessage(msg);

            //设置头像路径并显示
            String url = AppURL.showimage + "?nickname=" + nickname;
            ImageLoader.getInstance().displayImage(url, civ_home_favicon, ImageLoaderOption.pager_options);

        }
    }

    /**
     * 提供公开设置标题文本方法
     *
     * @param text 文本内容
     */
    public void setTitleText(CharSequence text) {
        tv_title_text.setText(text);
    }


    /**
     * 设置标题文本tag
     *
     * @param tag 标题文本内容
     */
    public void setTitleTag(Object tag) {
        tv_title_text.setTag(tag);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                if (slidingMenu.isMenuShowing()) {
//                    slidingMenu.showContent(true);
//                    return true;
//                } else  {
//                    // 两次返回退出
//                    if (System.currentTimeMillis() - exitTime > 2000) {
//                        ToastUtil.shortToast("再按一次返回键退出");
//                        exitTime = System.currentTimeMillis();
//                    } else {
//                        android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
//                        System.exit(0);
//                    }
//                    return true;
//                }
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
            case R.id.iv_title_left:
//                if (!slidingMenu.isMenuShowing()) {
//                    // 打开菜单栏
//                    slidingMenu.showMenu(true);
//                }
                break;

            case R.id.tv_menu_home:
                setTitleStyle(false, true, true);
                currentPage = Page_Home;
                // 替换Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, FragmentFactory.createFactory(0), "home").commit();
                // 关闭LeftMenu
//                slidingMenu.showContent(true);
                break;

            case R.id.tv_menu_calendar:
                setTitleStyle(false, true, true);
                currentPage = Page_Calendar;
                // 替换Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, FragmentFactory.createFactory(1), "calendar").commit();
                // 关闭LeftMenu
//                slidingMenu.showContent(true);
                break;

            case R.id.tv_menu_total:
                // 显示TextView，隐藏ImageView
                setTitleStyle(true, false, false);
                currentPage = Page_Total;
                // 替换Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, FragmentFactory.createFactory(2), "total").commit();
                // 关闭LeftMenu
//                slidingMenu.showContent(true);
                break;

            case R.id.tv_menu_friends:
                setTitleStyle(false, false, false);
                // 替换Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, FragmentFactory.createFactory(3), "friends").commit();
                // 关闭LeftMenu
//                slidingMenu.showContent(true);
                break;

            case R.id.tv_menu_setting:
                setTitleStyle(false, false, false);
                // 替换Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, FragmentFactory.createFactory(4), "setting").commit();
                // 关闭LeftMenu
//                slidingMenu.showContent(true);
                break;

            case R.id.tv_menu_message:
                setTitleStyle(false, false, false);
                startActivity(new Intent(this, MessageActivity.class));
//                slidingMenu.showContent(true);
                break;

            case R.id.tv_title_text:
                MonthPickerDialog monthdialog = new MonthPickerDialog(this, new OnDateSetListener
                        () {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int
                            dayOfMonth) {
                        switch (currentPage) {
                            case 1: // Home页
                                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
                                Calendar calendar_home = Calendar.getInstance();
                                calendar_home.set(year, monthOfYear, 1);
                                homeFragment.changeDateTo(calendar_home);
                                tv_title_text.setTag(calendar_home);

                                break;
                            case 2: // Calendar页
                                CalendarFragment calendarFargment = (CalendarFragment) getSupportFragmentManager().findFragmentByTag("calendar");
                                Calendar calendar_calendar = Calendar.getInstance();
                                calendar_calendar.set(year, monthOfYear, dayOfMonth);
                                calendarFargment.changeDateTo(calendar_calendar);
                                tv_title_text.setTag(calendar_calendar);

                                break;
                            default:
                                break;
                        }
                    }
                }, ((Calendar) tv_title_text.getTag()).get(Calendar.YEAR), ((Calendar) tv_title_text.getTag()).get(Calendar.MONTH), 1);
                monthdialog.show();
                break;

            case R.id.iv_title_right:
                startActivity(new Intent(this, AddJourneyActivity.class));
                break;

            case R.id.tv_total_year:
                YearPickerDialog yeardialog = new YearPickerDialog(this, new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int
                            dayOfMonth) {
                        TotalFragment totalFragment = (TotalFragment) getSupportFragmentManager().findFragmentByTag("total");
                        Calendar total_calendar = Calendar.getInstance();
                        total_calendar.set(year, monthOfYear, dayOfMonth);
                        totalFragment.changeDateTo(total_calendar);
                        tv_total_year.setText(String.valueOf(year));
                        tv_total_year.setTag(total_calendar);
                    }
                }, ((Calendar) tv_total_year.getTag()).get(Calendar.YEAR), ((Calendar) tv_total_year.getTag()).get(Calendar.MONTH), 1);
                yeardialog.show();
                break;

            default:
                break;
        }
    }

    /**
     * 设置标题样式
     *
     * @param year  年份显示boolean
     * @param right 右侧按钮显示boolean
     * @param text  文本内容显示boolean
     */
    private void setTitleStyle(boolean year, boolean right, boolean text) {
        tv_total_year.setVisibility(year ? View.VISIBLE : View.INVISIBLE);
        tv_total_year.setEnabled(year);
        iv_title_right.setVisibility(right ? View.VISIBLE : View.INVISIBLE);
        iv_title_right.setEnabled(right);
        tv_title_text.setEnabled(text);
    }

    protected void onStart() {
        super.onStart();

        if (InviteDao.queryAllInvite().size() > 0) {
//            civ_menu_message_sign.setVisibility(View.VISIBLE);
        } else {
//            civ_menu_message_sign.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置未读标记
     */
    public void setUnreadSign() {
        int count = EMChatManager.getInstance().getUnreadMsgsCount();
        if (count > 0) {
            civ_menu_unread_sign.setVisibility(View.VISIBLE);
        } else {
            civ_menu_unread_sign.setVisibility(View.INVISIBLE);
        }
    }

}

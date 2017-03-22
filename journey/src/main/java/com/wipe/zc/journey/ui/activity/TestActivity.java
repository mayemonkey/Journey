package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.ui.fragment.FragmentFactory;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestActivity extends Activity implements View.OnClickListener {

    private DrawerLayout dl_home;
    private CircleImageView civ_menu_unread_sign;
    private CircleImageView civ_menu_message_sign;
    private CircleImageView civ_home_favicon;
    private TextView tv_menu_home;
    private TextView tv_menu_calendar;
    private TextView tv_menu_total;
    private TextView tv_menu_setting;
    private TextView tv_menu_friends;
    private TextView tv_menu_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);

        initMenuComponent();

        dl_home = (DrawerLayout) findViewById(R.id.dl_home);

        ImageView iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        iv_title_left.setOnClickListener(this);
    }

    /**
     * 初始化菜单模块控件
     */
    private void initMenuComponent() {


        //头像、昵称、邮箱
        civ_home_favicon = (CircleImageView) findViewById(R.id.civ_home_favicon);
        TextView tv_home_nickname = (TextView) findViewById(R.id.tv_home_nickname);
        TextView tv_home_email = (TextView) findViewById(R.id.tv_home_email);

        //选项菜单栏
        LinearLayout ll_menu = (LinearLayout) findViewById(R.id.ly_menu);
        tv_menu_home = (TextView) findViewById(R.id.tv_menu_home);
        tv_menu_calendar = (TextView) findViewById(R.id.tv_menu_calendar);
        tv_menu_total = (TextView) findViewById(R.id.tv_menu_total);
        tv_menu_setting = (TextView) findViewById(R.id.tv_menu_setting);

        tv_menu_friends = (TextView) findViewById(R.id.tv_menu_friends);
        civ_menu_unread_sign = (CircleImageView) findViewById(R.id.civ_menu_unread_sign);

        tv_menu_message = (TextView) findViewById(R.id.tv_menu_message);
        civ_menu_message_sign = (CircleImageView) findViewById(R.id.civ_menu_message_sign);

        MenuClickListener listener = new MenuClickListener();
        tv_menu_home.setOnClickListener(listener);
        tv_menu_home.setTag(1);
        tv_menu_calendar.setOnClickListener(listener);
        tv_menu_calendar.setTag(2);
        tv_menu_total.setOnClickListener(listener);
        tv_menu_total.setTag(3);
        tv_menu_setting.setOnClickListener(listener);
        tv_menu_setting.setTag(4);
        tv_menu_friends.setOnClickListener(listener);
        tv_menu_friends.setTag(5);
        tv_menu_message.setOnClickListener(listener);
        tv_menu_message.setTag(6);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_left:
                dl_home.openDrawer(GravityCompat.START);
                break;




        }
    }


    private class MenuClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            tv_menu_home.setEnabled(true);
            tv_menu_calendar.setEnabled(true);
            tv_menu_total.setEnabled(true);
            tv_menu_friends.setEnabled(true);
            tv_menu_message.setEnabled(true);
            tv_menu_setting.setEnabled(true);
            v.setEnabled(false);

            dl_home.closeDrawers();

            switchFragment(v);
        }
    }

    private void switchFragment(View v) {
        int index = (int) v.getTag();
        getFragmentManager().beginTransaction().replace(R.id.fl_home, FragmentFactory.createFactory(index), String.valueOf(index)).commit();
    }
}

package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.wipe.zc.journey.R;

public class TestActivity extends Activity{

    private DrawerLayout dl_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);

        dl_home = (DrawerLayout) findViewById(R.id.dl_home);

        ImageView iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        iv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_home.openDrawer(GravityCompat.START);
            }
        });
    }
}

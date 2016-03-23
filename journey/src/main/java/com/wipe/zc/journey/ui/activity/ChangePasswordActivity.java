package com.wipe.zc.journey.ui.activity;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.ToastUtil;
import com.wipe.zc.journey.util.ViewUtil;

public class ChangePasswordActivity extends Activity implements View.OnClickListener {

    private ImageView iv_password_cancle;
    private ImageView iv_password_ensure;
    private EditText et_password_before;
    private View ve_password_before;
    private EditText et_password_new_first;
    private View ve_password_new_first;
    private EditText et_password_new_second;
    private View ve_password_new_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        intiWidget();
    }

    /**
     * 初始化控件
     */
    private void intiWidget() {
        iv_password_cancle = (ImageView) findViewById(R.id.iv_password_cancle);
        iv_password_cancle.setOnClickListener(this);

        iv_password_ensure = (ImageView) findViewById(R.id.iv_password_ensure);
        iv_password_ensure.setOnClickListener(this);

        et_password_before = (EditText) findViewById(R.id.et_password_before);
        ve_password_before = findViewById(R.id.ve_password_before);
        et_password_new_first = (EditText) findViewById(R.id.et_password_new_first);
        ve_password_new_first = findViewById(R.id.ve_password_new_first);
        et_password_new_second = (EditText) findViewById(R.id.et_password_new_second);
        ve_password_new_second = findViewById(R.id.ve_password_new_second);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_password_cancle:
                finish();
                break;

            case R.id.iv_password_ensure:
                //修改密码
                boolean flag_before = ViewUtil.checkEmptyData(et_password_before, ve_password_before);
                boolean flag_first = ViewUtil.checkEmptyData(et_password_new_first, ve_password_new_first);
                boolean flag_second = ViewUtil.checkEmptyData(et_password_new_second, ve_password_new_second);
                if (flag_before && flag_first && flag_second) {      //判断数据完整性
                    final String before = et_password_before.getText().toString();
                    final String first = et_password_new_first.getText().toString();
                    String second = et_password_new_second.getText().toString();

                    if (first.equals(second)) {     //两次输入密码相同
                        changePassword(before,first);
                    }
                }
                break;
        }
    }

    /**
     * 修改密码(子线程)
     */
    private void changePassword(final String before, final String first) {
        new Thread(new Runnable() {
            public void run() {
                String result = HttpUtil.requestUser_ByOK("", MyApplication.getNickname(), "");
                if (result != null) {      //当结果不为空
                    if (result.equals(before)) {
                        //修改密码
                        String result_update = HttpUtil.requestUser_ByOK("", MyApplication.getNickname(), first);
                        if (result_update != null) {
                            if(result_update.equals("修改成功")){
                                ToastUtil.shortToast("密码修改成功");
                                finish();
                            }else{
                                ToastUtil.shortToast("密码修改失败");
                            }
                        }
                    }
                }
            }
        }).start();
    }
}

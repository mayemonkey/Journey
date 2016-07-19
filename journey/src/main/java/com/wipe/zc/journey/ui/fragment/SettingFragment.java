package com.wipe.zc.journey.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.ui.activity.HomeActivity;
import com.wipe.zc.journey.ui.activity.LoginActivity;

/**
 * 设置
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_setting, null);

        //初始化标题
        ((HomeActivity) getActivity()).setTitleText("设置");
        initWidget();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        TextView tv_setting_exit = (TextView) view.findViewById(R.id.tv_setting_exit);
        tv_setting_exit.setOnClickListener(this);

        TextView tv_setting_changepassword = (TextView) view.findViewById(R.id.tv_setting_changepassword);
        tv_setting_changepassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setting_exit:
                new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确定要注销账号吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EMChatManager.getInstance().logout(new EMCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        //清除缓存用户名及密码
                                        getActivity().getSharedPreferences("config", Context.MODE_PRIVATE).edit().clear().commit();
                                        getActivity().finish();
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                    }

                                    @Override
                                    public void onError(int i, String s) {

                                    }

                                    @Override
                                    public void onProgress(int i, String s) {

                                    }
                                });
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();

                break;
        }
    }
}

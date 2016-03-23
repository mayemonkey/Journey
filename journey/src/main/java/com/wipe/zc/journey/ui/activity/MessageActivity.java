package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.dao.InviteDao;
import com.wipe.zc.journey.domain.Invite;
import com.wipe.zc.journey.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/3/22.
 */
public class MessageActivity extends Activity implements View.OnClickListener {

    private List<Invite> list = new ArrayList<>();
    private MessageAdapter adapter;
    private RecyclerView rv_message;
    private ImageView iv_message_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);

        //移除通知
        NotificationManager nManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        nManager.cancel(0);

        initWisget();
        requestData();
    }

    /**
     * 初始化控件
     */
    private void initWisget() {
        rv_message = (RecyclerView) findViewById(R.id.rv_message);
        iv_message_cancel = (ImageView) findViewById(R.id.iv_message_cancel);
        iv_message_cancel.setOnClickListener(this);

        rv_message.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageAdapter(list);
        rv_message.setAdapter(adapter);
//        rv_message.setItemAnimator();
    }

    private void requestData(){
        List<Invite> list_result = InviteDao.queryAllInvite();
        if(list_result!= null){
            list.clear();
            list.addAll(list_result);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_message_cancel:
                finish();
                break;
        }
    }
}

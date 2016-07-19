package com.wipe.zc.journey.util;

import android.content.Context;
import android.content.Intent;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.wipe.zc.journey.dao.InviteDao;
import com.wipe.zc.journey.global.MyApplication;

import java.util.List;

/**
 * 即时通讯工具包
 */
public class IMUtil {
    /**
     * 监听邀请方法
     */
    public static void setHXInviteListener(final Context packageContext) {
        //环信初始化
        EMContactManager.getInstance().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(List<String> list) {
                if (list != null && list.size() > 0) {
                    Intent intent = new Intent("im.add.broadcast.action");
                    String name_result = "";
                    int i = 0;
                    for (String name : list) {
                        if (i == 0) {
                            name_result = name;
                        } else {
                            name_result += "-" + name;
                        }
                        i++;
                    }
                    intent.putExtra("data", name_result);
                    MyApplication.getContext().sendBroadcast(intent);
                }
            }

            @Override
            public void onContactDeleted(List<String> list) {
            }

            @Override
            public void onContactInvited(String inviter, String reason) {
                LogUtil.i("环信好友", "好友:" + inviter + "申请说明" + reason);
                System.out.println("环信好友------好友:" + inviter + "申请说明" + reason);

                if (InviteDao.checkInvite(inviter)) {
                    return;
                }
                InviteDao.addInvite(inviter, reason);

                NotificationUtil.notificationShow(inviter, packageContext);
            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {

            }
        });
        EMChat.getInstance().setAppInited();
    }

}

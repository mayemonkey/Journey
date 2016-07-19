package com.wipe.zc.journey.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.ui.activity.HomeActivity;
import com.wipe.zc.journey.ui.activity.MessageActivity;

/**
 * 通知栏工具包
 */
public class NotificationUtil {

    public static void notificationShow(String inviter, Context packageContext) {
        NotificationManager nManager = (NotificationManager) MyApplication.getContext()
                .getSystemService(Context
                        .NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.icon_icon, "行程好友邀请",
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(packageContext, MessageActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(packageContext, 0,
                notificationIntent, 0);
        Context context = MyApplication.getContext();
        CharSequence contentTitle = "好友邀请提示";
        CharSequence contentText = inviter + "  想要添加你为好友";
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        nManager.notify(0, notification);
    }
}

package com.wipe.zc.journey.global;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

public class MyApplication extends Application {

    private static int total_year = Calendar.getInstance().get(Calendar.YEAR);

    private static Context context;

    //登陆完成，全局使用昵称
    private static String nickname;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //ImageLoader初始化
        initImageLoader(context);

        //环信初始化
        initHX();

        //百度地图初始化
        SDKInitializer.initialize(context);
    }

    /**
     * 初始化环信
     */
    private void initHX() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
        // name就立即返回

        if (processAppName == null
                || !processAppName.equalsIgnoreCase("com.wipe.zc.journey")) {
            Log.e("IM", "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

//		EMChat.getInstance().setAutoLogin(false);
        EMChat.getInstance().init(getApplicationContext());
        // 在做代码混淆的时候需要设置成false
        EMChat.getInstance().setDebugMode(true);
        initHXOptions();
    }


    /**
     * 初始化环信配置
     */
    protected void initHXOptions() {
        Log.d("IM", "init HuanXin Options");

        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的true，改成需要验证false
        options.setAcceptInvitationAlways(false);
        // 默认环信是不维护好友关系列表的，如果app依赖环信的好友关系，把这个属性设置为true
        options.setUseRoster(true);
        options.setNumberOfMessagesLoaded(1);
    }

    public static int getTotal_year() {
        return total_year;
    }

    public static void setTotal_year(int total_year) {
        MyApplication.total_year = total_year;
    }

    /**
     * 环信获取App名称
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)
                    (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    /**
     * 初始化ImageLoader
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
//		  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);				//配置下载图片的线程优先级
        config.denyCacheImageMultipleSizesInMemory();					//不会在内存中缓存多个大小的图片
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());	//为了保证图片名称唯一
        config.diskCacheSize(50 * 1024 * 1024); 						// 50 MiB
        //内存缓存大小默认是：app可用内存的1/8
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
//		ImageLoader.getInstance().init( ImageLoaderConfiguration.createDefault(this));
    }

    /**
     * 获取应用上下文
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }

    public static String getNickname() {
        return nickname;
    }

    public static void setNickname(String nickname) {
        MyApplication.nickname = nickname;
    }

}

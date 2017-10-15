package com.linmama.dinning.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.R;

import java.lang.reflect.Method;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by jingkang on 2017/3/20
 */

public class NotificationUtils {
    public static Notification createNotification(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification notification;
        if (isNotificationBuilderSupported()) {
            LogUtils.d("NotificationUtils", "isNotificationBuilderSupported");
            notification = buildNotificationWithBuilder(context, pendingIntent, title, text, iconId);
        } else {
            // 低于API 11 Honeycomb
            LogUtils.d("NotificationUtils", "buildNotificationPreHoneycomb");
            notification = buildNotificationPreHoneycomb(context, pendingIntent, title, text, iconId);
        }
        return notification;
    }

    public static boolean isNotificationBuilderSupported() {
        try {
            return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) && Class.forName("android.app.Notification.Builder") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    private static Notification buildNotificationPreHoneycomb(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification notification = new Notification(iconId, "", System.currentTimeMillis());
        try {
            // try to call "setLatestEventInfo" if available
            Method m = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
            m.invoke(notification, context, title, text, pendingIntent);
        } catch (Exception e) {
            // do nothing
        }
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    private static Notification buildNotificationWithBuilder(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        android.app.Notification.Builder builder = new android.app.Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(iconId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

    public static void showNotification(Context context, String msg, int type) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // 工具类判断版本，通过不同的方式获取Notification
        Notification notification = createNotification(context, pendingIntent, "您有新消息", msg, R.mipmap.ic_launcher);
        notification.tickerText = "您有订单提醒";
        notification.when = System.currentTimeMillis();
        // 使用默认的声音
//        notification.defaults = Nification.DEFAULT_SOUND;
        int voiceId = -1;
        switch (type) {
            case 0:
                voiceId = R.raw.voice_self_warn;
                break;
            case 1:
                voiceId = R.raw.voice_self_help;
                break;
            case 2:
                voiceId = R.raw.voice_cutomer_remind;
                break;
            case 3:
                voiceId = R.raw.voice_quit_order;
                break;
            case 4:
                voiceId = R.raw.voice_appoint_order;
                break;
            case 5:
                voiceId = R.raw.voice_takeout_order;
                break;
            case 6:
                voiceId = R.raw.voice_close;
                break;
        }
        boolean isVoiceWarn = (boolean) SpUtils.get(Constants.VOICE_WARN, true);
        if (voiceId == -1) {
            notification.defaults = Notification.DEFAULT_SOUND;
        } else {
            if (isVoiceWarn) {
                notification.sound = Uri.parse("android.resource://com.linmama.dinning/" + voiceId);
            }
        }
        // 使用默认的震动 需要添加uses-permission  android.permission.VIBRATE
        // notification.defaults = Notification.DEFAULT_VIBRATE;
        // 使用默认的Light
        notification.defaults = Notification.DEFAULT_LIGHTS;
        // 所有的都是用默认值
//        notification.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(R.mipmap.ic_launcher, notification);

        // 5S后，执行取消的方法，即5S后 自动清除该通知栏 ,根据需求考虑是否需要这样
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                notificationManager.cancel(R.drawable.flag_mark_blue);
//            }
//        },5*1000);

    }
}

package com.dengyun.baselibrary.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


/**
 * 可以提取 init 静态方法，配置App的通知相关参数，
 * 之后就静态发送通知了
 * 目前用处不多，只在推送处使用，暂不重新封装处理
 */

/**
 * @Title 推送的通知工具类
 * @Desc: 适配8.0的通知栏问题，9.0暂没有适配
 * @Author: zhoubo
 * @CreateDate: 2020年03月05日16:09:05
 */
public class PushNotificationUtils {
    private final String channelId; // 8.0以上版本必须设置渠道id，否则不能推送,例如xxx_push
    private final String channelName; // 渠道名称 例如 xxx通知
    private final Class<?> broadcastIntentCls;//点击通知跳转的广播
    private final int smallIcon; // 状态栏上的通知小图标，必须设置

    private NotificationManager mManager;

    /**
     * @param channelId   8.0以上版本必须设置渠道id，否则不能推送
     * @param channelName 渠道名称
     * @param smallIcon   状态栏上的通知小图标，必须设置
     * @param broadcastIntentCls
     */
    public PushNotificationUtils(String channelId, String channelName, int smallIcon, Class<?> broadcastIntentCls) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.broadcastIntentCls = broadcastIntentCls;
        this.smallIcon = smallIcon;
        if (Build.VERSION.SDK_INT >= 26) createChannels();
    }

    /**
     * 发送通知
     *
     * @param notificationId 通知的id号,如果一个应用id号相同的id，后面的会替换掉前面的
     * @param contentTitle   通知卡片的标题
     * @param contentText    通知卡片的内容
     * @param bundle         通知中携带的bundle
     */
    public void sendNotification(int notificationId, String contentTitle, String contentText, Bundle bundle) {
        Notification notification = getNotificationBuilder(contentTitle, contentText, bundle).build();
        getManager().notify(notificationId, notification);
    }

    /**
     * 取消通知
     */
    public void cancelNotification(int notificationId) {
        getManager().cancel(notificationId);
    }

    /*-----------------------------------------------------------------------------------------------*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        // channel.canBypassDnd();//可否绕过，请勿打扰模式
        //闪光
        channel.enableLights(true);
        //指定闪光是的灯光颜色
        channel.setLightColor(Color.RED);
        //是否允许震动
        channel.enableVibration(true);
        //锁屏显示通知
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        //桌面laucher消息角标
        channel.canShowBadge();
        //获取系统通知响铃声音配置
        //channel.getAudioAttributes();
        //获取通知渠道组
        channel.getGroup();
        //设置可以绕过，请勿打扰模式
        channel.setBypassDnd(true);
        // channel.setVibrationPattern(new long[]{100, 100, 200});//震动的模式，震3次，第一次100，第二次100，第三次200毫秒
        //是否会闪光
        channel.shouldShowLights();
        /*Uri soundUri = Uri.parse("android.resource://" + AppUtils.getAppPackageName()+ "/" + R.raw.notifysound);
        channel.setSound(soundUri,null);*/
        //通知管理者创建的渠道
        getManager().createNotificationChannel(channel);
    }

    /**
     * @param contentTitle 通知标题
     * @param contentText  通知内容
     * @param bundle       携带的内容
     * @return
     */
    private NotificationCompat.Builder getNotificationBuilder(String contentTitle, String contentText, Bundle bundle) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.getApp(), channelId)
                .setTicker("妃子校有新通知")
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)
                .setSmallIcon(smallIcon)  //状态栏上的通知小图标，必须设置
                .setOnlyAlertOnce(true) //设置是否只通知一次，这个效果主要体现在震动、提示音、悬挂通知上
                // .setSound(soundUri)
                .setAutoCancel(true)   //允许用户点击删除按钮删除
                .setOngoing(false);      //允许滑动删除

        //设置点击通知栏的动作为启动另外一个广播
        Intent broadcastIntent = new Intent(Utils.getApp(), broadcastIntentCls);
        if (null != bundle) broadcastIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Utils.getApp(), 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        return builder;
    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
}

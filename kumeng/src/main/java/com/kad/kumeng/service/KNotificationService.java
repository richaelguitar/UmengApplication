package com.kad.kumeng.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.kad.kumeng.R;
import com.kad.kumeng.push.KPushManager;
import com.kad.kumeng.utils.KUmengUtils;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * 自定义通知类
 * @author xww
 * @since 2018-09-28
 */
public class KNotificationService extends Service {


    private static final String TAG = KNotificationService.class.getName();
    public static  final int KAD_NOTIFICATION_STYLE_1=10010;
    public static  final int KAD_NOTIFICATION_STYLE_2=10011;
    public static  final String NOTIFICATION_CHANNEL_ID ="kad_channel_id";
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        //每当服务器推送消息过来的时候就会带值过来
        String message = intent.getStringExtra(KPushManager.PUSH_MESSAGE);
        try {
            UMessage msg = new UMessage(new JSONObject(message));
            //如果是需要发通知栏的则发出消息
            if("notification".equalsIgnoreCase(msg.display_type)){

                //todo这里根据服务器推送过来的配置设置
                //设置通知栏样式
                switch (msg.builder_id){
                    case KAD_NOTIFICATION_STYLE_1:
                        showNotificationStyleOne(msg);
                        break;
                    case KAD_NOTIFICATION_STYLE_2:
                        showNotificationStyleTwo(msg);
                        break;
                    default:
                        showDefaultNotification(msg);
                        break;
                }
            }

            //发广播本地保存
            Intent saveIntent = new Intent(KPushManager.PUSH_RECEIVER_MESSAGE_ACTION);
            saveIntent.putExtra(KPushManager.PUSH_MESSAGE,message);
            sendBroadcast(saveIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 自定义样式1
     * @param msg
     */
    private void showNotificationStyleOne(UMessage msg) {
        // TODO: 2018/9/28
    }

    /**
     * 自定义样式2
     * @param msg
     */
    private void showNotificationStyleTwo(UMessage msg) {
        // TODO: 2018/9/28
    }

    private void showDefaultNotification(UMessage msg) {
        int id = new Random(System.nanoTime()).nextInt();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        NotificationCompat.Builder mBuilder = null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            //创建渠道通知
            manager.createNotificationChannel(createNotificationChannel(NOTIFICATION_CHANNEL_ID,(String)KUmengUtils.getAndroidManifestMetaData(mContext,"UMENG_CHANNEL")));
            mBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        }else{
            mBuilder = new NotificationCompat.Builder(this,null);
        }
        mBuilder.setContentTitle(msg.title)
                .setContentText(msg.text)
                .setTicker(msg.ticker)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.umeng_push_notification_default_small_icon)
                .setAutoCancel(true);
        Notification notification = mBuilder.build();
        PendingIntent clickPendingIntent = getClickPendingIntent(this, msg);
        PendingIntent dismissPendingIntent = getDismissPendingIntent(this, msg);
        notification.deleteIntent = dismissPendingIntent;
        notification.contentIntent = clickPendingIntent;
        manager.notify(id, notification);
    }

    @TargetApi(26)
    private NotificationChannel createNotificationChannel(String channelId,String channelName){
        NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
        channel.setLightColor(Color.RED);//设置小红点颜色
        channel.enableLights(true);//是否在桌面icon右上角展示小红点
        channel.setShowBadge(true);//长按桌面应用图标是否会显示渠道通知
        return channel;
    }

    /**
     * 点击查看通知
     * @param context
     * @param msg
     * @return
     */
    public PendingIntent getClickPendingIntent(Context context, UMessage msg) {
        Intent clickIntent = new Intent(KPushManager.ACTION_CLICK);
        clickIntent.putExtra(KPushManager.PUSH_MESSAGE,
                msg.getRaw().toString());

        return PendingIntent.getBroadcast(context,
                (int) (System.currentTimeMillis()),
                clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /**
     * 点击忽略通知
     * @param context
     * @param msg
     * @return
     */
    public PendingIntent getDismissPendingIntent(Context context, UMessage msg) {
        Intent deleteIntent = new Intent(KPushManager.ACTION_DISMISS);
        deleteIntent.putExtra(KPushManager.PUSH_MESSAGE,
                msg.getRaw().toString());

         return PendingIntent.getBroadcast(context,
                (int) (System.currentTimeMillis() + 1),
                deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

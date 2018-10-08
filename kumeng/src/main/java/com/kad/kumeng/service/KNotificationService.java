package com.kad.kumeng.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.kad.kumeng.R;
import com.kad.kumeng.entity.KMessage;
import com.kad.kumeng.push.KPushManager;
import com.kad.kumeng.utils.KUmengUtils;
import com.umeng.message.MsgConstant;
import com.umeng.message.UTrack;
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
    public static UMessage oldMessage = null;
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
            KMessage msg = new KMessage(new JSONObject(message));
            if (oldMessage != null) {
                UTrack.getInstance(getApplicationContext()).setClearPrevMessage(true);
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(oldMessage);
            }
            //todo这里根据服务器推送过来的配置设置
            if(msg.play_vibrate){//设置震动
                KUmengUtils.getInstance(mContext).getPushAgent().setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
            }
            if(msg.play_sound){//设置声音
                KUmengUtils.getInstance(mContext).getPushAgent().setNotificationPlaySound(R.raw.medic_sound);
            }
            if(msg.play_lights){//设置呼吸灯
                KUmengUtils.getInstance(mContext).getPushAgent().setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
            }
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 自定义样式1
     * @param msg
     */
    private void showNotificationStyleOne(KMessage msg) {
        // TODO: 2018/9/28
    }

    /**
     * 自定义样式2
     * @param msg
     */
    private void showNotificationStyleTwo(KMessage msg) {
        // TODO: 2018/9/28
    }

    private void showDefaultNotification(KMessage msg) {
        int id = new Random(System.nanoTime()).nextInt();
        oldMessage = msg;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle(msg.title)
                .setContentText(msg.text)
                .setTicker(msg.ticker)
                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.umeng_push_notification_default_small_icon)
                .setAutoCancel(true);
        Notification notification = mBuilder.getNotification();
        PendingIntent clickPendingIntent = getClickPendingIntent(this, msg);
        PendingIntent dismissPendingIntent = getDismissPendingIntent(this, msg);
        notification.deleteIntent = dismissPendingIntent;
        notification.contentIntent = clickPendingIntent;
        manager.notify(id, notification);
    }

    /**
     * 点击查看通知
     * @param context
     * @param msg
     * @return
     */
    public PendingIntent getClickPendingIntent(Context context, UMessage msg) {
        Intent clickIntent = new Intent();
        clickIntent.setClass(context, KPushManager.KPushBroadcastReceiver.class);
        clickIntent.putExtra(KPushManager.PUSH_MESSAGE,
                msg.getRaw().toString());
        clickIntent.putExtra(KPushManager.EXTRA_KEY_ACTION,
                KPushManager.ACTION_CLICK);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                (int) (System.currentTimeMillis()),
                clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        return clickPendingIntent;
    }

    /**
     * 点击忽略通知
     * @param context
     * @param msg
     * @return
     */
    public PendingIntent getDismissPendingIntent(Context context, UMessage msg) {
        Intent deleteIntent = new Intent();
        deleteIntent.setClass(context, KPushManager.KPushBroadcastReceiver.class);
        deleteIntent.putExtra(KPushManager.PUSH_MESSAGE,
                msg.getRaw().toString());
        deleteIntent.putExtra(
                KPushManager.EXTRA_KEY_ACTION,
                KPushManager.ACTION_DISMISS);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context,
                (int) (System.currentTimeMillis() + 1),
                deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return deletePendingIntent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

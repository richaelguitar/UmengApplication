package com.kad.kumeng.service;

import android.content.Context;
import android.content.Intent;


import com.kad.kumeng.push.KPushManager;
import com.umeng.message.UmengMessageService;

import org.android.agoo.common.AgooConstants;

/**
 * 完全自定义消息透传处理
 * 1、需要自己去完成通知显示
 * 2、自定义处理消息的方式
 * @author  xww
 * @since 2018-09-30
 */
public class KPushService extends UmengMessageService {

    @Override
    public void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Intent notificationIntent = new Intent(context,KNotificationService.class);
        notificationIntent.putExtra(KPushManager.PUSH_MESSAGE,message);
        context.startService(notificationIntent);
    }
}

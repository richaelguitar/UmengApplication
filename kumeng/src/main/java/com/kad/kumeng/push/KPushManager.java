package com.kad.kumeng.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.kad.kumeng.callback.IPushReceiverMessageCallback;
import com.kad.kumeng.callback.IPushRegisterCallback;
import com.kad.kumeng.callback.KTagCallback;
import com.kad.kumeng.entity.KMessage;
import com.kad.kumeng.exception.UmengIsNotInitException;
import com.kad.kumeng.service.KNotificationService;
import com.kad.kumeng.service.KPushService;
import com.kad.kumeng.utils.KUmengUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.tag.TagManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送模块管理
 * @author xww
 * @since 2018-09-28
 */
public class KPushManager {

    public static final String PUSH_MESSAGE = "Message";

    public static final String PUSH_RECEIVER_MESSAGE_ACTION = "com.kad.kumeng.service.KPushBroadcastReceiver.action";

    public static final String EXTRA_KEY_ACTION = "ACTION";

    public static final String ACTION_CLICK = "click";

    public static final String ACTION_DISMISS = "dismiss";

    public static final int EXTRA_ACTION_NOT_EXIST = -1;

    private IPushRegisterCallback pushRegisterCallback;//push推送注册回调

    private IPushReceiverMessageCallback pushReceiverMessageCallback;//push推送消息完成处理回调

    private boolean isInit;

    private Context mContext;

    private PushAgent mPushAgent;

    private KPushBroadcastReceiver kPushBroadcastReceiver;


    public KPushManager(Context context){
        this.mContext = context;
        initMessageReceiver();
    }

    public PushAgent getPushAgent() {
        return mPushAgent;
    }

    public void setPushRegisterCallback(IPushRegisterCallback pushRegisterCallback) {
        this.pushRegisterCallback = pushRegisterCallback;
    }

    public void setPushReceiverMessageCallback(IPushReceiverMessageCallback pushReceiverMessageCallback) {
        this.pushReceiverMessageCallback = pushReceiverMessageCallback;
    }

    /**
     * umeng推送功能初始化
     * 适合在代码里面动态设置appkey,channel
     * @param context
     * @param appkey
     * @param channel
     * @param deviceType
     * @param pushSecret
     */
    public  void init(Context context, String appkey, String channel, int deviceType, String pushSecret) throws Exception {
        UMConfigure.init(context,appkey,channel,deviceType,pushSecret);
        isInit = true;
        register(context);
    }

    /**
     * umeng推送功能初始化
     * 适合在AndroidManifest.xml中设置appkey,channel
     * @param context
     * @param deviceType
     * @param pushSecret
     */
    public  void init(Context context, int deviceType, String pushSecret) throws Exception {

        init(context, KUmengUtils.getAndroidManifestMetaData(context,"UMENG_APPKEY"),KUmengUtils.getAndroidManifestMetaData(context,"UMENG_CHANNEL"),deviceType,pushSecret);
    }

    /**
     * 注册推送服务
     * @param context
     */
    public  void register(Context context) throws Exception {
        if(isInit){
            mPushAgent = PushAgent.getInstance(context);
            mPushAgent.register(umengRegisterCallback);
            mPushAgent.setPushIntentServiceClass(KPushService.class);
        }else{
            throw new UmengIsNotInitException("请先调用KUmengUtils.getInstance(context).init进行初始化后再进行注册");
        }
    }

    //push注册回调
    private IUmengRegisterCallback umengRegisterCallback = new IUmengRegisterCallback() {
        @Override
        public void onSuccess(String s) {
            if(pushRegisterCallback!=null){
                pushRegisterCallback.onSuccess(s);
            }
        }

        @Override
        public void onFailure(String s, String s1) {
            if(pushRegisterCallback!=null){
                pushRegisterCallback.onFailure(s,s1);
            }
        }
    };





    /**
     * 此方法与统计分析sdk中统计日活的方法无关！请务必调用此方法！
     * 如果不调用此方法，不仅会导致按照”几天不活跃”条件来推送失效，还将导致广播发送不成功以及设备描述红色等问题发生。
     * 可以只在应用的主Activity中调用此方法，但是由于SDK的日志发送策略，有可能由于主activity的日志没有发送成功，
     * 而导致未统计到日活数据。
     * @param context
     */
    public  void onAppStart(Context context){
        PushAgent.getInstance(context).onAppStart();
    }


    /**
     * 当应用处于前台时候是否需要显示通知
     * 注意：必须在register之前设置才有效
     * @param isShow
     */
    public void setNotificaitonOnForeground(boolean isShow){
        if(mPushAgent!=null){
            mPushAgent.setNotificaitonOnForeground(isShow);
        }
    }


    /**
     * 添加推送Tag
     * @param tCallBack
     * @param tags
     */
    public void addTags(KTagCallback tCallBack, String... tags){
        if(mPushAgent!=null){
            mPushAgent.getTagManager().addTags(tCallBack,tags);
        }
    }

    /**
     * 删除推送Tag
     * @param tCallBack
     * @param tags
     */
    public void deleteTags(KTagCallback tCallBack, String... tags){
        if(mPushAgent!=null){
            mPushAgent.getTagManager().deleteTags(tCallBack,tags);
        }
    }


    /**
     * 获取服务器所有Tags
     */
    public List<String> getAllTags(){
        final List<String> tags = new ArrayList<>();
        if(mPushAgent!=null){
            mPushAgent.getTagManager().getTags(new TagManager.TagListCallBack() {
                @Override
                public void onMessage(boolean b, List<String> list) {
                    if(b){
                        tags.addAll(list);
                    }
                }
            });
        }

        return tags;
    }


    //全局接受广播，当接受到推送信息后进行处理
    public class KPushBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            KMessage kMessage = (KMessage) intent.getSerializableExtra(PUSH_MESSAGE);
            if(PUSH_RECEIVER_MESSAGE_ACTION.equals(action)){
                if(pushReceiverMessageCallback!=null){
                    pushReceiverMessageCallback.receiverMsg(kMessage);
                }
            }

            switch (action){
                case ACTION_CLICK:
                    //手动统计点击
                    UTrack.getInstance(mContext).setClearPrevMessage(true);
                    KNotificationService.oldMessage = null;
                    UTrack.getInstance(context).trackMsgClick(kMessage);
                    if(pushReceiverMessageCallback!=null){
                        pushReceiverMessageCallback.go(mContext,kMessage);
                    }
                    break;
                case ACTION_DISMISS:
                    //手动统计忽略
                    UTrack.getInstance(context).setClearPrevMessage(true);
                    UTrack.getInstance(context).trackMsgDismissed(kMessage);
                    break;
            }
        }
    }


    /**
     * 初始化并注册广播，这里也是全局广播，不在xml里面声明是
     * 为了方便传入回调接口
     */
    private void initMessageReceiver(){

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PUSH_RECEIVER_MESSAGE_ACTION);
        kPushBroadcastReceiver = new KPushBroadcastReceiver();
        if(mContext!=null){
           mContext.registerReceiver(kPushBroadcastReceiver,intentFilter);
        }
    }
}

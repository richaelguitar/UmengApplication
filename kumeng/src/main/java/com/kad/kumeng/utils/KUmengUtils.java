package com.kad.kumeng.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.kad.kumeng.BuildConfig;
import com.kad.kumeng.analytics.KAnalyticsManager;
import com.kad.kumeng.callback.IPushReceiverMessageCallback;
import com.kad.kumeng.callback.IPushRegisterCallback;
import com.kad.kumeng.callback.KTagCallback;
import com.kad.kumeng.push.KPushManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.PushAgent;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.umeng.commonsdk.UMConfigure.DEVICE_TYPE_PHONE;

/**
 * 友盟统计模块与推送模块工具类
 */
public class KUmengUtils {

    private Context mContext;
    public static  final String UMENG_APP_KEY="5bbec64eb465f553d000014d";
    public static  final String UMENG_CHANNEL="kad";
    public static  final String UMENG_MESSAGE_SECRET="be95300943f56a73181d19b1d1d7211a";

    public static  int deviceType=DEVICE_TYPE_PHONE;

    private   boolean isEnablePush;//是否使用推送模块

    private  boolean isEnableAnalytics;//是否使用分析模块

    private static  KUmengUtils kUmengUtils;

    private  KPushManager kPushManager;

    private KAnalyticsManager kAnalyticsManager;



    private KUmengUtils(Context context){
        this.mContext = context;
        initConfig();

    }

    /**
     * 初始化配置
     */
    private void initConfig() {

        isEnableAnalytics = (Boolean)getAndroidManifestMetaData(mContext,"ENABLE_PUSH");
        isEnablePush = (Boolean)getAndroidManifestMetaData(mContext,"ENABLE_ANALYTICS");

        if(isEnableAnalytics){
            kAnalyticsManager = new KAnalyticsManager(mContext);
        }

        if(isEnablePush){
            kPushManager = new KPushManager(mContext);
        }
    }

    /**
     * 单例模式
     * @param context
     * @return
     */
    public static KUmengUtils with(Context context){

        if(kUmengUtils== null){
            synchronized (KUmengUtils.class){
                if(kUmengUtils == null){
                    kUmengUtils = new KUmengUtils(context);
                }
            }
        }

        return kUmengUtils;
    }

    /**
     * 开启日志
     * @param enable
     */
    public KUmengUtils enabledUmengLog(boolean enable){
        UMConfigure.setLogEnabled(true);
        return  this;
    }

    /**
     * umeng模块初始化
     */
    public  KUmengUtils init() throws Exception {
       String pushSecret =  (String)getAndroidManifestMetaData(mContext,"UMENG_MESSAGE_SECRET");
        if(TextUtils.isEmpty(pushSecret)){
            init(mContext,deviceType,UMENG_MESSAGE_SECRET);
        }else{
            init(mContext,deviceType,pushSecret);
        }

        return this;
    }


    /**
     * umeng初始化
     * 适合在代码里面动态设置appkey,channel
     * @param context
     * @param appkey
     * @param channel
     * @param deviceType
     * @param pushSecret
     */
    public  KUmengUtils init(Context context, String appkey, String channel, int deviceType, String pushSecret) throws Exception {

        UMConfigure.init(context,appkey,channel,deviceType,pushSecret);
        //注册统计
        if(isEnableAnalytics){
            kAnalyticsManager.register();
        }
        //注册推送
        if(isEnablePush){
            kPushManager.register(mContext);
        }

        return this;
    }

    /**
     * umeng功能初始化
     * 适合在AndroidManifest.xml中设置appkey,channel
     * @param context
     * @param deviceType
     * @param pushSecret
     */
    public  KUmengUtils init(Context context, int deviceType, String pushSecret) throws Exception {
        init(context, (String)KUmengUtils.getAndroidManifestMetaData(context,"UMENG_APP_KEY"),KUmengUtils.getChannelName(context),deviceType,pushSecret);
        return this;
    }


    /**
     * 设置push推送模块注册回调
     * @param pushRegisterCallback
     */
    public KUmengUtils setPushRegisterCallback(IPushRegisterCallback pushRegisterCallback) {
        if(kPushManager!=null){
            kPushManager.setPushRegisterCallback(pushRegisterCallback);
        }

        return this;
    }

    /**
     * 设置push推送模块接受消息回调
     * @param pushReceiverMessageCallback
     */
    public KUmengUtils setPushReceiverMessageCallback(IPushReceiverMessageCallback pushReceiverMessageCallback) {
        if(kPushManager!=null){
            kPushManager.setPushReceiverMessageCallback(pushReceiverMessageCallback);
        }

        return this;
    }




    private static String getChannelName(Context context) {
        String channel = "";

        try {
            Class walleChannelReaderClass = Class.forName("com.meituan.android.walle.WalleChannelReader");
            Method getChannelMethod = walleChannelReaderClass.getMethod("getChannel", Context.class);
            channel = (String)getChannelMethod.invoke(walleChannelReaderClass, context);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return !TextUtils.isEmpty(channel) && !channel.equals("null") ? channel : "kad";
    }



    /**
     * 获取AndroidManifest里面的MetaData
     * @param context
     * @param key
     * @return
     */
    public static   Object getAndroidManifestMetaData(Context context,String key){
        ApplicationInfo applicationInfo = null;
        Object value = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value= applicationInfo.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        switch (key.toUpperCase()){
            case "UMENG_APP_KEY":
                 if(TextUtils.isEmpty((String)value)){
                     return UMENG_APP_KEY;
                 }
            case "UMENG_CHANNEL":
                if(TextUtils.isEmpty((String)value)){
                    return UMENG_CHANNEL;
                }
                break;
        }

        return value;
    }


    /**
     * 设置应用处于前台时候是否显示通知
     * @param isShow
     */
    public  void setNotificaitonOnForeground(boolean isShow){
        if(kPushManager!=null){
            kPushManager.setNotificaitonOnForeground(isShow);
        }
    }

    /**
     * 添加推送tag
     * @param kTagCallback
     * @param tags
     */
    public void addPushTags(KTagCallback kTagCallback,String... tags){
        if(kPushManager!=null){
            kPushManager.addTags(kTagCallback,tags);
        }
    }

    /**
     * 删除推送tag
     * @param kTagCallback
     * @param tags
     */
    public void deletePushTags(KTagCallback kTagCallback,String... tags){
        if(kPushManager!=null){
            kPushManager.deleteTags(kTagCallback,tags);
        }
    }


    /**
     * 获取服务器所有的tags
     * @return
     */
    public List<String>  getAllTags(){
        if(kPushManager!=null){
          return   kPushManager.getAllTags();
        }

        return null;
    }

    public PushAgent getPushAgent(){
        if(kPushManager!=null){
            return kPushManager.getPushAgent();
        }
        return null;
    }

    public void onAppStart(){
        if(kPushManager!=null){
            kPushManager.getPushAgent().onAppStart();
        }
    }

    /*===================================================app统计模块====================================================*/

    public KUmengUtils onResume(Context context){
        if(kAnalyticsManager!=null){
            kAnalyticsManager.onResume(context);
        }

        return this;
    }

    public KUmengUtils onPause(Context context){
        if(kAnalyticsManager!=null){
            kAnalyticsManager.onPause(context);
        }

        return this;
    }

    public KUmengUtils onPageStart(String viewName){
        if(kAnalyticsManager!=null){
            kAnalyticsManager.onPageStart(viewName);
        }

        return this;
    }

    public KUmengUtils onPageEnd(String viewName){
        if(kAnalyticsManager!=null){
            kAnalyticsManager.onPageEnd(viewName);
        }

        return this;
    }


    public KUmengUtils onEvent(Context context, String eventID){
        if(kAnalyticsManager!=null){
            kAnalyticsManager.onEvent(context,eventID);
        }
        return this;
    }

    public KUmengUtils onEvent(Context context, String eventID, String label){
        if(kAnalyticsManager!=null){
            kAnalyticsManager.onEvent(context,eventID,label);
        }
        return this;
    }

    public KUmengUtils onEvent(Context context, String eventID, Map<String, String> map){
        if(kAnalyticsManager!=null){
            kAnalyticsManager.onEvent(context,eventID,map);
        }
        return this;
    }

    public KUmengUtils onEventValue(Context context, String eventID, Map<String, String> map, int du){
        if(kAnalyticsManager!=null){
            kAnalyticsManager.onEventValue(context,eventID,map,du);
        }
        return this;
    }
}

package com.kad.kumeng.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.kad.kumeng.analytics.KAnalyticsManager;
import com.kad.kumeng.callback.IPushReceiverMessageCallback;
import com.kad.kumeng.callback.IPushRegisterCallback;
import com.kad.kumeng.callback.KTagCallback;
import com.kad.kumeng.push.KPushManager;
import com.umeng.message.PushAgent;

import java.util.List;

/**
 * 友盟统计模块与推送模块工具类
 */
public class KUmengUtils {

    private Context mContext;
    public static  final String UMENG_APP_KEY="5a2756eab27b0a46ab00012f";
    public static  final String UMENG_CHANNEL="kad";
    public static  final String UMENG_MESSAGE_SECRET="431535565011d368ceac3c1d483dc6ba";

    public static  int deviceType=0;

    private String messageSecret=UMENG_MESSAGE_SECRET;

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

        isEnableAnalytics = Boolean.getBoolean(getAndroidManifestMetaData(mContext,"ENABLE_PUSH"));
        isEnablePush = Boolean.getBoolean(getAndroidManifestMetaData(mContext,"ENABLE_ANALYTICS"));

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
    public static KUmengUtils getInstance(Context context){

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
     * umeng模块初始化
     */
    public  void init() throws Exception {

        if(isEnableAnalytics){
            kAnalyticsManager.init();
        }

        if(isEnablePush){
            messageSecret = getAndroidManifestMetaData(mContext,"UMENG_MESSAGE_SECRET");
            kPushManager.init(mContext,deviceType,messageSecret);
        }
    }


    /**
     * 设置push推送模块注册回调
     * @param pushRegisterCallback
     */
    public void setPushRegisterCallback(IPushRegisterCallback pushRegisterCallback) {
        if(kPushManager!=null){
            kPushManager.setPushRegisterCallback(pushRegisterCallback);
        }
    }

    /**
     * 设置push推送模块接受消息回调
     * @param pushReceiverMessageCallback
     */
    public void setPushReceiverMessageCallback(IPushReceiverMessageCallback pushReceiverMessageCallback) {
        if(kPushManager!=null){
            kPushManager.setPushReceiverMessageCallback(pushReceiverMessageCallback);
        }
    }



    /**
     * 获取AndroidManifest里面的MetaData
     * @param context
     * @param key
     * @return
     */
    public static   String getAndroidManifestMetaData(Context context,String key){
        ApplicationInfo applicationInfo = null;
        String value = "";
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value= applicationInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        switch (key){
            case "UMENG_APP_KEY":
                 if(TextUtils.isEmpty(value)){
                     return UMENG_APP_KEY;
                 }
            case "UMENG_CHANNEL":
                if(TextUtils.isEmpty(value)){
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




}

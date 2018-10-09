package com.kad.kumeng.analytics;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.kad.kumeng.callback.KHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * umeng统计模块
 * @author xww
 * @since 2018-09-28
 */
public class KAnalyticsManager {


    private Context mContext;
    private KHandler kHandler;

    public KAnalyticsManager(Context context) {
        this.mContext = context;
        this.kHandler = new KHandler(context);
    }

    private boolean isCallOnPageEnd;//必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。

    public void register(){
        //设置场景
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //禁止默认的页面统计功能
        MobclickAgent.openActivityDurationTrack(false);
    }

    public void  onResume(Context context){
        if(context !=null){
            MobclickAgent.onResume(context);
        }
    }

    public void  onPause(Context context){
        if(context!=null){
           if(isCallOnPageEnd){
               MobclickAgent.onPause(context);
           }else{
                kHandler.sendEmptyMessage(KHandler.END_TO_PAUSE);
           }
        }
    }

    public void onPageStart(String viewName){
        if(!TextUtils.isEmpty(viewName)){
            MobclickAgent.onPageStart(viewName);
        }
    }


    public void onPageEnd(String viewName){
        if(!TextUtils.isEmpty(viewName)) {
            isCallOnPageEnd = true;
            MobclickAgent.onPageEnd(viewName);
        }
    }


}

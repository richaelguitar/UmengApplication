package com.kad.kumeng.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.kad.kumeng.callback.KHandler;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

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

    /**
     * 开始统计页面时长
     * @param context
     */
    public void  onResume(Context context){
        if(context !=null){
            MobclickAgent.onResume(context);
        }
    }

    /**
     * 结束统计页面时长
     * @param context
     */
    public void  onPause(Context context){
        if(context!=null){
           if(isCallOnPageEnd){
               MobclickAgent.onPause(context);
           }else{
                kHandler.sendEmptyMessage(KHandler.END_TO_PAUSE);
           }
        }
    }

    /**
     * 统计页面开始路径
     * @param viewName
     */
    public void onPageStart(String viewName){
        if(!TextUtils.isEmpty(viewName)){
            MobclickAgent.onPageStart(viewName);
        }
    }


    /**
     * 统计页面结束路径
     * @param viewName
     */
    public void onPageEnd(String viewName){
        if(!TextUtils.isEmpty(viewName)) {
            isCallOnPageEnd = true;
            MobclickAgent.onPageEnd(viewName);
        }
    }

    /**
     *使用计数事件需要在后台添加事件时选择”计数事件”。
     * @param context 当前宿主进程的ApplicationContext上下文
     * @param eventID 为当前统计的事件ID。
     */
    public  void onEvent(Context context, String eventID){
        if(context!=null){
            MobclickAgent.onEvent(context,eventID);
        }
    }

    /**
     *使用计数事件需要在后台添加事件时选择”计数事件”。
     * @param context 当前宿主进程的ApplicationContext上下文
     * @param eventID 为当前统计的事件ID。
     * @param label 事件的标签属性。
     */
    public  void onEvent(Context context, String eventID, String label){
        if(context!=null){
            MobclickAgent.onEvent(context,eventID,label);
        }
    }

    /**
     *统计点击行为各属性被触发的次数
     * @param context
     * @param eventID
     * @param map 为当前事件的属性和取值（Key-Value键值对）。
     * eg:
     * HashMap<String,String> map = new HashMap<String,String>();
     * map.put("type","book");
     * map.put("quantity","3");
     * MobclickAgent.onEvent(mContext, "purchase", map);
     */
    public  void onEvent(Context context, String eventID, Map<String, String> map){
        if(context!=null&&map!=null&&map.size()>0){
            MobclickAgent.onEvent(context,eventID,map);
        }
    }


    /**
     * 统计数值型变量的值的分布
     * 统计一个数值类型的连续变量（该变量必须为整数），用户每次触发的数值的分布情况，如事件持续时间、每次付款金额等
     * @param context
     * @param eventID
     * @param map
     * @param du  当前事件的数值，取值范围是-2,147,483,648 到 +2,147,483,647 之间的
     * 有符号整数，即int 32类型，如果数据超出了该范围，会造成数据丢包，
     * 影响数据统计的准确性。
     */
    public  void onEventValue(Context context, String eventID, Map<String, String> map, int du){
        if(context!=null&&map!=null&&map.size()>0){
            MobclickAgent.onEventValue(context,eventID,map,du);
        }
    }


}

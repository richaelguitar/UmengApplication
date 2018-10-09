package com.kad.umengapplication;

import android.app.Application;
import com.kad.kumeng.utils.KUmengUtils;
import com.kad.umengapplication.push.KPushRegisterCallback;
import com.kad.umengapplication.push.PushReceiverMessageCallbackImp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            KUmengUtils.with(this)
                .enabledUmengLog(BuildConfig.DEBUG)
                .setPushRegisterCallback(new KPushRegisterCallback())
                .setPushReceiverMessageCallback(new PushReceiverMessageCallbackImp())
                .init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

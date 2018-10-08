package com.kad.umengapplication;

import android.app.Application;
import android.content.Context;

import com.kad.kumeng.utils.KUmengUtils;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            KUmengUtils.getInstance(this).init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

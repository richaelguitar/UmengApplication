package com.kad.umengapplication.push;

import android.util.Log;

import com.kad.kumeng.callback.IPushRegisterCallback;

public class KPushRegisterCallback implements IPushRegisterCallback {

    public static final String TAG = KPushRegisterCallback.class.getSimpleName();
    @Override
    public void onSuccess(String deviceToken) {
        Log.d(TAG,"==deviceToken=="+deviceToken);
    }

    @Override
    public void onFailure(String deviceToken, String errorMsg) {
        Log.d(TAG,"==deviceToken=="+deviceToken+"--errorMsg--"+errorMsg);
    }
}

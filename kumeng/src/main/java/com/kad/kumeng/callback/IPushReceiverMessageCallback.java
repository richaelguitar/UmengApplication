package com.kad.kumeng.callback;

import android.content.Context;

import com.kad.kumeng.entity.KMessage;

public interface IPushReceiverMessageCallback {
    void receiverMsg(KMessage kMessage);
    void go(Context context,KMessage kMessage);
}

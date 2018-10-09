package com.kad.umengapplication.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.kad.kumeng.callback.IPushReceiverMessageCallback;
import com.kad.kumeng.entity.KMessage;
import com.kad.umengapplication.MainActivity;
import com.kad.umengapplication.ui.viewmodule.ViewModuleFragment;

public class PushReceiverMessageCallbackImp implements IPushReceiverMessageCallback {
    @Override
    public void receiverMsg(KMessage kMessage) {
        Log.d("kMessage=", String.valueOf(kMessage));
    }

    @Override
    public void go(Context context, KMessage kMessage) {

        String action = kMessage.after_open.toLowerCase();

        switch (action){
            case "go_activity":
                if(!TextUtils.isEmpty(kMessage.activity)){
                    try {
                        Class clazz = Class.forName(kMessage.activity);
                        Intent target = new Intent(context,clazz);
                        target.putExtra(ViewModuleFragment.PARAM_ARG,kMessage.text);
                        target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(target);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "go_url":
                break;
            case "go_app":
                Intent intent = new Intent(context,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case "go_custom":
                break;
        }
    }
}

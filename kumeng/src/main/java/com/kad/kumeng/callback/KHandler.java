package com.kad.kumeng.callback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class KHandler extends Handler {


    public static final int  END_TO_PAUSE = 100;

    private Context mContext;

    public KHandler(Context context) {
        super(context.getMainLooper());
        this.mContext = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case END_TO_PAUSE:
                Toast.makeText(mContext,"在调用onPause时候，必须先调用onPageEnd方法",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

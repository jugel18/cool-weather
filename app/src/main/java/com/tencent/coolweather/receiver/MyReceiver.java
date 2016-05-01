package com.tencent.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.coolweather.service.AutoUpdateService;

/**
 * Created by lenovo on 2016/5/1.
 */
public class MyReceiver extends BroadcastReceiver {
    public void onReceive(Context context,Intent intent) {
        Intent intent1 = new Intent(context, AutoUpdateService.class);
        context.startService(intent);
        Log.e("Receiver","received");
    }
}

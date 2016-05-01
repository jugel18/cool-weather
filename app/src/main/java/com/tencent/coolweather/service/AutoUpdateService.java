package com.tencent.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.coolweather.activity.WeatherActivity;
import com.tencent.coolweather.receiver.MyReceiver;
import com.tencent.coolweather.util.HttpURLConnectionListener;
import com.tencent.coolweather.util.HttpUtil;
import com.tencent.coolweather.util.SolveJSON;

/**
 * Created by lenovo on 2016/5/1.
 */
public class AutoUpdateService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }
    public int onStartCommand(final Intent intent, int flag, int startId) {
        new Thread(new Runnable() {
          @Override
             public void run() {
              Log.e("service", "give me a kiss");
              updateWeather(intent);




          }
          }).start();
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + 60 * 60 * 1000;
        Intent intent1 = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent1,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
       Log.e("service", "service works");
        return super.onStartCommand(intent,flag,startId);

    }
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateWeather(Intent intent) {

        String weatherCode = intent.getStringExtra("weatherCode");
        final String countyCode = intent.getStringExtra("countyCode");
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        HttpUtil.sendHttpRequest(address, new HttpURLConnectionListener() {
            public void onFinish(String response) {
                if(!TextUtils.isEmpty(response)) {
                    SolveJSON.HandleWeatherInfo(AutoUpdateService.this,response,countyCode);
                }
            }
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

}

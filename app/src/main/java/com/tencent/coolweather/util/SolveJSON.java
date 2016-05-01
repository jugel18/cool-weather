package com.tencent.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by lenovo on 2016/4/30.
 */
public class SolveJSON {
    public static void HandleWeatherInfo(Context context, String response,String countyCode) {
        try {
            Log.e("WeatherActivity", "开始执行solveJson");
            JSONObject jsonObject = new JSONObject(response);
            Log.e("wc", "before got jsonobject");
            JSONObject weatherinfo = jsonObject.getJSONObject("weatherinfo");
            Log.e("wc", "得到jsonobject");
            String name = weatherinfo.getString("city");
            Log.e("wc", name);
            String cityid = weatherinfo.getString("cityid");
            String temp1 = weatherinfo.getString("temp1");
            String temp2 = weatherinfo.getString("temp2");
            String weather = weatherinfo.getString("weather");
            String ptime = weatherinfo.getString("ptime");
            Log.e("WeatherActivity","错误发生");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString("name", name);
            editor.putBoolean("county_selected",true);
            editor.putString("temp1",  temp1);
            editor.putString("temp2", temp2);
            editor.putString("weather", weather);
            editor.putString("ptime", ptime);
            editor.putString("countyCode", countyCode);
            editor.commit();
            Log.e("wc","提交操作完成");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
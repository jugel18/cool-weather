package com.tencent.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tencent.coolweather.R;
import com.tencent.coolweather.service.AutoUpdateService;
import com.tencent.coolweather.util.HttpURLConnectionListener;
import com.tencent.coolweather.util.HttpUtil;
import com.tencent.coolweather.util.SolveJSON;


/**
 * Created by lenovo on 2016/4/30.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
    private String weatherCode;
    private TextView countyTextView;
    private TextView ptimeTextView;
    private TextView tem1TextView;
    private TextView tem2TextView;
    private TextView weatherTextView;
    private Button update;
    private Button chooseCity;
    private String countyCode;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        Intent intent = getIntent();
        countyTextView = (TextView) findViewById(R.id.county);
        ptimeTextView = (TextView) findViewById(R.id.ptime);
        tem1TextView = (TextView) findViewById(R.id.tem1);
        tem2TextView = (TextView) findViewById(R.id.tem2);
        weatherTextView = (TextView) findViewById(R.id.weather);
        update = (Button) findViewById(R.id.update);
        chooseCity = (Button) findViewById(R.id.choose_city);
        update.setOnClickListener(this);
        chooseCity.setOnClickListener(this);
        countyCode = intent.getStringExtra("countyCode");

        if (!TextUtils.isEmpty(countyCode)) {
            queryWeatherCode(countyCode);


        } else {
            showWeather();
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_city:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putBoolean("county_selected", false);
                editor.commit();

                Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                intent.putExtra("fromWeatherActivity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.update:

                queryWeather(weatherCode, countyCode);
        }
    }

    public void queryWeatherCode(final String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        HttpUtil.sendHttpRequest(address, new HttpURLConnectionListener() {
            public void onFinish(String response) {
                if (!TextUtils.isEmpty(response)) {
                    String[] a = response.split("\\|");
                    if (a != null && a.length == 2) {
                        weatherCode = a[1];
                        Log.e("WeatherActivity", weatherCode);
                        queryWeather(weatherCode,countyCode);
                        createService(weatherCode, countyCode);


                    }
                }

            }


            public void onError(Exception e) {
                Toast.makeText(WeatherActivity.this, "failed", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void queryWeather(String weatherCode, final String countyCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        Log.e("wc", weatherCode + 2);
        HttpUtil.sendHttpRequest(address, new HttpURLConnectionListener() {
            @Override
            public void onFinish(String response) {
                if (!TextUtils.isEmpty(response)) {
                    Log.e("WeatherActivity", response);
                    SolveJSON.HandleWeatherInfo(WeatherActivity.this, response, countyCode);
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                    Log.e("WeatherActivity", sp.getString("tem1", "just see see"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                    Log.e("WeatherActivity", "queryWeather succeed");
                }
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        Log.e("WeatherActivity", "onError");
                    }
                });

            }
        });
    }

    public void showWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("wc", sp.getString("name", "有没有"));

        countyTextView.setText(sp.getString("name", "没有信息"));
        ptimeTextView.setText(sp.getString("ptime", "请连接网络"));
        tem1TextView.setText(sp.getString("temp1", "没有数据"));
        tem2TextView.setText(sp.getString("temp2", "no data"));
        weatherTextView.setText(sp.getString("weather", "try again"));
        Log.e("WeatherActivity", "到这儿没");
    }

    public void onBackPressed() {
        Log.e("onBackPressed", "onBackPressed");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("county_selected", false);
        editor.commit();
        Log.e("wc", String.valueOf(sp.getBoolean("county_selected", false)));
        Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void createService(String weatherCode, String countyCode) {
        Intent intent = new Intent(this, AutoUpdateService.class);
        intent.putExtra("weatherCode", weatherCode);
        intent.putExtra("countyCode", weatherCode);
        startService(intent);
    }


}
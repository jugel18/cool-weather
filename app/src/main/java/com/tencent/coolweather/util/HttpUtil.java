package com.tencent.coolweather.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 2016/4/29.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address, final HttpURLConnectionListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                     connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null)
                        response.append(line);
                    if(listener != null){
                        listener.onFinish(response.toString());
                        Log.e("HttpUtil", response.toString());

                    }

                }catch (Exception e) {
                    if(listener != null)
                        listener.onError(e);
                }
                finally {
                    if(connection != null)
                        connection.disconnect();
                }

            }
        }).start();
    }
}

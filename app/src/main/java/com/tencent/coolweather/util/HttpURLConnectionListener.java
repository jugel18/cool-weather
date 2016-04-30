package com.tencent.coolweather.util;

/**
 * Created by lenovo on 2016/4/29.
 */
public interface HttpURLConnectionListener {
    void onFinish(String response);
    void onError(Exception e);
}

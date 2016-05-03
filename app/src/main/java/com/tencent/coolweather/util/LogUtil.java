package com.tencent.coolweather.util;

import android.util.Log;

/**
 * Created by lenovo on 2016/5/1.
 */
public class LogUtil {
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private  static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;

    private static final int LEVEL = 6;

    public static void v(String tag, String cont) {
        if(LEVEL <= VERBOSE)
            Log.v(tag, cont);
    }

    public static void d(String tag, String cont) {
        if(LEVEL <= DEBUG)
            Log.v(tag, cont);
    }
    public static void e(String tag, String cont) {
        if(LEVEL <= ERROR)
            Log.e(tag, cont);
    }

}

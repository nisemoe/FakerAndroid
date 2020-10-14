package com.faker.android;

import android.util.Log;

public class Logger {
    private static final boolean DEBUG = true;
    private static final String TAG = "faker";
    public static void log(String log){
        if(DEBUG){
            Log.e(TAG,log);
        }
    }
}

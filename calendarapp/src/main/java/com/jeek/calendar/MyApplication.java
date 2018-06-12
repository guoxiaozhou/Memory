package com.jeek.calendar;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by Administrator on 2018/4/7.
 */

/**
 * Created by Administrator on 2017/8/23.
 */

public class MyApplication extends Application {


    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
        Log.i("Application","MyApplication");

    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }
}

package com.example.openaiclient;

import android.app.Application;

import com.example.openaiclient.util.Util;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Util.getInstance().init(this.getApplicationContext());
    }
}

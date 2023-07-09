package com.example.openaiclient.util;

import android.content.Context;

public class Util {
    private Context context;
    private Util() {
    }

    public static Util getInstance() {
        return Util.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final Util sInstance = new Util();
    }
    public void init(Context context){
        this.context = context;
    }
    public Context getContext(){
        return context;
    }
}

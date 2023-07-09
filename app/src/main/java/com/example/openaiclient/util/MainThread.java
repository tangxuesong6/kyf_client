package com.example.openaiclient.util;

import android.os.Handler;
import android.os.Looper;

public class MainThread {
    private MainThread() {
    }

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void run(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }

    private static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void postDelay(Runnable runnable, long delay) {
        HANDLER.postDelayed(runnable, delay);
    }
}

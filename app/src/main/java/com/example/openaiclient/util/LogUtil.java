package com.example.openaiclient.util;

import android.util.Log;


public class LogUtil {
    public static void d(String tag, String message) {
        Log.d("OAI_" + tag, message);

    }

    public static void d(String message) {
        String info = getLogInfo(message);
        Log.d("OAI", info);
    }

    public static void e(String message) {
        String info = getLogInfo(message);
        Log.e("OAI", info);
    }


    private static String getLogInfo(String message) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StackTraceElement element;

        int callerIndex;
        boolean markedIndex = false;

        for (callerIndex = 0; callerIndex < stack.length; callerIndex++) {
            element = stack[callerIndex];
            if (element.getClassName().equals(LogUtil.class.getName())) {
                markedIndex = true;
            }
            if (!element.getClassName().equals(LogUtil.class.getName()) && markedIndex) {
                break;
            }
        }

        element = null;

        if (callerIndex < stack.length) {
            element = stack[callerIndex];
        }

        if (element != null) {
            String className = "UnknownClass";
            String methodName = "unknownMethod";
            int lineNumber = -1;

            className = element.getClassName();
            methodName = element.getMethodName();
            lineNumber = element.getLineNumber();

            if (message != null && !message.isEmpty()) message = " :: " + message;
            if (message == null) message = "";

            String lineNumberPart = " (line:" + lineNumber + ")";

            String info = className + "." + methodName + "()" + lineNumberPart + message;
            return info;
        }
        return "LOG";
    }

}

package com.example.openaiclient.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.openaiclient.ConfigParams;

public class ConfigSharedPreferences {

    private ConfigSharedPreferences() {
    }

    public static ConfigSharedPreferences getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final ConfigSharedPreferences sInstance = new ConfigSharedPreferences();
    }

    public void saveServerUrl(Context applicationContext, String url) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConfigParams.SP_SERVER_URL, url);
        editor.apply();
    }

    public boolean hasServerUrl(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(ConfigParams.SP_SERVER_URL);
    }

    public String getServerUrl(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConfigParams.SP_SERVER_URL, "");
    }

    public void saveServerApiKey(Context applicationContext, String key) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConfigParams.SP_SERVER_API_KEY, key);
        editor.apply();
    }

    public boolean hasServerApiKey(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(ConfigParams.SP_SERVER_API_KEY);
    }

    public String getServerApiKey(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConfigParams.SP_SERVER_API_KEY, "");
    }
    public void saveLocalApiKey(Context applicationContext, String key) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConfigParams.SP_LOCAL_API_KEY, key);
        editor.apply();
    }

    public boolean hasLocalApiKey(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(ConfigParams.SP_LOCAL_API_KEY);
    }

    public String getLocalApiKey(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConfigParams.SP_LOCAL_API_KEY, "");
    }
    public void saveMaxTokens(Context applicationContext, int maxTokens) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ConfigParams.SP_MAX_TOKENS, maxTokens);
        editor.apply();
    }

    public boolean hasMaxTokens(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(ConfigParams.SP_MAX_TOKENS);
    }

    public int getMaxTokens(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(ConfigParams.SP_MAX_TOKENS, 4096);
    }
    public void saveContentLength(Context applicationContext, int contentLength){
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ConfigParams.SP_CONTENT_LENGTH, contentLength);
        editor.apply();
    }

    public boolean hasContentLength(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(ConfigParams.SP_CONTENT_LENGTH);
    }

    public int getContentLength(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(ConfigParams.SP_CONTENT_LENGTH, 6);
    }

    public void saveCurrentMode(Context applicationContext, int mode){
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ConfigParams.SP_CURRENT_MODE, mode);
        editor.apply();
    }


    public int getCurrentMode(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(ConfigParams.SP_CURRENT_MODE, ConfigParams.SERVER_MODE);
    }
    public void savePrompts(Context applicationContext, String prompts) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConfigParams.SP_PROMPTS, prompts);
        editor.apply();
    }
    public boolean hasPrompts(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(ConfigParams.SP_PROMPTS);
    }


    public String getPrompts(Context applicationContext) {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(ConfigParams.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConfigParams.SP_PROMPTS, "");
    }


}

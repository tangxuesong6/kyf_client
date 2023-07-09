package com.example.openaiclient;

import android.text.TextUtils;

import com.example.openaiclient.bean.RequestBean;
import com.example.openaiclient.bean.RespBean;
import com.example.openaiclient.listener.OpenAiListener;
import com.example.openaiclient.listener.ResponseCallback;
import com.example.openaiclient.util.ConfigSharedPreferences;
import com.example.openaiclient.util.LogUtil;
import com.example.openaiclient.util.MainThread;
import com.example.openaiclient.util.NetUtil;
import com.example.openaiclient.util.Util;
import com.google.gson.Gson;

public class ServerModeOpenAi implements OpenAiImpl {
    private String API_KEY;

    @Override
    public void init(String apiKey) {
        API_KEY = apiKey;
    }

    @Override
    public void request(RequestBean requestBean, OpenAiListener callback) {
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(API_KEY)) {
            requestBean.apiKey = API_KEY;
        }

        NetUtil.getInstance().postJson(ConfigSharedPreferences.getInstance().getServerUrl(Util.getInstance().getContext())+"/chat", gson.toJson(requestBean), new ResponseCallback<byte[]>() {
            @Override
            public void success(byte[] resp) {
                String str = new String(resp);
                LogUtil.d(str);
                RespBean respBean1 = gson.fromJson(str, RespBean.class);
                if (respBean1.code == 200) {
                    callback.onSuccess(respBean1);
                } else {
                    callback.onFail(respBean1.message);
                }

            }

            @Override
            public void fail(String error) {
                MainThread.run(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(error);
                    }
                });
                LogUtil.d(error);
            }
        });
    }
}

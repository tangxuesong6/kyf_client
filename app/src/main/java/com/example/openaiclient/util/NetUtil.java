package com.example.openaiclient.util;

import com.example.openaiclient.listener.ResponseCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetUtil {
    private OkHttpClient okHttpClient;
    private boolean debug = true;
    public static final MediaType JSON = MediaType.get("application/json");

    private NetUtil() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (debug) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        okHttpClient = new OkHttpClient.Builder()
//                .hostnameVerifier(new HostnameVerifier() {//证书信任
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

    }

    public static NetUtil getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final NetUtil sInstance = new NetUtil();
    }

    public void postJson(String url, String json, final ResponseCallback<byte[]> callback) {
        RequestBody requestBody = RequestBody.create(json, JSON);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        enqueue(call, callback);

    }
    public void get(String url, final ResponseCallback<byte[]> callback) {
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        enqueue(call, callback);

    }
    private void enqueue(Call call, final ResponseCallback<byte[]> callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.fail(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] result = response.body().bytes();
                try {

                    if (callback != null) {
                        callback.success(result);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.fail(e.toString());
                    }
                }

            }
        });
    }
}

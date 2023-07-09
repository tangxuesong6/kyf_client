package com.example.openaiclient;

import com.example.openaiclient.bean.RequestBean;
import com.example.openaiclient.listener.OpenAiListener;

public interface OpenAiImpl {
    void init(String apiKey);
    void request(RequestBean requestBean, OpenAiListener callback);
}

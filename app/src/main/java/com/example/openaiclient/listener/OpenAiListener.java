package com.example.openaiclient.listener;

import com.example.openaiclient.bean.RespBean;

public interface OpenAiListener {
    void onSuccess(RespBean respBean);

    void onFail(String msg);
}

package com.example.openaiclient.listener;

import com.example.openaiclient.sql.MsgDatabase;

public interface DbInitListener {
    void onSuccess(MsgDatabase msgDatabase);
}

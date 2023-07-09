package com.example.openaiclient.listener;

import com.example.openaiclient.sql.MsgDb;

import java.util.List;

public interface DbMsgListListener {
    void onGetMsgList(List<MsgDb> msgDbList);
}

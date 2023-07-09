package com.example.openaiclient.listener;

import com.example.openaiclient.sql.MsgDb;

public interface DbMsgListener {

    void  onGedMsg(MsgDb msgDb);
}

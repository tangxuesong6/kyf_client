package com.example.openaiclient.sql;

import androidx.room.Room;

import com.example.openaiclient.listener.DbDeleteListener;
import com.example.openaiclient.listener.DbInitListener;
import com.example.openaiclient.listener.DbInsertListener;
import com.example.openaiclient.listener.DbMsgListListener;
import com.example.openaiclient.listener.DbMsgListener;
import com.example.openaiclient.util.MainThread;
import com.example.openaiclient.util.ThreadPoolManager;
import com.example.openaiclient.util.Util;

import java.util.List;

public class DbManager {
    private MsgDatabase msgDatabase;

    private DbManager() {

    }

    public static DbManager getInstance() {
        return DbManager.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final DbManager sInstance = new DbManager();
    }

    public void init(DbInitListener dbInitListener) {
        if (msgDatabase != null) {
            dbInitListener.onSuccess(msgDatabase);
        }else {
            ThreadPoolManager.executeTask(new Runnable() {
                @Override
                public void run() {
                    msgDatabase = Room.databaseBuilder(Util.getInstance().getContext(), MsgDatabase.class, "msg_database").build();
                    MainThread.run(new Runnable() {
                        @Override
                        public void run() {
                            dbInitListener.onSuccess(msgDatabase);
                        }
                    });
                }
            });
        }
    }

    public void getLastMsg(DbMsgListener dbMsgListener) {
        ThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                MsgDb lastMsg = msgDatabase.getMsgDao().getLastMsg();
                MainThread.run(new Runnable() {
                    @Override
                    public void run() {
                        dbMsgListener.onGedMsg(lastMsg);
                    }
                });
            }
        });
    }

    public void getLimitMsg(int lastId, int limit, DbMsgListListener dbMsgListener) {
        ThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                List<MsgDb> msgDbs = msgDatabase.getMsgDao().getLimitMsg(lastId, limit);
                MainThread.run(new Runnable() {
                    @Override
                    public void run() {
                        dbMsgListener.onGetMsgList(msgDbs);
                    }
                });
            }
        });
    }

    public void insert(DbInsertListener dbInsertListener, MsgDb... msgDb) {
        ThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                msgDatabase.getMsgDao().insert(msgDb);
                MainThread.run(new Runnable() {
                    @Override
                    public void run() {
                        dbInsertListener.onSuccess();

                    }
                });
            }
        });
    }

    public void deleteAll(DbDeleteListener dbDeleteListener) {
        ThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                msgDatabase.getMsgDao().deleteAll();
                MainThread.run(new Runnable() {
                    @Override
                    public void run() {
                        dbDeleteListener.onSuccess();
                    }
                });
            }
        });
    }
}

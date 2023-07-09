package com.example.openaiclient.sql;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MsgDb.class}, version = 1)
public abstract class  MsgDatabase extends RoomDatabase {
    public abstract MsgDao getMsgDao();
}

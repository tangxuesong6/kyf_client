package com.example.openaiclient.sql;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "msg")
public class MsgDb {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "type")
    private int type;

    public MsgDb(String content,int type){
        this.content = content;
        this.type = type;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

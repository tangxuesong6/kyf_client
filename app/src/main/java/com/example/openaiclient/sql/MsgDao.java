package com.example.openaiclient.sql;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MsgDao {
    @Insert
    void insert(MsgDb... msgDb);
    @Update
    int update(MsgDb... msgDb);
    @Delete
    void delete(MsgDb... msgDb);

    @Query("SELECT * FROM msg ORDER BY id ASC")
    List<MsgDb> getAll();

    @Query("SELECT * FROM msg WHERE id <= :id ORDER BY id ASC LIMIT :limit")
    List<MsgDb> getLimitMsg(int id,int limit);

    @Query("SELECT * FROM msg ORDER BY id DESC LIMIT 1")
    MsgDb getLastMsg();

    @Query("DELETE FROM msg")
    void deleteAll();
}

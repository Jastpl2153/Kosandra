package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kosandra.entity.Record;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface RecordsDAO {
    @Insert
    void insert(Record record);

    @Delete
    void delete(Record record);

    @Update
    void update(Record record);

    @Query("select * from records")
    LiveData<List<Record>> getAllRecord();

    @Query("select * from records where visitDate = :date")
    LiveData<List<Record>> getDateRecord(LocalDate date);

    @Query("select * from records")
    List<Record> getAllRecordList();
}

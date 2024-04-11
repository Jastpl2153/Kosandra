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

/**
 * This interface defines the Data Access Object (DAO) for handling Record entities in the database.
 */
@Dao
public interface RecordsDAO {
    /**
     * Inserts a new Record entity into the database.
     *
     * @param record The Record object to be inserted.
     */
    @Insert
    void insert(Record record);

    /**
     * Deletes a Record entity from the database.
     *
     * @param record The Record object to be deleted.
     */
    @Delete
    void delete(Record record);

    /**
     * Updates an existing Record entity in the database.
     *
     * @param record The Record object to be updated.
     */
    @Update
    void update(Record record);

    /**
     * Retrieves all records present in the database as LiveData.
     *
     * @return A LiveData object containing a list of all records in the database.
     */
    @Query("select * from records")
    LiveData<List<Record>> getAllRecord();

    /**
     * Retrieves records from the database based on a specific visit date as LiveData.
     *
     * @param date The LocalDate object representing the visit date to filter records by.
     * @return A LiveData object containing a list of records matching the provided visit date.
     */
    @Query("select * from records where visitDate = :date")
    LiveData<List<Record>> getDateRecord(LocalDate date);

    /**
     * Retrieves all records present in the database as a List.
     *
     * @return A List of all records in the database.
     */
    @Query("select * from records")
    List<Record> getAllRecordList();
}

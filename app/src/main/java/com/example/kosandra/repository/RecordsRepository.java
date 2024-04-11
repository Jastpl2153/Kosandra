package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.daos.RecordsDAO;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.Record;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The RecordsRepository class handles the interaction between the database and the ViewModel.
 * <p>
 * It provides methods to insert, update, delete records, and retrieve records based on different criteria.
 * <p>
 * The class uses LiveData to observe changes in the database and ensure data consistency.
 */
public class RecordsRepository {
    private final RecordsDAO recordsDAO;
    private LiveData<List<Record>> recordsLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructs a new RecordsRepository object, initializes the RecordsDAO and LiveData objects.
     *
     * @param application The application context for initializing the database.
     */
    public RecordsRepository(Application application) {
        KosandraDataBase dataBase = KosandraDataBase.getInstance(application);
        recordsDAO = dataBase.recordsDAO();
        this.recordsLiveData = recordsDAO.getAllRecord();
    }

    /**
     * Inserts a new record into the database in a background thread.
     *
     * @param record The record to be inserted.
     */
    public void insert(Record record) {
        executor.execute(() -> recordsDAO.insert(record));
    }

    /**
     * Updates an existing record in the database in a background thread.
     *
     * @param record The record to be updated.
     */
    public void update(Record record) {
        executor.execute(() -> recordsDAO.update(record));
    }

    /**
     * Deletes a record from the database in a background thread.
     *
     * @param record The record to be deleted.
     */
    public void delete(Record record) {
        executor.execute(() -> recordsDAO.delete(record));
    }

    /**
     * Retrieves a LiveData object containing a list of all records from the database.
     *
     * @return LiveData<List < Record>> The LiveData object containing the list of all records.
     */
    public LiveData<List<Record>> getAllRecords() {
        return recordsLiveData;
    }

    /**
     * Retrieves a LiveData object containing a list of records filtered by a specific date.
     *
     * @param date The date to filter the records by.
     * @return LiveData<List < Record>> The LiveData object containing the list of filtered records.
     */
    public LiveData<List<Record>> getDateRecord(LocalDate date) {
        return recordsDAO.getDateRecord(date);
    }
}

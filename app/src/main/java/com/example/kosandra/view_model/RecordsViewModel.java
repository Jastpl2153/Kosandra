package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.entity.Record;
import com.example.kosandra.repository.RecordsRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * The RecordsViewModel class extends AndroidViewModel and acts as a communication center between the UI controller and the data repository. It provides access to the data operations related to the records.
 */
public class RecordsViewModel extends AndroidViewModel {
    private final RecordsRepository repository;
    private LiveData<List<Record>> allRecords;

    /**
     * Constructor for the RecordsViewModel class.
     *
     * @param application The application context to initialize the RecordsRepository.
     */
    public RecordsViewModel(@NonNull Application application) {
        super(application);
        repository = new RecordsRepository(application);
        allRecords = repository.getAllRecords();
    }

    /**
     * Inserts a new record into the database through the repository.
     *
     * @param record The Record object to be inserted.
     */
    public void insert(Record record) {
        repository.insert(record);
    }

    /**
     * Updates an existing record in the database through the repository.
     *
     * @param record The Record object to be updated.
     */
    public void update(Record record) {
        repository.update(record);
    }

    /**
     * Deletes a record from the database through the repository.
     *
     * @param record The Record object to be deleted.
     */
    public void delete(Record record) {
        repository.delete(record);
    }

    /**
     * Retrieves all records from the database.
     *
     * @return LiveData object containing a list of all records.
     */
    public LiveData<List<Record>> getAllRecords() {
        return allRecords;
    }

    /**
     * Retrieves records based on the given date.
     *
     * @param date The LocalDate object representing the date to filter records.
     * @return LiveData object containing a list of records filtered by the given date.
     */
    public LiveData<List<Record>> getDateRecord(LocalDate date) {
        return repository.getDateRecord(date);
    }
}

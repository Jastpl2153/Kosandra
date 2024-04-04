package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.entity.Record;
import com.example.kosandra.repository.RecordsRepository;

import java.time.LocalDate;
import java.util.List;

public class RecordsViewModel extends AndroidViewModel {
    private final RecordsRepository repository;
    private LiveData<List<Record>> allRecords;

    public RecordsViewModel(@NonNull Application application) {
        super(application);
        repository = new RecordsRepository(application);
        allRecords = repository.getAllRecords();
    }

    public void insert(Record record) {
        repository.insert(record);
    }

    public void update(Record record) {
        repository.update(record);
    }

    public void delete(Record record) {
        repository.delete(record);
    }

    public LiveData<List<Record>> getAllRecords() {
        return allRecords;
    }

    public LiveData<List<Record>> getDateRecord(LocalDate date) {
        return repository.getDateRecord(date);
    }
}

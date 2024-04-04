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

public class RecordsRepository {
    private final RecordsDAO recordsDAO;
    private LiveData<List<Record>> recordsLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public RecordsRepository(Application application) {
        KosandraDataBase dataBase = KosandraDataBase.getInstance(application);
        recordsDAO = dataBase.recordsDAO();
        this.recordsLiveData = recordsDAO.getAllRecord();
    }

    public void insert(Record record) {
        executor.execute(() -> recordsDAO.insert(record));
    }

    public void update(Record record) {
        executor.execute(() -> recordsDAO.update(record));
    }

    public void delete(Record record) {
        executor.execute(() -> recordsDAO.delete(record));
    }

    public LiveData<List<Record>> getAllRecords() {
        return recordsLiveData;
    }

    public LiveData<List<Record>> getDateRecord(LocalDate date) {
        return recordsDAO.getDateRecord(date);
    }
}

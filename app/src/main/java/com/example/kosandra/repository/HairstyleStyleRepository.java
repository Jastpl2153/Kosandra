package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.daos.HairstyleVisitDAO;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.entity.HairstyleVisit;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HairstyleStyleRepository {
    private final HairstyleVisitDAO hairstyleVisitDAO;
    private LiveData<List<HairstyleVisit>> hairstyleLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public HairstyleStyleRepository(Application application) {
        KosandraDataBase dataBase = KosandraDataBase.getInstance(application);
        hairstyleVisitDAO = dataBase.haircutDAO();

    }

    public void insertHairstyle(HairstyleVisit hairstyleVisit) {
        // Вызов метода вставки клиента в базу данных через DAO
        executor.execute(() -> hairstyleVisitDAO.insert(hairstyleVisit));
    }

    public void updateHairstyle(HairstyleVisit hairstyleVisit) {
        // Вызов метода обновления клиента в базе данных через DAO
        executor.execute(() -> hairstyleVisitDAO.update(hairstyleVisit));
    }

    public void deleteHairstyle(HairstyleVisit hairstyleVisit) {
        // Вызов метода удаления клиента из базы данных через DAO
        executor.execute(() -> hairstyleVisitDAO.delete(hairstyleVisit));
    }

    public LiveData<List<HairstyleVisit>> getAllHairstyles(int clientId) {
        // Получение списка клиентов из базы данных через DAO с использованием LiveData
        return hairstyleLiveData = hairstyleVisitDAO.getAllHairstyles(clientId);
    }
}

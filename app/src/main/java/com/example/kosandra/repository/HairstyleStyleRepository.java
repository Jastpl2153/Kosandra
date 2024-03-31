package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.daos.HairstyleVisitDAO;
import com.example.kosandra.daos.MaterialsDAO;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.db.returnSql.SqlIncomeHairstyle;
import com.example.kosandra.db.returnSql.SqlIncomeHairstyleRangeDate;
import com.example.kosandra.entity.HairstyleVisit;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HairstyleStyleRepository {
    private final HairstyleVisitDAO hairstyleVisitDAO;
    private LiveData<List<HairstyleVisit>> hairstyleLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    private final MaterialsDAO materialsDAO;

    public HairstyleStyleRepository(Application application) {
        KosandraDataBase dataBase = KosandraDataBase.getInstance(application);
        hairstyleVisitDAO = dataBase.haircutDAO();
        materialsDAO = dataBase.materialsDAO();
    }

    public void insert(HairstyleVisit hairstyleVisit) {
        executor.execute(() -> hairstyleVisitDAO.insert(hairstyleVisit));
    }

    public void update(HairstyleVisit hairstyleVisit) {
        executor.execute(() -> hairstyleVisitDAO.update(hairstyleVisit));
    }

    public void delete(HairstyleVisit hairstyleVisit) {
        executor.execute(() -> hairstyleVisitDAO.delete(hairstyleVisit));
    }

    public LiveData<List<HairstyleVisit>> getAllClientHairstyles(int clientId) {
        return hairstyleLiveData = hairstyleVisitDAO.getAllClientHairstyles(clientId);
    }

    public LiveData<HairstyleVisit> getHairstyleVisit(int id){
        return hairstyleVisitDAO.getHairstyleVisit(id);
    }

    public LiveData<SqlIncomeHairstyle> getMostPopularHairstyle() {
        return hairstyleVisitDAO.getMostPopularHairstyle();
    }

    public LiveData<SqlIncomeHairstyle> getMostProfitableHairstyle() {
        return hairstyleVisitDAO.getMostProfitableHairstyle();
    }

    public  LiveData<List<SqlIncomeHairstyle>> getHairstyleCostsByDateRangeBarCharts(LocalDate startDate, LocalDate endDate){
        return hairstyleVisitDAO.getHairstyleCostsByDateRangeBarCharts(startDate, endDate);
    }

    public LiveData<List<SqlIncomeHairstyleRangeDate>> getHairstyleIncome(LocalDate startDate, LocalDate endDate) {
        return hairstyleVisitDAO.getHairstyleIncome(startDate, endDate);
    }

    public LiveData<List<HairstyleVisit>> getAllDateMaterials(LocalDate startDate, LocalDate endDate) {
        return hairstyleVisitDAO.getAllDateMaterials(startDate, endDate);
    }
}

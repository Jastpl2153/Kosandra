package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.db.returnSql.SqlIncomeHairstyle;
import com.example.kosandra.db.returnSql.SqlIncomeHairstyleRangeDate;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.repository.HairstyleStyleRepository;

import java.time.LocalDate;
import java.util.List;

public class HairstyleVisitViewModel extends AndroidViewModel {
    private HairstyleStyleRepository repository;

    public HairstyleVisitViewModel(@NonNull Application application) {
        super(application);
        repository = new HairstyleStyleRepository(application);
    }

    public void insert(HairstyleVisit hairstyleVisit) {
        repository.insert(hairstyleVisit);
    }

    public void update(HairstyleVisit hairstyleVisit) {
        repository.update(hairstyleVisit);
    }

    public void delete(HairstyleVisit hairstyleVisit) {
        repository.delete(hairstyleVisit);
    }

    public LiveData<List<HairstyleVisit>> getAllClientHairstyles(int clientId) {
        return repository.getAllClientHairstyles(clientId);
    }

    public LiveData<HairstyleVisit> getHairstyleVisit(int id) {
        return repository.getHairstyleVisit(id);
    }

    public LiveData<SqlIncomeHairstyle> getMostPopularHairstyle() {
        return repository.getMostPopularHairstyle();
    }

    public LiveData<SqlIncomeHairstyle> getMostProfitableHairstyle() {
        return repository.getMostProfitableHairstyle();
    }

    public LiveData<List<SqlIncomeHairstyleRangeDate>> getHairstyleIncome(LocalDate startDate, LocalDate endDate) {
        return repository.getHairstyleIncome(startDate, endDate);
    }
}

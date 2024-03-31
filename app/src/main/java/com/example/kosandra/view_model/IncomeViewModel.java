package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.db.returnSql.SqlBarCharts;
import com.example.kosandra.entity.Income;
import com.example.kosandra.repository.IncomeRepository;

import java.time.LocalDate;
import java.util.List;

public class IncomeViewModel extends AndroidViewModel {
    private IncomeRepository repository;

    public IncomeViewModel(@NonNull Application application) {
        super(application);
        repository = new IncomeRepository(application);
    }

    public void insert(Income income) {
        repository.insert(income);
    }

    public void update(Income income) {
        repository.update(income);
    }

    public void delete(Income income) {
        repository.delete(income);
    }

    public LiveData<List<Income>> getAllIncomeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return repository.getAllIncomeByRangeDate(startDate, endDate);
    }

    public LiveData<List<SqlBarCharts>> getGroupTypeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return repository.getGroupTypeByRangeDate(startDate, endDate);
    }

    public LiveData<List<SqlBarCharts>> getCombinedResults(LocalDate startDate, LocalDate endDate) {
        return repository.getCombinedResults(startDate, endDate);
    }
}

package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.daos.IncomeDAO;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.db.returnSql.SqlBarCharts;
import com.example.kosandra.entity.Income;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IncomeRepository {
    private final IncomeDAO incomeDAO;

    private Executor executor = Executors.newSingleThreadExecutor();

    public IncomeRepository(Application application) {
        KosandraDataBase database = KosandraDataBase.getInstance(application);
        incomeDAO = database.incomeDAO();
    }

    public void insert(Income income) {
        executor.execute(() -> incomeDAO.insert(income));
    }

    public void update(Income income) {
        executor.execute(() -> incomeDAO.update(income));
    }

    public void delete(Income income) {
        executor.execute(() -> incomeDAO.delete(income));
    }

    public LiveData<List<Income>> getAllIncomeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return incomeDAO.getAllIncomeByRangeDate(startDate, endDate);
    }

    public LiveData<List<SqlBarCharts>> getCombinedResults(LocalDate startDate, LocalDate endDate) {
        return incomeDAO.getCombinedResults(startDate, endDate);
    }
}

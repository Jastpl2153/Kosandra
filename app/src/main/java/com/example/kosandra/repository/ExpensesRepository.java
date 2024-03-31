package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.daos.ExpensesDAO;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.db.returnSql.SqlBarCharts;
import com.example.kosandra.entity.Expenses;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExpensesRepository {
    private final ExpensesDAO expensesDAO;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ExpensesRepository(Application application) {
        KosandraDataBase database = KosandraDataBase.getInstance(application);
        expensesDAO = database.expensesDAO();
    }

    public void insert(Expenses expenses) {
        executor.execute(() -> expensesDAO.insert(expenses));
    }

    public void update(Expenses expenses) {
        executor.execute(() -> expensesDAO.update(expenses));
    }

    public void delete(Expenses expenses) {
        executor.execute(() -> expensesDAO.delete(expenses));
    }

    public LiveData<List<Expenses>> getAllExpensesByRangeDate(LocalDate startDate, LocalDate endDate) {
        return expensesDAO.getAllExpensesByRangeDate(startDate, endDate);
    }

    public LiveData<List<SqlBarCharts>> getGroupTypeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return expensesDAO.getGroupTypeByRangeDate(startDate, endDate);
    }
}

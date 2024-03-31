package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.db.returnSql.SqlBarCharts;
import com.example.kosandra.entity.Expenses;
import com.example.kosandra.repository.ExpensesRepository;

import java.time.LocalDate;
import java.util.List;

public class ExpensesViewModel extends AndroidViewModel {
    private ExpensesRepository repository;

    public ExpensesViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpensesRepository(application);

    }

    public void insert(Expenses expenses) {
        repository.insert(expenses);
    }

    public void update(Expenses expenses) {
        repository.update(expenses);
    }

    public void delete(Expenses expenses) {
        repository.delete(expenses);
    }

    public LiveData<List<Expenses>> getAllExpensesByRangeDate(LocalDate startDate, LocalDate endDate) {
        return repository.getAllExpensesByRangeDate(startDate, endDate);
    }

    public LiveData<List<SqlBarCharts>> getGroupTypeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return repository.getGroupTypeByRangeDate(startDate, endDate);
    }
}

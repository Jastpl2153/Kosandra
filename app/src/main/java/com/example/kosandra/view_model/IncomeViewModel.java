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

/**
 * IncomeViewModel class extends AndroidViewModel and acts as a communication link between the UI controller and the data repository for handling income-related operations.
 */
public class IncomeViewModel extends AndroidViewModel {
    private IncomeRepository repository;

    /**
     * Constructor for IncomeViewModel which initializes the IncomeRepository with the application context.
     *
     * @param application The application context required for initializing the repository.
     */
    public IncomeViewModel(@NonNull Application application) {
        super(application);
        repository = new IncomeRepository(application);
    }

    /**
     * Method to insert new income data into the repository.
     *
     * @param income The income object to be inserted.
     */
    public void insert(Income income) {
        repository.insert(income);
    }

    /**
     * Method to update existing income data in the repository.
     *
     * @param income The income object to be updated.
     */
    public void update(Income income) {
        repository.update(income);
    }

    /**
     * Method to delete income data from the repository.
     *
     * @param income The income object to be deleted.
     */
    public void delete(Income income) {
        repository.delete(income);
    }

    /**
     * Method to retrieve all income data within a specified date range from the repository.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return LiveData object containing a list of income data within the specified range.
     */
    public LiveData<List<Income>> getAllIncomeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return repository.getAllIncomeByRangeDate(startDate, endDate);
    }

    /**
     * Method to retrieve combined results of income data along with SQL-based bar chart data within a specified date range from the repository.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return LiveData object containing a list of combined income and bar chart data within the specified range.
     */
    public LiveData<List<SqlBarCharts>> getCombinedResults(LocalDate startDate, LocalDate endDate) {
        return repository.getCombinedResults(startDate, endDate);
    }
}

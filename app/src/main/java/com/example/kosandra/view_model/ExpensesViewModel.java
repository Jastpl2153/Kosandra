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

/**
 * The ExpensesViewModel class extends AndroidViewModel and acts as a bridge between the UI and the data layer, specifically the ExpensesRepository.
 * <p>
 * It provides methods for inserting, updating, deleting expenses, as well as retrieving expenses and group type data by range date.
 */
public class ExpensesViewModel extends AndroidViewModel {
    private ExpensesRepository repository;

    /**
     * Constructor for ExpensesViewModel class that initializes the ExpensesRepository.
     *
     * @param application The application context.
     */
    public ExpensesViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpensesRepository(application);

    }

    /**
     * Inserts the given Expenses object into the repository.
     *
     * @param expenses The Expenses object to be inserted.
     */
    public void insert(Expenses expenses) {
        repository.insert(expenses);
    }

    /**
     * Updates the given Expenses object in the repository.
     *
     * @param expenses The Expenses object to be updated.
     */
    public void update(Expenses expenses) {
        repository.update(expenses);
    }

    /**
     * Deletes the given Expenses object from the repository.
     *
     * @param expenses The Expenses object to be deleted.
     */
    public void delete(Expenses expenses) {
        repository.delete(expenses);
    }

    /**
     * Retrieves all expenses within the specified date range using LiveData.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return LiveData object containing a list of Expenses within the specified date range.
     */
    public LiveData<List<Expenses>> getAllExpensesByRangeDate(LocalDate startDate, LocalDate endDate) {
        return repository.getAllExpensesByRangeDate(startDate, endDate);
    }

    /**
     * Retrieves group type data within the specified date range using LiveData.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return LiveData object containing a list of SqlBarCharts representing group type data within the specified date range.
     */
    public LiveData<List<SqlBarCharts>> getGroupTypeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return repository.getGroupTypeByRangeDate(startDate, endDate);
    }
}

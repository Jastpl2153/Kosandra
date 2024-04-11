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

/**
 * The IncomeRepository class serves as an intermediary between the application code and the data access objects (IncomeDAO).
 * <p>
 * It manages the asynchronous execution of database operations using an Executor.
 */
public class IncomeRepository {
    private final IncomeDAO incomeDAO;

    private Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructs an IncomeRepository object with the provided application context to initialize the IncomeDAO.
     *
     * @param application The application context to obtain the database instance.
     */
    public IncomeRepository(Application application) {
        KosandraDataBase database = KosandraDataBase.getInstance(application);
        incomeDAO = database.incomeDAO();
    }

    /**
     * Inserts the specified Income object into the database via asynchronous execution.
     *
     * @param income The Income object to be inserted.
     */
    public void insert(Income income) {
        executor.execute(() -> incomeDAO.insert(income));
    }

    /**
     * Updates the specified Income object in the database via asynchronous execution.
     *
     * @param income The Income object to be updated.
     */
    public void update(Income income) {
        executor.execute(() -> incomeDAO.update(income));
    }

    /**
     * Deletes the specified Income object from the database via asynchronous execution.
     *
     * @param income The Income object to be deleted.
     */
    public void delete(Income income) {
        executor.execute(() -> incomeDAO.delete(income));
    }

    /**
     * Retrieves a LiveData object containing a list of Income objects within the specified date range.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return LiveData object holding a list of Income objects within the specified date range.
     */
    public LiveData<List<Income>> getAllIncomeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return incomeDAO.getAllIncomeByRangeDate(startDate, endDate);
    }

    /**
     * Retrieves a LiveData object containing combined results from the database for a specific date range.
     *
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return LiveData object holding combined results for the specified date range.
     */
    public LiveData<List<SqlBarCharts>> getCombinedResults(LocalDate startDate, LocalDate endDate) {
        return incomeDAO.getCombinedResults(startDate, endDate);
    }
}

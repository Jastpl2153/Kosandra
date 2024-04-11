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

/**
 * The ExpensesRepository class handles the data operations for Expenses objects. It provides methods to insert, update,
 * <p>
 * and delete expenses, as well as retrieve expenses and group type data within a specified date range.
 */
public class ExpensesRepository {
    private final ExpensesDAO expensesDAO;
    private Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructs a new ExpensesRepository instance, initializing the ExpensesDAO using the provided Application instance.
     *
     * @param application The Application instance used to obtain the database instance and expenses DAO.
     */
    public ExpensesRepository(Application application) {
        KosandraDataBase database = KosandraDataBase.getInstance(application);
        expensesDAO = database.expensesDAO();
    }

    /**
     * Inserts a new Expenses object into the database asynchronously.
     *
     * @param expenses The Expenses object to be inserted.
     */
    public void insert(Expenses expenses) {
        executor.execute(() -> expensesDAO.insert(expenses));
    }

    /**
     * Updates an existing Expenses object in the database asynchronously.
     *
     * @param expenses The Expenses object to be updated.
     */
    public void update(Expenses expenses) {
        executor.execute(() -> expensesDAO.update(expenses));
    }

    /**
     * Deletes an existing Expenses object from the database asynchronously.
     *
     * @param expenses The Expenses object to be deleted.
     */
    public void delete(Expenses expenses) {
        executor.execute(() -> expensesDAO.delete(expenses));
    }

    /**
     * Retrieves all Expenses objects within a specified date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @return A LiveData object containing a list of Expenses objects within the specified date range.
     */
    public LiveData<List<Expenses>> getAllExpensesByRangeDate(LocalDate startDate, LocalDate endDate) {
        return expensesDAO.getAllExpensesByRangeDate(startDate, endDate);
    }

    /**
     * Retrieves group type data within a specified date range for generating bar charts.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @return A LiveData object containing a list of SqlBarCharts objects representing group types within the specified date range.
     */
    public LiveData<List<SqlBarCharts>> getGroupTypeByRangeDate(LocalDate startDate, LocalDate endDate) {
        return expensesDAO.getGroupTypeByRangeDate(startDate, endDate);
    }
}

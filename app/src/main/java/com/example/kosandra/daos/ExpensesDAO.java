package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kosandra.db.returnSql.SqlBarCharts;
import com.example.kosandra.entity.Expenses;

import java.time.LocalDate;
import java.util.List;

/**
 * The ExpensesDAO interface provides methods to interact with the Expenses entity in the database.
 * <p>
 * This interface is annotated with @Dao annotation to indicate it is a Data Access Object for Room.
 */
@Dao
public interface ExpensesDAO {

    /**
     * Inserts a new Expenses object into the database.
     *
     * @param expenses the Expenses object to be inserted
     */
    @Insert
    void insert(Expenses expenses);

    /**
     * Deletes an existing Expenses object from the database.
     *
     * @param expenses the Expenses object to be deleted
     */
    @Delete
    void delete(Expenses expenses);

    /**
     * Updates an existing Expenses object in the database.
     *
     * @param expenses the Expenses object to be updated
     */
    @Update
    void update(Expenses expenses);

    /**
     * Retrieves all Expenses objects within a specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return a LiveData object containing a list of Expenses within the specified date range
     */
    @Query("select * from expenses where date >= :startDate and date <= :endDate")
    LiveData<List<Expenses>> getAllExpensesByRangeDate(LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves aggregated data of expenses based on type within a specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return a LiveData object containing a list of SqlBarCharts with aggregated sum of expenses by type within the specified date range
     */
    @Query("select typeExpenses as type, SUM(cost) as sumCost from expenses where date >= :startDate and date <= :endDate group by typeExpenses")
    LiveData<List<SqlBarCharts>> getGroupTypeByRangeDate(LocalDate startDate, LocalDate endDate);
}

package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kosandra.db.returnSql.SqlBarCharts;
import com.example.kosandra.entity.Income;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface defining data access methods for interacting with the Income entity in the database.
 */
@Dao
public interface IncomeDAO {
    /**
     * Insert a new Income entry into the database.
     *
     * @param income The Income object to be inserted.
     */
    @Insert
    void insert(Income income);

    /**
     * Delete an existing Income entry from the database.
     *
     * @param income The Income object to be deleted.
     */
    @Delete
    void delete(Income income);

    /**
     * Update an existing Income entry in the database.
     *
     * @param income The Income object to be updated.
     */
    @Update
    void update(Income income);

    /**
     * Retrieve a list of Income entries within a specified date range.
     *
     * @param startDate Start date of the date range.
     * @param endDate   End date of the date range.
     * @return A LiveData object containing the list of Income entries within the specified date range.
     */
    @Query("select * from income where date >= :startDate and date <= :endDate")
    LiveData<List<Income>> getAllIncomeByRangeDate(LocalDate startDate, LocalDate endDate);

    /**
     * Retrieve combined results of income and hairstyle visit costs within a specified date range.
     *
     * @param startDate Start date of the date range.
     * @param endDate   End date of the date range.
     * @return A LiveData object containing the combined results of income and hairstyle visit costs.
     */
    @Query("SELECT typeIncome AS type, SUM(cost) AS sumCost FROM income WHERE date >= :startDate AND date <= :endDate GROUP BY typeIncome " +
            "UNION " +
            "SELECT haircutName AS type, SUM(haircutCost) AS sumCost FROM hairstyleVisit WHERE visitDate >= :startDate AND visitDate <= :endDate GROUP BY haircutName")
    LiveData<List<SqlBarCharts>> getCombinedResults(LocalDate startDate, LocalDate endDate);

}

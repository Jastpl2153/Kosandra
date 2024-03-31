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

@Dao
public interface IncomeDAO {
    @Insert
    void insert(Income income);

    @Delete
    void delete(Income income);

    @Update
    void update(Income income);

    @Query("select * from income where date >= :startDate and date <= :endDate")
    LiveData<List<Income>> getAllIncomeByRangeDate(LocalDate startDate, LocalDate endDate);

    @Query("select typeIncome as type, SUM(cost) as sumCost from income where date >= :startDate and date <= :endDate group by typeIncome")
    LiveData<List<SqlBarCharts>> getGroupTypeByRangeDate(LocalDate startDate, LocalDate endDate);

    @Query("SELECT typeIncome AS type, SUM(cost) AS sumCost FROM income WHERE date >= :startDate AND date <= :endDate GROUP BY typeIncome " +
            "UNION " +
            "SELECT haircutName AS type, SUM(haircutCost) AS sumCost FROM hairstyleVisit WHERE visitDate >= :startDate AND visitDate <= :endDate GROUP BY haircutName")
    LiveData<List<SqlBarCharts>> getCombinedResults(LocalDate startDate, LocalDate endDate);

}

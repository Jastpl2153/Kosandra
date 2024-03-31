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

@Dao
public interface ExpensesDAO {
    @Insert
    void insert(Expenses expenses);

    @Delete
    void delete(Expenses expenses);

    @Update
    void update(Expenses expenses);

    @Query("select * from expenses where date >= :startDate and date <= :endDate")
    LiveData<List<Expenses>> getAllExpensesByRangeDate(LocalDate startDate, LocalDate endDate);

    @Query("select typeExpenses as type, SUM(cost) as sumCost from expenses where date >= :startDate and date <= :endDate group by typeExpenses")
    LiveData<List<SqlBarCharts>> getGroupTypeByRangeDate(LocalDate startDate, LocalDate endDate);
}

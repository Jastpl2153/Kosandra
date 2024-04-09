package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.kosandra.db.returnSql.SqlIncomeHairstyle;
import com.example.kosandra.db.returnSql.SqlIncomeHairstyleRangeDate;
import com.example.kosandra.entity.HairstyleVisit;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface HairstyleVisitDAO {
    @Insert
    void insert(HairstyleVisit haircut);

    @Update
    void update(HairstyleVisit haircut);

    @Delete
    void delete(HairstyleVisit haircut);

    @Query("select * from hairstyleVisit where visitId =:clientId")
    LiveData<List<HairstyleVisit>> getAllClientHairstyles(int clientId);

    @Query("select * from hairstyleVisit where id=:id")
    LiveData<HairstyleVisit> getHairstyleVisit(int id);

    @Query("select * from hairstyleVisit")
    LiveData<List<HairstyleVisit>>  getAllHairstyleVisit();

    @Query("select haircutName, COUNT(*) as cost from hairstyleVisit group by haircutName ORDER BY cost DESC LIMIT 1")
    LiveData<SqlIncomeHairstyle> getMostPopularHairstyle();

    @Query("select haircutName, SUM(haircutCost) as cost from hairstyleVisit group by haircutName ORDER BY cost DESC LIMIT 1")
    LiveData<SqlIncomeHairstyle> getMostProfitableHairstyle();

    @Query("select haircutName, COUNT(*) as countHairstyle, AVG(haircutCost) as avgPrice from hairstyleVisit WHERE visitDate >= :startDate AND visitDate <= :endDate group by haircutName")
    LiveData<List<SqlIncomeHairstyleRangeDate>> getHairstyleIncome(LocalDate startDate, LocalDate endDate);
}

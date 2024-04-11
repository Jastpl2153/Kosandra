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

/**
 * Interface for interacting with the HairstyleVisit entity in the database.
 */
@Dao
public interface HairstyleVisitDAO {
    /**
     * Insert a new HairstyleVisit entity into the database.
     *
     * @param haircut The HairstyleVisit object to be inserted.
     */
    @Insert
    void insert(HairstyleVisit haircut);

    /**
     * Update an existing HairstyleVisit entity in the database.
     *
     * @param haircut The HairstyleVisit object to be updated.
     */
    @Update
    void update(HairstyleVisit haircut);

    /**
     * Delete a HairstyleVisit entity from the database.
     *
     * @param haircut The HairstyleVisit object to be deleted.
     */
    @Delete
    void delete(HairstyleVisit haircut);

    /**
     * Retrieve all HairstyleVisits associated with a particular client.
     *
     * @param clientId The id of the client.
     * @return A LiveData list of HairstyleVisit objects.
     */
    @Query("select * from hairstyleVisit where visitId =:clientId")
    LiveData<List<HairstyleVisit>> getAllClientHairstyles(int clientId);

    /**
     * Retrieve a specific HairstyleVisit entity from the database.
     *
     * @param id The id of the HairstyleVisit entity.
     * @return A LiveData object representing the HairstyleVisit.
     */
    @Query("select * from hairstyleVisit where id=:id")
    LiveData<HairstyleVisit> getHairstyleVisit(int id);

    /**
     * Retrieve all HairstyleVisits in the database.
     *
     * @return A LiveData list of all HairstyleVisit objects.
     */
    @Query("select * from hairstyleVisit")
    LiveData<List<HairstyleVisit>> getAllHairstyleVisit();

    /**
     * Retrieve the most popular Hairstyle based on the number of visits.
     *
     * @return A LiveData object representing the most popular Hairstyle.
     */
    @Query("select haircutName, COUNT(*) as cost from hairstyleVisit group by haircutName ORDER BY cost DESC LIMIT 1")
    LiveData<SqlIncomeHairstyle> getMostPopularHairstyle();

    /**
     * Retrieve the most profitable Hairstyle based on the total cost.
     *
     * @return A LiveData object representing the most profitable Hairstyle.
     */
    @Query("select haircutName, SUM(haircutCost) as cost from hairstyleVisit group by haircutName ORDER BY cost DESC LIMIT 1")
    LiveData<SqlIncomeHairstyle> getMostProfitableHairstyle();

    /**
     * Retrieve the income details for each Hairstyle within a specified date range.
     *
     * @param startDate The start date of the date range.
     * @param endDate   The end date of the date range.
     * @return A LiveData list of SqlIncomeHairstyleRangeDate objects.
     */
    @Query("select haircutName, COUNT(*) as countHairstyle, AVG(haircutCost) as avgPrice from hairstyleVisit WHERE visitDate >= :startDate AND visitDate <= :endDate group by haircutName")
    LiveData<List<SqlIncomeHairstyleRangeDate>> getHairstyleIncome(LocalDate startDate, LocalDate endDate);
}

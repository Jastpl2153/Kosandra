package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.daos.HairstyleVisitDAO;
import com.example.kosandra.daos.MaterialsDAO;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.db.returnSql.SqlIncomeHairstyle;
import com.example.kosandra.db.returnSql.SqlIncomeHairstyleRangeDate;
import com.example.kosandra.entity.HairstyleVisit;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * HairstyleStyleRepository class contains methods to interact with the database for handling Hairstyle visits and related data.
 * <p>
 * It includes methods to insert, update, delete, and retrieve various types of Hairstyle visits and related information.
 */
public class HairstyleStyleRepository {
    // DAO object for interacting with HairstyleVisit entities
    private final HairstyleVisitDAO hairstyleVisitDAO;

    // Executor for database operations
    private Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructor for HairstyleStyleRepository that initializes DAO objects from the database instance.
     *
     * @param application the Application context for creating database instance
     */
    public HairstyleStyleRepository(Application application) {
        KosandraDataBase dataBase = KosandraDataBase.getInstance(application);
        hairstyleVisitDAO = dataBase.haircutDAO();
    }

    /**
     * Inserts a new HairstyleVisit entity into the database.
     *
     * @param hairstyleVisit the HairstyleVisit object to be inserted
     */
    public void insert(HairstyleVisit hairstyleVisit) {
        executor.execute(() -> hairstyleVisitDAO.insert(hairstyleVisit));
    }

    /**
     * Updates an existing HairstyleVisit entity in the database.
     *
     * @param hairstyleVisit the HairstyleVisit object to be updated
     */
    public void update(HairstyleVisit hairstyleVisit) {
        executor.execute(() -> hairstyleVisitDAO.update(hairstyleVisit));
    }

    /**
     * Deletes a HairstyleVisit entity from the database.
     *
     * @param hairstyleVisit the HairstyleVisit object to be deleted
     */
    public void delete(HairstyleVisit hairstyleVisit) {
        executor.execute(() -> hairstyleVisitDAO.delete(hairstyleVisit));
    }

    /**
     * Retrieves all HairstyleVisit entities related to a specific client from the database.
     *
     * @param clientId the ID of the client
     * @return LiveData object containing the list of HairstyleVisit entities
     */
    public LiveData<List<HairstyleVisit>> getAllClientHairstyles(int clientId) {
        return hairstyleVisitDAO.getAllClientHairstyles(clientId);
    }

    /**
     * Retrieves a specific HairstyleVisit entity by its ID from the database.
     *
     * @param id the ID of the HairstyleVisit entity
     * @return LiveData object containing the HairstyleVisit entity
     */
    public LiveData<HairstyleVisit> getHairstyleVisit(int id) {
        return hairstyleVisitDAO.getHairstyleVisit(id);
    }

    /**
     * Retrieves all HairstyleVisit entities from the database.
     *
     * @return LiveData object containing the list of all HairstyleVisit entities
     */
    public LiveData<List<HairstyleVisit>> getAllHairstyleVisit() {
        return hairstyleVisitDAO.getAllHairstyleVisit();
    }

    /**
     * Retrieves the most popular Hairstyle entity based on visits from the database.
     *
     * @return LiveData object containing the most popular Hairstyle entity
     */
    public LiveData<SqlIncomeHairstyle> getMostPopularHairstyle() {
        return hairstyleVisitDAO.getMostPopularHairstyle();
    }

    /**
     * Retrieves the most profitable Hairstyle entity based on income from the database.
     *
     * @return LiveData object containing the most profitable Hairstyle entity
     */
    public LiveData<SqlIncomeHairstyle> getMostProfitableHairstyle() {
        return hairstyleVisitDAO.getMostProfitableHairstyle();
    }

    /**
     * Retrieves Hairstyle income information within a specific date range from the database.
     *
     * @param startDate the start date of the income range
     * @param endDate   the end date of the income range
     * @return LiveData object containing the list of Hairstyle income information within the specified date range
     */
    public LiveData<List<SqlIncomeHairstyleRangeDate>> getHairstyleIncome(LocalDate startDate, LocalDate endDate) {
        return hairstyleVisitDAO.getHairstyleIncome(startDate, endDate);
    }
}

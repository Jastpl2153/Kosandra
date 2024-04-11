package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.daos.MaterialsDAO;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.entity.Materials;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The MaterialsRepository class acts as a bridge between the application code and the data access layer for Materials entities.
 * <p>
 * It provides methods to interact with the MaterialsDAO, which is responsible for database operations related to Materials.
 */
public class MaterialsRepository {
    private MaterialsDAO materialsDAO;
    private Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructs a MaterialsRepository object by initializing the MaterialsDAO using the provided application context.
     *
     * @param application The application context used to obtain the database instance.
     */
    public MaterialsRepository(Application application) {
        KosandraDataBase dataBase = KosandraDataBase.getInstance(application);
        materialsDAO = dataBase.materialsDAO();
    }

    /**
     * Inserts the provided Materials object into the database using a separate thread.
     *
     * @param materials The Materials object to be inserted.
     */
    public void insert(Materials materials) {
        executor.execute(() -> materialsDAO.insert(materials));
    }

    /**
     * Deletes the provided Materials object from the database using a separate thread.
     *
     * @param materials The Materials object to be deleted.
     */
    public void delete(Materials materials) {
        executor.execute(() -> materialsDAO.delete(materials));
    }

    /**
     * Updates the provided Materials object in the database using a separate thread.
     *
     * @param materials The Materials object to be updated.
     */
    public void update(Materials materials) {
        executor.execute(() -> materialsDAO.update(materials));
    }

    /**
     * Updates the provided Materials object in the database without using a separate thread.
     *
     * @param materials The Materials object to be updated.
     */
    public void updateNoThread(Materials materials) {
        materialsDAO.update(materials);
    }

    /**
     * Retrieves a LiveData object containing a list of Materials based on the specified type.
     *
     * @param type The type of Materials to retrieve.
     * @return LiveData object containing a list of Materials.
     */
    public LiveData<List<Materials>> getMaterialsList(String type) {
        return materialsDAO.getAllTypeMaterials(type);
    }

    /**
     * Retrieves a LiveData object containing a single Materials object based on the specified id.
     *
     * @param id The id of the Materials object to retrieve.
     * @return LiveData object containing a single Materials object.
     */
    public LiveData<Materials> getMaterial(int id) {
        return materialsDAO.getMaterial(id);
    }

    /**
     * Retrieves a Materials object based on the specified codeMaterial.
     *
     * @param codeMaterial The codeMaterial of the Materials object to retrieve.
     * @return The Materials object corresponding to the provided codeMaterial.
     */
    public Materials getMaterial(String codeMaterial) {
        return materialsDAO.getMaterial(codeMaterial);
    }

    /**
     * Retrieves a list of all Materials codes from the database.
     *
     * @return List of Strings containing all Materials codes.
     * @throws RuntimeException if there is an error executing the database query.
     */
    public List<String> getAllMaterialsCode() {
        try {
            return Executors.newSingleThreadExecutor().submit(() -> materialsDAO.getAllMaterialsCode()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a LiveData List of Materials sorted by color in ascending order for a specific type.
     *
     * @param type The type of materials to retrieve
     * @return A LiveData object containing a List of Materials sorted by color in ascending order
     */
    public LiveData<List<Materials>> getAllMaterialSortedByColorAscending(String type) {
        return materialsDAO.getAllMaterialSortedByColorAscending(type);
    }

    /**
     * Retrieves a LiveData List of Materials sorted by color in descending order for a specific type.
     *
     * @param type The type of materials to retrieve
     * @return A LiveData object containing a List of Materials sorted by color in descending order
     */
    public LiveData<List<Materials>> getAllMaterialSortedByColorDescending(String type) {
        return materialsDAO.getAllMaterialSortedByColorDescending(type);
    }

    /**
     * Retrieves a LiveData List of Materials sorted by count in ascending order for a specific type.
     *
     * @param type The type of materials to retrieve
     * @return A LiveData object containing a List of Materials sorted by count in ascending order
     */
    public LiveData<List<Materials>> getAllMaterialSortedByCountAscending(String type) {
        return materialsDAO.getAllMaterialSortedByCountAscending(type);
    }

    /**
     * Retrieves a LiveData List of Materials sorted by count in descending order for a specific type.
     *
     * @param type The type of materials to retrieve
     * @return A LiveData object containing a List of Materials sorted by count in descending order
     */
    public LiveData<List<Materials>> getAllMaterialSortedByCountDescending(String type) {
        return materialsDAO.getAllMaterialSortedByCountDescending(type);
    }

    /**
     * Retrieves a LiveData List of Materials sorted by rating in ascending order for a specific type.
     *
     * @param type The type of materials to retrieve
     * @return A LiveData object containing a List of Materials sorted by rating in ascending order
     */
    public LiveData<List<Materials>> getAllMaterialSortedByRatingAscending(String type) {
        return materialsDAO.getAllMaterialSortedByRatingAscending(type);
    }

    /**
     * Retrieves a LiveData List of Materials sorted by rating in descending order for a specific type.
     *
     * @param type The type of materials to retrieve
     * @return A LiveData object containing a List of Materials sorted by rating in descending order
     */
    public LiveData<List<Materials>> getAllMaterialSortedByRatingDescending(String type) {
        return materialsDAO.getAllMaterialSortedByRatingDescending(type);
    }
}

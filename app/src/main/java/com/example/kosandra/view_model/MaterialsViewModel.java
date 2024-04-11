package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.entity.Materials;
import com.example.kosandra.repository.MaterialsRepository;

import java.util.List;

/**
 * The MaterialsViewModel class extends AndroidViewModel and serves as a mediator between the UI and the data layer.
 * <p>
 * It provides various methods for interacting with the MaterialsRepository, which in turn handles interactions
 * <p>
 * with the database and exposes LiveData objects for observing changes to the data.
 */
public class MaterialsViewModel extends AndroidViewModel {
    private MaterialsRepository repository;

    /**
     * Constructor for MaterialsViewModel class that initializes the MaterialsRepository with the application context.
     *
     * @param application The application object used to initialize the repository.
     */
    public MaterialsViewModel(@NonNull Application application) {
        super(application);
        repository = new MaterialsRepository(application);
    }

    /**
     * Inserts a Materials object into the database via the repository.
     *
     * @param materials The Materials object to be inserted.
     */
    public void insert(Materials materials) {
        repository.insert(materials);
    }

    /**
     * Deletes a Materials object from the database via the repository.
     *
     * @param materials The Materials object to be deleted.
     */
    public void delete(Materials materials) {
        repository.delete(materials);
    }

    /**
     * Updates a Materials object in the database via the repository.
     *
     * @param materials The Materials object to be updated.
     */
    public void update(Materials materials) {
        repository.update(materials);
    }

    /**
     * Updates a Materials object in the database without using a background thread via the repository.
     *
     * @param materials The Materials object to be updated.
     */
    public void updateNoThread(Materials materials) {
        repository.updateNoThread(materials);
    }

    /**
     * Retrieves a LiveData object containing a list of Materials based on the specified type.
     *
     * @param type The type of Materials to retrieve.
     * @return A LiveData object containing a list of Materials.
     */
    public LiveData<List<Materials>> getMaterialsList(String type) {
        return repository.getMaterialsList(type);
    }

    /**
     * Retrieves a LiveData object containing a single Materials object based on the specified ID.
     *
     * @param id The ID of the Materials object to retrieve.
     * @return A LiveData object containing a single Materials object.
     */
    public LiveData<Materials> getMaterial(int id) {
        return repository.getMaterial(id);
    }

    /**
     * Retrieves a Materials object based on the specified material code.
     *
     * @param codeMaterial The material code used to identify the Materials object.
     * @return The corresponding Materials object.
     */
    public Materials getMaterial(String codeMaterial) {
        return repository.getMaterial(codeMaterial);
    }

    /**
     * Retrieves a list of all material codes stored in the database.
     *
     * @return A list of all material codes present in the database.
     */
    public List<String> getAllMaterialsCode() {
        return repository.getAllMaterialsCode();
    }

    /**
     * Retrieve a LiveData List of Materials sorted by color in ascending order.
     *
     * @param type The type of materials.
     * @return A LiveData List of Materials sorted by color in ascending order.
     */
    public LiveData<List<Materials>> getAllMaterialSortedByColorAscending(String type) {
        return repository.getAllMaterialSortedByColorAscending(type);
    }

    /**
     * Retrieve a LiveData List of Materials sorted by color in descending order.
     *
     * @param type The type of materials.
     * @return A LiveData List of Materials sorted by color in descending order.
     */
    public LiveData<List<Materials>> getAllMaterialSortedByColorDescending(String type) {
        return repository.getAllMaterialSortedByColorDescending(type);
    }

    /**
     * Retrieve a LiveData List of Materials sorted by count in ascending order.
     *
     * @param type The type of materials.
     * @return A LiveData List of Materials sorted by count in ascending order.
     */
    public LiveData<List<Materials>> getAllMaterialSortedByCountAscending(String type) {
        return repository.getAllMaterialSortedByCountAscending(type);
    }

    /**
     * Retrieve a LiveData List of Materials sorted by count in descending order.
     *
     * @param type The type of materials.
     * @return A LiveData List of Materials sorted by count in descending order.
     */
    public LiveData<List<Materials>> getAllMaterialSortedByCountDescending(String type) {
        return repository.getAllMaterialSortedByCountDescending(type);
    }

    /**
     * Retrieve a LiveData List of Materials sorted by rating in ascending order.
     *
     * @param type The type of materials.
     * @return A LiveData List of Materials sorted by rating in ascending order.
     */
    public LiveData<List<Materials>> getAllMaterialSortedByRatingAscending(String type) {
        return repository.getAllMaterialSortedByRatingAscending(type);
    }

    /**
     * Retrieve a LiveData List of Materials sorted by rating in descending order.
     *
     * @param type The type of materials.
     * @return A LiveData List of Materials sorted by rating in descending order.
     */
    public LiveData<List<Materials>> getAllMaterialSortedByRatingDescending(String type) {
        return repository.getAllMaterialSortedByRatingDescending(type);
    }
}

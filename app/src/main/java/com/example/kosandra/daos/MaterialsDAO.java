package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kosandra.entity.Materials;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing Materials entities in the database.
 */
@Dao
public interface MaterialsDAO {
    /**
     * Inserts a new Materials entity into the database.
     *
     * @param materials The Materials entity to be inserted.
     */
    @Insert
    void insert(Materials materials);

    /**
     * Deletes an existing Materials entity from the database.
     *
     * @param materials The Materials entity to be deleted.
     */
    @Delete
    void delete(Materials materials);

    /**
     * Updates an existing Materials entity in the database.
     *
     * @param materials The Materials entity to be updated.
     */
    @Update
    void update(Materials materials);

    /**
     * Retrieves all Materials entities of a specific type from the database.
     *
     * @param typeMaterials The type of Materials entities to retrieve.
     * @return A LiveData list of Materials entities of the specified type.
     */
    @Query("select * from materials where typeMaterials=:typeMaterials")
    LiveData<List<Materials>> getAllTypeMaterials(String typeMaterials);

    /**
     * Retrieves a specific Materials entity based on its id.
     *
     * @param id The id of the Materials entity to retrieve.
     * @return A LiveData of the Materials entity with the specified id.
     */
    @Query("select * from materials where id=:id")
    LiveData<Materials> getMaterial(int id);

    /**
     * Retrieves a specific Materials entity based on its codeMaterial.
     *
     * @param codeMaterial The codeMaterial of the Materials entity to retrieve.
     * @return The Materials entity with the specified codeMaterial.
     */
    @Query("select * from materials where codeMaterial=:codeMaterial")
    Materials getMaterial(String codeMaterial);

    /**
     * Retrieves a list of all Materials codes from the database.
     *
     * @return A list of all Materials codes.
     */
    @Query("select codeMaterial from materials")
    List<String> getAllMaterialsCode();

    /**
     * Retrieves all Materials entities of a specific type from the database sorted by color in ascending order.
     *
     * @param typeMaterials The type of Materials entities to retrieve.
     * @return A LiveData list of Materials entities sorted by color in ascending order.
     */
    @Query("select * from materials where typeMaterials=:typeMaterials ORDER BY colorMaterial ASC")
    LiveData<List<Materials>> getAllMaterialSortedByColorAscending(String typeMaterials);

    /**
     * Retrieves all materials of a specific type sorted by color in descending order.
     *
     * @param typeMaterials The type of materials to filter by.
     * @return A LiveData object containing a list of materials sorted by color in descending order.
     */
    @Query("select * from materials where typeMaterials=:typeMaterials ORDER BY colorMaterial DESC")
    LiveData<List<Materials>> getAllMaterialSortedByColorDescending(String typeMaterials);

    /**
     * Retrieves all materials of a specific type sorted by count in ascending order.
     *
     * @param typeMaterials The type of materials to filter by.
     * @return A LiveData object containing a list of materials sorted by count in ascending order.
     */
    @Query("select * from materials where typeMaterials=:typeMaterials ORDER BY count ASC")
    LiveData<List<Materials>> getAllMaterialSortedByCountAscending(String typeMaterials);

    /**
     * Retrieves all materials of a specific type sorted by count in descending order.
     *
     * @param typeMaterials The type of materials to filter by.
     * @return A LiveData object containing a list of materials sorted by count in descending order.
     */
    @Query("select * from materials where typeMaterials=:typeMaterials ORDER BY count ASC")
    LiveData<List<Materials>> getAllMaterialSortedByCountDescending(String typeMaterials);

    /**
     * Retrieves all materials of a specific type sorted by rating in ascending order.
     *
     * @param typeMaterials The type of materials to filter by.
     * @return A LiveData object containing a list of materials sorted by rating in ascending order.
     */
    @Query("select * from materials where typeMaterials=:typeMaterials ORDER BY rating ASC")
    LiveData<List<Materials>> getAllMaterialSortedByRatingAscending(String typeMaterials);

    /**
     * Retrieves all materials of a specific type sorted by rating in descending order.
     *
     * @param typeMaterials The type of materials to filter by.
     * @return A LiveData object containing a list of materials sorted by rating in descending order.
     */
    @Query("select * from materials where typeMaterials=:typeMaterials ORDER BY rating DESC")
    LiveData<List<Materials>> getAllMaterialSortedByRatingDescending(String typeMaterials);
}

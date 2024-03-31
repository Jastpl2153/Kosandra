package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kosandra.entity.Materials;

import java.util.List;

@Dao
public interface MaterialsDAO {
    @Insert
    void insert(Materials materials);

    @Delete
    void delete(Materials materials);

    @Update
    void update(Materials materials);

    @Query("select * from materials where typeMaterials=:typeMaterials")
    LiveData<List<Materials>> getAllTypeMaterials(String typeMaterials);

    @Query("select * from materials where id=:id")
    LiveData<Materials> getMaterial(int id);

    @Query("select * from materials where codeMaterial=:codeMaterial")
    Materials getMaterial(String codeMaterial);

    @Query("select codeMaterial from materials")
    List<String> getAllMaterialsCode();
}

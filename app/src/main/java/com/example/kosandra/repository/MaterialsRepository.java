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

public class MaterialsRepository {
    private MaterialsDAO materialsDAO;
    private Executor executor = Executors.newSingleThreadExecutor();

    public MaterialsRepository(Application application) {
        KosandraDataBase dataBase = KosandraDataBase.getInstance(application);
        materialsDAO = dataBase.materialsDAO();
    }

    public void insert(Materials materials) {
        executor.execute(() -> materialsDAO.insert(materials));
    }
    public void delete(Materials materials) {
        executor.execute(() -> materialsDAO.delete(materials));
    }
    public void update(Materials materials) {
        executor.execute(() -> materialsDAO.update(materials));
    }

    public void updateNoThread(Materials materials) {
        materialsDAO.update(materials);
    }
    public LiveData<List<Materials>> getMaterialsList(String type) {
        return materialsDAO.getAllTypeMaterials(type);
    }
    public LiveData<Materials> getMaterial(int id){
        return materialsDAO.getMaterial(id);
    }

    public Materials getMaterial(String codeMaterial){
        return materialsDAO.getMaterial(codeMaterial);
    }

    public List<String> getAllMaterialsCode() {
        try {
            return Executors.newSingleThreadExecutor().submit(() -> materialsDAO.getAllMaterialsCode()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Materials>> getAllMaterialSortedByColorAscending(String type) {
        return materialsDAO.getAllMaterialSortedByColorAscending(type);
    }

    public LiveData<List<Materials>> getAllMaterialSortedByColorDescending(String type) {
        return materialsDAO.getAllMaterialSortedByColorDescending(type);
    }

    public LiveData<List<Materials>> getAllMaterialSortedByCountAscending(String type) {
        return materialsDAO.getAllMaterialSortedByCountAscending(type);
    }

    public LiveData<List<Materials>> getAllMaterialSortedByCountDescending(String type) {
        return materialsDAO.getAllMaterialSortedByCountDescending(type);
    }

    public LiveData<List<Materials>> getAllMaterialSortedByRatingAscending(String type) {
        return materialsDAO.getAllMaterialSortedByRatingAscending(type);
    }

    public LiveData<List<Materials>> getAllMaterialSortedByRatingDescending(String type) {
        return materialsDAO.getAllMaterialSortedByRatingDescending(type);
    }
}

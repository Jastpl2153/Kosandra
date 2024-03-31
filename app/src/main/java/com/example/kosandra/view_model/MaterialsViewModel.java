package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.entity.Materials;
import com.example.kosandra.repository.MaterialsRepository;

import java.util.List;

public class MaterialsViewModel extends AndroidViewModel {
    private MaterialsRepository repository;

    public MaterialsViewModel(@NonNull Application application) {
        super(application);
        repository = new MaterialsRepository(application);
    }

    public void insert(Materials materials) {
        repository.insert(materials);
    }

    public void delete(Materials materials) {
        repository.delete(materials);
    }

    public void update(Materials materials) {
        repository.update(materials);
    }

    public void updateNoThread(Materials materials) {
        repository.updateNoThread(materials);
    }

    public LiveData<List<Materials>> getMaterialsList(String type) {
        return repository.getMaterialsList(type);
    }

    public LiveData<Materials> getMaterial(int id) {
        return repository.getMaterial(id);
    }

    public Materials getMaterial(String codeMaterial) {
        return repository.getMaterial(codeMaterial);
    }

    public List<String> getAllMaterialsCode() {
        return repository.getAllMaterialsCode();
    }
}

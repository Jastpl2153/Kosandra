package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.repository.HairstyleStyleRepository;

import java.util.List;

public class HairstyleVisitViewModel extends AndroidViewModel {
    private HairstyleStyleRepository repository;
    private LiveData<List<HairstyleVisit>> allHairstyleClient;

    public HairstyleVisitViewModel(@NonNull Application application) {
        super(application);
        repository = new HairstyleStyleRepository(application);
    }

    public void insert(HairstyleVisit hairstyleVisit) {
        repository.insertHairstyle(hairstyleVisit);
    }

    public void update(HairstyleVisit hairstyleVisit) {
        repository.updateHairstyle(hairstyleVisit);
    }

    public void delete(HairstyleVisit hairstyleVisit) {
        repository.deleteHairstyle(hairstyleVisit);
    }

    public LiveData<List<HairstyleVisit>> getAllHairstyleClient(int clientId) {
        return allHairstyleClient = repository.getAllHairstyles(clientId);
    }
}

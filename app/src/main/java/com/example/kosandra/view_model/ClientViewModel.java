package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.entity.Client;
import com.example.kosandra.repository.ClientRepository;

import java.util.List;

public class ClientViewModel extends AndroidViewModel {
    private ClientRepository repository;
    private LiveData<List<Client>> allClient;

    public ClientViewModel(@NonNull Application application) {
        super(application);
        repository = new ClientRepository(application);
        allClient = repository.getAllClients();
    }

    public void insert(Client client) {
        repository.insert(client);
    }

    public void update(Client client) {
        repository.update(client);
    }

    public void delete(Client client) {
        repository.delete(client);
    }

    public LiveData<List<Client>> getAllClients() {
        return allClient;
    }

    public LiveData<Client> getClient(int id) {
        return repository.getClient(id);
    }

    public LiveData<List<String>> getNameAllClients() {
        return repository.getNameAllClients();
    }

    public LiveData<List<Client>> getAllClientsSortedByNameAscending() {
        return repository.getAllClientsSortedByNameAscending();
    }

    public LiveData<List<Client>> getAllClientsSortedByNameDescending() {
        return repository.getAllClientsSortedByNameDescending();
    }

    public LiveData<List<Client>> getAllClientsSortedByVisitsAscending() {
        return repository.getAllClientsSortedByVisitsAscending();
    }

    public LiveData<List<Client>> getAllClientsSortedByVisitsDescending() {
        return repository.getAllClientsSortedByVisitsDescending();
    }
}
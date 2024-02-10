package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.KosandraDataBase;
import com.example.kosandra.daos.ClientDAO;
import com.example.kosandra.ui.client.Client;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClientRepository {
    private final ClientDAO clientDAO;
    private LiveData<List<Client>> clientLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ClientRepository(Application application) {
        KosandraDataBase database = KosandraDataBase.getInstance(application);
        clientDAO = database.clientDAO();
        clientLiveData = clientDAO.getAllClients();
    }

    public void insertClient(Client client) {
        // Вызов метода вставки клиента в базу данных через DAO
        executor.execute(() -> clientDAO.insert(client));
    }

    public void updateClient(Client client) {
        // Вызов метода обновления клиента в базе данных через DAO
        executor.execute(() -> clientDAO.update(client));
    }

    public void deleteClient(Client client) {
        // Вызов метода удаления клиента из базы данных через DAO
        executor.execute(() -> clientDAO.delete(client));
    }

    public LiveData<List<Client>> getAllClients() {
        // Получение списка клиентов из базы данных через DAO с использованием LiveData
        return clientLiveData;
    }
}

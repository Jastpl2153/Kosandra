package com.example.kosandra.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.daos.ClientDAO;
import com.example.kosandra.entity.Client;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The ClientRepository class serves as an intermediary between the ViewModel and the DAO layer for managing client data.
 * <p>
 * It provides methods for inserting, updating, and deleting clients in the database, as well as fetching lists of clients
 * <p>
 * with various sorting options. All database operations are executed asynchronously using an Executor.
 */
public class ClientRepository {
    // Data Access Object for client database operations
    private final ClientDAO clientDAO;
    // LiveData object containing a list of clients
    private LiveData<List<Client>> clientLiveData;
    // Single thread executor for asynchronous tasks
    private Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructor for ClientRepository class that initializes the clientDAO and clientLiveData objects by fetching
     * the application context and initializing the database instance.
     *
     * @param application the Application context
     */
    public ClientRepository(Application application) {
        KosandraDataBase database = KosandraDataBase.getInstance(application);
        clientDAO = database.clientDAO();
        clientLiveData = clientDAO.getAllClients();
    }

    /**
     * Inserts a new client into the database using the clientDAO insert method.
     *
     * @param client the client to be inserted
     */
    public void insert(Client client) {
        // Вызов метода вставки клиента в базу данных через DAO
        executor.execute(() -> clientDAO.insert(client));
    }

    /**
     * Updates an existing client in the database using the clientDAO update method.
     *
     * @param client the client to be updated
     */
    public void update(Client client) {
        // Вызов метода обновления клиента в базе данных через DAO
        executor.execute(() -> clientDAO.update(client));
    }

    /**
     * Deletes a client from the database using the clientDAO delete method.
     *
     * @param client the client to be deleted
     */
    public void delete(Client client) {
        // Вызов метода удаления клиента из базы данных через DAO
        executor.execute(() -> clientDAO.delete(client));
    }

    /**
     * Retrieves a LiveData object containing a list of all clients from the database using the clientDAO getAllClients method.
     *
     * @return LiveData<List < Client>> object containing a list of all clients
     */
    public LiveData<List<Client>> getAllClients() {
        // Получение списка клиентов из базы данных через DAO с использованием LiveData
        return clientLiveData;
    }

    /**
     * Retrieves a LiveData object containing the client with the specified ID from the database using the clientDAO getClient method.
     *
     * @param id the ID of the client to retrieve
     * @return LiveData<Client> object representing the client with the specified ID
     */
    public LiveData<Client> getClient(int id) {
        // Получение клиент из базы данных по id через DAO с использованием LiveData
        return clientDAO.getClient(id);
    }

    /**
     * Retrieves a LiveData object containing a list of names of all clients from the database using the clientDAO getNameAllClients method.
     *
     * @return LiveData<List < String>> object containing a list of names of all clients
     */
    public LiveData<List<String>> getNameAllClients() {
        return clientDAO.getNameAllClients();
    }

    /**
     * Retrieves a LiveData object containing a list of all clients sorted by name in ascending order from the database using the clientDAO getAllClientsSortedByNameAscending method.
     *
     * @return LiveData<List < Client>> object containing a list of clients sorted by name in ascending order
     */
    public LiveData<List<Client>> getAllClientsSortedByNameAscending() {
        return clientDAO.getAllClientsSortedByNameAscending();
    }

    /**
     * Retrieves a LiveData object containing a list of all clients sorted by name in descending order from the database using the clientDAO getAllClientsSortedByNameDescending method.
     *
     * @return LiveData<List < Client>> object containing a list of clients sorted by name in descending order
     */
    public LiveData<List<Client>> getAllClientsSortedByNameDescending() {
        return clientDAO.getAllClientsSortedByNameDescending();
    }

    /**
     * Retrieves a LiveData object containing a list of all clients sorted by visits in ascending order from the database using the clientDAO getAllClientsSortedByVisitsAscending method.
     *
     * @return LiveData<List < Client>> object containing a list of clients sorted by visits in ascending order
     */
    public LiveData<List<Client>> getAllClientsSortedByVisitsAscending() {
        return clientDAO.getAllClientsSortedByVisitsAscending();
    }

    /**
     * Retrieves a LiveData object containing a list of all clients sorted by visits in descending order from the database using the clientDAO getAllClientsSortedByVisitsDescending method.
     *
     * @return LiveData<List < Client>> object containing a list of clients sorted by visits in descending order
     */
    public LiveData<List<Client>> getAllClientsSortedByVisitsDescending() {
        return clientDAO.getAllClientsSortedByVisitsDescending();
    }
}

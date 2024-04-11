package com.example.kosandra.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kosandra.entity.Client;
import com.example.kosandra.repository.ClientRepository;

import java.util.List;

/**
 * ViewModel class responsible for handling operations related to clients.
 * <p>
 * This class manages client data and interacts with the repository to perform CRUD operations.
 */
public class ClientViewModel extends AndroidViewModel {
    private ClientRepository repository;
    private LiveData<List<Client>> allClient;

    /**
     * Constructor for ClientViewModel class.
     * Initializes the repository and fetches all clients from the database.
     *
     * @param application The application context.
     */
    public ClientViewModel(@NonNull Application application) {
        super(application);
        repository = new ClientRepository(application);
        allClient = repository.getAllClients();
    }

    /**
     * Inserts a new client into the database.
     *
     * @param client The client object to be inserted.
     */
    public void insert(Client client) {
        repository.insert(client);
    }

    /**
     * Updates an existing client in the database.
     *
     * @param client The client object to be updated.
     */
    public void update(Client client) {
        repository.update(client);
    }

    /**
     * Deletes a client from the database.
     *
     * @param client The client object to be deleted.
     */
    public void delete(Client client) {
        repository.delete(client);
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return LiveData object containing a list of all clients.
     */
    public LiveData<List<Client>> getAllClients() {
        return allClient;
    }

    /**
     * Retrieves a specific client based on its ID.
     *
     * @param id The ID of the client to retrieve.
     * @return LiveData object containing the client with the specified ID.
     */
    public LiveData<Client> getClient(int id) {
        return repository.getClient(id);
    }

    /**
     * Retrieves the names of all clients from the database.
     *
     * @return LiveData object containing a list of client names.
     */
    public LiveData<List<String>> getNameAllClients() {
        return repository.getNameAllClients();
    }

    /**
     * Retrieves all clients sorted by name in ascending order.
     *
     * @return LiveData object containing a list of clients sorted by name in ascending order.
     */
    public LiveData<List<Client>> getAllClientsSortedByNameAscending() {
        return repository.getAllClientsSortedByNameAscending();
    }

    /**
     * Retrieves all clients sorted by name in descending order.
     *
     * @return LiveData object containing a list of clients sorted by name in descending order.
     */
    public LiveData<List<Client>> getAllClientsSortedByNameDescending() {
        return repository.getAllClientsSortedByNameDescending();
    }

    /**
     * Retrieves all clients sorted by visits in ascending order.
     *
     * @return LiveData object containing a list of clients sorted by visits count in ascending order.
     */
    public LiveData<List<Client>> getAllClientsSortedByVisitsAscending() {
        return repository.getAllClientsSortedByVisitsAscending();
    }

    /**
     * Retrieves all clients sorted by visits in descending order.
     *
     * @return LiveData object containing a list of clients sorted by visits count in descending order.
     */
    public LiveData<List<Client>> getAllClientsSortedByVisitsDescending() {
        return repository.getAllClientsSortedByVisitsDescending();
    }
}
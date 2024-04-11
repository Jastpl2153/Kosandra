package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kosandra.entity.Client;

import java.util.List;
/**
 * This interface defines the Data Access Object (DAO) for handling Client entities in the database.
 */
@Dao
public interface ClientDAO {

    /**
     * Inserts a new client entity into the database.
     *
     * @param client The client entity to be inserted.
     */
    @Insert
    void insert(Client client);

    /**
     * Deletes a client entity from the database.
     *
     * @param client The client entity to be deleted.
     */
    @Delete
    void delete(Client client);

    /**
     * Updates an existing client entity in the database.
     *
     * @param client The client entity to be updated.
     */
    @Update
    void update(Client client);

    /**
     * Retrieves all clients from the database.
     *
     * @return LiveData object containing a list of all clients.
     */
    @Query("select * from client")
    LiveData<List<Client>> getAllClients();

    /**
     * Retrieves a specific client by ID from the database.
     *
     * @param id The ID of the client to retrieve.
     * @return LiveData object containing the client with the specified ID.
     */
    @Query("select * from client where id=:id")
    LiveData<Client> getClient(int id);

    /**
     * Retrieves the names of all clients from the database.
     *
     * @return LiveData object containing a list of names of all clients.
     */
    @Query("select name from client")
    LiveData<List<String>>  getNameAllClients();


    /**
     * Retrieves all clients from the database, sorted by name in ascending order.
     *
     * @return LiveData object containing a list of clients sorted by name in ascending order.
     */
    @Query("SELECT * FROM client ORDER BY name ASC")
    LiveData<List<Client>> getAllClientsSortedByNameAscending();

    /**
     * Retrieves all clients from the database, sorted by name in descending order.
     *
     * @return LiveData object containing a list of clients sorted by name in descending order.
     */
    @Query("SELECT * FROM client ORDER BY name DESC")
    LiveData<List<Client>> getAllClientsSortedByNameDescending();

    /**
     * Retrieves all clients from the database, sorted by number of visits in ascending order.
     *
     * @return LiveData object containing a list of clients sorted by number of visits in ascending order.
     */
    @Query("SELECT * FROM client ORDER BY numberOfVisits ASC")
    LiveData<List<Client>> getAllClientsSortedByVisitsAscending();

    /**
     * Retrieves all clients from the database, sorted by number of visits in descending order.
     *
     * @return LiveData object containing a list of clients sorted by number of visits in descending order.
     */
    @Query("SELECT * FROM client ORDER BY numberOfVisits DESC")
    LiveData<List<Client>> getAllClientsSortedByVisitsDescending();
}

package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kosandra.entity.Client;

import java.util.List;

@Dao
public interface ClientDAO {

    @Insert
    void insert(Client client);

    @Delete
    void delete(Client client);

    @Update
    void update(Client client);

    @Query("select * from client")
    LiveData<List<Client>> getAllClients();

    @Query("select * from client where id=:id")
    LiveData<Client> getClient(int id);

    @Query("select name from client")
    LiveData<List<String>>  getNameAllClients();

    @Query("SELECT * FROM client ORDER BY name ASC")
    LiveData<List<Client>> getAllClientsSortedByNameAscending();

    @Query("SELECT * FROM client ORDER BY name DESC")
    LiveData<List<Client>> getAllClientsSortedByNameDescending();

    @Query("SELECT * FROM client ORDER BY numberOfVisits ASC")
    LiveData<List<Client>> getAllClientsSortedByVisitsAscending();

    @Query("SELECT * FROM client ORDER BY numberOfVisits DESC")
    LiveData<List<Client>> getAllClientsSortedByVisitsDescending();
}

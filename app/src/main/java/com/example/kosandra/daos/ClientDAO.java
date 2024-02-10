package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kosandra.ui.client.Client;

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
}

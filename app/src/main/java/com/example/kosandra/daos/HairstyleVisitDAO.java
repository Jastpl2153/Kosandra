package com.example.kosandra.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;

import java.util.List;

@Dao
public interface HairstyleVisitDAO {
    @Insert
    void insert(HairstyleVisit haircut);

    @Update
    void update(HairstyleVisit haircut);

    @Delete
    void delete(HairstyleVisit haircut);

    @Query("select * from hairstyleVisit where visitId =:clientId")
    LiveData<List<HairstyleVisit>> getAllHairstyles(int clientId);

    @Query("select * from hairstyleVisit where id=:id")
    LiveData<HairstyleVisit> getHairstyleVisit(int id);
}

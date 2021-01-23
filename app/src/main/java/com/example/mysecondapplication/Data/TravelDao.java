package com.example.mysecondapplication.Data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

/**
 * enables to get data from room using SQL query's
 */
@Dao
public interface TravelDao {

    @Query("select * from travels")
    LiveData<List<Travel>> getAll();

    @Query("select * from travels where travelId=:id")
    LiveData<Travel> get(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Travel travel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Travel> travels);

    @Update
    void update(Travel travel);

    @Delete
    void delete(Travel... travels);

    @Query("delete from travels")
    void clear();

}

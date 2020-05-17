package com.example.brunovsiq.guarana_test.database;

import com.example.brunovsiq.guarana_test.model.Place;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM place")
    List<Place> getAll();

    @Query("SELECT * FROM place WHERE id=:id")
    Place getPlace(String id);

    @Insert
    void insert(Place place);

    @Delete
    void delete(Place user);
}

package com.example.brunovsiq.guarana_test.database;

import android.content.Context;

import com.example.brunovsiq.guarana_test.model.Place;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { Place.class }, version = 1, exportSchema = false)
public abstract class PlaceDatabase extends RoomDatabase {

    private static final String DB_NAME = "placeDatabase.db";
    private static volatile PlaceDatabase instance;

    public static synchronized PlaceDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static PlaceDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                PlaceDatabase.class,
                DB_NAME).build();
    }

    public abstract PlaceDao getPlaceDao();
}
